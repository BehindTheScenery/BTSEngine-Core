package dev.behindthescenery.core.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class ResourcesUtils {

    public static byte[] readResourceBytes(Identifier resourceLocation){
        try {
            var resource = MinecraftClient.getInstance().getResourceManager().getResource(resourceLocation);
            return resource.get().getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
