package dev.behindthescenery.core.system.rendering.vfx.particle;

import dev.behindthescenery.core.system.rendering.textures.atlas.TextureAtlasGenerator;
import dev.behindthescenery.core.system.rendering.textures.atlas.TextureRegion;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class VFXAnimatedParticle extends VFXParticle{

    public VFXAnimatedParticle(Identifier particleResourceID, float size) {
        super(particleResourceID, size);
    }

    public abstract int getMaxTime();

    @Nullable
    public Identifier getTextureByTime(int time) {
        return null;
    }

    /**
     * Использует ванильную систему анимированных текстур
     */
    public static class Vanilla extends VFXAnimatedParticle {

        public Vanilla(Identifier particleResourceID, float size) {
            super(particleResourceID, size);
        }

        @Override
        public int getMaxTime() {
            return 0;
        }
    }

    /**
     * Использует систему массива для анимированных текстур
     */
    public static class Array extends VFXAnimatedParticle {

        protected LinkedList<Identifier> textures = new LinkedList<>();

        public Array(float size) {
            super(null, size);
        }

        public Array(Collection<Identifier> textures, float size) {
            super(null, size);
            this.textures.addAll(textures);
        }

        public Array(Identifier[] textures, float size) {
            super(null, size);
            this.textures.addAll(Arrays.stream(textures).toList());
        }

        public Array addTexture(Identifier identifier) {
            this.textures.add(identifier);
            return this;
        }

        @Override
        public VFXParticle copy() {
            return new Array(textures, weightTexture);
        }

        @Override
        public int getMaxTime() {
            return textures.size();
        }

        @Override
        public List<Identifier> getParticleImageArray() {
            return textures;
        }

        @Override
        public void addVertexEmitter(MatrixStack.Entry matrix, BufferBuilder consumer, VFXRenderParticle renderParticle) {

            if(renderParticle.emitter.getEmitterSetting().useAnimation && renderParticle.animationTick >= getMaxTime() && renderParticle.emitter.getEmitterSetting().loopAnimation) {
                renderParticle.animationTick = 0;
            }

            Identifier textureID = getTextureByTime(renderParticle.animationTick);
            if(textureID == null) return;

            checkLight(renderParticle.emitter, renderParticle.position);

            float w = renderParticle.weight;
            float h = renderParticle.height;

            TextureAtlasGenerator atlas = renderParticle.emitter.getTextureAtlasGenerator();
            TextureRegion region = atlas.getRegion(textureID);

            addVertex(consumer, matrix, -w,  h, 0.0f, region.u0(), region.v1());
            addVertex(consumer, matrix, -w, -h, 0.0f, region.u0(), region.v0());
            addVertex(consumer, matrix,  w, -h, 0.0f, region.u1(), region.v0());
            addVertex(consumer, matrix,  w,  h, 0.0f, region.u1(), region.v1());
        }

        @Override
        @Nullable
        public Identifier getTextureByTime(int time) {
            return textures.get(Math.clamp(time, 0, getMaxTime() - 1));
        }
    }

    /**
     * Использует систему атласов из игровых движков
     */
    public static class Engine extends VFXAnimatedParticle {

        protected EngineType engineType;

        public Engine(Identifier particleResourceID, float size, EngineType engineType) {
            super(particleResourceID, size);
            this.engineType = engineType;
        }

        @Override
        public int getMaxTime() {
            return 0;
        }


        public enum EngineType {
            UNREAL,
            UNITY,
            CRYENGINE,
            UNIGINE,
            GODOTE
        }
    }
}
