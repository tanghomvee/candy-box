package com.slst.common.enums;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用一句话描述该文件做什么)
 * @Date Created in 2018/5/21 14:58
 */
public enum AgentLevelEnum {

    TOP_LEVEL(0,"顶级代理商"),

    ONE_LEVEL(1,"一级代理商"),

    TOW_LEVEL(2,"二级代理商"),

    THREE_LEVEL(3,"三级代理商");

    private Integer val;

    private String desc;

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    AgentLevelEnum(Integer val,String desc){
        this.val=val;
        this.desc=desc;
    }

    public static  AgentLevelEnum getByVal(Integer val){
        for (AgentLevelEnum agentLevelEnum : AgentLevelEnum.values()) {
            if (agentLevelEnum.val.equals(val))
                return  agentLevelEnum;
        }
        return null;
    }

}
