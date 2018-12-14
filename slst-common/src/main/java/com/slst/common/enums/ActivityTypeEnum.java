package com.slst.common.enums;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/5 18:45
 */
public enum ActivityTypeEnum {

    SMS(1,"短信"), PHONE_CALL(2,"电话");

    private Integer val;
    private String desc;

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    ActivityTypeEnum(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }


    public  static ActivityTypeEnum getByVal(Integer val){
        for (ActivityTypeEnum tmp : ActivityTypeEnum.values()){
            if(tmp.val.equals(val)){
                return tmp;
            }
        }
        return null;
    }
}
