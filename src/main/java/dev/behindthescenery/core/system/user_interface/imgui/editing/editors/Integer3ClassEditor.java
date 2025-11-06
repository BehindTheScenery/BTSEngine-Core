package dev.behindthescenery.core.system.user_interface.imgui.editing.editors;

import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassConstructor;
import dev.behindthescenery.core.system.user_interface.imgui.editing.ImGuiClassEditorWithClass;
import imgui.ImGui;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3i;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Integer3ClassEditor implements ImGuiClassEditorWithClass {
    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class[] { Vector3i.class, BlockPos.class, Vec3i.class };
    }

    @Override
    public Runnable createClassEditor(String name, Supplier<Object> _input, Consumer<Object> onChange, float elementWidth, int flag) {
        final Object input = _input.get();

        if(input instanceof Vector3i vector3i) {
            return () -> {
                int[] values = new int[] { vector3i.x, vector3i.y, vector3i.z };

                if(elementWidth > 0)
                    ImGui.setNextItemWidth(elementWidth);

                if(ImGui.inputInt3(name, values, flag)) {
                    onChange.accept(new Vector3i(values));
                }
            };
        }

        if(input instanceof BlockPos blockPos) {
            return () -> {
                int[] values = new int[] { blockPos.getX(), blockPos.getY(), blockPos.getZ() };

                if(elementWidth > 0)
                    ImGui.setNextItemWidth(elementWidth);

                if(ImGui.inputInt3(name, values, flag)) {
                    onChange.accept(new BlockPos(values[0], values[1], values[2]));
                }
            };
        }

        if (input instanceof Vec3i vec3i) {
            return () -> {
                int[] values = new int[] { vec3i.getX(), vec3i.getY(), vec3i.getZ() };

                if(elementWidth > 0)
                    ImGui.setNextItemWidth(elementWidth);

                if(ImGui.inputInt3(name, values, flag)) {
                    onChange.accept(new Vec3i(values[0], values[1], values[2]));
                }
            };
        }


        return null;
    }

    protected static class Vector3iConstructor implements ImGuiClassConstructor {

        @Override
        public List<Class<?>> getClassConstructorInput() {
            return List.of(Integer.class, Integer.class, Integer.class);
        }

        @Override
        public Object createClassFromConstructor(List<Object> inputClasses) {
            return new Vector3i((Integer) inputClasses.get(0), (Integer) inputClasses.get(1), (Integer) inputClasses.get(2));
        }
    }
}
