package camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import utils.Maths;

/**
 * Camera for the scene. Handles both orthographic and perspective projection.
 */
public class Camera {

    public static final boolean ORTHOGRAPHIC_PROJECTION = false;
    public static final boolean PERSPECTIVE_PROJECTION = true;

    private Vector3f position = new Vector3f(10, 10, 40);
    // Angle up and down
    private float pitch = 0;
    // Angle side to side
    private float yaw = 0;
    // Angle of field of view
    private float fov = 60f;
    // Distance to far plane
    private float far = 1000f;
    // Distance to near plane
    private float near = 0.1f;
    // Type of projection
    private boolean projection;
    // If projection matrix need to be changed
    private boolean projectionChanged = true;

    private static Camera instance;

    private Camera(boolean projection) {
        this.projection = projection;
    }

    /**
     * Get camera and create it if needed. There can only be one camera.
     * @return camera
     */
    public static Camera getCamera() {
        if (Camera.instance == null) {
            Camera.instance = new Camera(true);
        }

        return instance;
    }

    /**
     * Get the invert model matrix of the camera, also called the view matrix
     * @return view matrix
     */
    public static Matrix4f getViewMatrix() {
        return Maths.convertToViewMatrix(getCamera().position, getCamera().pitch, getCamera().yaw);
    }

    /**
     * Get the orientation of the camera
     * @return model matrix of the camera with no translation
     */
    public static Matrix4f getOrientationMatrix() {
        return Maths.convertToModelMatrix(new Vector3f(0), getCamera().pitch, getCamera().yaw,1);
    }

    /**
     * Get the matrix responsible for the projection of the scene to the screen raster
     * @return projection matrix
     */
    public static Matrix4f getProjectionMatrix() {
        Matrix4f projectionMatrix;
        if (getCamera().projection == Camera.ORTHOGRAPHIC_PROJECTION) {
            projectionMatrix =
                    Maths.convertToOrthographicProjectionMatrix(
                            getCamera().fov / 3, getCamera().near, getCamera().far);
        } else if (getCamera().projection == Camera.PERSPECTIVE_PROJECTION) {
            projectionMatrix =
                    Maths.convertToPerspectiveProjectionMatrix(getCamera().fov, getCamera().far, getCamera().near);
        } else {
            System.err.println("Projection mode of Camera not set properly! ");
            System.exit(-1);
            return null;
        }
        getCamera().projectionChanged = false;
        return projectionMatrix;
    }

    public static boolean getProjection() {
        return getCamera().projection;
    }
    public static Vector3f getPosition() {
        return getCamera().position;
    }

    public static void move(float dx, float dy, float dz) {
        getCamera().position.add(dx, dy, dz);
    }

    /**
     * Move the camera in the forward direction
     * @param distance
     */
    public static void move(float distance) {
        Vector3f headingDirection = getOrientationMatrix().transformDirection(new Vector3f(0, 0, 1));
        getCamera().position.add(headingDirection.normalize(distance));
    }

    //////////////////////////////
    // CHANGE CAMERA PARAMETERS //
    //////////////////////////////

    public static void changeFov(float dFov) {
        getCamera().fov += dFov;
        getCamera().projectionChanged = true;
    }
    public static void changeFar(float dFar) {
        getCamera().far += dFar;
        getCamera().projectionChanged = true;
    }
    public static void changeNear(float dNear) {
        getCamera().near += dNear;
        getCamera().projectionChanged = true;
    }
    public static void changePitch(float dPitch) {
        getCamera().pitch += dPitch;
    }
    public static void changeYaw(float dYaw) {
        getCamera().yaw += dYaw;
    }
    public static void changeProjection() {
        getCamera().projection = !getCamera().projection;
        getCamera().projectionChanged = true;
    }

    public static boolean hasProjectionChanged() {
        return getCamera().projectionChanged;
    }
}
