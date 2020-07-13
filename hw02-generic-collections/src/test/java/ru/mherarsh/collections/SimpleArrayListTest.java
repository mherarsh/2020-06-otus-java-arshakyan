package ru.mherarsh.collections;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleArrayListTest {
    private static final int SIMPLE_LIST_ELEMENTS_COUNT = 30;
    private static final int GENERATED_NUMS_COUNT = 100;

    @Test
    void addAllTest() {
        List<Integer> sampleNumsList = generateSampleNumsList(GENERATED_NUMS_COUNT);
        List<Integer> simpleArrayList = new SimpleArrayList<>();

        simpleArrayList.addAll(sampleNumsList);

        assertIterableEquals(sampleNumsList, simpleArrayList);
    }

    @Test
    void copyTest() {
        List<Integer> sampleNumsList = generateSampleNumsList(GENERATED_NUMS_COUNT);
        List<Integer> simpleArrayList = new SimpleArrayList<>(generateSampleNumsList(SIMPLE_LIST_ELEMENTS_COUNT));

        Collections.copy(sampleNumsList, simpleArrayList);

        assertIterableEquals(sampleNumsList.subList(0, SIMPLE_LIST_ELEMENTS_COUNT), simpleArrayList);
    }

    @Test
    void sortTest() {
        List<Integer> sampleNumsList = generateSampleNumsList(GENERATED_NUMS_COUNT);
        List<Integer> simpleArrayList = new SimpleArrayList<>(sampleNumsList);

        Comparator<Integer> compareByDescending = (num1, num2) -> {
            if (num1.equals(num2)) {
                return 0;
            } else if (num1 > num2) {
                return -1;
            } else {
                return 1;
            }
        };

        Collections.sort(sampleNumsList, compareByDescending);
        Collections.sort(simpleArrayList, compareByDescending);

        assertIterableEquals(sampleNumsList, simpleArrayList);
    }

    private List<Integer> generateSampleNumsList(int size) {
        ArrayList<Integer> integerList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            integerList.add((int) (Math.random() * 100));
        }

        return integerList;
    }
}
