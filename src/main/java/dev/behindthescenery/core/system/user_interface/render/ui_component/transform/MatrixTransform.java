package dev.behindthescenery.core.system.user_interface.render.ui_component.transform;

import org.joml.Matrix3f;

public class MatrixTransform implements SimpleTransform<Matrix3f> {

    protected Matrix3f matrix3f;
    protected float scale = 1;

    public MatrixTransform() {
        this(new Matrix3f());
    }

    public MatrixTransform(Matrix3f matrix3f) {
        this.matrix3f = matrix3f;
    }

    public static MatrixTransform withReset() {
        return new MatrixTransform().reset();
    }

    public static MatrixTransform withReset(Matrix3f matrix3f) {
        return new MatrixTransform(matrix3f).reset();
    }

    public MatrixTransform reset() {
        matrix3f.identity();
        return this;
    }

    @Override
    public void setPosX(int x) {
        matrix3f.m20(x);
    }

    @Override
    public void setPosY(int y) {
        matrix3f.m21(y);
    }

    @Override
    public void setWidth(int w) {
        matrix3f.m00(w);
    }

    @Override
    public void setHeight(int h) {
        matrix3f.m11(h);
    }

    @Override
    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public void setRotation(float degrees) {
        float radians = (float) Math.toRadians(degrees);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        matrix3f.m00(cos);
        matrix3f.m01(-sin);
        matrix3f.m10(sin);
        matrix3f.m11(cos);
    }

    @Override
    public int getPosX() {
        return (int) matrix3f.m20();
    }

    @Override
    public int getPosY() {
        return (int) matrix3f.m21();
    }

    @Override
    public int getWidth() {
        return (int) matrix3f.m00();
    }

    @Override
    public int getHeight() {
        return (int) matrix3f.m11();
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public float getRotation() {
        return (float) Math.toDegrees(Math.atan2(matrix3f.m10(), matrix3f.m00()));
    }

    @Override
    public Matrix3f getObject() {
        return matrix3f;
    }
}
