package dev.behindthescenery.core.system.rendering.shader;


/**
 * ID масок для шейдеров <br><br>
 *
 * modelViewMat - {@link ShaderProgramBits#MODEL_VIEW_MATRIX} <br>
 * projectionMat - {@link ShaderProgramBits#PROJECTION_VIEW_MATRIX} <br>
 * color - {@link ShaderProgramBits#COLOR} <br>
 * screenSize - {@link ShaderProgramBits#SCREEN_SIZE} <br>
 * gameTime - {@link ShaderProgramBits#GAME_TIME_VANILLA} | {@link ShaderProgramBits#GAME_TIME_BTS} <br>
 * textureMat - {@link ShaderProgramBits#TEXTURE_MATRIX} <br>
 * viewPos - {@link ShaderProgramBits#CAMERA_POSITION} </br>
 */
public interface ShaderProgramBits {

    int MODEL_VIEW_MATRIX = 1 << 1;
    int PROJECTION_VIEW_MATRIX = 1 << 2;
    int COLOR = 1 << 3;
    int SCREEN_SIZE = 1 << 4;
    int GAME_TIME_VANILLA = 1 << 5;
    int GAME_TIME_BTS = 1 << 6;
    int TEXTURE_MATRIX = 1 << 7;
    int CAMERA_POSITION = 1 << 8;

    int DEFAULT_MATRIX = MODEL_VIEW_MATRIX | PROJECTION_VIEW_MATRIX;
}
