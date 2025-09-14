package dev.behindthescenery.core.system.rendering.shader.uniform;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.rendering.color.Texture;
import dev.behindthescenery.core.system.rendering.shader.ShaderProgram;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.AbstractTexture;
import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.Objects;

public class ShaderUniformImpl<T> implements ShaderUniform<T> {

    protected String name;
    protected T defaultValue;
    protected T value;
    protected ShaderUniformType type;
    protected int id;
    protected boolean optional = false;

    public ShaderUniformImpl(String name, T defaultValue, ShaderUniformType type, ShaderProgram shaderProgram) {
        this(name, defaultValue, defaultValue, type, shaderProgram);
    }

    public ShaderUniformImpl(String name, T defaultValue, T value, ShaderUniformType type, ShaderProgram shaderProgram) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = value;
        this.type = type;
        this.id = GL20.glGetUniformLocation(shaderProgram.getProgramId(), name);
    }

    public ShaderUniformImpl<T> setOptional() {
        this.optional = true;
        return this;
    }

    public int getId() {
        return id;
    }

    public ShaderUniformType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        this.value = (T) value;
    }

    public boolean current(String name) {
        return Objects.equals(this.name, name);
    }

    @Override
    public void bind(ShaderProgram shaderProgram) {
        T value = getValue();

        if(value == null && optional) {
            shaderProgram.setUniform("has" + name, 0);
        } else if(value != null && optional) {
            shaderProgram.setUniform("has" + name, 1);
        }

        switch (type) {
            case BOOL -> shaderProgram.setUniform(name, (Boolean) getValue());
            case INT -> shaderProgram.setUniform(name, (Integer) getValue());
            case FLOAT -> shaderProgram.setUniform(name, (Float) getValue());
            case VEC2 -> {
                float[] values = new float[2];
                switch (value) {
                    case Vector2f vector2f -> {
                        values[0] = vector2f.x;
                        values[1] = vector2f.y;
                    }
                    case Vector2i vector2i -> {
                        values[0] = vector2i.x;
                        values[1] = vector2i.y;
                    }
                    case SimpleColor color -> {
                        values[0] = color.getRedF();
                        values[1] = color.getGreenF();
                    }
                    case null, default -> {
                        if (optional) return;
                    }
                }

                shaderProgram.setUniform(name, values);
            }
            case VEC3 -> {
                float[] values = new float[3];
                switch (value) {
                    case Vector3f vec -> {
                        values[0] = vec.x;
                        values[1] = vec.y;
                        values[2] = vec.z;
                    }
                    case Vector3i vec -> {
                        values[0] = vec.x;
                        values[1] = vec.y;
                        values[2] = vec.z;
                    }
                    case SimpleColor color -> {
                        values[0] = color.getRedF();
                        values[1] = color.getGreenF();
                        values[2] = color.getBlueF();
                    }
                    case null, default -> {
                        if (optional) return;
                    }
                }

                shaderProgram.setUniform(name, values);
            }
            case VEC4 -> {
                float[] values = new float[4];
                switch (value) {
                    case Vector4f vec -> {
                        values[0] = vec.x;
                        values[1] = vec.y;
                        values[2] = vec.z;
                        values[3] = vec.w;
                    }
                    case Vector4i vec -> {
                        values[0] = vec.x;
                        values[1] = vec.y;
                        values[2] = vec.z;
                        values[3] = vec.w;
                    }
                    case SimpleColor color -> {
                        values[0] = color.getRedF();
                        values[1] = color.getGreenF();
                        values[2] = color.getBlueF();
                        values[3] = color.getAlphaF();
                    }
                    case null, default -> {
                        if (optional) return;
                    }
                }

                shaderProgram.setUniform(name, values);
            }
            case MATRIX3 -> {
                if(value == null && optional) return;
                shaderProgram.setUniform(name, (Matrix3f) value);
            }
            case MATRIX4 -> {
                if(value == null && optional) return;
                shaderProgram.setUniform(name, (Matrix4f) value);
            }
            case MATRIX4_ARRAY -> {
                if(value == null && optional) return;
                shaderProgram.setUniform(name, (Matrix4f[]) value);
            }
            default -> {
                if (type.bind) {
                    int l = -1;
                    if (value instanceof Framebuffer framebuffer) {
                        l = framebuffer.getColorAttachment();
                    } else if (value instanceof AbstractTexture texture) {
                        l = texture.getGlId();
                    } else if (value instanceof Integer integer) {
                        l = integer;
                    } else if(value instanceof Texture texture) {
                        l = texture.getTexture().getGlId();
                    }
                    if (l == -1) {
                        if(optional) return;

                        throw new RuntimeException("Can't load texture for shader! [texture: null, shader: " + shaderProgram.getProgramId() + "]");
                    }

                    int textureUnitIndex = type.id - GL20.GL_TEXTURE0;
                    GL20.glActiveTexture(type.id);
                    GL20.glBindTexture(GL11.GL_TEXTURE_2D, l);
                    GL20.glUniform1i(id, textureUnitIndex);
                }
            }
        }
    }

    @Override
    public void unbind(ShaderProgram shaderProgram) {
        if (type.bind) {
            RenderSystem.bindTexture(0);
        }
    }

    @Override
    public boolean isCurrent(ShaderUniform<?> shaderUniform) {
        return shaderUniform.getType() == this.type && shaderUniform.isCurrent(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getGlIndex() {
        return id;
    }

    public T getValue() {
        return value == null ? defaultValue : value;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }
}
