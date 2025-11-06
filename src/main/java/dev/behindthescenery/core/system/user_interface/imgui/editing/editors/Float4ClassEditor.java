package dev.behindthescenery.core.system.user_interface.imgui.editing.editors;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassConstructor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditorWithClass;
import imgui.ImGui;
import org.joml.Vector4f;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Float4ClassEditor implements ImGuiClassEditorWithClass {

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class[] { Vector4f.class };
    }

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> _input, Consumer<Object> onChange, float elementWidth, int flag) {
        final Object input = _input.get();

        if(input instanceof Vector4f vector3f) {
            return () -> {
                float[] values = new float[] { vector3f.x, vector3f.y, vector3f.z, vector3f.w };


                if(elementWidth > 0)
                    ImGui.setNextItemWidth(elementWidth);

                if(ImGui.inputFloat4(name, values, flag)) {
                    onChange.accept(new Vector4f(values));
                }
            };
        }

        return null;
    }

    @Override
    public Map<Class<?>, ImGuiClassConstructor> getConstructor() {
        return Map.of(Vector4f.class, new Vector4fConstructor());
    }

    protected static class Vector4fConstructor implements ImGuiClassConstructor  {
        @Override
        public List<Class<?>> getClassConstructorInput() {
            return List.of(Float.class, Float.class, Float.class, Float.class);
        }

        @Override
        public Object createClassFromConstructor(List<Object> inputClasses) {
            return new Vector4f((Float) inputClasses.get(0), (Float) inputClasses.get(1), (Float) inputClasses.get(2), (Float) inputClasses.get(3));
        }
    }
}
