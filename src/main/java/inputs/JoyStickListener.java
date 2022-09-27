package inputs;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Manager for joystick events
 */
public class JoyStickListener {

    private static final int JOYSTICK_ID = GLFW_JOYSTICK_1;

    private static JoyStickListener instance;

    private String name;
    private boolean connected;

    private JoyStickListener() {
        // Try this to connect to the right controller
        this.name = glfwGetGamepadName(JOYSTICK_ID);
        this.connected = (this.name != null);
    }

    public static JoyStickListener get() {
        if (JoyStickListener.instance == null) {
            JoyStickListener.instance = new JoyStickListener();
        }

        return instance;
    }

    /**
     * Callback responsible for the connection and disconnection of a joystick
     * @param joystickID id of the connected joystick
     * @param event type of the event
     */
    public static void joystickConnectedCallback(int joystickID, int event) {
        if (event == GLFW_CONNECTED) {
            get().connected = true;
        } else if (event == GLFW_DISCONNECTED) {
            get().connected = false;
        }
    }

    /**
     * Get the state of the desired button
     * @param button button of choice
     * @return whether it is pressed or not
     */
    public static boolean getButtonPress(int button) {
        if (get().connected) {
            return glfwGetJoystickButtons(JOYSTICK_ID).get(button) == GLFW_PRESS;
        } else {
            return false;
        }
    }

    /**
     * Get the value of the desired joystick axis
     * @param axis axis of choice
     * @return value of the axis [-1, 1]
     */
    public static float getAxis(int axis) {
        if (get().connected) {
            return glfwGetJoystickAxes(JOYSTICK_ID).get(axis);
        } else {
            return 0;
        }
    }
}
