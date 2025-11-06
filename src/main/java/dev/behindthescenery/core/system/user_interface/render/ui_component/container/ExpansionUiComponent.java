package dev.behindthescenery.core.system.user_interface.render.ui_component.container;

import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.rendering.utils.helpers.TextRenderHelper;
import dev.behindthescenery.core.system.user_interface.render.CursorType;
import dev.behindthescenery.core.system.user_interface.render.MouseClick;
import dev.behindthescenery.core.system.user_interface.render.ui_component.AbstractUiComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.joml.Vector2f;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.Supplier;

public class ExpansionUiComponent extends AbstractUiComponent implements UiComponentContainer {

    public static final RGBA HEADER = RGBA.of(100, 235, 5);
    public static final RGBA HEADER_CONTENT = RGBA.of(5, 235, 36, 135);

    protected int sizeComponents = 0;

    protected SimpleColor color = SimpleColor.DEFAULT;
    protected Text title = Text.empty();
    protected Collection<AbstractUiComponent> components;
    protected Collection<AbstractUiComponent> sortedComponents;
    protected boolean dropped = false;
    protected int titleHeight;

    public ExpansionUiComponent(int titleHeight) {
        this(titleHeight, HashSet::new);
    }

    public ExpansionUiComponent(int titleHeight, Supplier<Collection<AbstractUiComponent>> collectionContainer) {
        this.titleHeight = titleHeight;
        this.components = collectionContainer.get();
    }

    public ExpansionUiComponent setTextColor(SimpleColor color) {
        this.color = color;
        return this;
    }

    public ExpansionUiComponent setTitle(Text title) {
        this.title = title;
        return this;
    }

    public ExpansionUiComponent setDropped(boolean value) {
        this.dropped = value;
        return this;
    }

    public boolean isDropped() {
        return dropped;
    }

    @Override
    public void init() {}

    @Override
    public void refreshWidget() {
        recalculate();
        sortedComponents.forEach(AbstractUiComponent::refreshWidget);
    }

    protected void recalculate() {
        sortedComponents = getUiComponents().stream().sorted(Comparator.comparingInt(AbstractUiComponent::getOrdinal)).toList();
    }

    @Override
    public int getHeight() {
        if(dropped) {
            return titleHeight + getHeightComponents();
        }

        return titleHeight;
    }

    protected int getHeightComponents() {
        int h = 0;
        for (AbstractUiComponent component : sortedComponents) {
            h += component.getHeightRotation();
        }

        return sizeComponents = h;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, MouseClick mouseClick) {
        if(checkMouseOver(mouseX, mouseY) && mouseClick.isLeft()) {
            dropped = !dropped;

            System.out.println(dropped);
            return true;
        }

        return false;
    }

    @Override
    public Collection<AbstractUiComponent> getUiComponents() {
        return components;
    }

    @Override
    public Collection<AbstractUiComponent> getUiComponentsSortedByOrdinal() {
        return sortedComponents;
    }

    @Override
    public <T extends AbstractUiComponent> T addComponent(T component) {
        if(containsComponent(component)) {
            throw new RuntimeException("UiComponent already Added!");
        }

        component.setParent(this);
        component.init();
        components.add(component);
        recalculate();
        return component;
    }

    @Override
    public <T extends AbstractUiComponent> boolean containsComponent(T component) {
        return components.contains(component);
    }

    @Override
    public void clearUiComponents() {
        components = new HashSet<>();
        sortedComponents.clear();
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.HAND;
    }

    @Override
    public void draw(DrawContext context, int x, int y, int w, int h) {
        drawTitle(context, x, y, w, titleHeight);


        if(!dropped) return;

        int size = getHeightComponents();
        if(size == 0) return;

        drawComponentsBackground(context, x, y + titleHeight, w, size);
        drawComponents(context, x, y + titleHeight, w, size);
    }

    public void drawTitle(DrawContext context, int x, int y, int w, int h) {
        HEADER.draw(context, x, y, w, titleHeight);

        TextRenderHelper.drawTextOverWight(
                context,
                MinecraftClient.getInstance().textRenderer,
                title.getString(),
                new Vector2f(x + 10, y),
                w - 10,
                color
        );
    }

    public void drawComponentsBackground(DrawContext context, int x, int y, int w, int h) {
        HEADER_CONTENT.draw(context, x, y, w, h);
    }

    public void drawComponents(DrawContext context, int x, int y, int w, int h) {
        for (AbstractUiComponent sortedComponent : sortedComponents) {
            sortedComponent.draw(context, x, y + titleHeight, w, h);
        }
    }
}
