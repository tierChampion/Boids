package inputs;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private final static int NUMBER_OF_KEYS = 350;

    private static KeyListener instance;
    private boolean[] keysPressed = new boolean[NUMBER_OF_KEYS];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return instance;
    }

    public static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keysPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keysPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keysPressed[keyCode];
    }
}
