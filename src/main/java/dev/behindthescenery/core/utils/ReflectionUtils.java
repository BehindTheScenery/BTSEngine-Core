package dev.behindthescenery.core.utils;

import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T)o;
    }

    public static boolean canCast(Class<?> obj, Class<?> caster){

        if(obj.equals(caster)) return true;

        List<Class<?>> d1 = getParent(obj, true);
        if(d1.contains(caster)) return true;

        d1 = getParent(caster, true);
        return d1.contains(obj);
    }

    public static List<Class<?>> getParent(Class<?> obj, boolean useInterfaces){
        List<Class<?>> classes = new ArrayList<>();

        if(useInterfaces) {
            for (Class<?> anInterface : obj.getInterfaces()) {
                classes.add(anInterface);
                classes.addAll(getParent(anInterface, true));
            }
        }

        if(obj.getSuperclass() != null){
            classes.add(obj.getSuperclass());
            classes.addAll(getParent(obj.getSuperclass(), useInterfaces));
        }

        return classes;
    }
}
