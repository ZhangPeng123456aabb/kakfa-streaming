package com.baizhi.kafkalog.entity;

public class Log {
    private String ip;
    private String accessTime;
    private String method;
    private String resource;
    private String status;

    public Log(String ip, String accessTime, String method, String resource, String status) {
        this.ip = ip;
        this.accessTime = accessTime;
        this.method = method;
        this.resource = resource;
        this.status = status;
    }

    public Log() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Log{" +
                "ip='" + ip + '\'' +
                ", accessTime='" + accessTime + '\'' +
                ", method='" + method + '\'' +
                ", resource='" + resource + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
