package dev.behindthescenery.core.utils.code_generator.basic;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public class ObjectCodeInput implements CodeInputSupport {

    public Object object;

    public ObjectCodeInput(Object object) {
        this.object = object;
    }

    @Override
    public CodeElement copy() {
        return new ObjectCodeInput(object);
    }

    @Override
    public CodeElement copyEmpty() {
        return copy();
    }

    @Override
    public Class<?> getInputType() {
        return object.getClass();
    }

    @Override
    public String generateCodeInput(GenerationCodeContext codeContext) {
        return codeContext.getTypeConverter().convertType(object);
    }
}
