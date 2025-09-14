package dev.behindthescenery.core.utils;

import java.util.ArrayList;
import java.util.List;

public class BtsArrayUtils {
    public static <T> List<T> createList(int size, T defaultValue) {
        List<T> list = new ArrayList<>(size);
        fillList(list, size, defaultValue);
        return list;
    }

    public static <T> void fillList(List<T> list, T value) {
        for (int i = 0; i < list.size(); i++) {
            list.add(value);
        }
    }

    public static <T> void fillList(List<T> list, int size, T value) {
        for (int i = 0; i < size; i++) {
            list.add(value);
        }
    }

    public static List<Long> toList(long[] mainArray) {
        return List.of(toArray(mainArray));
    }

    public static List<Double> toList(double[] mainArray) {
        return List.of(toArray(mainArray));
    }

    public static List<Float> toList(float[] mainArray) {
        return List.of(toArray(mainArray));
    }

    public static List<Integer> toList(int[] mainArray) {
        return List.of(toArray(mainArray));
    }

    public static List<Character> toList(char[] mainArray) {
        return List.of(toArray(mainArray));
    }

    public static List<Boolean> toList(boolean[] mainArray) {
        return List.of(toArray(mainArray));
    }

    public static Long[] toArray(long[] mainArray) {
        Long[] array = new Long[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static Double[] toArray(double[] mainArray) {
        Double[] array = new Double[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static Float[] toArray(float[] mainArray) {
        Float[] array = new Float[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static Integer[] toArray(int[] mainArray) {
        Integer[] array = new Integer[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static Character[] toArray(char[] mainArray) {
        Character[] array = new Character[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static Boolean[] toArray(boolean[] mainArray) {
        Boolean[] array = new Boolean[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static long[] fromArray(Long[] mainArray) {
        long[] array = new long[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static double[] fromArray(Double[] mainArray) {
        double[] array = new double[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static float[] fromArray(Float[] mainArray) {
        float[] array = new float[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static int[] fromArray(Integer[] mainArray) {
        int[] array = new int[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static char[] fromArray(Character[] mainArray) {
        char[] array = new char[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static boolean[] fromArray(Boolean[] mainArray) {
        boolean[] array = new boolean[mainArray.length];
        for (int i = 0; i < mainArray.length; i++) {
            array[i] = mainArray[i];
        }
        return array;
    }

    public static <T> T[] fromList(List<T> mainArray) {
        Object[] array = new Object[mainArray.size()];
        for (int i = 0; i < mainArray.size(); i++) {
            array[i] = mainArray.get(i);
        }
        return (T[]) array;
    }

    public static long[] longFromList(List<Long> mainArray) {
        long[] array = new long[mainArray.size()];
        for (int i = 0; i < mainArray.size(); i++) {
            array[i] = mainArray.get(i);
        }
        return array;
    }
}
