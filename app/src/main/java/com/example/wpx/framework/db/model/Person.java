package com.example.wpx.framework.db.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class Person extends DataSupport {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
