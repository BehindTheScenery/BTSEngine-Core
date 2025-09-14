package dev.behindthescenery.core.arosnatives;

import dev.behindthescenery.core.arosnatives.functions.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.ValueLayout;
import java.util.Arrays;
import java.util.Map;

public class ANFuncHelper {
    private static final Map<Class<?>, ValueLayout> LAYOUT_PER_CLASS = Map.of(
            Boolean.class, ValueLayout.JAVA_BOOLEAN,
            Character.class, ValueLayout.JAVA_CHAR,
            Byte.class, ValueLayout.JAVA_BYTE,
            Short.class, ValueLayout.JAVA_SHORT,
            Integer.class, ValueLayout.JAVA_INT,
            Long.class, ValueLayout.JAVA_LONG,
            Float.class, ValueLayout.JAVA_FLOAT,
            Double.class, ValueLayout.JAVA_DOUBLE
    );

    @Contract("_, _, _, _ -> new")
    public static <P1, R> @NotNull ANFunction1<P1, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<R> ret) {
        return new ANFunction1<>(name, lib.getFunction(name, descriptorOf(ret, param1)), ret);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <P1, P2, R> @NotNull ANFunction2<P1, P2, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<R> ret) {
        return new ANFunction2<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2)), ret);
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static <P1, P2, P3, R> @NotNull ANFunction3<P1, P2, P3, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<P3> param3, Class<R> ret) {
        return new ANFunction3<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2, param3)), ret);
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static <P1, P2, P3, P4, R> @NotNull ANFunction4<P1, P2, P3, P4, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<P3> param3, Class<P4> param4, Class<R> ret) {
        return new ANFunction4<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2, param3, param4)), ret);
    }

    @Contract("_, _, _, _, _, _, _, _ -> new")
    public static <P1, P2, P3, P4, P5, R> @NotNull ANFunction5<P1, P2, P3, P4, P5, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<P3> param3, Class<P4> param4, Class<P5> param5, Class<R> ret) {
        return new ANFunction5<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2, param3, param4, param5)), ret);
    }

    @Contract("_, _, _, _, _, _, _, _, _ -> new")
    public static <P1, P2, P3, P4, P5, P6, R> @NotNull ANFunction6<P1, P2, P3, P4, P5, P6, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<P3> param3, Class<P4> param4, Class<P5> param5, Class<P6> param6, Class<R> ret) {
        return new ANFunction6<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2, param3, param4, param5, param6)), ret);
    }

    @Contract("_, _, _, _, _, _, _, _, _, _ -> new")
    public static <P1, P2, P3, P4, P5, P6, P7, R> @NotNull ANFunction7<P1, P2, P3, P4, P5, P6, P7, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<P3> param3, Class<P4> param4, Class<P5> param5, Class<P6> param6, Class<P7> param7, Class<R> ret) {
        return new ANFunction7<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2, param3, param4, param5, param6, param7)), ret);
    }

    @Contract("_, _, _, _, _, _, _, _, _, _, _ -> new")
    public static <P1, P2, P3, P4, P5, P6, P7, P8, R> @NotNull ANFunction8<P1, P2, P3, P4, P5, P6, P7, P8, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<P3> param3, Class<P4> param4, Class<P5> param5, Class<P6> param6, Class<P7> param7, Class<P8> param8, Class<R> ret) {
        return new ANFunction8<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2, param3, param4, param5, param6, param7, param8)), ret);
    }

    @Contract("_, _, _, _, _, _, _, _, _, _, _, _ -> new")
    public static <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> @NotNull ANFunction9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> of(@NotNull ANLib lib, String name, Class<P1> param1, Class<P2> param2, Class<P3> param3, Class<P4> param4, Class<P5> param5, Class<P6> param6, Class<P7> param7, Class<P8> param8, Class<P9> param9, Class<R> ret) {
        return new ANFunction9<>(name, lib.getFunction(name, descriptorOf(ret, param1, param2, param3, param4, param5, param6, param7, param8, param9)), ret);
    }

    private static FunctionDescriptor descriptorOf(Class<?> ret, Class<?>... args) {
        ValueLayout[] layouts = Arrays.stream(args).map(ANFuncHelper::layoutOfClass).toArray(ValueLayout[]::new);
        return ret == Void.class ? FunctionDescriptor.ofVoid(layouts) : FunctionDescriptor.of(layoutOfClass(ret), layouts);
    }

    private static ValueLayout layoutOfClass(Class<?> clazz) {
        return LAYOUT_PER_CLASS.get(clazz);
    }
}
