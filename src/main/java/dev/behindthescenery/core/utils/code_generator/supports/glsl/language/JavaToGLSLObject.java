package dev.behindthescenery.core.utils.code_generator.supports.glsl.language;

import java.util.Objects;
import java.util.function.Function;

public record JavaToGLSLObject<T>(Class<T> classObject, GLSLType glslType, Function<T, String> convertToGLSL) implements JavaToGlslConverter {

    @Override
    public String invoke(Object obj) {
        return convertToGLSL.apply((T) obj);
    }

    @Override
    public boolean isCurrent(Object obj) {
        return Objects.equals(obj.getClass(), classObject);
    }

    @Override
    public boolean isCurrent(Class<?> cls) {
        return Objects.equals(this.classObject, cls);
    }

    @Override
    public Object createDefault(Class<?> cls) {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class[] { classObject };
    }

    @Override
    public GLSLType getGlslType() {
        return glslType;
    }
}
