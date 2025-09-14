package dev.behindthescenery.core.utils.code_generator.api;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public interface CommentCodeElement extends CodeElement {

    String getComment();

    @Override
    default String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {
        return "   ".repeat(Math.max(0, tabs)) + "/*" + getComment() + "*/";
    }
}
