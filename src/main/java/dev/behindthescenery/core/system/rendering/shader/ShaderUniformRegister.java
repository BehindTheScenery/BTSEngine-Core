package dev.behindthescenery.core.system.rendering.shader;

import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniform;

public interface ShaderUniformRegister {

    <T> BasicShaderProgram registerUniform(ShaderUniform<T> shaderUniform);
}
