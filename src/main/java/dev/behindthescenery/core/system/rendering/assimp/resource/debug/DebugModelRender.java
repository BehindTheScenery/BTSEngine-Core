package dev.behindthescenery.core.system.rendering.assimp.resource.debug;

import org.joml.Vector3f;

public class DebugModelRender {

    public static boolean showBoundBox;
    public static boolean showOccluded;

    public static boolean renderOccluded;

    public static boolean useOcclusion;

    public static boolean disableRender;

    public static Vector3f position = new Vector3f();
    public static Vector3f scale = new Vector3f(1,1,1);
    public static Vector3f rotation = new Vector3f();

    public static float[] colorBoundBox = new float[]{1, 1, 1, 1};
    public static float[] colorOccluded = new float[]{1, 1, 1, 1};

    public static boolean isShowBoundBox() {
        return showBoundBox;
    }

    public static boolean isShowOccluded() {
        return showOccluded;
    }

    public static Vector3f getColorOccluded() {
        return get(colorOccluded);
    }

    public static Vector3f getColorBoundBox() {
        return get(colorBoundBox);
    }

    private static Vector3f get(float[] floats) {
        return new Vector3f(floats[0], floats[1], floats[2]);
    }
}
