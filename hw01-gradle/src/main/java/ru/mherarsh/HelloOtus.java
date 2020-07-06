package ru.mherarsh;

import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.List;

public class HelloOtus {
    public void seyHello() {
        List<String> stringList = Arrays.asList("H", "E", "L", "L", "O!");
        String margeResult = margeListToString(stringList, "*");
        System.out.println(Arrays.toString(stringList.toArray()) + ": marge result -> " + margeResult);
    }

    private String margeListToString(List<String> listForJoin, String separator) {
        return Joiner.on(separator).join(listForJoin);
    }
}
