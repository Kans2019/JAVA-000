package org.geektime.pojo;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2020/11/18
 * @since 1.8
 **/
public class Student {
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }
}
