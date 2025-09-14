package dev.behindthescenery.core.system.rendering.vfx;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.IWorldRender;
import dev.behindthescenery.core.system.rendering.RenderThreads;
import dev.behindthescenery.core.system.rendering.textures.atlas.TextureAtlasGenerator;
import dev.behindthescenery.core.system.rendering.vfx.animator.BasicEmitterAnimator;
import dev.behindthescenery.core.system.rendering.vfx.animator.CachedEmitterAnimator;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXAnimatedParticle;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXParticle;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXRenderParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ParticleEmitter implements IWorldRender {

    protected Random random = Random.create();

    /**
     * Возможные частицы для появления
     */
    protected List<VFXParticle> particles = new ArrayList<>();

    /**
     * Частицы которые на данный момент обрабатываются в потоке логики
     */
    protected List<VFXRenderParticle> logicParticles = new ArrayList<>();

    /**
     * Частицы которые на данный момент обрабатываются в потоке рендера
     */
    protected List<VFXRenderParticle> renderParticles = new ArrayList<>();

    /**
     * Позиция излучателя
     */
    protected Vec3d position;

    /**
     * Настройки излучателя
     */
    protected EmitterSetting emitterSetting = new EmitterSetting();

    /**
     * Поток обработки логики
     */
    protected ScheduledFuture<?> logicTask;

    /**
     * Поток обработки анимации
     */
    protected ScheduledFuture<?> animationTask;

    /**
     * Аниматор частиц, он позволяет взаимодействовать с частицами
     */
    protected BasicEmitterAnimator emitterAnimator = new CachedEmitterAnimator(this);

    protected TextureAtlasGenerator textureAtlasGenerator;

    /**
     * Скорость логики излучателя
     */
    protected long logicSpeed = 7;

    /**
     * Скорость анимации излучателя
     */
    protected long animationSpeed = 7;

    /**
     * Блокиратор, чтобы не было гонки данных и данные были синхронизированы
     */
    private final Object particleLock = new Object();

    public ParticleEmitter(Vec3d position, int maxParticleCounts) {
        this.position = position;
        this.emitterSetting.maxParticleCounts = maxParticleCounts;
        this.emitterSetting.emitterZone = new Box(new BlockPos((int) position.x, (int) position.y, (int) position.z));
        startParticleTask(logicSpeed);
        startParticleAnimation(animationSpeed);
        this.textureAtlasGenerator = new TextureAtlasGenerator("emitter_" + hashCode());
    }

    public ParticleEmitter updateData(Consumer<ParticleEmitter> emitterConsumer) {
        emitterConsumer.accept(this);
        return this;
    }

    public ParticleEmitter addParticle(VFXParticle particle) {
        if(particle instanceof VFXAnimatedParticle)
            emitterSetting.useAnimation = true;

        this.particles.add(particle);
        return this;
    }

    public ParticleEmitter setAdvancedCulling() {
        this.emitterSetting.useAdvancedCulling = true;
        return this;
    }

    public ParticleEmitter setMinParticleLive(int minParticleLive) {
        emitterSetting.minParticleLive = Math.clamp(minParticleLive, 0, emitterSetting.maxParticleLive);
        return this;
    }

    public ParticleEmitter setMaxParticleLive(int maxParticleLive) {
        emitterSetting.maxParticleLive = Math.max(maxParticleLive, emitterSetting.minParticleLive);
        return this;
    }

    public ParticleEmitter setPosition(Vec3d position) {
        this.position = position;
        return this;
    }

    public EmitterSetting getEmitterSetting() {
        return emitterSetting;
    }

    public Vec3d getPosition() {
        return position;
    }

    public int getMaxParticleCounts() {
        return emitterSetting.maxParticleCounts;
    }

    public int getMaxParticleLive() {
        return emitterSetting.maxParticleLive;
    }

    public int getMinParticleLive() {
        return emitterSetting.minParticleLive;
    }

    public List<VFXParticle> getParticles() {
        return particles;
    }

    public ParticleEmitter changeSpeed(long millis) {
        startParticleTask(millis);
        return this;
    }

    public ParticleEmitter changeAnimationSpeed(long millis) {
        startParticleAnimation(millis);
        return this;
    }

    /**
     * Количество частиц которые на данный момент рендеряться
     */
    public long getElementsOnEmitter() {
        return renderParticles.size();
    }

    public Random getRandom() {
        return random;
    }

    public Random recreateRandom() {
        random = Random.create();
        return random;
    }

    public Random recreateRandom(long seed) {
        random = Random.create(seed);
        return random;
    }

    /**
     * Проверяет находиться ли излучатель в поле зрения игрока
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isCanSee() {
        if(Objects.requireNonNull(BtsRenderSystem.getFrustum()).isVisible(this.emitterSetting.emitterZone)) {
            if(getEmitterSetting().renderDistance > 0)
                return MinecraftClient.getInstance().player.squaredDistanceTo(position) <= getEmitterSetting().renderDistance;
            return true;
        }

        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderInWorld(MatrixStack matrixStack, VertexConsumerProvider.Immediate builder) {
        if(MinecraftClient.getInstance().world == null) return;
        if(!isCanSee() && !getEmitterSetting().useAdvancedCulling) return;

        this.textureAtlasGenerator.bindEmitter(this);

        if(emitterSetting.renderZone) {
            matrixStack.push();
            BtsRenderSystem.applyCameraOffset(matrixStack, emitterSetting.emitterZone.getCenter());

            WorldRenderer.drawBox(matrixStack, builder.getBuffer(RenderLayer.getLines()), emitterSetting.emitterZone, 255,255,255,255);

            matrixStack.pop();
        }

        matrixStack.push();

        synchronized (particleLock) {

            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.depthMask(getEmitterSetting().useDepthMask);
            RenderSystem.setShader(getEmitterSetting().useLight ? GameRenderer::getPositionColorTexLightmapProgram : GameRenderer::getPositionTexColorProgram);

            RenderSystem.setShaderTexture(0, textureAtlasGenerator.getAtlasId());
            BufferBuilder consumer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, getEmitterSetting().useLight ? VertexFormats.POSITION_TEXTURE_COLOR_LIGHT : VertexFormats.POSITION_TEXTURE_COLOR);

            for (VFXRenderParticle particle : renderParticles) {

                if(!emitterSetting.useAdvancedCulling || particle.isCanSee()) {
                    matrixStack.push();
                    particle.applyCameraParam(matrixStack);
                    particle.addParticle(matrixStack.peek(), consumer);
                    matrixStack.pop();
                }
            }

            try {
                BuiltBuffer buf = consumer.endNullable();
                if (buf != null) {
                    BufferRenderer.drawWithGlobalProgram(buf);
                    buf.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        matrixStack.pop();
    }

    /**
     * Проверяет нужно ли добавлять новые частицы или удалить старые
     */
    protected void checkRenderParticle() {
        for (VFXRenderParticle logicParticle : logicParticles) {
            if(logicParticle.getParticleLive() >= getMaxParticleLive())
                emitterAnimator.onParticleDeath(logicParticle, logicParticles);
        }

        if(logicParticles.size() <= getMaxParticleCounts()) {
            addRenderParticle();
        }
    }

    /**
     * Обрабатывает логику частиц.
     */
    protected void updateEmitterLogic() {
        if(isGamePaused()) return;

        synchronized (particleLock) {
            for (VFXRenderParticle renderParticle : logicParticles) {
                emitterAnimator.updateParticleState(renderParticle, logicParticles);
                emitterAnimator.updateParticleLive(renderParticle, logicParticles);
            }
            checkRenderParticle();

            renderParticles.clear();
            renderParticles.addAll(logicParticles);

            emitterAnimator.updateParticleLogicPost();
        }
    }

    protected void updateParticleAnimation() {
        if(isGamePaused() || !emitterSetting.useAnimation) return;

        synchronized (particleLock) {
            for (VFXRenderParticle logicParticle : logicParticles) {
                logicParticle.updateAnimationTick();
            }
        }
    }

    protected boolean isGamePaused() {
        if(MinecraftClient.getInstance() != null && MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().isPaused()) return true;
        if(ServerLifecycleHooks.getCurrentServer() != null && ServerLifecycleHooks.getCurrentServer().isPaused()) return true;
        return false;
    }

    /**
     * Добавляет новые частицы
     */
    protected void addRenderParticle() {
        if(getParticles().isEmpty()) return;
        logicParticles.add(emitterAnimator.createParticle(getParticles(), getElementsOnEmitter() + 1));
    }

    /**
     * Запускает обработку излучателя на отдельно потоке используя задержки для установки скорости
     * @param intervalMillis Скорость в миллисекундах
     */
    protected void startParticleTask(long intervalMillis) {
        if (logicTask != null && !logicTask.isCancelled()) {
            logicTask.cancel(false);
        }

        this.logicTask = RenderThreads.PARTICLE_LOGIC_THREAD.scheduleAtFixedRate(this::updateEmitterLogic, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Запускает обработку анимации излучателя на отдельно потоке используя задержки для установки скорости
     * @param intervalMillis Скорость в миллисекундах
     */
    protected void startParticleAnimation(long intervalMillis) {
        if(animationTask != null && !animationTask.isCancelled()) {
            animationTask.cancel(false);
        }

        this.animationTask = RenderThreads.PARTICLE_ANIMATION_THREAD.scheduleAtFixedRate(this::updateParticleAnimation, 0, intervalMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Уничтожает излучатель, основная цель данного метода остановить поток
     */
    protected void destroy() {
        logicTask.cancel(false);
        textureAtlasGenerator.unbind();
    }

    /**
     * Получает индекс случайной частицы из списка возможных для создания {@link ParticleEmitter#particles}
     */
    public int getRandomParticle() {
       return random.nextInt(getParticles().size());
    }

    public TextureAtlasGenerator getTextureAtlasGenerator() {
        return textureAtlasGenerator;
    }

    public long getAnimationSpeed() {
        return animationSpeed;
    }

    public long getLogicSpeed() {
        return logicSpeed;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Current Elements: ").append(getElementsOnEmitter()).append("\n")
                .append("Max Elements: ").append(getMaxParticleCounts());
        return builder.toString();
    }
}
