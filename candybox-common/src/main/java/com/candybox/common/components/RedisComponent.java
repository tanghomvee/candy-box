package com.candybox.common.components;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Homvee.Tang(tanghongwei@ddcloudf.com)
 * @version V1.0
 * @Title: RedisComponent
 * @Package com.sinosig.components
 * @Description: (用一句话描述该文件做什么)
 * @date 2017-11-28 19:40
 */
@Component
public class RedisComponent {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisComponent.class);

    private static final Long DEFAULT_EXPIRE_SECONDS = 10L;

    private static final Integer DEFAULT_RETRY_TIMES = Integer.MAX_VALUE;

    private static final Long DEFAULT_SLEEP_MILLIS = 500L;

    private ThreadLocal<String> LOCK_VAL = new ThreadLocal<String>();

    private static final String UNLOCK_LUA;

    static {
        StringBuilder script = new StringBuilder();
        script.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        script.append("then ");
        script.append("    return redis.call(\"del\",KEYS[1]) ");
        script.append("else ");
        script.append("    return 0 ");
        script.append("end ");
        UNLOCK_LUA = script.toString();
    }


    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    /**
    * 原子自增
    * @author  Homvee.Tang(tanghongwei@ddcloudf.com)
    * @date  2017-11-28 19:52
    * @param key
    * @param liveTime
    * @return 返回为自增之前的值
    */
    public Long incr(String key, long liveTime) {
        try{

            RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
            Long increment = entityIdCounter.getAndIncrement();
            //初始设置过期时间
            if (null == increment || increment.longValue() < 0) {
                increment = 0L;
            }
            if(liveTime > 0 ){
                entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
            }
            return increment;
        }catch (Exception ex){
            LOGGER.error("原子自增异常" , ex);
        }
        return 0L;
    }
    /**
     * 原子自减
     * @author  Homvee.Tang(tanghongwei@ddcloudf.com)
     * @date  2017-11-28 19:52
     * @param key
     * @param liveTime
     * @return 返回为自减之前的值
     */
    public Long dncr(String key, long liveTime) {
        try{

            RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
            Long decrement = entityIdCounter.getAndDecrement();
            //初始设置过期时间
            if (null == decrement || decrement.longValue() < 0 ) {
                decrement = 0L;
            }

            if(liveTime > 0 ){
                entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
            }
            return decrement;
        }catch (Exception ex){
            LOGGER.error("原子自增异常" , ex);
        }
        return 0L;
    }

    /**
     * 获取原子值
     * @param key
     * @return
     */
    public Long getAtomicLong(final String key) {
        Long result = null;
        try{
            RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
            result = entityIdCounter.get();
        }catch (Exception ex){
            LOGGER.error("get Atomic Long key:{} from slave redis error" , key , ex);
        }
        return result;
    }
    public boolean lock(String key){
        return lock(key , DEFAULT_RETRY_TIMES);
    }

    public boolean lock(String key, int retryTimes){
      return   this.lock(key , DEFAULT_EXPIRE_SECONDS, retryTimes);
    }

    public boolean lock(String key, int retryTimes, long sleepMillis){
        return this.lock(key , DEFAULT_EXPIRE_SECONDS, retryTimes , sleepMillis);
    }

    public boolean lock(String key, long expire){
        return  this.lock(key , expire , DEFAULT_RETRY_TIMES);
    }

    public boolean lock(String key, long expire, int retryTimes){
        return this.lock(key , expire , retryTimes , DEFAULT_SLEEP_MILLIS);
    }

    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setRedis(key, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while((!result) && retryTimes-- > 0){
            try {
                LOGGER.debug("lock failed, retrying...{}" , retryTimes);
                Thread.sleep(sleepMillis);
            } catch (Exception e) {
                return false;
            }
            result = setRedis(key, expire);
        }
        return result;
    }

    public boolean unLock(String key) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            List<String> keys = Lists.newArrayList(key);
            List<String> lockVals = Lists.newArrayList(LOCK_VAL.get());
            if(lockVals.isEmpty()){
                return true;
            }

            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本

            Long result = redisTemplate.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式
                    if (nativeConnection instanceof JedisCluster) {

                        return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, lockVals);

                    }else if (nativeConnection instanceof Jedis) {
                        // 单机模式
                        return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, lockVals);
                    }
                    return 0L;
                }
            });

            return result != null && result > 0;
        } catch (Exception e) {
            LOGGER.error("release lock {} exception", key , e);
        }
        return false;
    }

    private boolean setRedis(String key, long expire) {
        try {
            String result = redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    String uuid = UUID.randomUUID().toString();
                    LOCK_VAL.set(uuid);
                    //PX:毫秒;EX:秒
                    return commands.set(key, uuid, "NX", "EX", expire);
                }
            });
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            LOGGER.error("set redis occured an exception", e);
        }
        return false;
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public String get(final String key) {
        String result = null;
        try{
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            result = operations.get(key);
        }catch (Exception ex){
            LOGGER.error("get key:{} from slave redis error" , key , ex);
        }
        return result;
    }
    public String set(final String key , final String val) {
        String result = null;
        try{
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key , val);
        }catch (Exception ex){
            LOGGER.error("set key:{} from master redis error" , key , ex);
        }
        return result;
    }
    public String set(final String key , final String val , long expire) {
        String result = null;
        try{
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key , val , expire ,TimeUnit.SECONDS);
        }catch (Exception ex){
            LOGGER.error("set key:{} from master redis error" , key , ex);
        }
        return result;
    }


    /**
     * Redis Hsetnx 命令用于为哈希表中不存在的的字段赋值 。
     *
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
     *
     * 如果字段已经存在于哈希表中，操作无效。
     *
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * @param redisKey
     * @param filed
     * @param val
     * @param seconds
     * @return
     */
    public boolean hSetNx(final String redisKey  , final String filed, final String val , int seconds) {
        try {
            Long result = redisTemplate.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    commands.expire(redisKey , seconds);

                    return commands.hsetnx(redisKey ,filed ,val);
                }
            });
            return result != null && result == 1;
        } catch (Exception e) {
            LOGGER.error("hSetNx redis occured an exception", e);
        }
        return false;
    }

    /**
     * 获取hashMap的值
     * @param redisKey
     * @param filed
     * @return
     */
    public String hGet(final String redisKey  , final String filed) {
        try {
            String result = redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    return commands.hget(redisKey ,filed);
                }
            });
            return result;
        } catch (Exception e) {
            LOGGER.error("hget redis occured an exception", e);
        }
        return null;
    }

    /**
     * 通过指定Hashmap
     *field值删除整个HashMap的key
     * @param redisKey
     * @param filed
     * @param filedVal
     * @return
     */
    public boolean hdelKeyByFieldVal(final String redisKey  , final String filed, final String filedVal) {
        try {
            Long result = redisTemplate.execute(new RedisCallback<Long>() {
                @Override
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    String val = commands.hget(redisKey ,filed);
                    if (filedVal.equals(val)){
                        return  commands.del(redisKey);
                    }
                    return 0L;
                }
            });
            return result != null && result == 1;
        } catch (Exception e) {
            LOGGER.error("hdelKeyByFieldVal redis occured an exception", e);
        }
        return false;
    }

    /**
     * 把map存入redis中
     * @param key
     * @param map
     */
    public void redisSaveMap(String key, Map<String, Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }
    /**
     * 把map存入redis中并设置失效时间
     * @param key
     * @param map
     * @param time 失效时间值
     * @param unit 失效时间单位
     * @return true(成功) / false（失败）
     */
    public Boolean redisSaveMap(String key, Map<String, Object> map, long time, TimeUnit unit){
        redisTemplate.opsForHash().putAll(key, map);
        return redisTemplate.expire(key, time, unit);
    }

    /**
     * 取去存入redis中的map
     * @param key
     * @return
     */
    public Map<Object, Object> getRedisMapByKey(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 根据参数key，删除对应redis中的数据
     * @param key key值
     * @return true（成功）/ false(失败)
     */
    public Boolean deleteByKey(String key){
        return redisTemplate.delete(key);
    }

}
