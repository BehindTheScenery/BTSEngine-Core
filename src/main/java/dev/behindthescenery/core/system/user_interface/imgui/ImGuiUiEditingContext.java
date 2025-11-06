package dev.behindthescenery.core.system.user_interface.imgui;

import dev.behindthescenery.core.system.rendering.UiEditingContext;
import dev.behindthescenery.core.system.rendering.assimp.resource.model.BoundingBox;
import dev.behindthescenery.core.system.rendering.color.RGB;
import dev.behindthescenery.core.system.rendering.color.RGBA;
import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.*;
import org.joml.*;

import java.lang.Math;
import java.util.function.Consumer;

public class ImGuiUiEditingContext implements UiEditingContext {

    @Override
    public void pushGroup(String name) {
        ImGui.pushID(name);
    }

    @Override
    public void popGroup() {
        ImGui.popID();
    }

    @Override
    public void addText(String name) {
        ImGui.text(name);
    }

    @Override
    public void addText(String name, Vector4i color) {
        ImGui.textColored(ImColor.rgba(color.x, color.y, color.z, color.w), name);
    }

    @Override
    public void button(String name, Runnable onClick) {
        if(ImGui.button(name)) {
            onClick.run();
        }
    }

    @Override
    public void space(int x, int y) {
        ImGui.dummy(x, y);
    }

