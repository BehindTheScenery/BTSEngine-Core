package dev.behindthescenery.core.system.rendering.textures.atlas;

import com.mojang.datafixers.util.Pair;
import dev.behindthescenery.core.BtsCore;
import dev.behindthescenery.core.system.rendering.vfx.ParticleEmitter;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXParticle;
import dev.behindthescenery.core.system.rendering.vfx.particle.VFXRenderParticle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.*;

public class TextureAtlasGenerator {

    private final Identifier atlasId;
    private final Map<Identifier, TextureRegion> regions = new HashMap<>();
    private NativeImage atlasImage;
    private boolean isInitialized = false;

    public TextureAtlasGenerator(String atlasName) {
        this.atlasId = Identifier.of(BtsCore.MOD_ID, atlasName);
    }

    public void loadFromRenderParticle(Collection<VFXRenderParticle> particles) {
        Collection<Identifier> list = new ArrayList<>();
        for (VFXRenderParticle particle : particles) {
            list.addAll(particle.getParticle().getParticleImageArray());
        }

        loadFromResources(list);
    }

    public void loadFromParticle(Collection<VFXParticle> particle) {
        Collection<Identifier> list = new ArrayList<>();
        for (VFXParticle p : particle) {
            list.addAll(p.getParticleImageArray());
        }

        loadFromResources(list);
    }

    public void loadFromResources(Collection<Identifier> textures) {
        BtsCore.LOGGER.info("Creating atlas: {}", atlasId);
        int atlasWidth = 0;
        int atlasHeight = 0;

        List<Identifier> done = new ArrayList<>();

        List<Pair<Identifier, NativeImage>> images = new ArrayList<>();
        for (Identifier id : textures) {
            try {
                if(done.contains(id)) continue;

                Resource resource = MinecraftClient.getInstance().getResourceManager().getResourceOrThrow(id);
                NativeImage image = NativeImage.read(resource.getInputStream());
                images.add(new Pair<>(id, image));
                atlasWidth += image.getWidth();
                atlasHeight = Math.max(atlasHeight, image.getHeight());

                BtsCore.LOGGER.info("Loaded Texture {}", id);
                done.add(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final int MAX_ATLAS_SIZE = 8192;

        atlasImage = new NativeImage(NativeImage.Format.RGBA, atlasWidth, atlasHeight, false);
        int x = 0;
        int y = 0;
        int maxY = 0;

        for (Pair<Identifier, NativeImage> pair : images) {
            Identifier id = pair.getFirst();
            NativeImage img = pair.getSecond();

            maxY = Math.max(maxY, img.getHeight());

            while (x + img.getWidth() > atlasImage.getWidth()) {
                x = 0;
                y += maxY;
                maxY = 0;

                if (y + img.getHeight() > atlasImage.getHeight()) {
                    int newWidth = Math.min(atlasImage.getWidth() * 2, MAX_ATLAS_SIZE);  // Увеличиваем размер в два раза
                    int newHeight = Math.min(atlasImage.getHeight() * 2, MAX_ATLAS_SIZE);  // Увеличиваем размер в два раза
                    NativeImage newAtlas = new NativeImage(NativeImage.Format.RGBA, newWidth, newHeight, false);

                    newAtlas.copyRect(atlasImage, 0, 0, 0, 0, atlasImage.getWidth(), atlasImage.getHeight(), false, false);

                    atlasImage.close();

                    atlasImage = newAtlas;

                    if (atlasImage.getWidth() == MAX_ATLAS_SIZE && atlasImage.getHeight() == MAX_ATLAS_SIZE) {
                        throw new IllegalStateException("Unable to fit textures into atlas, reached max size.");
                    }
                }
            }

            if (x + img.getWidth() <= atlasImage.getWidth() && y + img.getHeight() <= atlasImage.getHeight()) {
                safeCopyRect(atlasImage, img, x, y);

                float u0 = (float) x / atlasImage.getWidth();
                float v0 = (float) y / atlasImage.getHeight();
                float u1 = (float) (x + img.getWidth()) / atlasImage.getWidth();
                float v1 = (float) (y + img.getHeight()) / atlasImage.getHeight();

                regions.put(id, new TextureRegion(u0, v0, u1, v1));

                x += img.getWidth();
                maxY = Math.max(maxY, img.getHeight());
            } else {
                BtsCore.LOGGER.error("Texture {} cannot fit into the current atlas at position ({}, {}). Attempting to expand atlas.", id, x, y);
                writeAtlasInfo();
            }

            img.close();
        }

        BtsCore.LOGGER.info("Created atlas: {}", atlasId);
    }

    protected void safeCopyRect(NativeImage src, NativeImage dst, int dstX, int dstY) {
        if (dst.getWidth() > 0 && dst.getHeight() > 0) {
            dst.copyRect(src, 0, 0, dstX, dstY, dst.getWidth(), dst.getHeight(), false, false);
        } else {
            BtsCore.LOGGER.error("Texture out of bounds: Cannot place texture [{}] on the atlas [{}]", src, dst);
            writeAtlasInfo();
        }
    }

    protected void writeAtlasInfo() {
        StringBuilder builder = new StringBuilder();

        builder.append("Atlas ID: ").append(atlasId).append("\n")
                .append("Weight: ").append(atlasImage.getWidth()).append(" ").append("Height: ").append(atlasImage.getHeight()).append("\n")
                .append("Format: ").append(atlasImage.getFormat());

        BtsCore.LOGGER.info(builder.toString());
    }

    public void bindEmitter(ParticleEmitter emitter) {
        if(isInitialized) return;
        loadFromParticle(emitter.getParticles());
        bind();
    }

    public void bind() {
        if (isInitialized || atlasImage == null) return;

        TextureManager manager = MinecraftClient.getInstance().getTextureManager();
        NativeImageBackedTexture texture = new NativeImageBackedTexture(atlasImage);
        manager.registerTexture(atlasId, texture);

        isInitialized = true;
    }

    public void unbind() {
        MinecraftClient.getInstance().getTextureManager().destroyTexture(atlasId);
        isInitialized = false;
    }

    public TextureRegion getRegion(Identifier id) {
        return regions.get(id);
    }

    public Identifier getAtlasId() {
        return atlasId;
    }
}
