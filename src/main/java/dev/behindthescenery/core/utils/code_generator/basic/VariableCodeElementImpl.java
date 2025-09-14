package dev.behindthescenery.core.utils.code_generator.basic;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.VariableCodeElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public class VariableCodeElementImpl implements VariableCodeElement {

    protected String name;
    protected Class<?> valueType;
    protected CodeInputSupport inputSupport;
    protected VariableType type = VariableType.Local;

    public VariableCodeElementImpl(String name, Class<?> valueType) {
        this(name, valueType, null);
    }

    public VariableCodeElementImpl(String name, Class<?> valueType, CodeInputSupport inputSupport) {
        this.name = name;
        this.valueType = valueType;
        this.inputSupport = inputSupport;
    }

    public VariableCodeElementImpl setType(VariableType type) {
        this.type = type;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return valueType;
    }

    @Override
    public CodeInputSupport getInputObject() {
        return inputSupport;
    }

    @Override
    public VariableType getVariableType() {
        return type;
    }

    @Override
    public Class<?> getInputType() {
        return valueType;
    }

    @Override
    public CodeElement copy() {
        return new VariableCodeElementImpl(name, valueType, inputSupport);
    }

    @Override
    public CodeElement copyEmpty() {
        return new VariableCodeElementImpl(name, valueType);
    }

    @Override
    public String generateCodeInput(GenerationCodeContext codeContext) {
        return name;
    }
}
