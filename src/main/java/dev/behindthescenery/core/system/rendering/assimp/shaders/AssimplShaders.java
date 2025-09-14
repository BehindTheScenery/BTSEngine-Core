package dev.behindthescenery.core.system.rendering.assimp.shaders;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

public class AssimplShaders {

    public static final VertexFormat EMPTY = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
//            .add("", new VertexFormatElement(6, 0, VertexFormatElement.ComponentType.FLOAT, VertexFormatElement.Usage.GENERIC, 16))
            .build();


    public static ColorModelShader COLOR_RENDER;
    private static EmptyModelShader EMPTY_SHADER;

    public static ColorModelShader getColorShader() {
        if(COLOR_RENDER == null) {
            COLOR_RENDER = new ColorModelShader();
        }

        return COLOR_RENDER;
    }

    public static EmptyModelShader getEmptyShader() {
        if(EMPTY_SHADER == null) {
            EMPTY_SHADER = new EmptyModelShader();
        }

        return EMPTY_SHADER;
    }
}
