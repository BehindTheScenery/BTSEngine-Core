package dev.behindthescenery.core.system.rendering.shader.uniform;

import dev.behindthescenery.core.system.rendering.shader.ShaderProgram;

public interface ShaderUniform<T> {

    String getName();

    int getGlIndex();

    void setValue(Object value);

    T getValue();

    T getDefaultValue();

    ShaderUniformType getType();

    void bind(ShaderProgram shaderProgram);

    void unbind(ShaderProgram shaderProgram);

    default boolean isCurrent(String name) {
        return getName().equals(name);
    }

    boolean isCurrent(ShaderUniform<?> shaderUniform);
}
