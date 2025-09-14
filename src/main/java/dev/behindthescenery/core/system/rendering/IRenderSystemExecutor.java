package dev.behindthescenery.core.system.rendering;

import com.mojang.blaze3d.systems.RenderCall;
import com.mojang.blaze3d.systems.RenderSystem;

public interface IRenderSystemExecutor {

    default void executeRender(RenderCall renderCall) {
        if(!RenderSystem.isOnRenderThread()) RenderSystem.recordRenderCall(renderCall);
        else renderCall.execute();
    }
}
