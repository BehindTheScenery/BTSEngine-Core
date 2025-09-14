package dev.behindthescenery.core.system.rendering.vfx.animator;

import dev.behindthescenery.core.system.rendering.vfx.ParticleEmitter;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXParticle;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXRenderParticle;

import java.util.List;

/**
 * Аниматор - позволяет изменять поведение частиц, и взаимодействовать с излучателем
 */
public class BasicEmitterAnimator {

    protected final ParticleEmitter emitter;

    public BasicEmitterAnimator(ParticleEmitter emitter) {
        this.emitter = emitter;
    }

    /**
     * Вызывается перед обновлением жизни частицы. Здесь можно прописать поведение частицы (движение, поворот и т.д)
     * @param renderParticle Частица которая рендерится
     * @param allParticles Список всех частиц которые рендерятся
     */
    public void updateParticleState(VFXRenderParticle renderParticle, List<VFXRenderParticle> allParticles) {}

    /**
     * Вызывается после всей логики обновления излучателя {@link ParticleEmitter#updateEmitterLogic}
     */
    public void updateParticleLogicPost() {
        emitter.recreateRandom();
    }

    public void updateParticleLive(VFXRenderParticle renderParticle, List<VFXRenderParticle> allParticles) {
        renderParticle.updateParticleLive();
    }

    /**
     * Вызывается когда у частицы заканчивается время жизни
     * @param renderParticle Частица, которая умерла
     * @param allParticles Список всех частиц которые рендерятся
     */
    public void onParticleDeath(VFXRenderParticle renderParticle, List<VFXRenderParticle> allParticles) {
        renderParticle.invalidateData();
    }

    /**
     * Вызывается когда у излучателя есть место и он может создать новую частицу
     * @param availableParticles Список доступных частиц, которые есть в излучателе
     * @param particleID ID частицы присвоенное самим излучателем
     */
    public VFXRenderParticle createParticle(List<VFXParticle> availableParticles, long particleID) {
        return new VFXRenderParticle(emitter, availableParticles.get(emitter.getRandomParticle()), emitter.getPosition(), particleID);
    }
}
