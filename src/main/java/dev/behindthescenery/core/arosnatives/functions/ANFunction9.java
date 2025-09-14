package dev.behindthescenery.core.arosnatives.functions;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class ANFunction9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> extends ANRawFunction<R> {
    public ANFunction9(String name, MethodHandle handle, Class<R> returnClass) {
        super(name, handle, returnClass);
    }

    public Optional<R> invoke(P1 param1, P2 param2, P3 param3, P4 param4, P5 param5, P6 param6, P7 param7, P8 param8, P9 param9) {
        return invokeRaw(param1, param2, param3, param4, param5, param6, param7, param8, param9);
    }
}
