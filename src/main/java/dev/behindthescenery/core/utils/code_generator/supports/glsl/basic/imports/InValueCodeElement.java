package dev.behindthescenery.core.utils.code_generator.supports.glsl.basic.imports;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.basic.ImportCodeElementImpl;

public class InValueCodeElement extends ImportCodeElementImpl {

    protected Class<?> value;

    public InValueCodeElement(Object value, String name) {
        this(value.getClass(), name);
    }

    public InValueCodeElement(Class<?> value, String name) {
        super("", name, true);
        this.value = value;
    }

    @Override
    public int getPriority() {
        return 500;
    }

    @Override
    public String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {
        return "in " + codeContext.getTypeConverter().convertType(value) + " " + name;
    }
}
