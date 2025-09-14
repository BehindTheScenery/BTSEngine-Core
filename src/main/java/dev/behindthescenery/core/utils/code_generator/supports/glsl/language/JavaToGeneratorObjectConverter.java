package dev.behindthescenery.core.utils.code_generator.supports.glsl.language;

public interface JavaToGeneratorObjectConverter {

    String invoke(Object obj);

    boolean isCurrent(Object obj);

    boolean isCurrent(Class<?> cls);

    Object createDefault(Class<?> cls);

    Class<?>[] getSupportedClasses();
}
