package dev.behindthescenery.core.system.user_interface.render.ui_component.widgets;

import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.rendering.color.Texture;
import dev.behindthescenery.core.system.user_interface.render.ui_component.AbstractUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.shaders.ColorShader;
import dev.behindthescenery.core.system.user_interface.render.ui_component.shaders.UiShaders;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.joml.Vector2f;

public class TestUiComponent extends AbstractUiComponent {

    protected Texture texture = new Texture(Identifier.of("btsui", "textures/uniformclouds-1.png"));

    private static final Random random = Random.create();

    protected RGBA simpleColor = getDefault();

    @Override
    public void init() {
    }

    @Override
    public void refreshWidget() {

    }

    public RGBA getColor() {
        return simpleColor;
    }

    public void setColor(RGBA simpleColor) {
        this.simpleColor = simpleColor;
    }


    public RGBA getDefault() {
        return RGBA.of(235,94,40,255);
    }


    @Override
    public void draw(DrawContext context, int x, int y, int w, int h) {
        ColorShader shader = UiShaders.getColorShader();
        BtsRenderSystem.setGlobalShader(shader);
//        shader.setUniformValue("buttonSize", new Vector2f(w, h));
        shader.setUniformValue("someTexture", texture.getTexture());


        shader.setUniformValue("iMouse", new Vector2f((float) MinecraftClient.getInstance().mouse.getX(), (float) MinecraftClient.getInstance().mouse.getY()));

        simpleColor.draw(context, x, y, w, h);
        BtsRenderSystem.clearGlobalShader();
    }
}
