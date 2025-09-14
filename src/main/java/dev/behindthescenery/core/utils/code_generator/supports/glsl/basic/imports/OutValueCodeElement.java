package dev.behindthescenery.core.utils.code_generator.supports.glsl.basic.imports;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.basic.ImportCodeElementImpl;

public class OutValueCodeElement extends ImportCodeElementImpl {

    protected Class<?> value;

    public OutValueCodeElement(Object value, String name) {
        this(value.getClass(), name);
    }

    public OutValueCodeElement(Class<?> value, String name) {
        super("out", name, true);
        this.value = value;
    }

    @Override
    public String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {
        return "out " + codeContext.getTypeConverter().convertType(value) + " " + name;
    }
}
