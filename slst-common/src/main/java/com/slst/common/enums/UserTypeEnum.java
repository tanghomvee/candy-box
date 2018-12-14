package com.slst.common.enums;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(定义用户类型枚举)
 * @Date Created in 2018/5/18 17:24
 */
public enum UserTypeEnum {

    /**
     * 系统管理员
     */
    ADMIN(1,"系统管理员"),

    /**
     * 系统管理员员工
     */
    ADMIN_EMP(11,"系统管理员员工"),

    /**
     * 代理商
     */
    AGENT(2,"代理商"),

    /**
     * 代理商员工
     */
    AGENT_EMP(21,"代理商员工"),

    /**
     * 商家
     */
    VENDER(3,"商家"),

    /**
     * 商家员工
     */
    VENDER_EMP(31,"商家员工"),

    /**
     * 数据使用方
     */
    DATA_USER(4,"数据使用方");

    private Integer val;

    private String desc;

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    UserTypeEnum(Integer val, String desc){
        this.val=val;
        this.desc=desc;
    }

    public static UserTypeEnum getByVal(Integer val){
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.val.equals(val)){
                return  userTypeEnum;
            }
        }

        return  null;
    }

}
