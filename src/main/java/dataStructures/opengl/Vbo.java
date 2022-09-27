package dataStructures.opengl;

import static org.lwjgl.opengl.GL15.*;

/**
 * Verte buffer object. Stores the data of the vertices of a mesh
 */
public class Vbo extends ShaderBufferObject {

    public Vbo(float[] vertexArray) {
        super(vertexArray);
        glBindBuffer(GL_ARRAY_BUFFER, this.id);
        glBufferData(GL_ARRAY_BUFFER, this.buffer, GL_STATIC_DRAW);
    }
}
