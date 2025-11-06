package dev.behindthescenery.core.system.user_interface.render.ui_component.container;

import dev.behindthescenery.core.system.rendering.utils.helpers.GLRenderHelper;
import dev.behindthescenery.core.system.user_interface.render.CursorType;
import dev.behindthescenery.core.system.user_interface.render.MouseClick;
import dev.behindthescenery.core.system.user_interface.render.ui_component.AbstractUiComponent;
import dev.behindthescenery.core.system.user_interface.render.ui_component.animator.UiAnimator;
import dev.behindthescenery.core.system.user_interface.render.ui_component.animator.UiAnimatorSupport;
import dev.behindthescenery.core.system.user_interface.render.ui_component.layout.UiLayout;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.Supplier;

public abstract class AbstractUiElementsContainer extends AbstractUiComponent implements UiComponentContainer, UiAnimatorSupport {

    protected final Collection<AbstractUiComponent> components;
    protected final Collection<UiAnimator> animators;
    protected Collection<AbstractUiComponent> sortedComponents;

    protected UiLayout layout;
    protected boolean useScissor = true;
    protected int maxWidth, maxHeight;
    private int contentWidth = -1, contentHeight = -1;
    protected boolean dirty;

    public AbstractUiElementsContainer(Supplier<Collection<AbstractUiComponent>> collectionContainer) {
        this.components = collectionContainer.get();
        this.animators = new HashSet<>();
        this.layout = (_) -> {};
        this.scrollOffsetX = 0;
        this.scrollOffsetY = 0;
        this.useOffset = true;
    }

    @Override
    public void refreshWidget() {
        if(dirty) {
            recalculate();
        } else {
            sortedComponents.forEach(AbstractUiComponent::refreshWidget);
            layout.apply(this);
            recalculateScrollBounds();
        }
    }

    @Override
    public void tick() {
        for (AbstractUiComponent sortedComponent : sortedComponents) {
            sortedComponent.tick();
        }

        for (UiAnimator animator : animators) {
            animator.tick();
        }

    }

    public void reInit() {
        dirty = true;
        clearUiComponents();
        init();
        refreshWidget();
        sortedComponents.forEach(AbstractUiComponent::init);
        layout.apply(this);
        recalculateScrollBounds();
        for (UiAnimator animator : getAnimators()) {
            animator.reset();
        }
        dirty = false;
    }

    protected void recalculate() {
        sortedComponents = getUiComponents().stream().sorted(Comparator.comparingInt(AbstractUiComponent::getOrdinal)).toList();
        dirty = false;
    }

    public int getContentWidth() {
        if (contentWidth == -1) {
            var minX = Integer.MAX_VALUE;
            var maxX = Integer.MIN_VALUE;

            for (var widget : sortedComponents) {
                if(!widget.isUseOffset()) continue;

                if (widget.getPosX() < minX) {
                    minX = widget.getPosX();
                }

                if (widget.getPosX() + widget.getWidthRotation() > maxX) {
                    maxX = widget.getPosX() + widget.getWidthRotation();
                }
            }

            contentWidth = maxX - minX;
        }

        return contentWidth;
    }

    public int getContentHeight() {
        if (contentHeight == -1) {
            var minY = Integer.MAX_VALUE;
            var maxY = Integer.MIN_VALUE;

            for (var widget : sortedComponents) {
                if(!widget.isUseOffset()) continue;

                if (widget.getPosY() < minY) {
                    minY = widget.getPosY();
                }

                if (widget.getPosY() + widget.getHeightRotation() > maxY) {
                    maxY = widget.getPosY() + widget.getHeightRotation();
                }
            }

            contentHeight = maxY - minY;
        }

        return contentHeight;
    }

    protected void recalculateScrollBounds() {
        maxWidth = 0;
        maxHeight = 0;
        for (AbstractUiComponent component : sortedComponents) {
            int componentRight = component.getPosX() + component.getWidth();
            int componentBottom = component.getPosY() + component.getHeight();
            maxWidth = Math.max(maxWidth, componentRight);
            maxHeight = Math.max(maxHeight, componentBottom);
        }
        maxWidth = Math.max(maxWidth, getWidth());
        maxHeight = Math.max(maxHeight, getHeight());
        scrollOffsetX = Math.min(0, Math.max(scrollOffsetX, -(maxWidth - getWidth())));
        scrollOffsetY = Math.min(0, Math.max(scrollOffsetY, -(maxHeight - getHeight())));
    }

    @Override
    public Collection<AbstractUiComponent> getUiComponents() {
        return components;
    }

    @Override
    public <T extends AbstractUiComponent> T addComponent(T component) {
        if(containsComponent(component)) {
            throw new RuntimeException("UiComponent already Added!");
        }
        component.setParent(this);
        components.add(component);
        setDirty();
        return component;
    }

    @Override
    public Collection<AbstractUiComponent> getUiComponentsSortedByOrdinal() {
        return sortedComponents;
    }

    @Override
    public Collection<UiAnimator> getAnimators() {
        return animators;
    }

    @Override
    public <T extends AbstractUiComponent> boolean containsComponent(T component) {
        return components.contains(component);
    }

    @Override
    public void clearUiComponents() {
        components.clear();
        sortedComponents = new ArrayList<>();
    }

