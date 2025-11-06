package dev.behindthescenery.core.system.user_interface.render.ui_component.shaders;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;

public class UiShaders {

    public static final VertexFormat POSITION = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .build();

    public static final VertexFormat POSITION_NORMAL = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();

    public static final VertexFormat POSITION_UV_NORMAL = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("UV0", VertexFormatElement.UV_0)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();

    public static final VertexFormat POSITION_UV_NORMAL_COLOR = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("UV0", VertexFormatElement.UV_0)
            .add("Normal", VertexFormatElement.NORMAL)
            .add("Color", VertexFormatElement.COLOR)
            .build();

    protected static ColorShader COLOR_SHADER;


    public static ColorShader getColorShader() {
        if(COLOR_SHADER == null) {
            COLOR_SHADER = new ColorShader();
        }

        return COLOR_SHADER;
    }

}
