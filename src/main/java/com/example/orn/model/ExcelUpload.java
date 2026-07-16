package com.example.orn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "uploaded_orn")
public class ExcelUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partner_role_type")
    private String partnerRoleType;

    @Column(name = "scheme_name")
    private String schemeName;

    @Column(name = "scheme_number")
    private String schemeNumber;

    @Column(name = "rds_id")
    private String rdsId;

    @Column(name = "partner_name_rds")
    private String partnerNameRds;

    @Column(name = "partner_name_zd")
    private String partnerNameZd;

    @Column(name = "scheme_from_date")
    private LocalDate schemeFromDate;

    @Column(name = "scheme_to_date")
    private LocalDate schemeToDate;

    @Column(name = "state_code")
    private String stateCode;

    private String region;

    @Column(name = "pos_agent_code")
    private String posAgentCode;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "partner_role_name")
    private String partnerRoleName;

    @Column(name = "jio_center")
    private String jioCenter;

    @Column(name = "mnp_flag")
    private String mnpFlag;

    @Column(name = "caf_number")
    private String cafNumber;

    private String maktx;

    @Column(name = "sold_from")
    private String soldFrom;

    private String orn;

    @Column(name = "offer_code")
    private String offerCode;

    @Column(name = "recharge_code")
    private String rechargeCode;

    @Column(name = "sold_from_name")
    private String soldFromName;

    private String recharge;

    private Integer quantity;

    @Column(name = "imei_rank")
    private String imeiRank;

    @Column(name = "fr_code")
    private String frCode;

    private String prime;

    private Double commission;

    @Column(name = "rh_activation_date")
    private LocalDate rhActivationDate;

    @Column(name = "caf_date")
    private LocalDate cafDate;

    private LocalDate hierdate;

    @Column(name = "elig_header")
    private String eligHeader;

    private Double price;

    @Column(name = "recharge_days")
    private Integer rechargeDays;

    private String adhaarrank;

    @Column(name = "recharge_num")
    private String rechargeNum;

    private String misc05;

    private String misc01;

    private String misc02;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "created_by")
    private String createdBy;

    public ExcelUpload() {
        this.uploadedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartnerRoleType() {
        return partnerRoleType;
    }

    public void setPartnerRoleType(String partnerRoleType) {
        this.partnerRoleType = partnerRoleType;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeNumber() {
        return schemeNumber;
    }

    public void setSchemeNumber(String schemeNumber) {
        this.schemeNumber = schemeNumber;
    }

    public String getRdsId() {
        return rdsId;
    }

    public void setRdsId(String rdsId) {
        this.rdsId = rdsId;
    }

    public String getPartnerNameRds() {
        return partnerNameRds;
    }

    public void setPartnerNameRds(String partnerNameRds) {
        this.partnerNameRds = partnerNameRds;
    }

    public String getPartnerNameZd() {
        return partnerNameZd;
    }

    public void setPartnerNameZd(String partnerNameZd) {
        this.partnerNameZd = partnerNameZd;
    }

    public LocalDate getSchemeFromDate() {
        return schemeFromDate;
    }

    public void setSchemeFromDate(LocalDate schemeFromDate) {
        this.schemeFromDate = schemeFromDate;
    }

    public LocalDate getSchemeToDate() {
        return schemeToDate;
    }

    public void setSchemeToDate(LocalDate schemeToDate) {
        this.schemeToDate = schemeToDate;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPosAgentCode() {
        return posAgentCode;
    }

    public void setPosAgentCode(String posAgentCode) {
        this.posAgentCode = posAgentCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPartnerRoleName() {
        return partnerRoleName;
    }

    public void setPartnerRoleName(String partnerRoleName) {
        this.partnerRoleName = partnerRoleName;
    }

    public String getJioCenter() {
        return jioCenter;
    }

    public void setJioCenter(String jioCenter) {
        this.jioCenter = jioCenter;
    }

    public String getMnpFlag() {
        return mnpFlag;
    }

    public void setMnpFlag(String mnpFlag) {
        this.mnpFlag = mnpFlag;
    }

    public String getCafNumber() {
        return cafNumber;
    }

    public void setCafNumber(String cafNumber) {
        this.cafNumber = cafNumber;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getSoldFrom() {
        return soldFrom;
    }

    public void setSoldFrom(String soldFrom) {
        this.soldFrom = soldFrom;
    }

    public String getOrn() {
        return orn;
    }

    public void setOrn(String orn) {
        this.orn = orn;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getRechargeCode() {
        return rechargeCode;
    }

    public void setRechargeCode(String rechargeCode) {
        this.rechargeCode = rechargeCode;
    }

    public String getSoldFromName() {
        return soldFromName;
    }

    public void setSoldFromName(String soldFromName) {
        this.soldFromName = soldFromName;
    }

    public String getRecharge() {
        return recharge;
    }

    public void setRecharge(String recharge) {
        this.recharge = recharge;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImeiRank() {
        return imeiRank;
    }

    public void setImeiRank(String imeiRank) {
        this.imeiRank = imeiRank;
    }

    public String getFrCode() {
        return frCode;
    }

    public void setFrCode(String frCode) {
        this.frCode = frCode;
    }

    public String getPrime() {
        return prime;
    }

    public void setPrime(String prime) {
        this.prime = prime;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public LocalDate getRhActivationDate() {
        return rhActivationDate;
    }

    public void setRhActivationDate(LocalDate rhActivationDate) {
        this.rhActivationDate = rhActivationDate;
    }

    public LocalDate getCafDate() {
        return cafDate;
    }

    public void setCafDate(LocalDate cafDate) {
        this.cafDate = cafDate;
    }

    public LocalDate getHierdate() {
        return hierdate;
    }

    public void setHierdate(LocalDate hierdate) {
        this.hierdate = hierdate;
    }

    public String getEligHeader() {
        return eligHeader;
    }

    public void setEligHeader(String eligHeader) {
        this.eligHeader = eligHeader;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getRechargeDays() {
        return rechargeDays;
    }

    public void setRechargeDays(Integer rechargeDays) {
        this.rechargeDays = rechargeDays;
    }

    public String getAdhaarrank() {
        return adhaarrank;
    }

    public void setAdhaarrank(String adhaarrank) {
        this.adhaarrank = adhaarrank;
    }

    public String getRechargeNum() {
        return rechargeNum;
    }

    public void setRechargeNum(String rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    public String getMisc05() {
        return misc05;
    }

    public void setMisc05(String misc05) {
        this.misc05 = misc05;
    }

    public String getMisc01() {
        return misc01;
    }

    public void setMisc01(String misc01) {
        this.misc01 = misc01;
    }

    public String getMisc02() {
        return misc02;
    }

    public void setMisc02(String misc02) {
        this.misc02 = misc02;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
    public String getFileName() {
    return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}