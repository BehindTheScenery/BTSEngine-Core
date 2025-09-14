package dev.behindthescenery.core.utils.code_generator.api.operators;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.CodeOperatorElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

import java.util.List;

public interface ConditionCodeOperator extends CodeOperatorElement {

    List<CodeElement> getIfBody();

    List<CodeElement> getElseBody();

    void addElement(CodeElement element, boolean isIf);

    ComparisonOperator getComparisonOperator();

    LogicalOperator getLogicalOperator();

    @Override
    default boolean useEndLineSymbol() {
        return false;
    }

    enum LogicalOperator {
        AND("&&"),
        OR("||");

        private final String symbol;

        LogicalOperator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    enum ComparisonOperator {
        EQUAL("=="),
        NOT_EQUAL("!="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUAL("<="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUAL(">=");

        private final String symbol;

        ComparisonOperator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public String applyFormat(LogicalOperator logicalOperator, CodeInputSupport[] inputSupport, GenerationCodeContext context) {
            if (inputSupport == null) {
                throw new IllegalArgumentException("Input array cannot be null");
            }
            if (inputSupport.length == 0) {
                throw new IllegalArgumentException("Need at least more elements for comparison");
            }

            if (inputSupport.length == 1) {
                String str = inputSupport[0].generateCodeInput(context);
                if (str.isEmpty()) {
                    str = context.getCodeStyle().generationCodeInput(inputSupport[0], context);
                    if (str.isEmpty()) {
                        str = "error";
                    }
                }
                return str;
            }


            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < inputSupport.length; i += 2) {
                if (i > 0) {
                    builder.append(" ").append(logicalOperator.getSymbol()).append(" ");
                }

                CodeInputSupport el1 = inputSupport[i];
                CodeInputSupport el2 = inputSupport[i + 1];

                String str = el1.generateCodeInput(context);
                if(str.isEmpty()) {
                    str = context.getCodeStyle().generationCodeInput(el1, context);

                    if(str.isEmpty()) {
                        str = "error";
                    }
                }

                builder.append(str).append(" ").append(symbol).append(" ");

                str = el2.generateCodeInput(context);
                if(str.isEmpty()) {
                    str = context.getCodeStyle().generationCodeInput(el2, context);

                    if(str.isEmpty()) {
                        str = "error";
                    }
                }

                builder.append(str);
            }

            return builder.toString();
        }
    }
}
