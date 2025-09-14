package dev.behindthescenery.core.system.rendering.shader;

import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.shader.errorListener.ShaderError;
import dev.behindthescenery.core.system.rendering.shader.errorListener.ShaderErrorListener;
import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniformType;
import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniform;
import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniformImpl;
import net.minecraft.client.render.VertexFormat;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class BasicShaderProgram implements ShaderProgram{

    private final int programId;
    protected int shaderParamBits;
    private final VertexFormat vertexFormat;
    private final HashSet<ShaderUniform<?>> shaderUniforms;
    protected ShaderErrorListener shaderErrorListener;

    protected ShaderStatus shaderStatus = ShaderStatus.ERROR;

    public BasicShaderProgram(final String vertexCode, final String fragmentCode, int bits, VertexFormat vertexFormat) {
        this(vertexCode, fragmentCode, bits, vertexFormat, (s1, s2) -> {
            throw new RuntimeException(s1.name() + " ( " + s2 + " )");
        });
    }

    public BasicShaderProgram(final String vertexCode, final String fragmentCode, int bits, VertexFormat vertexFormat, ShaderErrorListener shaderErrorListener) {
        this.shaderParamBits = bits;
        this.vertexFormat = vertexFormat;
        this.shaderUniforms = new HashSet<>();
        this.shaderErrorListener = shaderErrorListener;

        final int vertexId = compileShader(vertexCode, ShaderType.VERTEX, shaderErrorListener);
        final int fragmentId = compileShader(fragmentCode, ShaderType.FRAGMENT, shaderErrorListener);

        programId = GL20.glCreateProgram();

        int attrIndex = 0;
        for (String attrName : vertexFormat.getAttributeNames()) {
            GL20.glBindAttribLocation(programId, attrIndex, attrName);
            attrIndex++;
        }

        GL20.glAttachShader(programId, vertexId);
        GL20.glAttachShader(programId, fragmentId);
        GL20.glLinkProgram(programId);

        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            shaderErrorListener.onShaderError(ShaderError.LINK_ERROR, GL20.glGetProgramInfoLog(programId));
            return;
        }

        GL20.glDetachShader(programId, vertexId);
        GL20.glDetachShader(programId, fragmentId);
        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);

        if((bits & MODEL_VIEW_MATRIX) != 0) {
            registerUniform("view", null, null, ShaderUniformType.MATRIX4);
        }

        if((bits & PROJECTION_VIEW_MATRIX) != 0) {
            registerUniform("projection", null, null, ShaderUniformType.MATRIX4);
        }

        if((bits & COLOR) != 0) {
            registerUniform("color", null, null, ShaderUniformType.VEC4);
        }

        if((bits & SCREEN_SIZE) != 0) {
            registerUniform("screenSize", null, null, ShaderUniformType.VEC2);
        }

        if((bits & GAME_TIME_VANILLA) != 0) {
            registerUniform("gameTime", null,null, ShaderUniformType.FLOAT);
        }

        if((bits & TEXTURE_MATRIX) != 0) {
            registerUniform("textureMatrix", null, null, ShaderUniformType.MATRIX4);
        }

        if((bits & CAMERA_POSITION) != 0) {
            registerUniform("viewPos", new Vector3f(), ShaderUniformType.VEC3);
        }

        registerCustomUniforms();

        shaderStatus = ShaderStatus.LINKED;
    }

    public void setShaderErrorListener(ShaderErrorListener shaderErrorListener) {
        this.shaderErrorListener = shaderErrorListener;
    }

    public void registerCustomUniforms() {}

    public ShaderStatus getShaderStatus() {
        return shaderStatus;
    }

    @Nullable
    public static BasicShaderProgram fromFile(InputStream vertex, InputStream fragment, VertexFormat vertexFormat) {
        return fromFile(vertex, fragment, 0, vertexFormat);
    }

    @Nullable
    public static BasicShaderProgram fromFile(InputStream vertex, InputStream fragment, int shaderParamBits, VertexFormat vertexFormat) {
        String vertexCode = "", fragmentCode = "";

        try(vertex) {
            vertexCode = new String(vertex.readAllBytes());
        } catch (IOException e) {
            BtsCore.LOGGER.error("Vertex Shader file read error", e);
            return null;
        }

        try(fragment) {
            fragmentCode = new String(fragment.readAllBytes());
        } catch (IOException e) {
            BtsCore.LOGGER.error("Fragment Shader file read error", e);
            return null;
        }

        return new BasicShaderProgram(vertexCode, fragmentCode, shaderParamBits, vertexFormat);
    }


    @Override
    public int getProgramId() {
        return programId;
    }

    @Override
    public Collection<ShaderUniform<?>> getUniforms() {
        return shaderUniforms;
    }

    @Override
    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }

    @Override
    public int getBits() {
        return shaderParamBits;
    }

    @Override
    public boolean instanceRenderSupport() {
        return false;
    }

    @Override
    public void setUniformValue(String name, Object value) {
        for (ShaderUniform<?> shaderUniform : shaderUniforms) {
            if(shaderUniform.isCurrent(name)) {
                shaderUniform.setValue(value);
                return;
            }
        }
    }

    public <T> BasicShaderProgram registerUniform(String name, T defaultValue, ShaderUniformType type) {
        return registerUniform(new ShaderUniformImpl<>(name, defaultValue, type, this));
    }

    public <T> BasicShaderProgram registerUniform(String name, T defaultValue, T value, ShaderUniformType type) {
        return registerUniform(new ShaderUniformImpl<>(name, defaultValue, value, type, this));
    }

    public <T> BasicShaderProgram registerUniformOptional(String name, T defaultValue, ShaderUniformType type) {
        var v1 = new ShaderUniformImpl<>(name, defaultValue, type, this).setOptional();
        registerUniform("has" + name, 0, ShaderUniformType.INT);
        return registerUniform(v1);
    }

    public <T> BasicShaderProgram registerUniformOptional(String name, T defaultValue, T value, ShaderUniformType type) {
        ShaderUniformImpl<T> v1 = new ShaderUniformImpl<>(name, defaultValue, value, type, this).setOptional();
        registerUniform("has" + name, 0, ShaderUniformType.INT);
        return registerUniform(v1);
    }

    @Override
    public <T> BasicShaderProgram registerUniform(ShaderUniform<T> shaderUniform) {
        shaderUniforms.add(shaderUniform);
        return this;
    }

}
