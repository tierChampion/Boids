import boids.BoidModels;
import org.joml.Vector2i;
import org.joml.Vector3f;
import scene.BoidScene;
import scene.Scene;
import window.Window;

public class MainApp {

    /**
     * TODO
     *
     * Fix initial camera orientation so it faces the middle of the boids bounds.
     * Maybe make the light position equal to the cameras (this would make everything always visible and
     * keep the point light distance property)
     *
     * Make two camera modes:
     * A flying one with full control
     * And a rotating one where the camera is always looking at the origin.
     */

    /*
    VISUAL PARAMETERS
     */
    private static final Vector2i WINDOW_DIMENSIONS = new Vector2i(1920, 1080);
    private static final String WINDOW_TITLE = "3D Boid simulation | ";
    private static final Vector3f CAMERA_POSITION = new Vector3f(10, 10, 40);
    private static final BoidModels BOID_MODEL = BoidModels.CONE;
    private static final String BOID_TEXTURE = "testTexture.png";
    private static final float BOID_SCALE = 0.2f;
    private static final float BOID_REFLECTIVITY = 0.0f;
    private static final float BOID_SHINE = 0.0f;
    private static final Vector3f LIGHT_DIRECTION = new Vector3f(1);

    /*
    SIMULATION PARAMETERS
     */
    private static final float FRAME_RATE = 60;
    private static final float TIME_STEP = 1 / FRAME_RATE;
    private static final int BOID_COUNT = 2000;
    private static final float BOID_MIN_SPEED = 8f;
    private static final float BOID_MAX_SPEED = 2f;
    private static final float BOID_MAX_FORCE = 3f;
    private static final Vector3f WORLD_BOUNDS = new Vector3f(20);

    /*
    BEHAVIOUR PARAMETERS
     */
    private static final float BOID_VIEW_RADIUS = 8f;
    private static final float BOID_AVOID_RADIUS = 0.3f;
    private static final float BOID_ALIGNMENT = 0.5f;
    private static final float BOID_COHESION = 3;
    private static final float BOID_AVOIDANCE = 4;

    public static void main(String[] args) {
        Window window = Window.create(WINDOW_DIMENSIONS.x, WINDOW_DIMENSIONS.y, WINDOW_TITLE);

        Scene simulation = new BoidScene(BOID_COUNT, WORLD_BOUNDS, BoidModels.fileName(BOID_MODEL),
                BOID_TEXTURE, BOID_SHINE, BOID_REFLECTIVITY, TIME_STEP, LIGHT_DIRECTION, BOID_SCALE,
                BOID_VIEW_RADIUS, BOID_AVOID_RADIUS, BOID_MAX_SPEED, BOID_MIN_SPEED, BOID_MAX_FORCE,
                BOID_ALIGNMENT, BOID_COHESION, BOID_AVOIDANCE);

        window.run(simulation);
    }
}
