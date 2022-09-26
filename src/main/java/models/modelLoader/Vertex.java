package models.modelLoader;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * 3D vertex
 */
public class Vertex {

    private final Vector3f pos;
    private final Vector2f textureCoordinates;
    private final Vector3f normal;

    /**
     * 3D vertex
     * @param pos Location in 3D space
     * @param textureCoordinates Location on a 2D plane
     * @param normal 3D vertex normal
     */
    public Vertex(Vector3f pos, Vector2f textureCoordinates, Vector3f normal) {
        this.pos = pos;
        this.textureCoordinates = textureCoordinates;
        this.normal = normal;
    }

    public Vector3f getPos() {
        return pos;
    }
    public Vector2f getTextureCoordinates() {
        return textureCoordinates;
    }
    public Vector3f getNormal() {
        return normal;
    }
}
