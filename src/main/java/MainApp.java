import boids.BoidModels;
import org.joml.Vector2i;
import org.joml.Vector3f;
import scene.BoidScene;
import scene.Scene;
import window.Window;

public class MainApp {

    /*
    VISUAL PARAMETERS
     */
    private static final Vector2i WINDOW_DIMENSIONS = new Vector2i(1920, 1080);
    private static final String WINDOW_TITLE = "3D Boid simulation | ";
    private static final BoidModels BOID_MODEL = BoidModels.CONE;
    private static final String BOID_TEXTURE = "testTexture.png";
    private static final float BOID_SCALE = 0.2f;
    private static final float BOID_REFLECTIVITY = 0.0f;
    private static final float BOID_SHINE = 0.0f;
    private static final Vector3f LIGHT_POSITION = new Vector3f(30);

    /*
    SIMULATION PARAMETERS
     */
    private static final float FRAME_RATE = 60;
    private static final float TIME_STEP = 1 / FRAME_RATE;
    private static final int BOID_COUNT = 500;
    private static final float BOID_MIN_SPEED = 8f;
    private static final float BOID_MAX_SPEED = 2f;
    private static final float BOID_MAX_FORCE = 3f;
    private static final Vector3f WORLD_BOUNDS = new Vector3f(20);
    private static final float DISTANCE_TO_CENTER = 40f;

    /*
    BEHAVIOUR PARAMETERS
     */
    private static final float BOID_VIEW_RADIUS = 8f;
    private static final float BOID_AVOID_RADIUS = 0.3f;
    private static final float BOID_ALIGNMENT = 1;
    private static final float BOID_COHESION = 1;
    private static final float BOID_AVOIDANCE = 1;

    public static void main(String[] args) {
        Window window = Window.create(WINDOW_DIMENSIONS.x, WINDOW_DIMENSIONS.y, WINDOW_TITLE);

        Scene simulation = new BoidScene(DISTANCE_TO_CENTER, BOID_COUNT, WORLD_BOUNDS, BoidModels.fileName(BOID_MODEL),
                BOID_TEXTURE, BOID_SHINE, BOID_REFLECTIVITY, TIME_STEP, LIGHT_POSITION, BOID_SCALE,
                BOID_VIEW_RADIUS, BOID_AVOID_RADIUS, BOID_MAX_SPEED, BOID_MIN_SPEED, BOID_MAX_FORCE,
                BOID_ALIGNMENT, BOID_COHESION, BOID_AVOIDANCE);

        window.run(simulation);
    }
}
