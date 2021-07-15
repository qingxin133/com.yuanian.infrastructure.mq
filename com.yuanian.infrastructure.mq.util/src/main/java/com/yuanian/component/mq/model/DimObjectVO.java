package com.yuanian.component.mq.model;


import com.alibaba.fastjson.annotation.JSONField;
import com.epoch.infrastructure.util.service.JsonUtils;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ToString
public class DimObjectVO implements Serializable{
    private Map<String, String> languageInfo;
    private Integer minDepth;
    private Integer maxDepth;
    private String typeName;
    private String typeCode;
    private String parentName;
    private String parentCode;
    private String accountId;
    private String accountNo;
    private String taxId;
    private String taxNo;
    private String orgId;
    @JSONField(
            serialize = false
    )
    private boolean isBuildExtend;
    @JSONField(
            serialize = false
    )
    private transient String synRowId;
    @JSONField(
            serialize = false
    )
    private transient List<String> toRemovedMutiPropertyMarks;
    private String manageOrgId;
    @JSONField(
            serialize = false
    )
    private boolean saveValidateByAccess = false;
    private LocalDateTime mqUpdateDate;

    public DimObjectVO() {
    }

    public LocalDateTime getMqUpdateDate() {
        return this.mqUpdateDate;
    }

    public void setMqUpdateDate(LocalDateTime mqUpdateDate) {
        this.mqUpdateDate = mqUpdateDate;
    }

    public boolean isSaveValidateByAccess() {
        return this.saveValidateByAccess;
    }

    public void setSaveValidateByAccess(boolean saveValidateByAccess) {
        this.saveValidateByAccess = saveValidateByAccess;
    }

    public String getManageOrgId() {
        return this.manageOrgId;
    }

    public void setManageOrgId(String manageOrgId) {
        this.manageOrgId = manageOrgId;
    }

    public List<String> getToRemovedMutiPropertyMarks() {
        return this.toRemovedMutiPropertyMarks;
    }

    public void setToRemovedMutiPropertyMarks(List<String> toRemovedMutiPropertyMarks) {
        this.toRemovedMutiPropertyMarks = toRemovedMutiPropertyMarks;
    }

    public String getSynRowId() {
        return this.synRowId;
    }

    public void setSynRowId(String synRowId) {
        this.synRowId = synRowId;
    }

    public boolean isBuildExtend() {
        return this.isBuildExtend;
    }

    public void setBuildExtend(boolean buildExtend) {
        this.isBuildExtend = buildExtend;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Map<String, String> getLanguageInfo() {
        return this.languageInfo;
    }

    public void setLanguageInfo(Map<String, String> languageInfo) {
        this.languageInfo = languageInfo;
    }

    public Integer getMinDepth() {
        return this.minDepth;
    }

    public void setMinDepth(Integer minDepth) {
        this.minDepth = minDepth;
    }

    public Integer getMaxDepth() {
        return this.maxDepth;
    }

    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return this.typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentCode() {
        return this.parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getTaxId() {
        return this.taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTaxNo() {
        return this.taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DimObjectVO: ");
        return sb.append(JsonUtils.toJSON(this)).toString();
    }
}
