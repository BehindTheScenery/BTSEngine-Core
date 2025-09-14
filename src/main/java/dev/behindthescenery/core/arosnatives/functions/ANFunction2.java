package dev.behindthescenery.core.arosnatives.functions;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class ANFunction2<P1, P2, R> extends ANRawFunction<R> {
    public ANFunction2(String name, MethodHandle handle, Class<R> returnClass) {
        super(name, handle, returnClass);
    }

    public Optional<R> invoke(P1 param1, P2 param2) {
        return invokeRaw(param1, param2);
    }
}
