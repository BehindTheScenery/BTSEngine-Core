package dev.behindthescenery.core.utils.code_generator.basic;

import dev.behindthescenery.core.utils.code_generator.api.CodeChangeFunction;
import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.CodeInputSupport;
import dev.behindthescenery.core.utils.code_generator.api.FunctionCodeElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.utils.CodeGeneratorFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FunctionCodeElementImpl implements FunctionCodeElement, CodeChangeFunction {

    protected String name;
    protected String invokeMethod;
    protected Output output;
    protected Input[] inputs;
    protected List<CodeElement> codeElements = new ArrayList<>();
    protected FunctionType type = FunctionType.Include;

    public FunctionCodeElementImpl(String name, Output output, Input... inputs) {
        this(name, createInvokeMethod(name, inputs), output, inputs);
    }

    public FunctionCodeElementImpl(String name, String invokeMethod, Output output, Input... inputs) {
        this.name = name;
        this.output = output;
        this.inputs = inputs;
        this.invokeMethod = invokeMethod;
    }

    public FunctionCodeElementImpl(String name, String invokeMethod, List<CodeElement> codeElements, Output output, Input... inputs) {
        this.name = name;
        this.output = output;
        this.inputs = inputs;
        this.codeElements = codeElements;
        this.invokeMethod = invokeMethod;
    }

    protected static String createInvokeMethod(String name, Input... inputs) {
        CodeInputSupport[] inputSupports = new CodeInputSupport[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            inputSupports[i] = inputs[i].getInputValue();
        }

        return name + "(" + CodeGeneratorFormatter.generateFormatInputs(inputSupports) + ")";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInvokeMethod() {
        return invokeMethod;
    }

    @Override
    public Output getOutput() {
        return output;
    }

    @Override
    public Input[] getInputs() {
        return inputs;
    }

    @Override
    public List<CodeElement> getFunctionBody() {
        return codeElements;
    }

    @Override
    public FunctionType getFunctionType() {
        return type;
    }

    @Override
    public FunctionCodeElement copyWithNewParams(Output output, Collection<Input> inputs) {
        Input[] i = new Input[inputs.size()];
        int f = 0;
        for (Input input : inputs) {
            i[f] = input;
            f++;
        }

        return new FunctionCodeElementImpl(name, invokeMethod, output, i);
    }

    @Override
    public void addCode(CodeElement element) {
        addCodeElement(element);
    }

    @Override
    public void addCode(Collection<? extends CodeElement> element) {
        addCodeElement(element);
    }

    @Override
    public CodeElement copy() {
        return new FunctionCodeElementImpl(name, invokeMethod, codeElements, output, inputs).setType(type);
    }

    @Override
    public CodeElement copyEmpty() {
        return new FunctionCodeElementImpl(name, invokeMethod, output, inputs).setType(type);
    }

    public FunctionCodeElementImpl setType(FunctionType type) {
        this.type = type;
        return this;
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
    public Class<?> getInputType() {
        return output.getValueType();
    }

    @Override
    public String generateCodeInput(GenerationCodeContext codeContext) {
        CodeInputSupport[] supports = new CodeInputSupport[inputs.length];

        for (int i = 0; i < inputs.length; i++) {
            supports[i] = inputs[i].getInputValue();
        }

        return CodeGeneratorFormatter.formatStringWithInputs(codeContext, invokeMethod, supports);
    }

    public static class InputImpl implements Input {

        protected String name;
        protected Class<?> valueType;
        protected CodeInputSupport inputSupport;
        protected Object defaultValue;

        public InputImpl(String name, Object object) {
            this(name, object.getClass(), object);
        }

        public InputImpl(String name, Class<?> valueType) {
            this(name, valueType, new Object());
        }

        public InputImpl(String name, Class<?> valueType, Object defaultValue) {
            this(name, valueType, null, defaultValue);
        }

        public InputImpl(String name, Class<?> valueType, CodeInputSupport inputSupport) {
            this(name, valueType, inputSupport, new Object());
        }

        public InputImpl(String name, Class<?> valueType, CodeInputSupport inputSupport, Object defaultValue) {
            this.name = name;
            this.valueType = valueType;
            this.inputSupport = inputSupport;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getValueType() {
            return valueType;
        }

        @Override
        public CodeInputSupport getInputValue() {
            return inputSupport;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }

        @Override
        public Input copyWithValue(Object value) {
            return new InputImpl(name, value.getClass(), new ObjectCodeInput(value));
        }

        @Override
        public Input copyWithValue(CodeInputSupport inputSupport) {
            return new InputImpl(name, inputSupport.getInputType(), inputSupport);
        }
    }

    public static class OutputImpl implements Output {

        protected String name;
        protected Class<?> valueType;

        public OutputImpl(Class<?> valueType) {
            this(valueType.getSimpleName().toLowerCase(), valueType);
        }

        public OutputImpl(String name, Class<?> valueType) {
            this.valueType = valueType;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getValueType() {
            return valueType;
        }
    }
}
