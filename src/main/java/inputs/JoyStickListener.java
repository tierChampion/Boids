package inputs;

import static org.lwjgl.glfw.GLFW.*;

public class JoyStickListener {

    private static final int JOYSTICK_ID = GLFW_JOYSTICK_1;

    private static JoyStickListener instance;

    private String name;
    private boolean connected;

    private JoyStickListener() {
        // Try this to connect to the right controller, if there is one
        this.name = glfwGetGamepadName(JOYSTICK_ID);
        this.connected = (this.name != null);
    }

    public static JoyStickListener get() {
        if (JoyStickListener.instance == null) {
            JoyStickListener.instance = new JoyStickListener();
        }

        return instance;
    }

    public static void joystickConnectedCallback(int joystickID, int event) {
        if (event == GLFW_CONNECTED) {
            get().connected = true;
        } else if (event == GLFW_DISCONNECTED) {
            get().connected = false;
        }
    }

    public static boolean getButtonPress(int button) {
        if (get().connected) {
            return glfwGetJoystickButtons(JOYSTICK_ID).get(button) == GLFW_PRESS;
        } else {
            return false;
        }
    }

    public static float getAxis(int axis) {
        if (get().connected) {
            return glfwGetJoystickAxes(JOYSTICK_ID).get(axis);
        } else {
            return 0;
        }
    }

}
