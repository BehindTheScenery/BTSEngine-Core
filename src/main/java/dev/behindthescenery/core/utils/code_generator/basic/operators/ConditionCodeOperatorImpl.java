package dev.behindthescenery.core.utils.code_generator.basic.operators;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.operators.ConditionCodeOperator;

import java.util.ArrayList;
import java.util.List;

public class ConditionCodeOperatorImpl implements ConditionCodeOperator {

    protected ComparisonOperator comparisonOperator;
    protected LogicalOperator logicalOperator;
    protected CodeInputSupport[] conditionInputs;
    protected List<CodeElement> ifElements;
    protected List<CodeElement> elseElements;

    public ConditionCodeOperatorImpl(ComparisonOperator comparisonOperator, CodeInputSupport... conditionInputs) {
        this(LogicalOperator.AND, comparisonOperator, conditionInputs);
    }

    public ConditionCodeOperatorImpl(LogicalOperator logicalOperator, ComparisonOperator comparisonOperator, CodeInputSupport... conditionInputs) {
        this(logicalOperator, comparisonOperator, conditionInputs, new ArrayList<>(), new ArrayList<>());
    }

    public ConditionCodeOperatorImpl(LogicalOperator logicalOperator, ComparisonOperator comparisonOperator,
                                     CodeInputSupport[] conditionInputs, List<CodeElement> ifElements, List<CodeElement> elseElements) {
        this.logicalOperator = logicalOperator;
        this.comparisonOperator = comparisonOperator;
        this.conditionInputs = conditionInputs;
        this.ifElements = ifElements;
        this.elseElements = elseElements;
    }

    @Override
    public List<CodeElement> getIfBody() {
        return ifElements;
    }

    @Override
    public List<CodeElement> getElseBody() {
        return elseElements;
    }

    @Override
    public void addElement(CodeElement element, boolean isIf) {
        if(isIf) {
            ifElements.add(element);
        } else elseElements.add(element);
    }

    @Override
    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    @Override
    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    @Override
    public CodeElement copy() {
        return new ConditionCodeOperatorImpl(logicalOperator, comparisonOperator, conditionInputs, ifElements, elseElements);
    }

    @Override
    public CodeElement copyEmpty() {
        return new ConditionCodeOperatorImpl(logicalOperator, comparisonOperator, conditionInputs);
    }

    @Override
    public String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {

        if(step == CodeGeneratorStep.MethodBody) {
            final StringBuilder builder = new StringBuilder();

            builder.repeat("    ", Math.max(0, tabs));
            builder.append("if(").append(comparisonOperator.applyFormat(logicalOperator, conditionInputs, codeContext)).append(") {");

            final boolean hasIf = !getIfBody().isEmpty();

            if(hasIf) {
                builder.append("\n");
                for (CodeElement element : getIfBody()) {

                    String gen = element.generateCodeFragment(tabs + 1, step, codeContext);
                    if(gen.isEmpty()) {
                        gen = codeContext.getCodeStyle().generationCodeFragment(tabs + 1, element, step, codeContext);

                        if(gen.isEmpty()) {
                            gen = "error";
                        }
                    }

                    builder.append(gen).append(element instanceof ConditionCodeOperator ? "" : ";").append("\n");
                }

                builder.repeat("    ", Math.max(0, tabs));
                builder.append("}");
            } else {
                builder.append(" }");
            }
            final boolean hasElse = !getElseBody().isEmpty();

            if(hasElse) {
                builder.append(" else { \n");
                for (CodeElement element : getElseBody()) {
                    String gen = element.generateCodeFragment(tabs + 1, step, codeContext);
                    if(gen.isEmpty()) {
                        gen = codeContext.getCodeStyle().generationCodeFragment(tabs + 1, element, step, codeContext);

                        if(gen.isEmpty()) {
                            gen = "error";
                        }
                    }

                    builder.append(gen).append(element instanceof ConditionCodeOperator ? "" : ";").append("\n");                }
                builder.repeat("    ", Math.max(0, tabs));
                builder.append("} ");
            }

            return builder.toString();
        }

        return "";
    }
}
