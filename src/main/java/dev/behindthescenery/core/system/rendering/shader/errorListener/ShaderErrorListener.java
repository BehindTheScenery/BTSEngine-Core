package dev.behindthescenery.core.system.rendering.shader.errorListener;

public interface ShaderErrorListener {

    void onShaderError(ShaderError shaderError, String shaderErrorInfo);
}
