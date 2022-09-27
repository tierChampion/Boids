package dataStructures.opengl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Vertex array object. Contains both the vertex data and the vertex indices
 */
public class Vao {

    private int vaoID;
    private Vbo vbo;
    private Ebo ebo;

    public Vao() {
        this.vaoID = glGenVertexArrays();
        bind();
    }

    /**
     * Set the data of the Vao.
     * @param vertexData Raw vertex data
     * @param indices Vertex indices
     */
    public void storeData(float[] vertexData, int[] indices) {
        this.vbo = new Vbo(vertexData);
        this.ebo = new Ebo(indices);
    }

    /**
     * Define the memory layout of the buffers
     */
    public void generateVertexPointers() {
        int positionSize = 3;
        int textureSize = 2;
        int normalSize = 3;
        int bytePerFloat = 4;

        int bytePerVertex = (positionSize + textureSize + normalSize) * bytePerFloat;
        int textureByteOffset = positionSize * bytePerFloat;
        int normalByteOffset = (positionSize + textureSize) * bytePerFloat;

        // Position attribute pointer
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, bytePerVertex, 0);
        // Color attribute pointer
        glVertexAttribPointer(1, textureSize, GL_FLOAT, false, bytePerVertex, textureByteOffset);
        // Normal attribute pointer
        glVertexAttribPointer(2, normalSize, GL_FLOAT, false, bytePerVertex, normalByteOffset);
        // Enable them
        enableVertexAttributes(0, 1, 2);
    }

    ////////////////////////////
    // ENABLE AND DISABLE VAO //
    ////////////////////////////

    public void enableVertexAttributes(int... indexes) {
        for (int index : indexes) {
            glEnableVertexAttribArray(index);
        }
    }
    public void disableVertexAttributes(int... indexes) {
        for (int index : indexes) {
            glDisableVertexAttribArray(index);
        }
    }
    public void bind() {
        glBindVertexArray(this.vaoID);
    }
    public void unbind() {
        glBindVertexArray(0);
    }

    /**
     * Get vertex count in the vao
     * @return size of ebo
     */
    public int getDrawCount() {
        return this.ebo.getLength();
    }

}