    public void setDirty() {
        this.dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setLayout(UiLayout layout) {
        this.layout = layout;
    }

    @Override
    public void draw(DrawContext context, int x, int y, int w, int h) {
        final MatrixStack matrix = context.getMatrices();

        if (useScissor)
            GLRenderHelper.enableScissor(context, x, y, w , h);

        drawBackground(context, x, y, w, h);
        drawOffsetBackground(context, x + scrollOffsetX, y + scrollOffsetY, w, h);

        for (AbstractUiComponent sortedComponent : sortedComponents) {
            matrix.push();

            final boolean offset = sortedComponent.isUseOffset();
            final int pX = sortedComponent.getGlobalX() + (offset ? scrollOffsetX : 0);
            final int pY = sortedComponent.getGlobalY() + (offset ? scrollOffsetY : 0);
            final int pW = sortedComponent.getWidth();
            final int pH = sortedComponent.getHeight();
            final float rot = sortedComponent.getTransform().getRotation();
            final float scale = sortedComponent.getScale();
            final int pX1 = pX + pW / 2;
            final int pY1 = pY + pH / 2;

            matrix.translate(pX1, pY1, 0);

            if(rot != 0) {
                matrix.multiply(new Quaternionf().rotateZ(rot));
            }

            if(scale != 1) {
                matrix.scale(scale, scale, scale);
            }

            matrix.translate(-pX1, -pY1, 0);

            drawComponent(
                    context,
                    sortedComponent,
                    pX, pY, pW, pH
            );

            matrix.pop();
        }

        if (useScissor)
            GLRenderHelper.disableScissor(context);
    }

    public void drawComponent(DrawContext context, AbstractUiComponent component, int x, int y, int w, int h) {
        component.draw(context, x, y, w, h);
    }

    public void drawBackground(DrawContext context, int x, int y, int w, int h) {}

    public void drawOffsetBackground(DrawContext context, int x, int y, int w, int h) {}

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, MouseClick mouseClick, double deltaX, double deltaY) {
        for (AbstractUiComponent component : sortedComponents) {
            final boolean offset = component.isUseOffset();

            if (component.mouseDragged(
                    mouseX - (offset ? scrollOffsetX : 0),
                    mouseY - (offset ? scrollOffsetY : 0),
                    mouseClick, deltaX, deltaY)
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        boolean consumed = false;

        if (checkMouseOver(mouseX, mouseY)) {

            if (getContentHeight() > getHeightRotation()) {
                int oldScrollOffsetY = scrollOffsetY;
                scrollOffsetY = Math.min(0, Math.max(scrollOffsetY - (int)(-verticalAmount * 20), -(getContentHeight() - getHeightRotation())));
                if (oldScrollOffsetY != scrollOffsetY) {
                    consumed = true;
                }
            }

            if (getContentWidth() > getWidthRotation()) {
                int oldScrollOffsetX = scrollOffsetX;
                scrollOffsetX = Math.min(0, Math.max(scrollOffsetX - (int)(-horizontalAmount * 20), -(getContentWidth() - getWidthRotation())));
                if (oldScrollOffsetX != scrollOffsetX) {
                    consumed = true;
                }
            }
        }

        for (AbstractUiComponent component : sortedComponents) {
            final boolean offset = component.isUseOffset();

            if(consumed && offset) {
                component.setScrollOffsetX(scrollOffsetX);
                component.setScrollOffsetY(scrollOffsetY);
            }

            if (component.mouseScrolled(
                    mouseX - (offset ? scrollOffsetX : 0),
                    mouseY - (offset ? scrollOffsetY : 0),
                    horizontalAmount, verticalAmount)) {
                consumed = true;
            }
        }
        return consumed;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (AbstractUiComponent component : sortedComponents) {
            final boolean offset = component.isUseOffset();

            component.mouseMoved(
                    mouseX - (offset ? scrollOffsetX : 0),
                    mouseY - (offset ? scrollOffsetY : 0)
            );
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, MouseClick mouseClick) {
        double adjustedMouseX = mouseX - scrollOffsetX;
        double adjustedMouseY = mouseY - scrollOffsetY;

        for (AbstractUiComponent component : sortedComponents) {
            final boolean offset = component.isUseOffset();
            if (component.mouseClicked(offset ? adjustedMouseX : mouseX, offset ? adjustedMouseY : mouseY, mouseClick)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, MouseClick mouseClick) {
        for (AbstractUiComponent component : sortedComponents) {
            final boolean offset = component.isUseOffset();
            if (component.mouseReleased(
                    mouseX - (offset ? scrollOffsetX : 0),
                    mouseY - (offset ? scrollOffsetY : 0),
                    mouseClick)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkMouseOver(double mouseX, double mouseY) {
        boolean isOver = super.checkMouseOver(mouseX, mouseY);

        double adjustedMouseX = mouseX - scrollOffsetX;
        double adjustedMouseY = mouseY - scrollOffsetY;

        for (AbstractUiComponent sortedComponent : sortedComponents) {
            final boolean offset = sortedComponent.isUseOffset();
            if (isOver && sortedComponent.checkMouseOver(
                    offset ? adjustedMouseX : mouseX,
                    offset ? adjustedMouseY : mouseY)
            ) {
                CursorType.set(sortedComponent.getCursorType());
                return true;
            }
        }

        CursorType.set(getCursorType());
        return isOver;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }
}
