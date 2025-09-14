package dev.behindthescenery.core.utils.code_generator.api;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public interface CodeElement {

    default String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {
        return "";
    }

    CodeElement copy();

    CodeElement copyEmpty();

    default boolean useEndLineSymbol() {
        return true;
    }
}
