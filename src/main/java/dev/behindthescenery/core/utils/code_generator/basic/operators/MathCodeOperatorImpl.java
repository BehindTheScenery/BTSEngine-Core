package dev.behindthescenery.core.utils.code_generator.basic.operators;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.operators.MathCodeOperator;

public class MathCodeOperatorImpl implements MathCodeOperator {

    protected MathOperator operator;
    protected CodeInputSupport[] inputs;
    protected Class<?> outType;

    public MathCodeOperatorImpl(MathOperator operator, CodeInputSupport... inputs) {
        this(operator, inputs[0].getInputType(), inputs);
    }

    public MathCodeOperatorImpl(MathOperator operator, Class<?> outType, CodeInputSupport... inputs) {
        this.operator = operator;
        this.inputs = inputs;
        this.outType = outType;
    }

    @Override
    public MathOperator getMathType() {
        return operator;
    }

    @Override
    public Class<?> getInputType() {
        return outType;
    }

    @Override
    public String generateCodeInput(GenerationCodeContext codeContext) {
        return operator.applyFormat(inputs, codeContext);
    }

    @Override
    public CodeElement copy() {
        return new MathCodeOperatorImpl(operator, outType, inputs);
    }

    @Override
    public CodeElement copyEmpty() {
        return copy();
    }
}
