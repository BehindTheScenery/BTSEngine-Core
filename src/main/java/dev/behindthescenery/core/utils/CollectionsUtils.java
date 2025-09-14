package dev.behindthescenery.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
public class CollectionsUtils {
    public static int findIndexOf(Collection<?> collection, Object object) {
        for (int i = 0; i < collection.size(); i++) {
            if (collection.toArray()[i].equals(object)) return i;
        }
        return -1;
    }

    public static <T> T getEntryByIndex(Collection<T> collection, int index) {
        int i = 0;
        for (T t : collection) {
            if (i == index) return t;
            i++;
        }
        return null;
    }

    public static int[] toArrayInt(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static float[] toArrayFloat(List<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static double[] toArrayDouble(List<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static char[] toArrayChar(List<Character> list) {
        char[] array = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static boolean[] toArrayBool(List<Boolean> list) {
        boolean[] array = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static <T> T[] toArrayObject(List<T> list, Function<Integer, T[]> contr) {
        T[] array = contr.apply(list.size());
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (var i : array) {
            list.add(i);
        }
        return list;
    }

    public static List<Float> toList(float[] array) {
        List<Float> list = new ArrayList<>();
        for (var i : array) {
            list.add(i);
        }
        return list;
    }

    public static List<Double> toList(double[] array) {
        List<Double> list = new ArrayList<>();
        for (var i : array) {
            list.add(i);
        }
        return list;
    }

    public static List<Character> toList(char[] array) {
        List<Character> list = new ArrayList<>();
        for (var i : array) {
            list.add(i);
        }
        return list;
    }

    public static List<Boolean> toList(boolean[] array) {
        List<Boolean> list = new ArrayList<>();
        for (var i : array) {
            list.add(i);
        }
        return list;
    }
}
