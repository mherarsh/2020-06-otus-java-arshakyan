package ru.mherarsh;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyGsonTest {
    @Test
    public void toJsonTest() throws Exception {
        var intArray = new int[2];
        intArray[0] = 10;
        intArray[1] = 25;

        var stringList = new ArrayList<String>();
        stringList.add("str 1");
        stringList.add("str 2");
        stringList.add("str 3");

        var testInnerData  = new JsonTestData(5, "5", 8, intArray, stringList, null);

        var obj = new JsonTestData(22, "test", 10, intArray, stringList, testInnerData);

        var myGson = new MyGson();
        String myJson = myGson.toJson(obj);

        var obj2 = new Gson().fromJson(myJson, JsonTestData.class);

        assertEquals(obj, obj2);
    }
}
