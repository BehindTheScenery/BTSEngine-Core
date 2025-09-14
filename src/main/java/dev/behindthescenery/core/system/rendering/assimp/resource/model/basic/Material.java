package dev.behindthescenery.core.system.rendering.assimp.resource.model.basic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.TextureType;
import dev.behindthescenery.core.system.rendering.color.Texture;
import dev.behindthescenery.core.system.rendering.shader.ShaderProgram;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class Material {

    public static Codec<Material> CODEC = RecordCodecBuilder.create(materialInstance ->
            materialInstance.group(
                    Codec.STRING.fieldOf("name").forGetter(Material::getMaterialName),
                    Codecs.strictUnboundedMap(TextureType.CODEC, Texture.CODEC).fieldOf("textures").forGetter(Material::getTextures)
            ).apply(materialInstance, Material::new));

    private String materialName = "";
    private Map<TextureType, Texture> textures = new HashMap<>();

    public Material() {

    }

    public Material(TextureType type1, Texture texture1) {
        setTexture(type1, texture1);
    }

    public Material(TextureType type1, Texture texture1,
                    TextureType type2, Texture texture2) {
        setTexture(type1, texture1).setTexture(type2, texture2);
    }

    public Material(TextureType type1, Texture texture1,
                    TextureType type2, Texture texture2,
                    TextureType type3, Texture texture3) {
        setTexture(type1, texture1).setTexture(type2, texture2)
        .setTexture(type3, texture3);
    }

    public Material(TextureType type1, Texture texture1,
                    TextureType type2, Texture texture2,
                    TextureType type3, Texture texture3,
                    TextureType type4, Texture texture4) {
        setTexture(type1, texture1).setTexture(type2, texture2)
        .setTexture(type3, texture3).setTexture(type4, texture4);
    }

    public Material(TextureType type1, Texture texture1,
                    TextureType type2, Texture texture2,
                    TextureType type3, Texture texture3,
                    TextureType type4, Texture texture4,
                    TextureType type5, Texture texture5) {
        setTexture(type1, texture1).setTexture(type2, texture2)
        .setTexture(type3, texture3).setTexture(type4, texture4)
        .setTexture(type5, texture5);
    }

    public Material(TextureType type1, Identifier texture1) {
        setTexture(type1, texture1);
    }

    public Material(TextureType type1, Identifier texture1,
                    TextureType type2, Identifier texture2) {
        setTexture(type1, texture1).setTexture(type2, texture2);
    }

    public Material(TextureType type1, Identifier texture1,
                    TextureType type2, Identifier texture2,
                    TextureType type3, Identifier texture3) {
        setTexture(type1, texture1).setTexture(type2, texture2)
        .setTexture(type3, texture3);
    }

    public Material(TextureType type1, Identifier texture1,
                    TextureType type2, Identifier texture2,
                    TextureType type3, Identifier texture3,
                    TextureType type4, Identifier texture4) {
        setTexture(type1, texture1).setTexture(type2, texture2)
        .setTexture(type3, texture3).setTexture(type4, texture4);
    }

    public Material(TextureType type1, Identifier texture1,
                    TextureType type2, Identifier texture2,
                    TextureType type3, Identifier texture3,
                    TextureType type4, Identifier texture4,
                    TextureType type5, Identifier texture5) {
        setTexture(type1, texture1).setTexture(type2, texture2)
        .setTexture(type3, texture3).setTexture(type4, texture4)
        .setTexture(type5, texture5);
    }

    protected Material(String s, Map<TextureType, Texture> textureTypeTextureMap) {
        this.materialName = s;
        this.textures = textureTypeTextureMap;
    }

    public Map<TextureType, Texture> getTextures() {
        return textures;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public Material setTexture(TextureType type, Texture texture) {
        this.textures.put(type, texture);
        return this;
    }

    public Material setTexture(TextureType type, Identifier texture) {
        this.textures.put(type, new Texture(texture));
        return this;
    }

    @Nullable
    public Texture getTexture(TextureType textureType) {
        return textures.get(textureType);
    }

    public void putToGlobalShader() {
        putToShader(BtsRenderSystem.getGlobalShader());
    }

    public void putToShader(@Nullable ShaderProgram shaderProgram) {
        if(shaderProgram == null) return;
        for (Map.Entry<TextureType, Texture> entry : textures.entrySet()) {
            shaderProgram.setUniformValue(entry.getKey().getShaderId(), entry.getValue());
        }
    }
}
