package utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

    /**
     * Create a model matrix.
     * @param position Position in space of the model
     * @param rotationX
     * @param rotationY
     * @param scale
     * @return
     */
    public static Matrix4f convertToModelMatrix(Vector3f position,
                                         float rotationX, float rotationY, float scale) {
        // Create 4x4 identity matrix
        Matrix4f modelMatrix = new Matrix4f();
        // Position of model
        modelMatrix.setTranslation(position);
        // Rotation of model
        Vector3f xAxis = new Vector3f(1, 0, 0).rotateAxis(rotationY, 0, 1, 0);
        modelMatrix.rotate(rotationX, xAxis.x, xAxis.y, xAxis.z);
        modelMatrix.rotate(rotationY, 0, 1, 0);
        // Scale of model
        modelMatrix.scale(scale);
        return modelMatrix;
    }

    /**
     * Calculate a view matrix that looks at the center.
     * @param position Position of the camera
     * @param pitch Rotation left-to-right
     * @param yaw Rotation up-to-down
     * @return view matrix
     */
    public static Matrix4f convertToCenterViewMatrix(Vector3f position, float pitch, float yaw) {

        // This view matrix looks at the origin and rotates and moves around it
        Matrix4f viewMatrix = new Matrix4f();

        viewMatrix.setRotationXYZ(pitch, yaw, 0);
        // Translate negative of camera position
        viewMatrix.setTranslation(new Vector3f(-position.x, -position.y, -position.z));
        return viewMatrix;
    }

    /**
     * Calculate a view matrix
     * @param position Position of the camera
     * @param pitch Rotation left-to-right
     * @param yaw Rotation up-to-down
     * @return view matrix
     */
    public static Matrix4f convertToViewMatrix(Vector3f position, float pitch, float yaw) {
        // This view matrix is similar to what your eyes would see
        Matrix4f viewMatrix = convertToModelMatrix(position, pitch, yaw, 1);

        return viewMatrix.invert();
    }

    /**
     * Calculate an orthographic projection matrix.
     * Orthographic projection conserves angles but lacks depth
     * @param width X dimension of the raster
     * @param near distance to near plane
     * @param far distance to far plane
     * @return projection matrix
     */
    public static Matrix4f convertToOrthographicProjectionMatrix(float width,
                                                                 float near, float far) {

        Matrix4f orthographicProjectionMatrix = new Matrix4f();

        float aspectRatio = (float) (1920) / (float) (1080);

        float ySize = width / aspectRatio;

        orthographicProjectionMatrix.m00(2 / (width + width));
        orthographicProjectionMatrix.m11(2 / (ySize + ySize));
        orthographicProjectionMatrix.m22(-2 / (far - near));
        orthographicProjectionMatrix.m30(-(0) / (width + width));
        orthographicProjectionMatrix.m31(-(0) / (ySize + ySize));
        orthographicProjectionMatrix.m32(-(far + near) / (far - near));

        return orthographicProjectionMatrix;
    }

    /**
     * Calculate a perspective projection matrix.
     * Perspective projection conserves depth but distorts angles
     * @param fieldOfView Angle for width of view
     * @param far distance to far plane
     * @param near distance to near plane
     * @return projection matrix
     */
    public static Matrix4f convertToPerspectiveProjectionMatrix(float fieldOfView, float far, float near) {

        Matrix4f perspectiveProjectionMatrix = new Matrix4f();

        float aspectRatio = (float) (1920) / (float) (1080);
        float yScale = (float) ((1f / Math.tan(Math.toRadians(fieldOfView / 2f))));
        float xScale = yScale / aspectRatio;
        float frustumLength = far - near;

        perspectiveProjectionMatrix.m00(xScale);
        perspectiveProjectionMatrix.m11(yScale);
        perspectiveProjectionMatrix.m22(-((far + near) / frustumLength));
        perspectiveProjectionMatrix.m23(-1);
        perspectiveProjectionMatrix.m32(-((2 * near * far) / frustumLength));
        perspectiveProjectionMatrix.m33(0);

        return perspectiveProjectionMatrix;
    }
}
