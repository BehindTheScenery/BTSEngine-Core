package dev.behindthescenery.core.system.rendering;

import dev.behindthescenery.core.system.rendering.assimp.resource.model.BoundingBox;
import dev.behindthescenery.core.system.rendering.color.RGB;
import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import net.minecraft.util.math.Box;
import org.joml.*;

import java.util.function.Consumer;

public interface UiEditingContext {

    void pushGroup(String name);

    void popGroup();

    void addText(String name);

    void addText(String name, Vector4i color);

    void button(String name, Runnable onClick);

    default void addText(String name, SimpleColor color) {
        addText(name, new Vector4i(color.getRedI(), color.getGreenI(), color.getBlueI(), color.getAlphaI()));
    }

    void space(int x, int y);

    void editInt(String name, int startValue, Consumer<Integer> onChange);

    void editInt(String name, int startValue, Consumer<Integer> onChange, int min, int max, int step);

    void editFloat(String name, float startValue, Consumer<Float> onChange);

    void editFloat(String name, float startValue, Consumer<Float> onChange, float min, float max, float step);

    void editDouble(String name, double startValue, Consumer<Double> onChange);

    void editDouble(String name, double startValue, Consumer<Double> onChange, double min, double max, double step);

    void editLong(String name, long startValue, Consumer<Long> onChange);

    void editLong(String name, long startValue, Consumer<Long> onChange, long min, long max, long step);

    void editString(String name, String startValue, Consumer<String> onChange);

    void editBoolean(String name, boolean startValue, Consumer<Boolean> onChange);

    void editCharacter(String name, char startValue, Consumer<Character> onChange);

    void editShort(String name, short startValue, Consumer<Short> onChange);

    void editShort(String name, short startValue, Consumer<Short> onChange, short min, short max, short step);

    void editByte(String name, byte startValue, Consumer<Byte> onChange);

    void editVector2i(String name, Vector2i startValue, Consumer<Vector2i> onChange);

    void editVector2f(String name, Vector2f startValue, Consumer<Vector2f> onChange);

    void editVector2d(String name, Vector2d startValue, Consumer<Vector2d> onChange);

    void editVector3i(String name, Vector3i startValue, Consumer<Vector3i> onChange);

    void editVector3f(String name, Vector3f startValue, Consumer<Vector3f> onChange);

    void editVector3d(String name, Vector3d startValue, Consumer<Vector3d> onChange);

    void editVector4i(String name, Vector4i startValue, Consumer<Vector4i> onChange);

    void editVector4f(String name, Vector4f startValue, Consumer<Vector4f> onChange);

    void editVector4d(String name, Vector4d startValue, Consumer<Vector4d> onChange);

    void editMatrix3f(String name, Matrix3f startValue, Consumer<Matrix3f> onChange);

    void editMatrix3d(String name, Matrix3d startValue, Consumer<Matrix3d> onChange);

    void editMatrix4f(String name, Matrix4f startValue, Consumer<Matrix4f> onChange);

    void editMatrix4d(String name, Matrix4d startValue, Consumer<Matrix4d> onChange);

    void editBoundingBox(String name, BoundingBox startValue, Consumer<BoundingBox> onChange);

    void editColor(String name, RGB startValue, Consumer<RGB> onChange);

    void editColor(String name, RGBA startValue, Consumer<RGBA> onChange);

    int getTextHeight();

    Vector2i getTextSize(String text);
}
