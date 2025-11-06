package dev.behindthescenery.core.system.user_interface.render.ui_component.transform;

public interface SimpleTransform<T> {

    void setPosX(int x);

    void setPosY(int y);

    void setWidth(int w);

    void setHeight(int h);

    void setScale(float scale);

    void setRotation(float z);

    int getPosX();

    int getPosY();

    int getWidth();

    int getHeight();

    float getScale();

    float getRotation();

    T getObject();
}
