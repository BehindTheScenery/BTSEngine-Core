package dev.behindthescenery.core.utils.code_generator.supports.glsl.generator;

import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeTypeConverter;
import dev.behindthescenery.core.utils.code_generator.api.generator.GeneratorCodeMessageBox;
import dev.behindthescenery.core.utils.code_generator.api.generator.GeneratorCodeStyle;

public class GlslCodeContext implements GenerationCodeContext {

    protected GlslCodeStyle codeStyle = new GlslCodeStyle();
    protected GlslTypeConverter converter = new GlslTypeConverter();
    protected GlslMessageBox messageBox = new GlslMessageBox();
    protected GlslMessageBox debugNMessageBox = new GlslMessageBox();

    @Override
    public GeneratorCodeStyle getCodeStyle() {
        return codeStyle;
    }

    @Override
    public GenerationCodeTypeConverter getTypeConverter() {
        return converter;
    }

    @Override
    public GeneratorCodeMessageBox getMessageBox() {
        return messageBox;
    }

    @Override
    public GeneratorCodeMessageBox getDebugMessageBox() {
        return debugNMessageBox;
    }
}
