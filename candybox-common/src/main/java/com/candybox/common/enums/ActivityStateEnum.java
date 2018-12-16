package com.candybox.common.enums;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/7/5 18:40
 */
public enum ActivityStateEnum {

    ACTIVATION(1,"激活"), FINISHED(2,"完结");

    private Integer val;
    private String desc;

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    ActivityStateEnum(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }


    public  static ActivityStateEnum getByVal(Integer val){
        for (ActivityStateEnum tmp : ActivityStateEnum.values()){
            if(tmp.val.equals(val)){
                return tmp;
            }
        }
        return null;
    }
}
