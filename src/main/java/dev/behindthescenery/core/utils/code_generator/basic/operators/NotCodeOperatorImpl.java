package dev.behindthescenery.core.utils.code_generator.basic.operators;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.operators.NotCodeOperator;

public class NotCodeOperatorImpl implements NotCodeOperator {

    protected CodeInputSupport input;

    public NotCodeOperatorImpl(CodeInputSupport input) {
        this.input = input;
    }

    @Override
    public Class<?> getInputType() {
        return Boolean.class;
    }

    @Override
    public CodeElement copy() {
        return new NotCodeOperatorImpl(input);
    }

    @Override
    public CodeElement copyEmpty() {
        return copy();
    }

    @Override
    public String generateCodeInput(GenerationCodeContext codeContext) {
        String str = input.generateCodeInput(codeContext);
        if(str.isEmpty()) {
            str = codeContext.getCodeStyle().generationCodeInput(input, codeContext);

            if(str.isEmpty()) {
                str = "error";
            }
        }

        return "!(" + str + ")";
    }
}
