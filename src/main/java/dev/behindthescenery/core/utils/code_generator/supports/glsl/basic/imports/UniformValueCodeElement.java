package dev.behindthescenery.core.utils.code_generator.supports.glsl.basic.imports;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.basic.ImportCodeElementImpl;

public class UniformValueCodeElement extends ImportCodeElementImpl {

    protected Class<?> value;

    public UniformValueCodeElement(Object value, String name) {
        this(value.getClass(), name);
    }

    public UniformValueCodeElement(Class<?> value, String name) {
        super("", name, true);
        this.value = value;
    }

    @Override
    public int getPriority() {
        return 250;
    }

    @Override
    public String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {
        return "uniform " + codeContext.getTypeConverter().convertType(value) + " " + name;
    }
}
