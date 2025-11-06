package dev.behindthescenery.core.system.user_interface.imgui.editing.editors;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassConstructor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditorWithClass;
import imgui.ImGui;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Float3ClassEditor implements ImGuiClassEditorWithClass {

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class[] { Vector3f.class };
    }

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> _input, Consumer<Object> onChange, float elementWidth, int flag) {
        final Object input = _input.get();

        if(input instanceof Vector3f vector3f) {
            return () -> {
                float[] values = new float[] { vector3f.x, vector3f.y, vector3f.z };


                if(elementWidth > 0)
                    ImGui.setNextItemWidth(elementWidth);

                if(ImGui.inputFloat3(name, values, flag)) {
                    onChange.accept(new Vector3f(values));
                }
            };
        }

        return null;
    }

    @Override
    public Map<Class<?>, ImGuiClassConstructor> getConstructor() {
        return Map.of(Vector3f.class, new Vector3fConstructor());
    }

    protected static class Vector3fConstructor implements ImGuiClassConstructor  {
        @Override
        public List<Class<?>> getClassConstructorInput() {
            return List.of(Float.class, Float.class, Float.class);
        }

        @Override
        public Object createClassFromConstructor(List<Object> inputClasses) {
            return new Vector3f((Float) inputClasses.get(0), (Float) inputClasses.get(1), (Float) inputClasses.get(2));
        }
    }
}
