package dev.behindthescenery.core.utils.code_generator.api.generator;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;

public interface GeneratorCodeStyle {

    String generationCodeFragment(int tabs, CodeElement element, CodeGeneratorStep step, GenerationCodeContext context);

    String generationCodeInput(CodeInputSupport inputSupport, GenerationCodeContext context);

    default String lineEndSymbol() {
        return ";";
    }
}
