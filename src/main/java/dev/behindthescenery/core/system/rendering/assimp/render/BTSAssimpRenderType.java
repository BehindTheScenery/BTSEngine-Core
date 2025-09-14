package dev.behindthescenery.core.system.rendering.assimp.render;


import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;
import java.util.function.Function;

public class BTSAssimpRenderType extends RenderLayer {

    private static final Map<Identifier, RenderLayer> RENDER_TYPES = new Object2ObjectOpenHashMap<>();

    public static final Function<Identifier, RenderLayer> CUTOUT_MESH_ENTITY = Util.memoize((resourceLocation) -> {
        MultiPhaseParameters compositeState = MultiPhaseParameters.builder()
                .program(ENTITY_CUTOUT_PROGRAM)
                .cull(ENABLE_CULLING)
                .lightmap(ENABLE_LIGHTMAP)
                .texture(new RenderPhase.Texture(resourceLocation, false, false))
                .transparency(NO_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                .build(true);
        return of("cutout_mesh_entity", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.TRIANGLES, 1536,
                true, false, compositeState);
    });

    public BTSAssimpRenderType(String name, VertexFormat format, VertexFormat.DrawMode mode, int bufferSize, boolean affectsCrumbling,
                               boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static boolean contains(RenderLayer layer) {
        return RENDER_TYPES.containsValue(layer);
    }

    public static RenderLayer getCutoutMeshEntity(Identifier identifier) {
        RenderLayer layer = RENDER_TYPES.get(identifier);
        if(layer == null) {
            layer = CUTOUT_MESH_ENTITY.apply(identifier);
            RENDER_TYPES.put(identifier, layer);
        }
        return layer;
    }
}
