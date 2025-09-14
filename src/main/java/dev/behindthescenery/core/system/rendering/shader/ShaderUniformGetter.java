package dev.behindthescenery.core.system.rendering.shader;

import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniform;
import net.minecraft.client.render.VertexFormat;

import java.util.Collection;

public interface ShaderUniformGetter {

    Collection<ShaderUniform<?>> getUniforms();

    int getUniformLocation(String name);

    int getUniform(String name);

    VertexFormat getVertexFormat();
}
