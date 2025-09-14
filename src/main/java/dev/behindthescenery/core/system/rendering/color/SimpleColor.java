package dev.behindthescenery.core.system.rendering.color;

public interface SimpleColor {

    SimpleColor DEFAULT = RGBA.DEFAULT;

    int getRedI();
    int getGreenI();
    int getBlueI();
    int getAlphaI();
    
    default float getRedF() {
        return (float) getRedI() / 255;
    }

    default float getGreenF() {
        return (float) getGreenI() / 255;
    }

    default float getBlueF() {
        return (float) getBlueI() / 255;
    }

    default float getAlphaF() {
        return (float) getAlphaI() / 255;
    }

    default int argbI() {
        return (getAlphaI() << 24) | (getRedI() << 16) | (getGreenI() << 8) | getBlueI();
    }

    default int[] getColorsI() {
        return new int[] { getRedI(), getGreenI(), getBlueI(), getAlphaI() };
    }

    default float[] getColorsF() {
        return new float[] { getRedF(), getGreenF(), getBlueF(), getAlphaF() };
    }
}
