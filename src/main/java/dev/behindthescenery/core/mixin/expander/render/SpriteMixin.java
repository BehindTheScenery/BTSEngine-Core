package dev.behindthescenery.core.mixin.expander.render;

import dev.behindthescenery.core.system.rendering.vertex.BtsUvExpander;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Sprite.class)
public class SpriteMixin implements BtsUvExpander {

    @Shadow @Final private float maxU;

    @Shadow @Final private float minU;

    @Shadow
    @Override
    public float getFrameU(float frame) {
        float f = this.maxU - this.minU;
        return this.minU + f * frame;
    }

    @Shadow
    @Override
    public float getFrameV(float frame) {
        float f = this.maxU - this.minU;
        return (frame - this.minU) / f;
    }
}
