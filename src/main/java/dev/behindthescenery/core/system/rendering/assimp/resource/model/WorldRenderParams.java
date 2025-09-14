package dev.behindthescenery.core.system.rendering.assimp.resource.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class WorldRenderParams {

    public static final Codec<WorldRenderParams> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("bits").forGetter(WorldRenderParams::getBits),
                    Codec.INT.fieldOf("depth_functions").forGetter(WorldRenderParams::getDepthFunctions)
            ).apply(instance, WorldRenderParams::new)
    );

    public static final int USE_DEPTH_TEST = 1 << 1;
    public static final int USE_OCCLUSION = 1 << 2;

    public int bits;

    /**
     * {@link org.lwjgl.opengl.GL11#GL_LEQUAL}
     */
    public int depth_functions;

    public WorldRenderParams(int bits, int depth_functions) {
        this.bits = bits;
        this.depth_functions = depth_functions;
    }

    public WorldRenderParams addBits(int bits) {
        this.bits |= bits;
        return this;
    }

    public WorldRenderParams removeBits(int bits) {
        if((this.bits & bits) != 0) {
            this.bits &= bits;
        }
        return this;
    }

    public int getBits() {
        return bits;
    }

    public boolean useDepthTest() {
        return (bits & USE_DEPTH_TEST) != 0;
    }

    public int getDepthFunctions() {
        return depth_functions;
    }
}
