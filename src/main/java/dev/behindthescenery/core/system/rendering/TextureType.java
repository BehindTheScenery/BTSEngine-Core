package dev.behindthescenery.core.system.rendering;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public enum TextureType {
    DIFFUSE("diffuseMap"),
    NORMAL("normalMap"),
    ROUGHNESS("roughnessMap"),
    METALLIC("metallicMap"),
    ORM("ormMap"),
    AO("aoMap");

    private final String shaderId;

    TextureType(String shaderId) {
        this.shaderId = shaderId;
    }

    public String getShaderId() {
        return shaderId;
    }

    public static final Codec<TextureType> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("id").forGetter(Enum::ordinal)
            ).apply(instance, s -> TextureType.values()[s])
    );

}
