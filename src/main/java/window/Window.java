package window;

import inputs.JoyStickListener;
import inputs.KeyListener;
import inputs.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import scene.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * GLFW window
 */
public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;

    private static Scene currentScene;

    private static Window window = null;

    private Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        init();
    }

    /**
     * Create a GLFW window.
     * @param width X dimension of the window
     * @param height Y dimension of the window
     * @param title Title of the window
     * @return Window
     */
    public static Window create(int width, int height, String title) {
        if (Window.window == null) {
            Window.window = new Window(width, height, title);
        }

        return Window.window;
    }

    /**
     * Launches Opengl with the desired scene
     * @param scene Scene to simulate
     */
    public void run(Scene scene) {
        System.out.println("LWJGL version : "  + Version.getVersion());

        currentScene = scene;
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        // Terminate GLFW and free error callback
        glfwTerminate();
        glfwSetErrorCallback(null);
    }

    private void init() {
        // Setup an error callback for asynchronicity
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW. ");
        }

        // Set default settings (hints) to GLFW window
        glfwDefaultWindowHints();
        // Configure settings
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create window
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        /////////////////////
        // INPUT CALLBACKS //
        /////////////////////

        // Mouse
        glfwSetCursorPosCallback(glfwWindow, MouseListener::cursorPositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::buttonPressedCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::scrollCallback);
        // Keyboard
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        // Joystick
        glfwSetJoystickCallback(JoyStickListener::joystickConnectedCallback);

        // Set the OpenGL context to the current thread
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync (buffer swapping)
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(glfwWindow);

        // Enables OpenGL to use the current GLFW window
        GL.createCapabilities();
    }

    /**
     * Main Opengl rendering loop
     */
    private void loop() {

        double currentTime = glfwGetTime();
        int fps = 0;

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            if (glfwGetTime() - currentTime >= 1) {
                currentTime = glfwGetTime();
                glfwSetWindowTitle(glfwWindow, this.title + " fps: " + fps);
                fps = 0;
            }

            // Clear the color buffer bit to the screen
            glClear(GL_COLOR_BUFFER_BIT);
            // Render Scene
            currentScene.renderScene();

            // Swap screen buffers
            glfwSwapBuffers(glfwWindow);

            fps++;
        }
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
}
