package dev.behindthescenery.core.system.rendering.vfx.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.IRenderSystemExecutor;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.rendering.textures.atlas.TextureAtlasGenerator;
import dev.behindthescenery.core.system.rendering.textures.atlas.TextureRegion;
import dev.behindthescenery.core.system.rendering.vfx.ParticleEmitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class VFXParticle implements IRenderSystemExecutor {
    public static final Codec<VFXParticle> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Identifier.CODEC.fieldOf("particleResourceID").forGetter(VFXParticle::getParticleResourceID),
            Codec.FLOAT.fieldOf("weightTexture").forGetter(VFXParticle::getWeightTexture),
            Codec.FLOAT.fieldOf("heightTexture").forGetter(VFXParticle::getHeightTexture),
            Codec.INT.fieldOf("particleColor").forGetter(VFXParticle::getParticleColorI)
    ).apply(builder, VFXParticle::new));

    protected final Identifier particleResourceID;
    protected final float weightTexture;
    protected final float heightTexture;
    protected SimpleColor particleColor = SimpleColor.DEFAULT;
    protected boolean useLight = false;
    protected int light;

    public VFXParticle(Identifier particleResourceID, float size) {
        this(particleResourceID, size, size);
    }

    public VFXParticle(Identifier particleResourceID, float weightTexture, float heightTexture) {
        this.particleResourceID = particleResourceID;
        this.weightTexture = weightTexture;
        this.heightTexture = heightTexture;
    }

    public VFXParticle(Identifier particleResourceID, float weightTexture, float heightTexture, int particleColor) {
        this(particleResourceID, weightTexture, heightTexture);
        this.particleColor = RGBA.of(particleColor);
    }

    public VFXParticle copy() {
        return new VFXParticle(particleResourceID, weightTexture, heightTexture, particleColor.argbI());
    }

    @OnlyIn(Dist.CLIENT)
    public void addVertexEmitter(MatrixStack.Entry matrix, BufferBuilder consumer, VFXRenderParticle renderParticle) {
        if(particleResourceID == null) {
            BtsCore.LOGGER.error("Can't render Particle because particleResourceID is null!");
            return;
        }

        checkLight(renderParticle.emitter, renderParticle.position);

        float w = renderParticle.weight;
        float h = renderParticle.height;

        TextureAtlasGenerator atlas = renderParticle.emitter.getTextureAtlasGenerator();
        TextureRegion region = atlas.getRegion(particleResourceID);

        addVertex(consumer, matrix, -w,  h, 0.0f, region.u0(), region.v1());
        addVertex(consumer, matrix, -w, -h, 0.0f, region.u0(), region.v0());
        addVertex(consumer, matrix,  w, -h, 0.0f, region.u1(), region.v0());
        addVertex(consumer, matrix,  w,  h, 0.0f, region.u1(), region.v1());
    }

    @OnlyIn(Dist.CLIENT)
    public void addVertex(MatrixStack.Entry matrix, BufferBuilder consumer) {
        addVertex(consumer, matrix, -weightTexture,  heightTexture, 0.0f, 0, 1);  // верхний левый
        addVertex(consumer, matrix, -weightTexture, -heightTexture, 0.0f, 0, 0);  // нижний левый
        addVertex(consumer, matrix,  weightTexture, -heightTexture, 0.0f, 1, 0);  // нижний правый
        addVertex(consumer, matrix,  weightTexture,  heightTexture, 0.0f, 1, 1);  // верхний правый
    }

    @OnlyIn(Dist.CLIENT)
    protected void addVertex(BufferBuilder consumer, MatrixStack.Entry matrix,
                           float x, float y, float z, float u, float v) {
        VertexConsumer vertex = consumer.vertex(matrix, x, y, z).texture(u, v);
        addColor(vertex);
    }

    @OnlyIn(Dist.CLIENT)
    protected VertexConsumer addColor(VertexConsumer consumer) {
        if (useLight) {
            consumer.light(light);
        }
        return consumer.color(particleColor.argbI());
    }

    protected void checkLight(ParticleEmitter emitter, Vec3d pos) {
        this.useLight = emitter.getEmitterSetting().useLight;
        BlockPos blockPos = new BlockPos((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
        this.light = WorldRenderer.getLightmapCoordinates(
                MinecraftClient.getInstance().world, blockPos);
    }

    public Identifier getParticleResourceID() {
        return particleResourceID;
    }

    public List<Identifier> getParticleImageArray() {
        return List.of(particleResourceID);
    }

    public float getWeightTexture() {
        return weightTexture;
    }

    public float getHeightTexture() {
        return heightTexture;
    }

    public SimpleColor getParticleColor() {
        return particleColor;
    }

    public int getParticleColorI() {
        return particleColor.argbI();
    }

    public Codec<? extends VFXParticle> codec() {
        return CODEC;
    }

    public void setParticleColor(SimpleColor particleColor) {
        this.particleColor = particleColor;
    }
}
