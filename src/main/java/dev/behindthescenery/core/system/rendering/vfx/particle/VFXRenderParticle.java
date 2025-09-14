package dev.behindthescenery.core.system.rendering.vfx.particle;

import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.vfx.ParticleEmitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.FrustumIntersection;
import org.joml.Vector3f;

import java.util.Objects;

public class VFXRenderParticle {

    protected long particleID;
    protected int animationTick;
    protected int particleLive;
    protected VFXParticle particle;
    protected Vec3d position;
    protected float weight;
    protected float height;
    protected final ParticleEmitter emitter;
    protected boolean isAnimated;
    protected Box occlusionBox;

    public VFXRenderParticle(ParticleEmitter emitter, VFXParticle particle, Vec3d position, long particleID) {
        this.particleLive = 0;
        this.animationTick = 0;
        this.weight = particle.getWeightTexture();
        this.height = particle.getHeightTexture();
        this.particle = particle;
        this.position = position;
        this.emitter = emitter;
        this.particleID = particleID;
        this.isAnimated = particle instanceof VFXAnimatedParticle;

        updateOcclusionBox();
    }

    public VFXRenderParticle copy() {
        return new VFXRenderParticle(emitter, particle.copy(), position, particleID);
    }

    public void copyFrom(VFXRenderParticle source) {
        this.position = source.position;
        this.particle = source.particle;
        this.isAnimated = source.isAnimated;
        this.occlusionBox = source.occlusionBox;
        this.height = source.height;
        this.weight = source.weight;
    }

    public void updateAnimationTick() {
        animationTick++;
    }

    public void updateParticleLive() {
        particleLive++;
    }

    public void invalidateData() {
        this.particleLive = 0;
        this.animationTick = 0;
        this.position = emitter.getPosition();
        updateOcclusionBox();
    }

    public void setPosition(Vec3d position) {
        if (position == null) {
            throw new IllegalArgumentException("Position can't be null!");
        }
        this.position = position;
        updateOcclusionBox();
    }

    public VFXRenderParticle updatePosition(Vec3d delta) {
        this.position = this.position.add(delta.multiply(1.2));
        updateOcclusionBox();
        return this;
    }

    protected void updateOcclusionBox() {
        float halfWidth = particle.getWeightTexture() / 2;
        float halfHeight = particle.getHeightTexture() / 2;
        this.occlusionBox = new Box(
                position.x - halfWidth,
                position.y - halfHeight,
                position.z - halfWidth,
                position.x + halfWidth,
                position.y + halfHeight,
                position.z + halfWidth
        );
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isCanSee() {
        Vector3f pPos = position.toVector3f();
        Vector3f fPos = BtsRenderSystem.getFrustumPosition().toVector3f();
        FrustumIntersection frustum = Objects.requireNonNull(BtsRenderSystem.getFrustumAccess()).getFrustumIntersection();

        if (!frustum.testPoint(pPos.x - fPos.x, pPos.y - fPos.y, pPos.z - fPos.z)) {
            return false;
        }

        if (emitter.getEmitterSetting().renderDistance > 0) {
            double distanceSq = MinecraftClient.getInstance().player.squaredDistanceTo(position);
            return distanceSq <= emitter.getEmitterSetting().renderDistance;
        }

        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void applyCameraParam(MatrixStack matrixStack) {
        BtsRenderSystem.applyCameraOffset(matrixStack, position);
        BtsRenderSystem.applyCameraRotation(matrixStack);
        float size = 0.5f;
        matrixStack.scale(-size, -size, -size);
    }

    @OnlyIn(Dist.CLIENT)
    public void addParticle(MatrixStack.Entry matrix, BufferBuilder buffer) {
        particle.addVertexEmitter(matrix, buffer, this);
    }

    public long getParticleID() {
        return particleID;
    }

    public int getParticleLive() {
        return particleLive;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public VFXParticle getParticle() {
        return particle;
    }

    public Vec3d getPosition() {
        return position;
    }

    public Box getOcclusionBox() {
        return occlusionBox;
    }

    public void setOcclusionBox(Box occlusionBox) {
        this.occlusionBox = occlusionBox;
    }
}
