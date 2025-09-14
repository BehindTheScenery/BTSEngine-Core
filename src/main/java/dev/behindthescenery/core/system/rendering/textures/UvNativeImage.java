package dev.behindthescenery.core.system.rendering.textures;

import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.vertex.BtsUvExpander;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public record UvNativeImage(NativeImage image) implements BtsUvExpander {

    @Override
    public float getFrameU(float frame) {
        return Math.clamp(frame, 0, 1);
    }

    @Override
    public float getFrameV(float frame) {
        return Math.clamp(frame, 0, 1);
    }

    @Nullable
    public static UvNativeImage getImage(Identifier identifier) {
        UvNativeImage image = null;
        try {
            Resource resource = MinecraftClient.getInstance().getResourceManager().getResource(identifier).orElseThrow();
            InputStream stream = resource.getInputStream();
            image = new UvNativeImage(NativeImage.read(stream));
            stream.close();
        } catch (IOException e) {
            BtsCore.LOGGER.error(e.toString());
        }
        return image;
    }
}
