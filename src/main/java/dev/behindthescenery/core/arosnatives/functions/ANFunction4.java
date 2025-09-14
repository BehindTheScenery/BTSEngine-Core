package dev.behindthescenery.core.arosnatives.functions;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class ANFunction4<P1, P2, P3, P4, R> extends ANRawFunction<R> {
    public ANFunction4(String name, MethodHandle handle, Class<R> returnClass) {
        super(name, handle, returnClass);
    }

    public Optional<R> invoke(P1 param1, P2 param2, P3 param3, P4 param4) {
        return invokeRaw(param1, param2, param3, param4);
    }
}
