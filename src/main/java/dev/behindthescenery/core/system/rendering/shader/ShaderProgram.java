package dev.behindthescenery.core.system.rendering.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.shader.errorListener.ShaderErrorListener;
import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.intellij.lang.annotations.Language;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

/**
 * ID масок для шейдеров <br><br>
 *
 * modelViewMat - {@link ShaderProgramBits#MODEL_VIEW_MATRIX} <br>
 * projectionMat - {@link ShaderProgramBits#PROJECTION_VIEW_MATRIX} <br>
 * color - {@link ShaderProgramBits#COLOR} <br>
 * screenSize - {@link ShaderProgramBits#SCREEN_SIZE} <br>
 * gameTime - {@link ShaderProgramBits#GAME_TIME_VANILLA} | {@link ShaderProgramBits#GAME_TIME_BTS} <br>
 * textureMat - {@link ShaderProgramBits#TEXTURE_MATRIX} <br>
 */
public interface ShaderProgram extends ShaderUniformSetter, ShaderUniformGetter, ShaderUniformRegister, ShaderProgramBits {

    @Language("glsl")
    final String DEFAULT_VERTEX_SHADER =
"""
#version 330 core

layout (location = 0) in vec3 Position;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vec4 worldPos = model * vec4(Position, 1.0);
    gl_Position = projection * view * worldPos;
}
""";

    int getProgramId();

    @Override
    default int getUniform(String name) {
        for (ShaderUniform<?> uniform : getUniforms()) {
            if(uniform.isCurrent(name)) return uniform.getGlIndex();
        }
        
        return -1;
    }

    @Override
    default void setUniform(String name, boolean value) {
        int i = getUniform(name);
        if(i == -1) return;
        GL20.glUniform1i(i, value ? 1 : 0);
    }

    @Override
    default void setUniform(String name, int value) {
        int i = getUniform(name);
        if(i == -1) return;
        GL20.glUniform1i(i, value);
    }

    @Override
    default void setUniform(String name, float value) {
        int i = getUniform(name);
        if(i == -1) return;
        GL20.glUniform1f(i, value);
    }

    @Override
    default void setUniform(String name, float v1, float v2) {
        int i = getUniform(name);
        if(i == -1) return;
        GL20.glUniform2f(i, v1, v2);
    }

    @Override
    default void setUniform(String name, float v1, float v2, float v3) {
        int i = getUniform(name);
        if(i == -1) return;
        GL20.glUniform3f(i, v1, v2, v3);
    }

    @Override
    default void setUniform(String name, float v1, float v2, float v3, float v4) {
        int i = getUniform(name);
        if(i == -1) return;
        GL20.glUniform4f(i, v1, v2, v3, v4);
    }

    @Override
    default void setUniform(String name, float[] value) {
        final int s = value.length;

        if(s == 1) setUniform(name, value[0]);
        if(s == 2) setUniform(name, value[0], value[1]);
        if(s == 3) setUniform(name, value[0], value[1], value[2]);
        if(s == 4) setUniform(name, value[0], value[1], value[2], value[3]);
    }

    @Override
    default void setUniform(String name, Matrix3f matrix3f) {
        int i = getUniform(name);
        if(i == -1) return;

        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(9);
            matrix3f.get(fb);
            GL20.glUniformMatrix3fv(i, false, fb);
        }
    }

    @Override
    default void setUniform(String name, Matrix4f matrix4f) {
        int i = getUniform(name);
        if(i == -1) return;

        try(MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            matrix4f.get(fb);
            GL20.glUniformMatrix4fv(i, false, fb);
        }
    }

    @Override
    default void setUniform(String name, Matrix4f[] matrices) {
        int uniformLocation = getUniform(name);
        if (uniformLocation == -1) {
            return;
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16 * matrices.length);

            for (int i = 0; i < matrices.length; i++) {
                if (matrices[i] != null) {
                    matrices[i].get(i * 16, buffer);
                } else {
                    new Matrix4f().identity().get(i * 16, buffer);
                }
            }

            GL20.glUniformMatrix4fv(uniformLocation, false, buffer);
        }
    }

    default int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(getProgramId(), name);
    }

    default void apply() {
        GL20.glUseProgram(getProgramId());

        applyRenderDefaultData();

        int currentTextureUnit = GL20.glGetInteger(GL20.GL_ACTIVE_TEXTURE);
        for (ShaderUniform<?> uniform : getUniforms()) {
            uniform.bind(this);
        }
        GL20.glActiveTexture(currentTextureUnit);
    }

    default void applyRenderDefaultData() {
        final int bits = getBits();

        if(bits == 0) return;

        if((bits & MODEL_VIEW_MATRIX) != 0) {
            applyModelViewMatrix();
        }

        if((bits & PROJECTION_VIEW_MATRIX) != 0) {
            applyProjectionMatrix();
        }

        if((bits & COLOR) != 0) {
            applyColor();
        }

        if((bits & SCREEN_SIZE) != 0) {
            applyScreenSize();
        }

        if((bits & GAME_TIME_VANILLA) != 0) {
            applyGameTimeVanilla();
        }

        if((bits & GAME_TIME_BTS) != 0) {
            applyGameTimeBts();
        }

        if((bits & TEXTURE_MATRIX) != 0) {
            applyTextureMatrix();
        }

        if((bits & CAMERA_POSITION) != 0) {
            applyCameraPosition();
        }
    }

    default void applyModelViewMatrix() {
        setUniformValue("view", RenderSystem.getModelViewMatrix());
    }

    default void applyProjectionMatrix() {
        setUniformValue("projection", RenderSystem.getProjectionMatrix());
    }

    default void applyColor() {
        setUniformValue("color", RenderSystem.getShaderColor());
    }

    default void applyScreenSize() {
        final Window window = MinecraftClient.getInstance().getWindow();
        setUniformValue("screenSize", new Vector2f(window.getFramebufferWidth(), window.getFramebufferHeight()));
    }

    default void applyGameTimeVanilla() {
        setUniformValue("gameTime", RenderSystem.getShaderGameTime());
    }

    default void applyGameTimeBts() {
        setUniformValue("gameTime", System.nanoTime() / 1_000_000_000.0f);
    }

    default void applyTextureMatrix() {
        setUniformValue("textureMatrix", RenderSystem.getTextureMatrix());
    }

    default void applyCameraPosition() {
        setUniformValue("viewPos", BtsRenderSystem.minecraftVectorToJOML(BtsRenderSystem.getCamera().getPos()));
    }

    default void stop() {
        for (ShaderUniform<?> uniform : getUniforms()) {
            uniform.unbind(this);
        }

        GL20.glUseProgram(0);
    }


    default void destroy() {
        stop();
        GL20.glDeleteProgram(getProgramId());
    }

    default int compileShader(String source, ShaderType shaderType, ShaderErrorListener errorListener) {
        final int shader = GL20.glCreateShader(shaderType.id);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
            errorListener.onShaderError(shaderType.getError(), GL20.glGetShaderInfoLog(shader));
            return -1;
        }

        return shader;
    }

    int getBits();

    boolean instanceRenderSupport();
}
