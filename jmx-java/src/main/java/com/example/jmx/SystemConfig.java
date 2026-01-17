package com.example.jmx;

public class SystemConfig implements SystemConfigMBean {
    private int threadCount;
    private String schemaName;

    public SystemConfig(int numThreads, String schema) {
        this.threadCount = numThreads;
        this.schemaName = schema;
    }

    @Override
    public void setThreadCount(int noOfThreads) {
        this.threadCount = noOfThreads;
        System.out.println("Thread Count changed to :: " + this.threadCount);
    }

    @Override
    public int getThreadCount() {
        return this.threadCount;
    }

    @Override
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        System.out.println("Schema Name changed to :: " + this.schemaName);
    }

    @Override
    public String getSchemaName() {
        return this.schemaName;
    }

    @Override
    public String doConfig() {
        return "Thread Count set to " + this.threadCount + " and Schema to " + this.schemaName;
    }
}
