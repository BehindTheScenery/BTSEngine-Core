package dev.behindthescenery.core.utils.code_generator.supports.glsl.generator;

import dev.behindthescenery.core.utils.code_generator.CodeGeneratorStep;
import dev.behindthescenery.core.utils.code_generator.api.*;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.generator.GeneratorCodeStyle;
import dev.behindthescenery.core.utils.code_generator.utils.CodeGeneratorFormatter;

/**
 * Генератор код для GLSL <br>
 * ConstructorCodeElement
 * FunctionCodeElement
 * VariableCodeElement
 * VariableSetCodeElement
 */
public class GlslCodeStyle implements GeneratorCodeStyle {


    @Override
    public String generationCodeFragment(int tabs, CodeElement el, CodeGeneratorStep step, GenerationCodeContext context) {
        context.getDebugMessageBox().info(methodMessage("generationCodeFragment", "Generation CodeFragment: " + el.getClass().getName() + " Step: " + step.name()));

        if(el instanceof ConstructorCodeElement element) {
            switch (step) {
                case AttachToVariable -> {
                    return generateCodeInputForElement(element, context);
                }
            }
        }

        if(el instanceof VariableCodeElement element) {
            context.getDebugMessageBox().info(methodMessage("generationCodeFragment", "Generate Variable: " + element.getName()));

            final VariableCodeElement.VariableType variableType = element.getVariableType();

            switch (step) {
                case Fields, MethodBody -> {
                    return createOutput(
                            tabs,
                            varibaleToString(variableType.isConstant() ? "const" : "", element, context)
                    );
                }
                case AttachToVariable -> {
                    return createOutput(tabs, element.getName());
                }
            }
        }

        if(el instanceof VariableSetCodeElement element) {
            context.getDebugMessageBox().info(methodMessage("generationCodeFragment", "Generate Variable Set: " + element.getVariableName()));

            if (step == CodeGeneratorStep.MethodBody) {
                return createOutput(tabs, element.getVariableName() +  generateCodeInputForElementWithAttach(element.getInput(), context));
            }
        }

        if(el instanceof FunctionCodeElement element) {
            final FunctionCodeElement.FunctionType functionType = element.getFunctionType();
            context.getDebugMessageBox().info(methodMessage("generationCodeFragment", "Generate Function: " + element.getName() + " with type:" + functionType.name()));

            if(step == CodeGeneratorStep.Method && functionType.isCustom()) {
                return generateFunction(tabs, element, context);
            }

            if(step == CodeGeneratorStep.MethodBody) {
                return createOutput(tabs, generateCodeInputForElement(element, context));
            }

            if(step == CodeGeneratorStep.AttachToVariable) {
                return generateCodeInputForElement(element, context);
            }

        }

        return "";
    }

    protected String generateFunction(int tabs, FunctionCodeElement element, GenerationCodeContext context) {
        StringBuilder builder = new StringBuilder();

        final String out = context.getTypeConverter().convertType(element.getOutput().getValueType());
        builder.append(out).append(" ").append(element.getName()).append("(")
                .append(CodeGeneratorFormatter.formatInputs(element, context)).append(") {\n");

        for (CodeElement codeElement : element.getFunctionBody()) {
            final String gen = generateCodeElement(tabs + 1, CodeGeneratorStep.MethodBody, codeElement, context);
            if(gen.isEmpty()) continue;

            builder.append(gen);

            if(codeElement.useEndLineSymbol()) {
                builder.append(context.getCodeStyle().lineEndSymbol());
            }

            builder.append("\n");
        }

        builder.append("} \n\n");

        return builder.toString();
    }

    protected String varibaleToString(String prefix, VariableCodeElement element, GenerationCodeContext context) {
        final Class<?> elementType = element.getType();
        final CodeInputSupport elementInput = element.getInputObject();
        return (prefix.isEmpty() ? "" : prefix + " ") + context.getTypeConverter().convertType(elementType) + " " + element.getName() + generateCodeInputForElementWithAttach(elementInput, context);
    }

    protected String generateCodeInputForElementWithAttach(CodeInputSupport codeInputSupport, GenerationCodeContext context) {
        String value = generateCodeInputForElement(codeInputSupport, context);
        if(value.isEmpty()) return "";
        return " = " + value;
    }

    protected String generateCodeInputForElement(CodeInputSupport codeInputSupport, GenerationCodeContext context) {
        if(codeInputSupport == null) return "";

        String d1 = codeInputSupport.generateCodeInput(context);
        if(d1.isEmpty()) {
            d1 = context.getCodeStyle().generationCodeInput(codeInputSupport, context);
        }
        return d1;
    }

    protected String generateCodeElement(int tabs, CodeGeneratorStep step, CodeElement codeElement, GenerationCodeContext context) {
        if(codeElement == null) return "";

        String value = codeElement.generateCodeFragment(tabs, step, context);
        if(value.isEmpty()) {
            value = context.getCodeStyle().generationCodeFragment(tabs, codeElement, step, context);
        }

        return value;
    }

    protected String createOutput(int tabs, String value) {
        return "    ".repeat(Math.max(0, tabs)) + value;
    }

    protected String error(int tabs, String error) {
        return createOutput(tabs, error);
    }

    protected String methodToString(String method) {
        return "[GlslCodeStyle::" + method + "]";
    }

    protected String methodMessage(String method, String message) {
        return methodToString(method) + ": " + message;
    }

    @Override
    public String generationCodeInput(CodeInputSupport el, GenerationCodeContext context) {
        if(el instanceof ConstructorCodeElement element) {
            final String str = context.getTypeConverter().convertType(element.getType());
            final String formated = CodeGeneratorFormatter.generateFormatInputs(element.getInputs());

            return str + "(" + CodeGeneratorFormatter.formatStringWithInputs(context, formated, element.getInputs()) + ")";
        }

        return "error";
    }
}
