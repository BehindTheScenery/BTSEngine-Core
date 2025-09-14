package dev.behindthescenery.core.utils.code_generator.api;

public interface VariableCodeElement extends CodeInputSupport {

    String getName();

    Class<?> getType();

    CodeInputSupport getInputObject();

    VariableType getVariableType();

    enum VariableType {
        Field,
        FieldConstant,
        Local,
        LocalConstant;

        public boolean isField() {
            return this == Field || this == FieldConstant;
        }

        public boolean isConstant() {
            return this == FieldConstant || this == LocalConstant;
        }

        public boolean isLocal() {
            return this == Local || this == LocalConstant;
        }
    }
}
