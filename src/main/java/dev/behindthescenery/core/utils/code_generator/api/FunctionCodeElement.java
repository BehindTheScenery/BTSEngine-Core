package dev.behindthescenery.core.utils.code_generator.api;

import java.util.Collection;
import java.util.List;

public interface FunctionCodeElement extends CodeInputSupport {

    /**
     * Имя метода
     */
    String getName();

    /**
     * Вызов метода в генерируемом коде. Знак '$' и цифра указывает на то что это за входящий параметр метода
     * {@code someMethod($0, $1)}
     */
    String getInvokeMethod();

    Output getOutput();

    Input[] getInputs();

    List<CodeElement> getFunctionBody();

    /**
     * Указывает какого типа это метод <br>
     * {@code Include} - Встроенный метод (генерация тела не будет) <br>
     * {@code Custom} - Пользовательский метод (будет сгенерировано тело) <br>
     */
    FunctionType getFunctionType();

    FunctionCodeElement copyWithNewParams(Output output, Collection<Input> inputs);

    void addCode(CodeElement element);

    void addCode(Collection<? extends CodeElement> element);

    interface Input {

        String getName();

        Class<?> getValueType();

        CodeInputSupport getInputValue();

        Object getDefaultValue();

        Input copyWithValue(Object value);

        Input copyWithValue(CodeInputSupport inputSupport);

    }

    interface Output {

        String getName();

        Class<?> getValueType();

    }

    enum FunctionType {
        Include,
        Custom;

        public boolean isInclude() {
            return this == Include;
        }

        public boolean isCustom() {
            return this == Custom;
        }
    }
}
