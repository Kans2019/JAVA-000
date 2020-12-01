package org.geektime.jdbc;

import com.google.common.base.Objects;

/**
 * sql 语句的声明
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/17
 * @since 1.8
 **/
public final class SqlStatement {
    /**
     * sql 语句
     */
    private final String sql;

    /**
     * sql 语句的参数
     */
    private final Object[] arguments;

    public SqlStatement(String sql, Object... arguments) {
        this.sql = sql;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlStatement that = (SqlStatement) o;
        return Objects.equal(sql, that.sql) && ArrayUtils.equals(this.arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sql, arguments);
    }

    public String getSql() {
        return sql;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
