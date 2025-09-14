package dev.behindthescenery.core.system.rendering.assimp;

import dev.behindthescenery.core.system.rendering.assimp.resource.AssimpResources;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManagerImpl;

public class AssimpResourcesImpl extends AssimpResources {

    public static AssimpResources instance() {
        return _instance;
    }

    protected static AssimpResources _instance;

    public static void initialize() {
        if(_instance != null) return;
        _instance = new AssimpResources();
        ((ReloadableResourceManagerImpl) MinecraftClient.getInstance().getResourceManager()).registerReloader(_instance);
    }
}
