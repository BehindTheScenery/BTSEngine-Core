package dev.behindthescenery.core.utils.code_generator.api;

import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public interface CodeInputSupport extends CodeElement {

    Class<?> getInputType();

    default String generateCodeInput(GenerationCodeContext codeContext) {
        return "";
    }
}
