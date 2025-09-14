package dev.behindthescenery.core.system.rendering.shader.uniform;

import org.lwjgl.opengl.GL20;

public enum ShaderUniformType {
    BOOL,
    FLOAT,
    INT,
    VEC2,
    VEC3,
    VEC4,
    MATRIX4,
    MATRIX3,
    MATRIX4_ARRAY,
    SAMPLER2D_0(true, GL20.GL_TEXTURE0),
    SAMPLER2D_1(true, GL20.GL_TEXTURE1),
    SAMPLER2D_2(true, GL20.GL_TEXTURE2),
    SAMPLER2D_3(true, GL20.GL_TEXTURE3),
    SAMPLER2D_4(true, GL20.GL_TEXTURE4),
    SAMPLER2D_5(true, GL20.GL_TEXTURE5),
    SAMPLER2D_6(true, GL20.GL_TEXTURE6),
    SAMPLER2D_7(true, GL20.GL_TEXTURE7),
    SAMPLER2D_8(true, GL20.GL_TEXTURE8),
    SAMPLER2D_9(true, GL20.GL_TEXTURE9),
    SAMPLER2D_10(true, GL20.GL_TEXTURE10),
    SAMPLER2D_11(true, GL20.GL_TEXTURE11),
    SAMPLER2D_12(true, GL20.GL_TEXTURE12);

    final boolean bind;
    final int id;

    ShaderUniformType() {
        this(false, -1);
    }

    ShaderUniformType(boolean bind, int id) {
        this.bind = bind;
        this.id = id;
    }

}
