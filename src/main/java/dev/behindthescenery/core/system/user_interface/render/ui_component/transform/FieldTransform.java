package dev.behindthescenery.core.system.user_interface.render.ui_component.transform;

public class FieldTransform implements SimpleTransform<Object> {

    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected float scale;
    protected float rotation;

    public FieldTransform() {
        this(0, 0, 0, 0, 1, 0);
    }

    public FieldTransform(int posX, int posY, int width, int height, float scale, float rotation) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.rotation = rotation;
    }

    @Override
    public void setPosX(int x) {
        posX = x;
    }

    @Override
    public void setPosY(int y) {
        posY = y;
    }

    @Override
    public void setWidth(int w) {
        width = w;
    }

    @Override
    public void setHeight(int h) {
        height = h;
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public void setRotation(float z) {
        rotation = z;
    }

    @Override
    public int getPosX() {
        return posX;
    }

    @Override
    public int getPosY() {
        return posY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public Object getObject() {
        return new Object();
    }
}
