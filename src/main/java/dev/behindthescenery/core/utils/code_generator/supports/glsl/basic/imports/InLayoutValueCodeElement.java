package dev.behindthescenery.core.utils.code_generator.supports.glsl.basic.imports;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public class InLayoutValueCodeElement extends InValueCodeElement{

    protected int layout;

    public InLayoutValueCodeElement(Object value, int layout, String name) {
        super(value, name);
        this.layout = layout;
    }

    public InLayoutValueCodeElement(Class<?> value, int layout, String name) {
        super(value, name);
        this.layout = layout;
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    @Override
    public String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {
        return "layout (location = " + layout + ") in " + codeContext.getTypeConverter().convertType(value) + " " + name;
    }
}
