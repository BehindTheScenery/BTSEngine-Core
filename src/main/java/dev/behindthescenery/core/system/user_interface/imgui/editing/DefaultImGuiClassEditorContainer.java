package dev.behindthescenery.core.system.user_interface.imgui.editing;

import dev.behindthescenery.core.system.user_interface.imgui.editing.editors.Double3ClassEditor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.editors.Float3ClassEditor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.editors.Float4ClassEditor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.editors.Integer3ClassEditor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.editors.basic.*;

import java.util.HashMap;

public class DefaultImGuiClassEditorContainer extends ImGuiClassEditorContainer {

    public static final DefaultImGuiClassEditorContainer Instance = new DefaultImGuiClassEditorContainer();

    public DefaultImGuiClassEditorContainer() {
        super(new HashMap<>());
        registerSupportedClasses();
    }

    protected void registerSupportedClasses() {
        registerEditor(String.class, new StringClassEditor());
        registerEditor(Integer.class, new IntegerClassEditor());
        registerEditor(Float.class, new FloatClassEditor());
        registerEditor(Double.class, new DoubleClassEditor());
        registerEditor(Boolean.class, new BooleanClassEditor());

        registerEditor(new Double3ClassEditor());
        registerEditor(new Float3ClassEditor());
        registerEditor(new Integer3ClassEditor());
        registerEditor(new Float4ClassEditor());
    }
}
