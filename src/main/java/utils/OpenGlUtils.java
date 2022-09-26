package utils;

import static org.lwjgl.opengl.GL11.*;

/**
 * Utility class for certain global rendering parameters
 */
public class OpenGlUtils {

    ////////////////////////////
    // DEPTH TESTING SETTINGS //
    ////////////////////////////

    public static void activateZBuffering() {
        glEnable(GL_DEPTH_TEST);
    }
    public static void deactivateZBuffering() {
        glDisable(GL_DEPTH_TEST);
    }

    public static void clearZBuffer() {
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    public static void setDepthTestingCondition(int function) {
        glDepthFunc(function);
    }

    //////////////////////
    // BACKFACE CULLING //
    //////////////////////

    public static void activateBackFaceCulling() {
        glEnable(GL_CULL_FACE);
    }
    public static void deactivateBackFaceCulling() {
        glDisable(GL_CULL_FACE);
    }

}
