package dataStructures.opengl;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glGenBuffers;

public abstract class ShaderBufferObject {

    public int id; // to make proteccted
    protected FloatBuffer buffer;

    public ShaderBufferObject(float[] data) {
        this.id = glGenBuffers();

        this.buffer = BufferUtils.createFloatBuffer(data.length);
        this.buffer.put(data).flip();
    }
}
