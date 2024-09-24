package com.easytrip.entity.config;

import com.easytrip.utils.StringTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${project.folder:}")
    private String projectFolder;
    @Value("${super.admin.phones:}")
    private String superAdminPhones;

    @Value("${jwt.common.secret:}")
    private String jwtCommonSecret;

    @Value("${app.domain:}")
    private String appDomain;

    @Value("${aliyun.sms.accessKeyId:}")
    private String accessKeyID;

    @Value("${aliyun.sms.secret:}")
    private String accessKey;

    @Value("${SMSSign:}")
    private String SMSSign;

    @Value("${SMSTemplate:}")
    private String SMSTemplate;

    @Value("${aliyun.sms.regionId:}")
    private String regionId;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getProjectFolder() {
        if (!StringTools.isEmpty(projectFolder) && !projectFolder.endsWith("/")) {
            projectFolder = projectFolder + "/";
        }
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public String getSuperAdminPhones() {
        return superAdminPhones;
    }

    public void setSuperAdminPhones(String superAdminPhones) {
        this.superAdminPhones = superAdminPhones;
    }

    public String getJwtCommonSecret() {
        return jwtCommonSecret;
    }

    public void setJwtCommonSecret(String jwtCommonSecret) {
        this.jwtCommonSecret = jwtCommonSecret;
    }

    public String getAppDomain() {
        return appDomain;
    }

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public void setAccessKeyID(String accessKeyID) {
        this.accessKeyID = accessKeyID;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSMSSign() {
        return SMSSign;
    }

    public void setSMSSign(String SMSSign) {
        this.SMSSign = SMSSign;
    }

    public String getSMSTemplate() {
        return SMSTemplate;
    }

    public void setSMSTemplate(String SMSTemplate) {
        this.SMSTemplate = SMSTemplate;
    }
}
