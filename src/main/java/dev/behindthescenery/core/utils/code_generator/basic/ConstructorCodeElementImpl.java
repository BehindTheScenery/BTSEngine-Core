package dev.behindthescenery.core.utils.code_generator.basic;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.ConstructorCodeElement;

public class ConstructorCodeElementImpl implements ConstructorCodeElement {

    protected Class<?> classType;
    protected CodeInputSupport[] inputSupports;

    public ConstructorCodeElementImpl(Class<?> classType, CodeInputSupport... inputSupports) {
        this.classType = classType;
        this.inputSupports = inputSupports;
    }

    @Override
    public Class<?> getType() {
        return classType;
    }

    @Override
    public CodeInputSupport[] getInputs() {
        return inputSupports;
    }

    @Override
    public Class<?> getInputType() {
        return classType;
    }

    @Override
    public CodeElement copy() {
        return new ConstructorCodeElementImpl(classType, inputSupports);
    }

    @Override
    public CodeElement copyEmpty() {
        return copy();
    }
}