    @Override
    public void editInt(String name, int startValue, Consumer<Integer> onChange) {
        final var i = new ImInt(startValue);
        if(ImGui.inputInt(name, i)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editInt(String name, int startValue, Consumer<Integer> onChange, int min, int max, int step) {
        final var i = new ImInt(startValue);
        if(ImGui.inputInt(name, i, min, max, step)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editFloat(String name, float startValue, Consumer<Float> onChange) {
        final var i = new ImFloat(startValue);
        if(ImGui.inputFloat(name, i)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editFloat(String name, float startValue, Consumer<Float> onChange, float min, float max, float step) {
        final var i = new ImFloat(startValue);
        if(ImGui.inputFloat(name, i, step)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editDouble(String name, double startValue, Consumer<Double> onChange) {
        final var i = new ImDouble(startValue);
        if(ImGui.inputDouble(name, i)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editDouble(String name, double startValue, Consumer<Double> onChange, double min, double max, double step) {
        final var i = new ImDouble(startValue);
        if(ImGui.inputDouble(name, i, step)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editLong(String name, long startValue, Consumer<Long> onChange) {
        final var i = new ImInt(Math.toIntExact(startValue));
        if(ImGui.inputInt(name, i)) {
            onChange.accept((long) i.get());
        }
    }

    @Override
    public void editLong(String name, long startValue, Consumer<Long> onChange, long min, long max, long step) {
        final var i = new ImInt(Math.toIntExact(startValue));
        if(ImGui.inputInt(name, i, Math.toIntExact(min), Math.toIntExact(max), Math.toIntExact(step))) {
            onChange.accept((long) i.get());
        }
    }

    @Override
    public void editString(String name, String startValue, Consumer<String> onChange) {
        final var i = new ImString(startValue);
        if(ImGui.inputText(name, i)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editBoolean(String name, boolean startValue, Consumer<Boolean> onChange) {
        final var i = new ImBoolean(startValue);
        if(ImGui.checkbox(name, i)) {
            onChange.accept(i.get());
        }
    }

    @Override
    public void editCharacter(String name, char startValue, Consumer<Character> onChange) {
        final var i = new ImString(startValue);
        if(ImGui.inputText(name, i)) {
            onChange.accept(i.get().charAt(0));
        }
    }

    @Override
    public void editShort(String name, short startValue, Consumer<Short> onChange) {
        final var i = new ImInt(startValue);
        if(ImGui.inputInt(name, i)) {
            onChange.accept((short) i.get());
        }
    }

    @Override
    public void editShort(String name, short startValue, Consumer<Short> onChange, short min, short max, short step) {
        final var i = new ImInt(startValue);
        if(ImGui.inputInt(name, i, min, max, step)) {
            onChange.accept((short) i.get());
        }
    }

    @Override
    public void editByte(String name, byte startValue, Consumer<Byte> onChange) {
        final var i = new ImInt(startValue);
        if(ImGui.inputInt(name, i)) {
            onChange.accept((byte) i.get());
        }
    }

    @Override
    public void editVector2i(String name, Vector2i startValue, Consumer<Vector2i> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        int[] inputArray = new int[] { startValue.x, startValue.y };
        if (ImGui.inputInt2("##" + name, inputArray)) {
            onChange.accept(new Vector2i(inputArray[0], inputArray[1]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector2f(String name, Vector2f startValue, Consumer<Vector2f> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        float[] inputArray = new float[] { startValue.x, startValue.y };
        if (ImGui.inputFloat2("##" + name, inputArray)) {
            onChange.accept(new Vector2f(inputArray[0], inputArray[1]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector2d(String name, Vector2d startValue, Consumer<Vector2d> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        float[] inputArray = new float[] {(float) startValue.x, (float) startValue.y};
        if (ImGui.inputFloat2("##" + name, inputArray)) {
            onChange.accept(new Vector2d(inputArray[0], inputArray[1]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector3i(String name, Vector3i startValue, Consumer<Vector3i> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        int[] inputArray = new int[] { startValue.x, startValue.y, startValue.z };
        if (ImGui.inputInt3("##" + name, inputArray)) {
            onChange.accept(new Vector3i(inputArray[0], inputArray[1], inputArray[2]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector3f(String name, Vector3f startValue, Consumer<Vector3f> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        float[] inputArray = new float[] { startValue.x, startValue.y, startValue.z };
        if (ImGui.inputFloat3("##" + name, inputArray)) {
            onChange.accept(new Vector3f(inputArray[0], inputArray[1], inputArray[2]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector3d(String name, Vector3d startValue, Consumer<Vector3d> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        float[] inputArray = new float[] {(float) (float) startValue.x, (float) startValue.y, (float) startValue.z};
        if (ImGui.inputFloat3("##" + name, inputArray)) {
            onChange.accept(new Vector3d(inputArray[0], inputArray[1], inputArray[2]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector4i(String name, Vector4i startValue, Consumer<Vector4i> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        int[] inputArray = new int[] { startValue.x, startValue.y, startValue.z, startValue.w };
        if (ImGui.inputInt4("##" + name, inputArray)) {
            onChange.accept(new Vector4i(inputArray[0], inputArray[1], inputArray[2], inputArray[3]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector4f(String name, Vector4f startValue, Consumer<Vector4f> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        float[] inputArray = new float[] { startValue.x, startValue.y, startValue.z, startValue.w };
        if (ImGui.inputFloat3("##" + name, inputArray)) {
            onChange.accept(new Vector4f(inputArray[0], inputArray[1], inputArray[2], inputArray[3]));
        }

        ImGui.popID();
    }

    @Override
    public void editVector4d(String name, Vector4d startValue, Consumer<Vector4d> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        ImGui.sameLine();

        float[] inputArray = new float[] {(float) startValue.x, (float) startValue.y, (float) (float) startValue.z, (float) startValue.w};
        if (ImGui.inputFloat3("##" + name, inputArray)) {
            onChange.accept(new Vector4d(inputArray[0], inputArray[1], inputArray[2], inputArray[3]));
        }

        ImGui.popID();
    }

    @Override
    public void editMatrix3f(String name, Matrix3f matrix, Consumer<Matrix3f> onChange) {
        float[] values1 = new float[] { matrix.m00(), matrix.m01(), matrix.m02()};
        float[] values2 = new float[] { matrix.m10(), matrix.m11(), matrix.m12()};
        float[] values3 = new float[] { matrix.m20(), matrix.m21(), matrix.m22()};
        Matrix3f mat1 = new Matrix3f();

        ImGui.pushID(name);
        ImGui.text(name);
        if(ImGui.inputFloat4("##1" + name, values1)) {
            mat1.m00(values1[0]);
            mat1.m01(values1[1]);
            mat1.m02(values1[2]);
            onChange.accept(mat1);
        }
        if(ImGui.inputFloat4("##2" + name, values2)) {
            mat1.m10(values2[0]);
            mat1.m11(values2[1]);
            mat1.m12(values2[2]);
            onChange.accept(mat1);
        }
        if(ImGui.inputFloat4("##3" + name, values3)) {
            mat1.m20(values3[0]);
            mat1.m21(values3[1]);
            mat1.m22(values3[2]);
            onChange.accept(mat1);
        }
        ImGui.popID();
    }

    @Override
    public void editMatrix3d(String name, Matrix3d startValue, Consumer<Matrix3d> onChange) {
        float[] values1 = new float[] { (float) startValue.m00(), (float) startValue.m01(), (float) startValue.m02()};
        float[] values2 = new float[] { (float) startValue.m10(), (float) startValue.m11(), (float) startValue.m12()};
        float[] values3 = new float[] { (float) startValue.m20(), (float) startValue.m21(), (float) startValue.m22()};
        Matrix3d mat1 = new Matrix3d();

        ImGui.pushID(name);
        ImGui.text(name);
        if(ImGui.inputFloat4("##1" + name, values1)) {
            mat1.m00(values1[0]);
            mat1.m01(values1[1]);
            mat1.m02(values1[2]);
            onChange.accept(mat1);
        }
        if(ImGui.inputFloat4("##2" + name, values2)) {
            mat1.m10(values2[0]);
            mat1.m11(values2[1]);
            mat1.m12(values2[2]);
            onChange.accept(mat1);
        }
        if(ImGui.inputFloat4("##3" + name, values3)) {
            mat1.m20(values3[0]);
            mat1.m21(values3[1]);
            mat1.m22(values3[2]);
            onChange.accept(mat1);
        }
        ImGui.popID();
    }

    @Override
    public void editMatrix4f(String name, Matrix4f startValue, Consumer<Matrix4f> onChange) {
        float[] values1 = new float[] { startValue.m00(), startValue.m01(), startValue.m02(), startValue.m03()};
        float[] values2 = new float[] { startValue.m10(), startValue.m11(), startValue.m12(), startValue.m13()};
        float[] values3 = new float[] { startValue.m20(), startValue.m21(), startValue.m22(), startValue.m23()};
        float[] values4 = new float[] { startValue.m30(), startValue.m31(), startValue.m32(), startValue.m33()};

        Matrix4f matrix = new Matrix4f();
        ImGui.pushID(name);
        ImGui.text(name);
        if(ImGui.inputFloat4("##1" + name, values1)) {
            matrix.m00(values1[0]);
            matrix.m01(values1[1]);
            matrix.m02(values1[2]);
            matrix.m03(values1[3]);
            onChange.accept(matrix);
        }
        if(ImGui.inputFloat4("##2" + name, values2)) {
            matrix.m10(values2[0]);
            matrix.m11(values2[1]);
            matrix.m12(values2[2]);
            matrix.m13(values2[3]);
            onChange.accept(matrix);
        }
        if(ImGui.inputFloat4("##3" + name, values3)) {
            matrix.m20(values3[0]);
            matrix.m21(values3[1]);
            matrix.m22(values3[2]);
            matrix.m23(values3[3]);
            onChange.accept(matrix);
        }
        if(ImGui.inputFloat4("##4" + name, values4)) {
            matrix.m30(values4[0]);
            matrix.m31(values4[1]);
            matrix.m32(values4[2]);
            matrix.m33(values4[3]);
            onChange.accept(matrix);
        }
        ImGui.popID();
    }

    @Override
    public void editMatrix4d(String name, Matrix4d startValue, Consumer<Matrix4d> onChange) {
        float[] values1 = new float[] { (float) startValue.m00(), (float) startValue.m01(), (float) startValue.m02(), (float) startValue.m03()};
        float[] values2 = new float[] { (float) startValue.m10(), (float) startValue.m11(), (float) startValue.m12(), (float) startValue.m13()};
        float[] values3 = new float[] { (float) startValue.m20(), (float) startValue.m21(), (float) startValue.m22(), (float) startValue.m23()};
        float[] values4 = new float[] { (float) startValue.m30(), (float) startValue.m31(), (float) startValue.m32(), (float) startValue.m33()};

        Matrix4d matrix = new Matrix4d();
        ImGui.pushID(name);
        ImGui.text(name);
        if(ImGui.inputFloat4("##1" + name, values1)) {
            matrix.m00(values1[0]);
            matrix.m01(values1[1]);
            matrix.m02(values1[2]);
            matrix.m03(values1[3]);
            onChange.accept(matrix);
        }
        if(ImGui.inputFloat4("##2" + name, values2)) {
            matrix.m10(values2[0]);
            matrix.m11(values2[1]);
            matrix.m12(values2[2]);
            matrix.m13(values2[3]);
            onChange.accept(matrix);
        }
        if(ImGui.inputFloat4("##3" + name, values3)) {
            matrix.m20(values3[0]);
            matrix.m21(values3[1]);
            matrix.m22(values3[2]);
            matrix.m23(values3[3]);
            onChange.accept(matrix);
        }
        if(ImGui.inputFloat4("##4" + name, values4)) {
            matrix.m30(values4[0]);
            matrix.m31(values4[1]);
            matrix.m32(values4[2]);
            matrix.m33(values4[3]);
            onChange.accept(matrix);
        }
        ImGui.popID();
    }

    @Override
    public void editBoundingBox(String name, BoundingBox startValue, Consumer<BoundingBox> onChange) {
        ImGui.pushID(name);
        ImGui.text(name);
        editVector3f("min", startValue.min, (n) -> {
            Vector3f t = startValue.max;
            onChange.accept(new BoundingBox(n.x, n.y, n.z, t.x, t.y, t.z));
        });
        editVector3f("max", startValue.max, (n) -> {
            Vector3f t = startValue.min;
            onChange.accept(new BoundingBox(t.x, t.y, t.z, n.x, n.y, n.z));
        });

        ImGui.popID();
    }

    @Override
    public void editColor(String name, RGB startValue, Consumer<RGB> onChange) {
        ImGui.pushID(name);

        float[] col = new float[] { startValue.getRedF(), startValue.getGreenF(), startValue.getBlueF() };
        if(ImGui.colorEdit3(name, col)) {
            onChange.accept(RGB.of(col[0], col[1], col[2]));
        }

        ImGui.popID();
    }

    @Override
    public void editColor(String name, RGBA startValue, Consumer<RGBA> onChange) {
        ImGui.pushID(name);

        float[] col = new float[] { startValue.getRedF(), startValue.getGreenF(), startValue.getBlueF(), startValue.getAlphaF() };
        if(ImGui.colorEdit4(name, col)) {
            onChange.accept(RGBA.of(col[0], col[1], col[2], col[3]));
        }

        ImGui.popID();
    }

    @Override
    public int getTextHeight() {
        return getTextSize("H").y;
    }

    @Override
    public Vector2i getTextSize(String text) {
        ImVec2 size = new ImVec2(0,0);
        ImGui.calcTextSize(size, text);
        return new Vector2i((int) size.x, (int) size.y);
    }
}
