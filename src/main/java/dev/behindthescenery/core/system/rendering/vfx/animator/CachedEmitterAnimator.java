package dev.behindthescenery.core.system.rendering.vfx.animator;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.behindthescenery.core.system.rendering.vfx.ParticleEmitter;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXRenderParticle;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * Аниматор с использованием кэширования через библиотеку <a href="https://github.com/ben-manes/caffeine">Caffeine</a>,
 * что позволяет не проводить постоянные однотипные расчёты
 */
public class CachedEmitterAnimator extends BasicEmitterAnimator{

    protected final Cache<TickKey, ParticleFrameData> cache;

    public CachedEmitterAnimator(ParticleEmitter emitter) {
        super(emitter);
        this.cache = Caffeine.newBuilder()
                .maximumSize(emitter.getMaxParticleCounts() * 10L)
                .build();
    }

    /**
     * Метод для обновления состояние частицы
     * @param currentTick Время жизни частицы
     * @param renderParticle Частица которая рендерится
     */
    protected void updateParticle(int currentTick, VFXRenderParticle renderParticle) {
        renderParticle.updatePosition(generateRandomPos());
    }

    /**
     * Метод, который кэширует частицы по их времени жизни и {@link VFXRenderParticle#particleID}
     * @param renderParticle Частица которая рендерится
     * @param allParticles Список всех частиц которые рендерятся
     */
    @Override
    public void updateParticleState(VFXRenderParticle renderParticle, List<VFXRenderParticle> allParticles) {
        int currentTick = renderParticle.getParticleLive();
        TickKey key = new TickKey(currentTick, renderParticle.getParticleID());

        ParticleFrameData frameData = cache.get(key, __ -> {
            updateParticle(currentTick, renderParticle);
            return new ParticleFrameData(renderParticle);
        });

        renderParticle.copyFrom(frameData.particle);
    }

    protected Vec3d generateRandomPos() {
        double x = emitter.getRandom().nextGaussian() * 0.2;
        double y = Math.abs(emitter.getRandom().nextGaussian() * 0.2);
        double z = emitter.getRandom().nextGaussian() * 0.2;

        x = Math.max(-0.5, Math.min(0.5, x));
        y = Math.max(0, Math.min(0.5, y));
        z = Math.max(-0.5, Math.min(0.5, z));

        return new Vec3d(x, y, z);
    }

    /**
     * Очищает кэш
     */
    public void clearCache() {
        cache.invalidateAll();
    }

    public record TickKey(int tick, long particleId) {}

    public record ParticleFrameData(VFXRenderParticle particle) {
        public ParticleFrameData(VFXRenderParticle particle) {
            this.particle = particle.copy();
        }
    }
}
