package dev.behindthescenery.core.utils.code_generator.supports.glsl.generator;

import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeTypeConverter;
import dev.behindthescenery.core.utils.code_generator.supports.glsl.language.GLSLLanguageContext;

public class GlslTypeConverter implements GenerationCodeTypeConverter {

    protected GLSLLanguageContext glslLanguageContext = new GLSLLanguageContext();

    public GlslTypeConverter() {

    }

    @Override
    public String convertType(Object classType) {
        return glslLanguageContext.convertToGlsl(classType);
    }

    @Override
    public String convertType(Class<?> classType) {
        return glslLanguageContext.getGlslType(classType).getGlslName();
    }


}
