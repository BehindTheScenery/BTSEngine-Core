package dev.behindthescenery.core.system.rendering.assimp.shaders;

import dev.behindthescenery.core.system.rendering.shader.BasicShaderProgram;
import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniformType;
import net.minecraft.client.render.VertexFormats;
import org.intellij.lang.annotations.Language;
import org.joml.Matrix4f;

public class EmptyModelShader extends BasicShaderProgram {

    @Language("glsl")
    private static final String FRAGMENT =
"""
#version 330 core

void main() {}
""";

    public EmptyModelShader() {
        super(DEFAULT_VERTEX_SHADER, FRAGMENT,
                DEFAULT_MATRIX, VertexFormats.POSITION);

        registerUniform("model", new Matrix4f().identity(), ShaderUniformType.MATRIX4);
    }
}
