package dev.behindthescenery.core.utils.code_generator.basic.operators;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.operators.CastCodeOperator;

public class CastCodeOperatorImpl implements CastCodeOperator {

    protected Class<?> classCast;
    protected CodeInputSupport inputSupport;

    public CastCodeOperatorImpl(Class<?> classCast, CodeInputSupport inputSupport) {
        this.classCast = classCast;
        this.inputSupport = inputSupport;
    }

    @Override
    public Class<?> getInputType() {
        return classCast;
    }

    @Override
    public CodeElement copy() {
        return new CastCodeOperatorImpl(classCast, inputSupport);
    }

    @Override
    public CodeElement copyEmpty() {
        return copy();
    }

    @Override
    public String generateCodeInput(GenerationCodeContext codeContext) {
        String s = "((" + codeContext.getTypeConverter().convertType(classCast) + ") ";

        String str = inputSupport.generateCodeInput(codeContext);
        if(str.isEmpty()) {
            str = codeContext.getCodeStyle().generationCodeInput(inputSupport, codeContext);
        }

        if(str.isEmpty()) str = "Can't Cast!";

        return s + str + ")";
    }
}
