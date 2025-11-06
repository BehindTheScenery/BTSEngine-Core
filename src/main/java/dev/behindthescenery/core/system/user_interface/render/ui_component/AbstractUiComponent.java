package dev.behindthescenery.core.system.user_interface.render.ui_component;

import dev.behindthescenery.core.system.user_interface.render.CursorType;
import dev.behindthescenery.core.system.user_interface.render.KeyboardKey;
import dev.behindthescenery.core.system.user_interface.render.MouseClick;
import dev.behindthescenery.core.system.user_interface.render.ui_component.transform.FieldTransform;
import dev.behindthescenery.core.system.user_interface.render.ui_component.transform.SimpleTransform;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractUiComponent implements DrawableElements {

    @Nullable
    protected AbstractUiComponent parent;

    protected SimpleTransform<?> transform = new FieldTransform();

    protected int ordinal;
    protected int scrollOffsetX;
    protected int scrollOffsetY;
    protected boolean mouseOver;

    protected boolean useOffset = true;

    protected AABB cacheRotationSizeGlobal;
    protected AABB cacheRotationSizeLocal;

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setParent(@Nullable AbstractUiComponent parent) {
        this.parent = parent;
        this.cacheRotationSizeGlobal = null;
        this.cacheRotationSizeLocal = null;
    }

    public AbstractUiComponent setTransform(SimpleTransform<?> transform) {
        this.transform = transform;
        return this;
    }

    public SimpleTransform<?> getTransform() {
        return transform;
    }

    public boolean isUseOffset() {
        return useOffset;
    }

    public void setUseOffset(boolean useOffset) {
        this.useOffset = useOffset;
    }

    public void setPosX(int posX) {
        transform.setPosX(posX);
    }

    public void setPosY(int posY) {
        transform.setPosY(posY);
    }

    public int getPosX() {
        return transform.getPosX() + scrollOffsetX;
    }

    public int getPosY() {
        return transform.getPosY() + scrollOffsetY;
    }

    public void setRotation(float rotation) {
        transform.setRotation(rotation);
    }

    public float getRotation() {
        return transform.getRotation();
    }

    /**
     * Координаты с учётом родителя и смещения
     */
    public int getGlobalX() {
        return (parent != null ? parent.getGlobalX() + transform.getPosX() : transform.getPosX()) + (useOffset ? getScrollOffsetX() : 0);
    }

    /**
     * Координаты с учётом родителя и смещения
     */
    public int getGlobalY() {
        return (parent != null ? parent.getGlobalY() + transform.getPosY() : transform.getPosY()) + (useOffset ? getScrollOffsetY() : 0);
    }

    public void setScrollOffsetX(int scrollOffsetX) {
        this.scrollOffsetX = scrollOffsetX;
    }

    public void setScrollOffsetY(int scrollOffsetY) {
        this.scrollOffsetY = scrollOffsetY;
    }

    public int getScrollOffsetX() {
        return scrollOffsetX;
    }

    public int getScrollOffsetY() {
        return scrollOffsetY;
    }

    public void setPosition(int posX, int posY) {
        setPosX(posX);
        setPosY(posY);
    }

    public void setScale(float scale) {
        transform.setScale(scale);
    }

    public float getScale() {
        return transform.getScale();
    }

    public int getHeight() {
        return transform.getHeight();
    }

    public int getHeightRotation() {
        return getSizeBox().height;
    }

    public int getWidth() {
        return transform.getWidth();
    }

    public int getWidthRotation() {
        return getSizeBox().width;
    }

    public void setWidth(int width) {
        transform.setWidth(width);
    }

    public void setHeight(int height) {
        transform.setHeight(height);
    }

    public void setSize(int weight, int height) {
        setWidth(weight);
        setHeight(height);
    }

    @Nullable
    public AbstractUiComponent getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public abstract void init();

    public abstract void refreshWidget();

    public void tick() {}

    public abstract void draw(DrawContext context, int x, int y, int w, int h);

    public boolean keyPressed(KeyboardKey key) {
        return true;
    }

    public boolean keyReleased(KeyboardKey key) {
        return true;
    }

    public boolean charTyped(char chr, int modifiers) {
        return true;
    }

    public void mouseMoved(double mouseX, double mouseY) {}

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, MouseClick mouseClick) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, MouseClick mouseClick) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, MouseClick mouseClick, double deltaX, double deltaY) {
        return false;
    }

    public boolean checkMouseOver(double mouseX, double mouseY) {
        final int posX = this.getGlobalX();
        final int posY = this.getGlobalY();
        final int w = getWidth();
        final int h = getHeight();
        final double angle = getTransform().getRotation();
        final float scale = getScale();

        if(angle == 0 && scale == 1) {
            return mouseOver = mouseX >= posX && mouseX <= posX + w &&
                    mouseY >= posY && mouseY <= posY + h;
        }

        final double centerX = posX + w / 2.0;
        final double centerY = posY + h / 2.0;

        double dx = mouseX - centerX;
        double dy = mouseY - centerY;

        if (angle != 0) {
            final double cos = Math.cos(-angle);
            final double sin = Math.sin(-angle);
            double rx = dx * cos - dy * sin;
            double ry = dx * sin + dy * cos;
            dx = rx;
            dy = ry;
        }

        if (scale != 1) {
            dx /= scale;
            dy /= scale;
        }

        return mouseOver = dx >= -w / 2.0 && dx <= w / 2.0 &&
                dy >= -h / 2.0 && dy <= h / 2.0;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Курсор который должен отображаться при наведении.
     * Если Null то будет стандартный в Windows
     */
    @Nullable
    public CursorType getCursorType() {
        return null;
    }

    public final AABB getSizeBox() {
        return getSizeBox(true);
    }

    /**
     * Считает размер зоны которую занимает виджет на экране с учётом поворота и размера
     * @param global - Координаты относительно которых будет проходить расчёт
     */
    public final AABB getSizeBox(boolean global) {
        if((global && cacheRotationSizeGlobal == null) || (!global && cacheRotationSizeLocal == null)) {
            final int width = getWidth();
            final int height = getHeight();
            final int posX = global ? getGlobalX() : getPosX();
            final int posY = global ? getGlobalY() : getPosY();
            final float rotation = getRotation();
            final float scale = getScale();

            final double centerX = posX + width / 2.0;
            final double centerY = posY + height / 2.0;

            if (rotation == 0f && scale == 1f) {
                return global ? (cacheRotationSizeGlobal = new AABB(posX, posY, width, height, true)) :
                        (cacheRotationSizeLocal = new AABB(posX, posY, width, height, false));
            }

            final double cosTheta = Math.cos(rotation);
            final double sinTheta = Math.sin(rotation);

            final double halfWidth = (width / 2.0) * scale;
            final double halfHeight = (height / 2.0) * scale;

            final double dx = Math.abs(halfWidth * cosTheta) + Math.abs(halfHeight * sinTheta);
            final double dy = Math.abs(halfWidth * sinTheta) + Math.abs(halfHeight * cosTheta);

            final int aabbX = (int) Math.floor(centerX - dx);
            final int aabbY = (int) Math.floor(centerY - dy);
            final int aabbWidth = (int) Math.ceil(2 * dx);
            final int aabbHeight = (int) Math.ceil(2 * dy);

            return global ? (cacheRotationSizeGlobal = new AABB(aabbX, aabbY, aabbWidth, aabbHeight, true)) :
                    (cacheRotationSizeLocal = new AABB(aabbX, aabbY, aabbWidth, aabbHeight, false));
        }


        return global ? cacheRotationSizeGlobal : cacheRotationSizeLocal;
    }

    public record AABB(int x, int y, int width, int height, boolean global) { }
}
