package dev.behindthescenery.core.mixin.access.render;

import net.minecraft.client.render.Frustum;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.FrustumIntersection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@OnlyIn(Dist.CLIENT)
@Mixin(Frustum.class)
public interface FrustumAccess {

    @Accessor
    FrustumIntersection getFrustumIntersection();

    @Accessor("x")
    double getX();

    @Accessor("y")
    double getY();

    @Accessor("z")
    double getZ();
}
