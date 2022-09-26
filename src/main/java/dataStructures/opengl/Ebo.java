package dataStructures.opengl;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

/*
    ELEMENTS_BUFFER_OBJECT
 */
public class Ebo {

    private int eboID;
    private IntBuffer elementBuffer;

    public Ebo(int[] elementArray) {

        // Create Buffer of the right size
        this.elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        // Place the data inside the buffer and flip it so it can be used with OpenGL
        this.elementBuffer.put(elementArray).flip();
        // Create pointer to buffer
        this.eboID = glGenBuffers();
        // Assign element buffer to Ebo
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.elementBuffer, GL_STATIC_DRAW);
    }

    public int getLength() {
        return elementBuffer.capacity();
    }
}
