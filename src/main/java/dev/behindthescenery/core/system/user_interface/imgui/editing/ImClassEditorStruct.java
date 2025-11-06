package dev.behindthescenery.core.system.user_interface.imgui.editing;


import org.jetbrains.annotations.Nullable;

public class ImClassEditorStruct {

    protected @Nullable ImGuiClassEditor classEditor;
    protected @Nullable ImGuiClassConstructor classConstructor;

    public ImClassEditorStruct(@Nullable ImGuiClassEditor classEditor, @Nullable ImGuiClassConstructor classConstructor) {
        this.classConstructor = classConstructor;
        this.classEditor = classEditor;
    }

    @Nullable
    public ImGuiClassConstructor getClassConstructor() {
        return classConstructor;
    }

    @Nullable
    public ImGuiClassEditor getClassEditor() {
        return classEditor;
    }
}
