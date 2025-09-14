package dev.behindthescenery.core.utils.code_generator.api.operators;

import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.CodeOperatorInputElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public interface MathCodeOperator extends CodeOperatorInputElement {

    MathOperator getMathType();

    enum MathOperator {
        PLUS("+"),
        MINUS("-"),
        DIVIDE("/"),
        MULTIPLY("*");

        private final String operator;

        MathOperator(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }

        public String applyFormat(CodeInputSupport[] inputSupport, GenerationCodeContext context) {
            if (inputSupport == null) {
                throw new IllegalArgumentException("Input array cannot be null");
            }
            if (inputSupport.length < 2) {
                throw new IllegalArgumentException("Need at least 2 elements for mathematical operation");
            }

            StringBuilder builder = new StringBuilder();

            builder.append("(");
            for (int i = 0; i < inputSupport.length; i++) {
                if (i > 0) {
                    builder.append(" ").append(operator).append(" ");
                }

                final CodeInputSupport el = inputSupport[i];
                String out = el.generateCodeInput(context);
                if(out.isEmpty()) {
                    out = context.getCodeStyle().generationCodeInput(el, context);
                }

                if(out.isEmpty())
                    out = "error";

                builder.append(out);
            }
            builder.append(")");

            return builder.toString();
        }
    }
}
