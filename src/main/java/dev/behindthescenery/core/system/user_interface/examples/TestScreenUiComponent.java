package dev.behindthescenery.core.system.user_interface.examples;

import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.rendering.color.Texture;
import dev.behindthescenery.core.system.user_interface.render.MouseClick;
import dev.behindthescenery.core.system.user_interface.render.ui_component.ScreenUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.animator.UiAnimation;
import dev.behindthescenery.core.system.user_interface.render.ui_component.animator.UiAnimator;
import dev.behindthescenery.core.system.user_interface.render.ui_component.container.ExpansionUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.shaders.ColorShader;
import dev.behindthescenery.core.system.user_interface.render.ui_component.shaders.UiShaders;
import dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.TestUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.button.CheckBoxUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.button.SimpleButtonUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.text.AdvancedTextUiComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public class TestScreenUiComponent extends ScreenUiComponent {

    protected Texture texture = new Texture(Identifier.of("btsui", "textures/uniformclouds-1.png"));

    protected AdvancedTextUiComponent textUiComponent;
    protected SimpleButtonUiComponent buttonUiComponent;
    protected TestUiComponent testUiComponent;
    protected ExpansionUiComponent expansionUiComponent;
    protected CheckBoxUiComponent checkBoxUiComponent;

    protected AdvancedTextUiComponent textUiComponent2;

    protected UiAnimator animator;
    protected UiAnimator spinGovna;

    @Override
    public void onInit() {
        setSize(window.getScaledWidth(), window.getScaledHeight());
        setCentered(true);

        textUiComponent = new AdvancedTextUiComponent(Text.literal("1. Hello world how are you! 2. Hello world how are you!"));
        textUiComponent.setMaxWight(this.getWidth());
        textUiComponent.setScale(1f);
//        addComponent(textUiComponent);

        buttonUiComponent = new SimpleButtonUiComponent() {
            @Override
            public void onClicked(double mouseX, double mouseY, MouseClick mouseClick) {
                animator.play();
                spinGovna.play();
            }

            @Override
            public void refreshWidget() {}
        };
        buttonUiComponent.setPosY(30);
        buttonUiComponent.setRotation(45);
        buttonUiComponent.setScale(0.45f);
        buttonUiComponent.setOrdinal(20);
//        addComponent(buttonUiComponent);

        testUiComponent = new TestUiComponent();
        testUiComponent.setSize(100, 20);
        testUiComponent.setOrdinal(0);
//        addComponent(testUiComponent);

        animator = addAnimator(UiAnimator.create()
                .then(
                    new UiAnimation<>(
                            () -> RGBA.of(0,0,0,100),
                            testUiComponent::setColor,
                            RGBA.of(255, 255, 255, 255),
                            5_000,
                            UiAnimation.Interpolators.COLOR_RGBA,
                            UiAnimation.Easings.EASE_IN_OUT
                    ),
                    new UiAnimation<>(
                            () -> new Vector2f(testUiComponent.getPosX(), testUiComponent.getPosY()),
                            (vec) -> testUiComponent.setPosition((int) vec.x, (int) vec.y),
                        new Vector2f(testUiComponent.getPosX() + 20, testUiComponent.getPosY() + 30),
                        5_000,
                        UiAnimation.Interpolators.arcBySagitta(50f),
                        UiAnimation.Easings.LINEAR
                    )
                )
                .yoyo().loop());

        spinGovna = addAnimator(UiAnimator.create()
                .then(
                        new UiAnimation<>(
                                () -> 0f,
                                buttonUiComponent::setRotation,
                                360f,
                                60_000,
                                UiAnimation.Interpolators.FLOAT,
                                UiAnimation.Easings.LINEAR
                        )
                ).yoyo().loop());

        checkBoxUiComponent = new CheckBoxUiComponent();
        checkBoxUiComponent.setSize(this.getWidth(), 16);
//        addComponent(checkBoxUiComponent);

        expansionUiComponent = new ExpansionUiComponent(9);
        expansionUiComponent.setWidth(this.getWidth());
        expansionUiComponent.setTitle(Text.of("Hello world!"));
//        addComponent(expansionUiComponent);

        textUiComponent2 = new AdvancedTextUiComponent(Text.of("Fobos it's working!"));
        textUiComponent2.setMaxWight(expansionUiComponent.getWidth());
//        expansionUiComponent.addComponent(textUiComponent2);
    }

    @Override
    public void drawBackground(DrawContext context, int x, int y, int w, int h) {
//        RGBA.of(255,0,0,255 /3).draw(context, x, y, w, h);


        ColorShader shader = UiShaders.getColorShader();
        BtsRenderSystem.setGlobalShader(shader);
        shader.setUniformValue("screenSize", new Vector2f(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight()));

        RGBA.of(255,255,255,255).draw(context, x, y, w, h);
        BtsRenderSystem.clearGlobalShader();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
