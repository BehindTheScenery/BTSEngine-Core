package dev.behindthescenery.core.utils.code_generator.api;

public interface ConstructorCodeElement extends CodeInputSupport {

    Class<?> getType();

    CodeInputSupport[] getInputs();

}
