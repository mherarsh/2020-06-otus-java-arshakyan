package ru.mherarsh.core.model;

import ru.mherarsh.jdbc.mapper.Id;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class User {
    @Id
    private long id;
    private String name;
    private int age;

    public User() {
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge(){
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
