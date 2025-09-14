package dev.behindthescenery.core.system.rendering.shader;

import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public interface ShaderUniformSetter {

    void setUniform(String name, boolean value);

    void setUniform(String name, int value);

    void setUniform(String name, float value);

    void setUniform(String name, float v1, float v2);

    void setUniform(String name, float v1, float v2, float v3);

    void setUniform(String name, float v1, float v2, float v3, float v4);

    void setUniform(String name, float[] value);

    void setUniform(String name, Matrix3f matrix3f);

    void setUniform(String name, Matrix4f matrix4f);

    void setUniform(String name, Matrix4f[] matrix4f);

    default void setUniform(String name, Vector2f vector2f) {
        setUniform(name, vector2f.x, vector2f.y);
    }

    default void setUniform(String name, SimpleColor color) {
        setUniform(name, color.getRedF(), color.getGreenF(), color.getBlueF(), color.getAlphaF());
    }

    void setUniformValue(String name, Object value);
}
