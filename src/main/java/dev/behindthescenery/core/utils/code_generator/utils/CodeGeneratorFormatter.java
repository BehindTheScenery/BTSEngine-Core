package dev.behindthescenery.core.utils.code_generator.utils;

import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.FunctionCodeElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;

public class CodeGeneratorFormatter {

    public static String formatInputs(FunctionCodeElement functionReturn, GenerationCodeContext context) {
        if (functionReturn == null || functionReturn.getInputs() == null) {
            throw new IllegalArgumentException("FunctionReturn or its inputs cannot be null");
        }

        final StringBuilder inputsString = new StringBuilder();
        FunctionCodeElement.Input[] inputs = functionReturn.getInputs();

        // Проверяем, есть ли входные параметры
        if (inputs.length == 0) {
            return "";
        }

        // Формируем строку с параметрами
        for (int i = 0; i < inputs.length; i++) {

            String typeName = context.getTypeConverter().convertType(inputs[i].getValueType());

            inputsString.append(typeName).append(" ").append(inputs[i].getName());

            // Добавляем запятую только если это не последний элемент
            if (i < inputs.length - 1) {
                inputsString.append(", ");
            }
        }

        return inputsString.toString();
    }

    public static String generateFormatInputs(CodeInputSupport... inputSupport) {
        StringBuilder builder = new StringBuilder();

        int len = inputSupport.length;

        for (int i = 0; i < inputSupport.length; i++) {

            builder.append("$").append(i);

            if(i < len - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    public static String formatStringWithInputs(GenerationCodeContext context, String code, CodeInputSupport... inputs) {
        if (code == null) {
            throw new IllegalArgumentException("Code cannot be null");
        }

        if (!code.contains("$") || inputs == null || inputs.length == 0) {
            return code;
        }

        StringBuilder result = new StringBuilder();
        int lastIndex = 0;

        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '$' && i + 1 < code.length() && Character.isDigit(code.charAt(i + 1))) {

                result.append(code, lastIndex, i);

                int start = i + 1;
                while (i + 1 < code.length() && Character.isDigit(code.charAt(i + 1))) {
                    i++;
                }
                String indexStr = code.substring(start, i + 1);
                int index = Integer.parseInt(indexStr);

                if (index >= inputs.length) {
                    throw new IllegalArgumentException("Not enough inputs for placeholder $" + index);
                }

                CodeInputSupport input = inputs[index];

                String signature = input.generateCodeInput(context);
                if(signature.isEmpty()) {
                    signature = context.getCodeStyle().generationCodeInput(input, context);
                }

                if (signature.isEmpty()) {
                    throw new IllegalArgumentException("Input signature at index " + index + " is null");
                }

                result.append(signature);
                lastIndex = i + 1;
            }
        }

        if (lastIndex < code.length()) {
            result.append(code, lastIndex, code.length());
        }

        return result.toString();
    }

}
