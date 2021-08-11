package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 考场信息表
 */
public class DBExamInfo extends RealmObject implements Serializable {

    private String appid;
    private String bykcxx;
    private String bzhkcid;
    private String bzhkdid;
    private String ccm;
    private Long createTime;
    private String dataStatus;
    private String kcbh;
    private String kcsjwz;
    private String kdbh;
    private String kqbh;
    private String ksdm;
    private String ksmc;
    private String sfbzhkc;
    private String sxturidz;
    private String updateTime;
    private String wybs;
    private String zcqswz;
    private String zcqswzm;
    private String zwbjfs;
    private String zwbjfsm;
    private String zwplfs;
    private String zwplfsm;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBykcxx() {
        return bykcxx;
    }

    public void setBykcxx(String bykcxx) {
        this.bykcxx = bykcxx;
    }

    public String getBzhkcid() {
        return bzhkcid;
    }

    public void setBzhkcid(String bzhkcid) {
        this.bzhkcid = bzhkcid;
    }

    public String getBzhkdid() {
        return bzhkdid;
    }

    public void setBzhkdid(String bzhkdid) {
        this.bzhkdid = bzhkdid;
    }

    public String getCcm() {
        return ccm;
    }

    public void setCcm(String ccm) {
        this.ccm = ccm;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getKcbh() {
        return kcbh;
    }

    public void setKcbh(String kcbh) {
        this.kcbh = kcbh;
    }

    public String getKcsjwz() {
        return kcsjwz;
    }

    public void setKcsjwz(String kcsjwz) {
        this.kcsjwz = kcsjwz;
    }

    public String getKdbh() {
        return kdbh;
    }

    public void setKdbh(String kdbh) {
        this.kdbh = kdbh;
    }

    public String getKqbh() {
        return kqbh;
    }

    public void setKqbh(String kqbh) {
        this.kqbh = kqbh;
    }

    public String getKsdm() {
        return ksdm;
    }

    public void setKsdm(String ksdm) {
        this.ksdm = ksdm;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    public String getSfbzhkc() {
        return sfbzhkc;
    }

    public void setSfbzhkc(String sfbzhkc) {
        this.sfbzhkc = sfbzhkc;
    }

    public String getSxturidz() {
        return sxturidz;
    }

    public void setSxturidz(String sxturidz) {
        this.sxturidz = sxturidz;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWybs() {
        return wybs;
    }

    public void setWybs(String wybs) {
        this.wybs = wybs;
    }

    public String getZcqswz() {
        return zcqswz;
    }

    public void setZcqswz(String zcqswz) {
        this.zcqswz = zcqswz;
    }

    public String getZcqswzm() {
        return zcqswzm;
    }

    public void setZcqswzm(String zcqswzm) {
        this.zcqswzm = zcqswzm;
    }

    public String getZwbjfs() {
        return zwbjfs;
    }

    public void setZwbjfs(String zwbjfs) {
        this.zwbjfs = zwbjfs;
    }

    public String getZwbjfsm() {
        return zwbjfsm;
    }

    public void setZwbjfsm(String zwbjfsm) {
        this.zwbjfsm = zwbjfsm;
    }

    public String getZwplfs() {
        return zwplfs;
    }

    public void setZwplfs(String zwplfs) {
        this.zwplfs = zwplfs;
    }

    public String getZwplfsm() {
        return zwplfsm;
    }

    public void setZwplfsm(String zwplfsm) {
        this.zwplfsm = zwplfsm;
    }
}
