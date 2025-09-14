package dev.behindthescenery.core.arosnatives.functions;

import dev.behindthescenery.core.BtsCore;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.MethodHandle;
import java.util.Optional;

public abstract class ANRawFunction<R> {
    private final String name;
    private final MethodHandle handle;
    private final Class<R> returnClass;

    public ANRawFunction(String name, MethodHandle handle, Class<R> returnClass) {
        this.name = name;
        this.handle = handle;
        this.returnClass = returnClass;
    }

    @ApiStatus.Internal
    public Optional<R> invokeRaw(Object... args) {
        try {
            Object result = handle.invokeWithArguments(args);
            if (result == null) return Optional.empty();

            if (returnClass.isInstance(result))
                return Optional.of(returnClass.cast(result));

            BtsCore.LOGGER.warn("Function {} returned {}, but {} was expected, returning null", name, result.getClass(), returnClass);
        } catch (Throwable t) {
            BtsCore.LOGGER.error("Failed to invoke function {}", name, t);
        }
        return Optional.empty();
    }
}
