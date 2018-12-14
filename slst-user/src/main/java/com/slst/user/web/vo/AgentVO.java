package com.slst.user.web.vo;

import java.io.Serializable;

/**
 * @Author <a href="mailto:npleung@163.com">NPLeung</a>
 * @Description TODO(用于接收前端代理商注册信息)
 * @Date Created in 2018/5/18 16:28
 */
public class AgentVO implements Serializable {

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 公司名字
     */
    private String companyName;
    /**
     * 公司简称
     */
    private String simpleName;
    /**
     * 联系人名字
     */
    private String contact;
    /**
     * 城市ID
     */
    private String cityId;
    /**
     * 城市名字
     */
    private String cityName;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 行业ID
     */
    private String industryId;
    /**
     * 行业名字
     */
    private String industryName;
    /**
     * 营业执照
     */
    private String bizlicense;
    /**
     * 公司LOGO
     */
    private String logo;
    /**
     * 公司官网
     */
    private String website;
    /**
     * 提成率
     */
    private Integer earningsRate;
    /**
     * 纳税唯一标识
     */
    private String taxIdNum;
    /**
     * 代理商ID
     */
    private Long agentId;

    /**
     * 项目访问地址 http://localhost:8080/slst-web
     * e.g. contextPath = request.getContextPath();
     * basePath= request.getScheme()+"://"+request.getServerName()+":"+
     *                 request.getServerPort()+contextPath+"/"
     */
    private String basePath;

    /**
     * 项目物理地址:D:\tomcat_home\webapps
     * request.getSession().
     *                 getServletContext().getRealPath("");
     */
    private String realPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getBizlicense() {
        return bizlicense;
    }

    public void setBizlicense(String bizlicense) {
        this.bizlicense = bizlicense;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getEarningsRate() {
        return earningsRate;
    }

    public void setEarningsRate(Integer earningsRate) {
        this.earningsRate = earningsRate;
    }

    public String getTaxIdNum() {
        return taxIdNum;
    }

    public void setTaxIdNum(String taxIdNum) {
        this.taxIdNum = taxIdNum;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }
}
