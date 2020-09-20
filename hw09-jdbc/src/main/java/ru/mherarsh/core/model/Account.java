package ru.mherarsh.core.model;

import ru.mherarsh.jdbc.mapper.Id;

/**
 * @author mherarsh
 */
public class Account {
    @Id
    private long no;
    private String type;
    private int rest;

    public Account() {
    }

    public Account(String type, int rest) {
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public int getRest() {
        return rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
