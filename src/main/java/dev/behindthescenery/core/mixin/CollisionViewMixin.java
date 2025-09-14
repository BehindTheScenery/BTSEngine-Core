package dev.behindthescenery.core.mixin;

import com.google.common.collect.Iterables;
import dev.behindthescenery.core.system.rendering.managers.LevelRenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.CollisionView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(CollisionView.class)
public interface CollisionViewMixin {

    @Inject(method = "getBlockCollisions", at = @At("RETURN"), cancellable = true)
    default void bts$getBlockCollisions(Entity entity, Box box, CallbackInfoReturnable<Iterable<VoxelShape>> cir) {
        List<VoxelShape> customCollisions = LevelRenderManager.INSTANCE.getCollisions(box);

        if(customCollisions.isEmpty()) return;

        Iterable<VoxelShape> originalCollisions = cir.getReturnValue();
        cir.setReturnValue(Iterables.concat(originalCollisions, customCollisions));
    }
}
