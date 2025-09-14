package dev.behindthescenery.core.utils.code_generator.basic.operators.loop;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.CodeChangeFunction;
import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.operators.loop.ForLoopOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ForLoopOperatorImpl implements ForLoopOperator, CodeChangeFunction {

    protected String varName;
    protected int startIndex;
    protected int endIndex;
    protected int step;
    protected List<CodeElement> codeElements;

    public ForLoopOperatorImpl(String varName, int startIndex, int endIndex) {
        this(varName, startIndex, endIndex, 1, new ArrayList<>());
    }

    public ForLoopOperatorImpl(String varName, int startIndex, int endIndex, List<CodeElement> codeElements) {
        this(varName, startIndex, endIndex, 1, codeElements);
    }

    public ForLoopOperatorImpl(String varName, int startIndex, int endIndex, int step) {
        this(varName, startIndex, endIndex, step, new ArrayList<>());
    }

    public ForLoopOperatorImpl(String varName, int startIndex, int endIndex, int step, List<CodeElement> codeElements) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.step = step;
        this.codeElements = codeElements;
        this.varName = varName;
    }

    @Override
    public void addCodeElement(CodeElement element) {
        this.codeElements.add(element);
    }

    @Override
    public void addCodeElement(Collection<? extends CodeElement> element) {
        this.codeElements.addAll(element);
    }

    @Override
    public String getVariableName() {
        return varName;
    }

    @Override
    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public int getEndIndex() {
        return endIndex;
    }

    @Override
    public int getStep() {
        return step;
    }

    @Override
    public List<CodeElement> getLoopBody() {
        return codeElements;
    }

    @Override
    public String generateCodeFragment(int tabs, CodeGeneratorStep step, GenerationCodeContext codeContext) {
        StringBuilder builder = new StringBuilder();

        builder.append("    ".repeat(Math.max(0, tabs)));
        builder.append("for(int ").append(varName).append(" = ").append(startIndex).append("; ").append(varName)
                .append(" < ").append(endIndex).append("; ").append(varName).append("+=").append(this.step).append(") { \n");

        for (CodeElement codeElement : getLoopBody()) {

            String value = codeElement.generateCodeFragment(tabs + 1, step, codeContext);
            if(value.isEmpty()) {
                value = codeContext.getCodeStyle().generationCodeFragment(tabs + 1, codeElement, step, codeContext);

                if(value.isEmpty())
                    value = "error";
            }

            builder.append(value);

            if(codeElement.useEndLineSymbol())
                builder.append(codeContext.getCodeStyle().lineEndSymbol());
            builder.append("\n");
        }

        builder.append("    ".repeat(Math.max(0, tabs))).append("}\n");

        return builder.toString();
    }

    @Override
    public CodeElement copy() {
        return new ForLoopOperatorImpl(varName, startIndex, endIndex, step, codeElements);
    }

    @Override
    public CodeElement copyEmpty() {
        return new ForLoopOperatorImpl(varName, startIndex, endIndex, step);
    }
}
