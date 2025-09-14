package dev.behindthescenery.core.utils.code_generator;

import dev.behindthescenery.core.utils.code_generator.api.CodeElement;
import dev.behindthescenery.core.utils.code_generator.api.FunctionCodeElement;
import dev.behindthescenery.core.utils.code_generator.api.ImportCodeElement;
import dev.behindthescenery.core.utils.code_generator.api.VariableCodeElement;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeContext;
import dev.behindthescenery.core.utils.code_generator.api.generator.GenerationCodeListener;

import java.util.*;

public class BtsCodeGenerator {

    protected GenerationCodeContext codeContext;

    public BtsCodeGenerator(GenerationCodeContext codeContext) {
        this.codeContext = codeContext;
    }

    protected List<ImportCodeElement> importElements = new ArrayList<>();
    protected List<VariableCodeElement> classFieldElements = new ArrayList<>();
    protected List<FunctionCodeElement> functionElements = new ArrayList<>();

    protected Map<String, List<GenerationCodeListener>> functionGenerator = new HashMap<>();

    protected int currentFunction;

    public BtsCodeGenerator addImport(ImportCodeElement codeElement) {
        this.importElements.add(codeElement);
        return this;
    }

    public BtsCodeGenerator addImport(ImportCodeElement... codeElement) {
        this.importElements.addAll(List.of(codeElement));
        return this;
    }

    public BtsCodeGenerator addField(VariableCodeElement codeElement) {
        if(codeElement.getVariableType().isLocal())
            return this;

        this.classFieldElements.add(codeElement);
        return this;
    }

    public BtsCodeGenerator addField(VariableCodeElement... codeElement) {
        for (VariableCodeElement variableCodeElement : codeElement) {
            addField(variableCodeElement);
        }
        return this;
    }

    public BtsCodeGenerator addFunction(FunctionCodeElement codeElement) {
        this.functionElements.add(codeElement);
        return this;
    }

    public BtsCodeGenerator addFunction(FunctionCodeElement... codeElement) {
        this.functionElements.addAll(List.of(codeElement));
        return this;
    }

    public BtsCodeGenerator addGeneratorListener(String funcName, GenerationCodeListener listener) {
        List<GenerationCodeListener> data = functionGenerator.getOrDefault(funcName, new ArrayList<>());
        data.add(listener);
        functionGenerator.put(funcName, data);
        return this;
    }

    public BtsCodeGenerator addGeneratorListener(String funcName, Collection<? extends GenerationCodeListener> listeners) {
        List<GenerationCodeListener> data = functionGenerator.getOrDefault(funcName, new ArrayList<>());
        data.addAll(listeners);
        functionGenerator.put(funcName, data);
        return this;
    }

    protected FunctionCodeElement getNext() {
        if (functionElements.isEmpty() || currentFunction >= functionElements.size()) {
            return null;
        }
        return functionElements.get(currentFunction++);
    }

    public void generateCode(StringBuilder builder) {
        currentFunction = 0;

        generateHeader(builder);
        generateFunctions(builder);
    }

    protected void generateHeader(StringBuilder builder) {
        builder.append("//Code Generated with BTS Code Generator!").append("\n\n");

        importElements.sort(((o1, o2) -> Integer.compare(o2.getPriority(), o1.getPriority())));

        builder.append(generatedCode(CodeGeneratorStep.Imports, importElements));
        builder.append(generatedCode(CodeGeneratorStep.Fields, classFieldElements, codeContext.getCodeStyle().lineEndSymbol()));
        builder.append("\n");
    }

    protected void generateFunctions(StringBuilder builder) {
        builder.append("//Step: ").append(CodeGeneratorStep.Method).append("\n\n");

        FunctionCodeElement element;
        while ((element = getNext()) != null) {
            generateFunction(element, builder);
        }
    }

    protected void generateFunction(FunctionCodeElement functionCodeElement, StringBuilder builder) {
        if(functionCodeElement == null) return;
        functionCodeElement = (FunctionCodeElement) functionCodeElement.copyEmpty();

        List<GenerationCodeListener> generators = functionGenerator.getOrDefault(functionCodeElement.getName(), new ArrayList<>());

        for (GenerationCodeListener generator : generators) {
            generator.generateFragment(functionCodeElement, codeContext);
        }


        String gen = functionCodeElement.generateCodeFragment(0, CodeGeneratorStep.Method, codeContext);
        if(gen.isEmpty()) {
            gen = codeContext.getCodeStyle().generationCodeFragment(0, functionCodeElement, CodeGeneratorStep.Method, codeContext);
        }

        builder.append(gen);
    }

    protected String generatedCode(CodeGeneratorStep step, Collection<? extends CodeElement> elements) {
        return generatedCode(step, elements, "");
    }

    protected String generatedCode(CodeGeneratorStep step, Collection<? extends CodeElement> elements, String end) {
        final StringBuilder builder = new StringBuilder();

        builder.append("\n").append("//Step: ").append(step.name()).append("\n\n");

        List<String> containsCode = new ArrayList<>();

        for (CodeElement element : elements) {
            String builded = element.generateCodeFragment(0, step, codeContext);
            if(builded.isEmpty()) {
                builded = codeContext.getCodeStyle().generationCodeFragment(0, element, step, codeContext);
            }

            if(builded.isEmpty()) continue;

            if(containsCode.contains(builded)) continue;

            if(element.useEndLineSymbol()) {
                builded += codeContext.getCodeStyle().lineEndSymbol();
            }

            containsCode.add(builded);
        }

        for (String s : containsCode) {
            builder.append(s).append(end).append("\n");
        }

        return builder.toString();
    }
}
