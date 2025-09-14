package dev.behindthescenery.core.arosnatives.functions;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class ANFunction3<P1, P2, P3, R> extends ANRawFunction<R> {
    public ANFunction3(String name, MethodHandle handle, Class<R> returnClass) {
        super(name, handle, returnClass);
    }

    public Optional<R> invoke(P1 param1, P2 param2, P3 param3) {
        return invokeRaw(param1, param2, param3);
    }
}
