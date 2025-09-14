package dev.behindthescenery.core.system.rendering.shader;

import dev.behindthescenery.core.system.rendering.shader.errorListener.ShaderErrorListener;
import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniformType;
import net.minecraft.client.render.VertexFormat;

public class BasicInstancingShaderProgram extends BasicShaderProgram{

    public BasicInstancingShaderProgram(String vertexCode, String fragmentCode, int bits, VertexFormat vertexFormat) {
        super(vertexCode, fragmentCode, bits, vertexFormat);
    }

    public BasicInstancingShaderProgram(String vertexCode, String fragmentCode, int bits, VertexFormat vertexFormat, ShaderErrorListener shaderErrorListener) {
        super(vertexCode, fragmentCode, bits, vertexFormat, shaderErrorListener);
        registerUniform("useInstancing", false, ShaderUniformType.BOOL);
    }

    @Override
    public boolean instanceRenderSupport() {
        return true;
    }
}
