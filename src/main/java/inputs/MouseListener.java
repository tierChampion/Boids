package inputs;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Manager for the mouse events
 */
public class MouseListener {

    private static final int BUTTON_COUNT = 3;

    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[BUTTON_COUNT];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.lastX = 0;
        this.lastY = 0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return instance;
    }

    /**
     * Callback for the GLFW event of moving the mouse
     * @param window pointer to GLFW window
     * @param xPos new lateral position
     * @param yPos new vertical position
     */
    public static void cursorPositionCallback(long window, double xPos, double yPos) {

        get().lastX = get().xPos;
        instance.lastY = instance.yPos;
        instance.xPos = xPos;
        instance.yPos = yPos;
        // If any button is pressed while the position is changing, it is dragging
        instance.isDragging = false;
        for (boolean button : instance.mouseButtonPressed) {
            instance.isDragging = button || instance.isDragging;
        }
    }

    /**
     * Callback for the GLFW event of pressing a mouse button
     * @param window pointer to GLFW window
     * @param button button affected by the event
     * @param action action applied to the button
     * @param mods modifications
     */
    public static void buttonPressedCallback(long window, int button, int action, int mods) {
        // Valid button
        if (button < BUTTON_COUNT) {
            if (action == GLFW_PRESS) {
                get().mouseButtonPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseButtonPressed[button] = false;
                instance.isDragging = false;
            }
        }
    }

    /**
     * Callback for the GLFW event of scrolling the mouse wheel
     * @param window pointer to GLFW window
     * @param xOffset lateral scrolling
     * @param yOffset vertical scrolling
     */
    public static void scrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        instance.scrollY = yOffset;
    }

    /**
     * Prepare the mouse for the next frame
     */
    public static void endFrame() {
        get().scrollX = 0;
        instance.scrollY = 0;
        instance.lastX = get().xPos;
        instance.lastY = get().yPos;
    }

    /////////////
    // GETTERS //
    /////////////

    public static float getX() {
        return (float) get().xPos;
    }
    public static float getY() {
        return (float) get().yPos;
    }
    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }
    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }
    public static float getScrollX() {
        return (float) get().scrollX;
    }
    public static float getScrollY() {
        return (float) get().scrollY;
    }

    /////////////
    // SETTERS //
    /////////////

    public static boolean isDragging() {
        return get().isDragging;
    }
    public static boolean isMouseButtonDown(int button) {
        if (button < BUTTON_COUNT) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }
}
