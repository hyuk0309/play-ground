package com.example.jmx;

public interface SystemConfigMBean {
    // 속성 (Attributes) - Getter/Setter
    void setThreadCount(int noOfThreads);
    int getThreadCount();

    void setSchemaName(String schemaName);
    String getSchemaName();

    // 오퍼레이션 (Operations)
    String doConfig();
}
