package dev.behindthescenery.core.system.user_interface.imgui.modules.editor;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.minecraft.util.math.Vec3i;

import java.util.function.Consumer;

public class BTSEnginePropertiesEditors {


    public static void objectTransformEditor(String name, ObjectTransform<Vec3i> transform, Consumer<ObjectTransform<Vec3i>> consumer, ImBoolean open) {

        boolean isChanged = false;

        ImGui.pushID(name);

        ObjectTransform<Vec3i> newPos = new ObjectTransform<>(transform.position);

        if(ImGui.collapsingHeader("Transform")) {
            Vec3i pos = transform.position;
            int[] array = new int[] { pos.getX(), pos.getY(), pos.getZ() };
            if(ImGui.inputInt3("Position (X,Y,Z)", array)) {
                isChanged = true;
                newPos = new ObjectTransform<>(new Vec3i(array[0], array[1], array[2]));
            }

            if (transform instanceof ObjectTransformWithRotation<?> rotation) {
                float[] arrayRot = new float[] { rotation.xRot, rotation.yRot };
                if(ImGui.inputFloat2("Rotation (X,Y)", arrayRot)) {
                    isChanged = true;
                    rotation.xRot = arrayRot[0];
                    rotation.yRot = arrayRot[1];
                }
            }

        }

        if(isChanged) {
            consumer.accept(newPos);
        }

        ImGui.popID();
    }


    public static class ObjectTransform<T extends Vec3i> {
        public T position;

        public ObjectTransform(T position) {
            this.position = position;
        }

        @Override
        public int hashCode() {
            return position.hashCode();
        }
    }

    public static class ObjectTransformWithRotation<T extends Vec3i> extends ObjectTransform<T> {
        public float yRot;
        public float xRot;

        public ObjectTransformWithRotation(T position, float yRot, float xRot) {
            super(position);
            this.yRot = yRot;
            this.xRot = xRot;
        }

        @Override
        public int hashCode() {
            return (int) (super.hashCode() + yRot + xRot);
        }
    }
}
