package dev.behindthescenery.core.utils.code_generator.api.generator;

public interface GenerationCodeContext {

    GeneratorCodeStyle getCodeStyle();

    GenerationCodeTypeConverter getTypeConverter();

    GeneratorCodeMessageBox getMessageBox();

    GeneratorCodeMessageBox getDebugMessageBox();
}
