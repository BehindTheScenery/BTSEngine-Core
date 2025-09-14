package dev.behindthescenery.core.arosnatives.functions;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

public class ANFunction1<P1, R> extends ANRawFunction<R> {
    public ANFunction1(String name, MethodHandle handle, Class<R> returnClass) {
        super(name, handle, returnClass);
    }

    public Optional<R> invoke(P1 param1) {
        return invokeRaw(param1);
    }
}
