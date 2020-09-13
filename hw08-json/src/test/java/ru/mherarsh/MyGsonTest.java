package ru.mherarsh;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        var testInnerData = new JsonTestData(5, "5", 8, intArray, stringList, null, null);

        var objectList = List.of(testInnerData, testInnerData);

        var obj = new JsonTestData(22, "test", 10, intArray, stringList, testInnerData, objectList);

        var myGson = new MyGson();
        String myJson = myGson.toJson(obj);

        var obj2 = new Gson().fromJson(myJson, JsonTestData.class);

        assertEquals(obj, obj2);
    }

    @Test
    public void test() throws Exception {
        var gson = new Gson();
        var serializer = new MyGson();
        assertEquals(gson.toJson(null), serializer.toJson(null));
        assertEquals(gson.toJson((byte) 1), serializer.toJson((byte) 1));
        assertEquals(gson.toJson((short) 1f), serializer.toJson((short) 1f));
        assertEquals(gson.toJson(1), serializer.toJson(1));
        assertEquals(gson.toJson(1L), serializer.toJson(1L));
        assertEquals(gson.toJson(1f), serializer.toJson(1f));
        assertEquals(gson.toJson(1d), serializer.toJson(1d));
        assertEquals(gson.toJson("aaa"), serializer.toJson("aaa"));
        assertEquals(gson.toJson('a'), serializer.toJson('a'));
        assertEquals(gson.toJson(new int[]{1, 2, 3}), serializer.toJson(new int[]{1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2, 3)), serializer.toJson(List.of(1, 2, 3)));
        assertEquals(gson.toJson(Collections.singletonList(1)), serializer.toJson(Collections.singletonList(1)));
    }
}
