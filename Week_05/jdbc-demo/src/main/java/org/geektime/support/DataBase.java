package org.geektime.support;

/**
 * 数据库的抽象
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public enum DataBase {
    MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql://", 3306),
    MYSQL_5("com.mysql.jdbc.Driver", "jdbc:mysql://", 3306),
    MYSQL_8("com.mysql.cj.jdbc.Driver", "jdbc:mysql://", 3306);

    /**
     * 驱动类
     */
    private final String driverClass;

    /**
     * jdbc url前缀
     */
    private final String urlPrefix;

    /**
     * 默认端口
     */
    private final int defaultPort;

    DataBase(String driverClass, String urlPrefix, int defaultPort) {
        this.driverClass = driverClass;
        this.urlPrefix = urlPrefix;
        this.defaultPort = defaultPort;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public int getDefaultPort() {
        return defaultPort;
    }
}
