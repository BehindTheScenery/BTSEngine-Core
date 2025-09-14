package dev.behindthescenery.core.utils.code_generator.basic;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.VariableSetCodeElement;

public class VariableSetCodeElementImpl implements VariableSetCodeElement {

    protected String variableName;
    protected CodeInputSupport input;

    public VariableSetCodeElementImpl(String name, Object value) {
        this(name, new ObjectCodeInput(value));
    }

    public VariableSetCodeElementImpl(String name, CodeInputSupport value) {
        this.variableName = name;
        this.input = value;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

    @Override
    public CodeInputSupport getInput() {
        return input;
    }

    @Override
    public Class<?> getInputType() {
        return input.getInputType();
    }

    @Override
    public CodeElement copy() {
        return new VariableSetCodeElementImpl(variableName, input);
    }

    @Override
    public CodeElement copyEmpty() {
        return copy();
    }
}
