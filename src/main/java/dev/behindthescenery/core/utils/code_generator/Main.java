package dev.behindthescenery.core.utils.code_generator;

import dev.behindthescenery.core.utils.code_generator.api.FunctionCodeElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeListener;
import dev.behindthescenery.core.utils.code_generator.api.operators.ConditionCodeOperator;
import dev.behindthescenery.core.utils.code_generator.api.operators.MathCodeOperator;
import dev.behindthescenery.core.utils.code_generator.basic.*;
import dev.behindthescenery.core.utils.code_generator.basic.operators.CastCodeOperatorImpl;
import dev.behindthescenery.core.utils.code_generator.basic.operators.ConditionCodeOperatorImpl;
import dev.behindthescenery.core.utils.code_generator.basic.operators.MathCodeOperatorImpl;
import dev.behindthescenery.core.utils.code_generator.basic.operators.NotCodeOperatorImpl;
import dev.behindthescenery.core.utils.code_generator.basic.operators.loop.ForLoopOperatorImpl;
import dev.behindthescenery.core.utils.code_generator.supports.glsl.generator.GlslCodeContext;

public class Main {
    public static void main(String[] args) {

        BtsCodeGenerator codeGenerator = new BtsCodeGenerator(new GlslCodeContext());

        FunctionCodeElementImpl functionCodeElement =
                new FunctionCodeElementImpl(
                        "main",
                        new FunctionCodeElementImpl.OutputImpl(Void.class)
                );
        functionCodeElement.setType(FunctionCodeElement.FunctionType.Custom);

        codeGenerator.addFunction(functionCodeElement);
        codeGenerator.addGeneratorListener("main", new ClassChangerListener());

        StringBuilder builder = new StringBuilder();
        codeGenerator.generateCode(builder);

        System.out.println(builder.toString());
    }

    protected static class ClassChangerListener implements GenerationCodeListener {

        @Override
        public void generateFragment(FunctionCodeElement function, GenerationCodeContext context) {

            ConstructorCodeElementImpl codeElement = new ConstructorCodeElementImpl(
                    Void.class,
                    new ObjectCodeInput(10)
            );

            FunctionCodeElementImpl include =
                    new FunctionCodeElementImpl(
                            "test", "test($0)",
                            new FunctionCodeElementImpl.OutputImpl(Void.class),
                            new FunctionCodeElementImpl.InputImpl("fob", Void.class, codeElement)
                    );
            function.addCode(new VariableSetCodeElementImpl("test", include));


            CastCodeOperatorImpl castCodeOperator = new CastCodeOperatorImpl(Double.class, codeElement);

            CastCodeOperatorImpl castCodeOperator1 = new CastCodeOperatorImpl(Float.class, castCodeOperator);
            CastCodeOperatorImpl castCodeOperator2 = new CastCodeOperatorImpl(Double.class, castCodeOperator1);
            VariableCodeElementImpl vr_1 = new VariableCodeElementImpl("Test", Integer.class, castCodeOperator2);
            function.addCode(vr_1);

            VariableCodeElementImpl var = new VariableCodeElementImpl("var_test", Double.class,
                    new MathCodeOperatorImpl(MathCodeOperator.MathOperator.MULTIPLY, new ObjectCodeInput(20), new ObjectCodeInput(10), new ObjectCodeInput(50)));
            function.addCode(var);
            VariableSetCodeElementImpl setTest =
                    new VariableSetCodeElementImpl("test", var);
            function.addCode(setTest);

            ConditionCodeOperatorImpl cond =
                    new ConditionCodeOperatorImpl(ConditionCodeOperator.ComparisonOperator.EQUAL, new ObjectCodeInput(10), new ObjectCodeInput(5));

            NotCodeOperatorImpl notCodeOperator = new NotCodeOperatorImpl(new ObjectCodeInput(true));

            ConditionCodeOperatorImpl cond2 =
                    new ConditionCodeOperatorImpl(ConditionCodeOperator.ComparisonOperator.EQUAL, notCodeOperator);

            cond.addElement(cond2, true);

            function.addCode(cond);

            ForLoopOperatorImpl forLoopOperator =
                    new ForLoopOperatorImpl("int_i", 0, 100, 5);

            forLoopOperator.addCodeElement(vr_1);

            ForLoopOperatorImpl forLoopOperator2 =
                    new ForLoopOperatorImpl("int_i2", 0, 100, 5);
            forLoopOperator.addCodeElement(forLoopOperator2);

            function.addCode(forLoopOperator);
        }
    }
}