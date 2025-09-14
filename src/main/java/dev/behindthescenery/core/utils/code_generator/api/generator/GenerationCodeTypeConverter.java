package dev.behindthescenery.core.utils.code_generator.api.generator;

public interface GenerationCodeTypeConverter {

    String convertType(Object classType);

    String convertType(Class<?> classType);
}
