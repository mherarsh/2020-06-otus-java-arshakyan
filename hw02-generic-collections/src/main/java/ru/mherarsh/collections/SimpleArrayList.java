package ru.mherarsh.collections;

import java.util.*;

public class SimpleArrayList<T> implements List<T> {
    private Object[] dataArray;
    private int size = 0;

    public SimpleArrayList() {
        dataArray = new Object[]{};
    }

    public SimpleArrayList(Collection<? extends T> c) {
        this();

        if (c.isEmpty()) {
            return;
        }

        addAll(c);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return dataArray.length == 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.stream(dataArray).limit(size).toArray();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()) {
            return true;
        }

        if (dataArray.length - size < c.size()) {
            extendDataArray(Math.max(2 * dataArray.length, dataArray.length + c.size()));
            System.arraycopy(c.toArray(), 0, dataArray, size, c.size());
            size += c.size();
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);
        return (T) dataArray[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, size);
        Object oldValue = dataArray[index];
        dataArray[index] = element;
        return (T) oldValue;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new SimpleArrayIterator();
    }

    @Override
    public String toString() {
        if (dataArray.length == 0) {
            return "[]";
        }

        StringJoiner arrayJoiner = new StringJoiner(", ", "[", "]");

        for (int i = 0; i < size; i++) {
            Object arrayElement = dataArray[i];
            if (arrayElement == null) {
                arrayJoiner.add(" ");
            } else {
                arrayJoiner.add(arrayElement.toString());
            }
        }

        return arrayJoiner.toString();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return new SimpleArrayIterator();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private void extendDataArray(int newSize) {
        dataArray = Arrays.copyOf(dataArray, newSize, Object[].class);
    }

    class SimpleArrayIterator implements ListIterator<T> {
        private int iteratorPosition = 0;

        @Override
        public boolean hasNext() {
            return iteratorPosition < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            return (T) dataArray[iteratorPosition++];
        }

        @Override
        public int nextIndex() {
            return iteratorPosition + 1;
        }

        @Override
        public void set(T value) {
            dataArray[iteratorPosition - 1] = value;
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}
