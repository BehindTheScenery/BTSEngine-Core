package dev.behindthescenery.core.system.user_interface.imgui.editing.editors;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassConstructor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditorWithClass;
import imgui.ImGui;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Double3ClassEditor implements ImGuiClassEditorWithClass {

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class[] { Vector3d.class, Vec3d.class };
    }

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> _input, Consumer<Object> onChange, float elementWidth, int flag) {
        final Object input = _input.get();


        if(input instanceof Vector3d vector3d) {
            return () -> {
                float[] values = new float[] {(float) vector3d.x, (float) vector3d.y, (float) vector3d.z};

                if(elementWidth > 0)
                    ImGui.setNextItemWidth(elementWidth);

                if(ImGui.inputFloat3(name, values, flag)) {
                    onChange.accept(new Vector3d(values));
                }
            };
        }

        if(input instanceof Vec3d vec3d) {
            return () -> {
                float[] values = new float[] {(float) vec3d.x, (float) vec3d.y, (float) vec3d.z};

                if(elementWidth > 0)
                    ImGui.setNextItemWidth(elementWidth);

                if(ImGui.inputFloat3(name, values, flag)) {
                    onChange.accept(new Vec3d(values[0], values[1], values[2]));
                }
            };
        }

        return null;
    }

    @Override
    public Map<Class<?>, ImGuiClassConstructor> getConstructor() {
        return Map.of(
                Vec3d.class, new Vec3dConstructor(),
                Vector3d.class, new Vector3dConstructor()
        );
    }

    protected static class Vec3dConstructor implements ImGuiClassConstructor {
        @Override
        public List<Class<?>> getClassConstructorInput() {
            return List.of(Double.class, Double.class, Double.class);
        }

        @Override
        public Object createClassFromConstructor(List<Object> inputClasses) {
            return new Vec3d((Double) inputClasses.get(0), (Double) inputClasses.get(1), (Double) inputClasses.get(2));
        }
    }

    protected static class Vector3dConstructor implements ImGuiClassConstructor {
        @Override
        public List<Class<?>> getClassConstructorInput() {
            return List.of(Double.class, Double.class, Double.class);
        }

        @Override
        public Object createClassFromConstructor(List<Object> inputClasses) {
            return new Vector3d((Double) inputClasses.get(0), (Double) inputClasses.get(1), (Double) inputClasses.get(2));
        }
    }


}
