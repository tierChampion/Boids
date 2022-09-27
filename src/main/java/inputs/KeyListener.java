package inputs;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Manager for the keyboard events
 */
public class KeyListener {

    private final static int NUMBER_OF_KEYS = 350;

    private static KeyListener instance;
    private boolean[] keysPressed = new boolean[NUMBER_OF_KEYS];

    private KeyListener() {}

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return instance;
    }

    /**
     * Callback for the GLFW typing event.
     * @param window pointer to the GLFW window
     * @param key key of the event
     * @param scanCode scancode of the key
     * @param action nature of the event
     * @param mods modifications
     */
    public static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keysPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keysPressed[key] = false;
        }
    }

    /**
     * Get state of the desired key
     * @param keyCode code of the key to check
     * @return whether it is pressed or not
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().keysPressed[keyCode];
    }
}
