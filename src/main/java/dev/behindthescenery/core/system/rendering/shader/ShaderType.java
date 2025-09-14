package dev.behindthescenery.core.system.rendering.shader;

import dev.behindthescenery.core.system.rendering.shader.errorListener.ShaderError;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;

public enum ShaderType {
    VERTEX(GL20.GL_VERTEX_SHADER),
    FRAGMENT(GL20.GL_FRAGMENT_SHADER),
    COMPUTE(GL43.GL_COMPUTE_SHADER);

    final int id;

    ShaderType(int id) {
        this.id = id;
    }

    ShaderError getError() {
        return id == GL20.GL_VERTEX_SHADER ? ShaderError.COMPILE_VERTEX_ERROR : ShaderError.COMPILE_FRAGMENT_ERROR;
    }
}
