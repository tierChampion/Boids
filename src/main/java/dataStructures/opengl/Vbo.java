package dataStructures.opengl;

import static org.lwjgl.opengl.GL15.*;

/*
    VERTEX_BUFFER_OBJECT
 */
public class Vbo extends ShaderBufferObject {

    public Vbo(float[] vertexArray) {
        super(vertexArray);
        // Assign vertex buffer to Vbo
        glBindBuffer(GL_ARRAY_BUFFER, this.id);
        glBufferData(GL_ARRAY_BUFFER, this.buffer, GL_STATIC_DRAW);
    }
}
