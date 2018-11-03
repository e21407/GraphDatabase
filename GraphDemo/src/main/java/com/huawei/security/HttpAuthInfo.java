package com.huawei.security;


import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

public class HttpAuthInfo {
    private static final String HTTP_URL_PREFIX = "https://";
    private static final String RESOUCE_SEP = "/";
    private static final Range<Integer> PORT_RANGE = Range.between(0, 65536);
    /**
     * IP地址
     */
    private final String ip;
    /**
     * 服务器端口
     */
    private final int port;
    /**
     * 服务名
     */
    private final String service;
    /**
     * CAS IP
     */
    private final String casIP;
    /**
     * CAS port
     */
    private final int casPort;

    /**
     * 用户名
     */
    private final String username;
    /**
     * 密码
     */
    private final String password;
    /**
     * base url
     */
    private final String baseUrl;
    /**
     * cas url
     */
    private final String casUrl;

    /**
     * <p> 构造器 </p>
     *
     * @param builder
     */
    HttpAuthInfo(Builder builder) {
        this.ip = builder.ip;
        this.port = builder.port;
        this.service = builder.service;
        this.casIP = builder.casIP;
        this.casPort = builder.casPort;
        this.username = builder.username;
        this.password = builder.password;
        this.baseUrl = HTTP_URL_PREFIX + ip + ":" + port + RESOUCE_SEP + service;
        this.casUrl = HTTP_URL_PREFIX + casIP + ":" + casPort + RESOUCE_SEP + "cas" + RESOUCE_SEP + "login";
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getService() {
        return service;
    }

    public String getCasIP() {
        return casIP;
    }

    public int getCasPort() {
        return casPort;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCasUrl() {
        return casUrl;
    }

    @Override
    public String toString() {
        return "BASE URL:" + baseUrl + "\n" +
                "CAS URL:" + casUrl + "\n" +
                "LOGIN IN:[" + username + "," + password + "]";
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String ip;
        private int port = -1;
        private String service;
        private String casIP;
        private int casPort = -1;
        private String username;
        public String password;

        Builder() {
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setService(String service) {
            this.service = service;
            return this;
        }

        public Builder setCasIP(String casIP) {
            this.casIP = casIP;
            return this;
        }

        public Builder setCasPort(int casPort) {
            this.casPort = casPort;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public HttpAuthInfo build() {
            StringUtils.isEmpty("");
            if (StringUtils.isEmpty(ip))
                throw new IllegalArgumentException("invalid ip:" + ip);
            if (port <= PORT_RANGE.getMinimum() || port >= PORT_RANGE.getMaximum())
                throw new IllegalArgumentException("invalid port:" + port);
            if (StringUtils.isEmpty(casIP))
                throw new IllegalArgumentException("invalid cas ip:" + ip);
            if (casPort <= PORT_RANGE.getMinimum() || casPort >= PORT_RANGE.getMaximum())
                throw new IllegalArgumentException("invalid cas port:" + port);
            if (StringUtils.isEmpty(username))
                throw new IllegalArgumentException("invalid username:" + username);
            if (StringUtils.isEmpty(password))
                throw new IllegalArgumentException("invalid password:" + password);
            return new HttpAuthInfo(this);
        }
    }

}
