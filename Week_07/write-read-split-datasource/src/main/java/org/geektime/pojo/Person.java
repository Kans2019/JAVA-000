package org.geektime.pojo;

import com.google.common.base.MoreObjects;

/**
 * 用户处理的Person
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/12/4
 * @since 1.8
 **/
public class Person {
    /**
     * id 列
     */
    private Long id;

    /**
     * name 列
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }
}
