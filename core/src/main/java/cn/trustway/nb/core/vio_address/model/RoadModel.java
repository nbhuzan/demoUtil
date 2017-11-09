package cn.trustway.nb.core.vio_address.model;

import java.io.Serializable;

/**
 * Created by huzan  2017/3/31 16:19.
 * 描述：
 */

public class RoadModel implements Serializable {
    private String glbm;
    private String dldm;
    private String dlmc;
    private String lddm;
    private String ldmc;
    private String qs ;
    private String js;
    private String xzqh;
    private Integer qsms;
    private Integer jsms;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGlbm() {
        return glbm;
    }

    public void setGlbm(String glbm) {
        this.glbm = glbm;
    }

    public String getDldm() {
        return dldm;
    }

    public void setDldm(String dldm) {
        this.dldm = dldm;
    }

    public String getDlmc() {
        return dlmc;
    }

    public void setDlmc(String dlmc) {
        this.dlmc = dlmc;
    }

    public String getLddm() {
        return lddm;
    }

    public void setLddm(String lddm) {
        this.lddm = lddm;
    }

    public String getLdmc() {
        return ldmc;
    }

    public void setLdmc(String ldmc) {
        this.ldmc = ldmc;
    }

    public String getQs() {
        return qs;
    }

    public void setQs(String qs) {
        this.qs = qs;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    public String getXzqh() {
        return xzqh;
    }

    public void setXzqh(String xzqh) {
        this.xzqh = xzqh;
    }

    public Integer getQsms() {
        return qsms;
    }

    public void setQsms(Integer qsms) {
        this.qsms = qsms;
    }

    public Integer getJsms() {
        return jsms;
    }

    public void setJsms(Integer jsms) {
        this.jsms = jsms;
    }
}
