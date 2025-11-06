package dev.behindthescenery.core.system.user_interface.imgui.editing;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ImGuiClassEditorContainer {

    protected final Map<Class<?>, ImClassEditorStruct> editorMap;
    
    public ImGuiClassEditorContainer() {
        this(new HashMap<>());
    }    
    
    public ImGuiClassEditorContainer(Map<Class<?>, ImClassEditorStruct> editorMap) {
        this.editorMap = editorMap;
    }
    
    public ImGuiClassEditorContainer registerEditor(Class<?> classObj, ImGuiClassEditor editor) {
        return registerEditor(classObj, editor, null);
    }

    public ImGuiClassEditorContainer registerEditor(Class<?> classObj, ImGuiClassEditor editor, ImGuiClassConstructor constructor) {
        editorMap.put(classObj, new ImClassEditorStruct(editor, constructor));
        return this;
    }

    public ImGuiClassEditorContainer registerEditor(ImGuiClassEditorWithClass editor) {
        for (Class<?> supportedClass : editor.getSupportedClasses()) {
            registerEditor(supportedClass, editor, editor.getConstructor().getOrDefault(supportedClass, null));
        }

        return this;
    }

    public Optional<ImClassEditorStruct> getEditor(Class<?> classObj) {
        return Optional.ofNullable(editorMap.get(classObj));
    }

    public Optional<Runnable> getEditor(String name, Supplier<Object> input, Consumer<Object> onChange) {
        return getEditor(name, input, onChange, 0, 0);
    }

    public Optional<Runnable> getEditor(String name, Supplier<Object> input, Consumer<Object> onChange, float elementWidth, int flags) {
        ImClassEditorStruct obj = editorMap.get(input.getClass());
        if(obj == null) return Optional.empty();

        var editor = obj.getClassEditor();
        if(editor == null) return Optional.empty();

        return Optional.ofNullable(editor.createClassEditor(name, input, onChange, elementWidth, flags));
    }
}
