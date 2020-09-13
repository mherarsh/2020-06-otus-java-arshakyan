package ru.mherarsh;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JsonTestData {
    private final int integer1;
    private final int integer2;
    private final String someString;
    private final int[] intArray;
    private final List<String> stringList;
    private final JsonTestData testData;
    private final List<JsonTestData> testDataList;

    public JsonTestData(int integer1, String someString, int integer2, int[] intArray, List<String> stringList, JsonTestData testData, List<JsonTestData> testDataList) {
        this.integer1 = integer1;
        this.someString = someString;
        this.integer2 = integer2;
        this.intArray = intArray;
        this.stringList = stringList;
        this.testData = testData;
        this.testDataList = testDataList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonTestData that = (JsonTestData) o;
        return integer1 == that.integer1 &&
                integer2 == that.integer2 &&
                Objects.equals(someString, that.someString) &&
                Arrays.equals(intArray, that.intArray) &&
                Objects.equals(stringList, that.stringList) &&
                Objects.equals(testData, that.testData) &&
                Objects.equals(testDataList, that.testDataList);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(integer1, integer2, someString, stringList, testData);
        result = 31 * result + Arrays.hashCode(intArray);
        return result;
    }
}
