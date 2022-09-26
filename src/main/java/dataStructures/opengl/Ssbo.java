package dataStructures.opengl;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL43.*;

public class Ssbo extends ShaderBufferObject {

    private int binding;

    public Ssbo(float[] data, int binding) {

        super(data);

        this.binding = binding;

        glBindBuffer(GL_SHADER_STORAGE_BUFFER, this.id);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, this.binding, this.id);
        glBufferData(GL_SHADER_STORAGE_BUFFER, this.buffer, GL_DYNAMIC_DRAW);

    }

    public void bindToProgram(int shaderProgramId, String blockName) {
        int blockIndex = glGetProgramResourceIndex(shaderProgramId, GL_SHADER_STORAGE_BLOCK, blockName);
        glShaderStorageBlockBinding(shaderProgramId, blockIndex, this.binding);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, this.binding, this.id);
        // MIGHT NOT WORK
        glMapBufferRange(GL_SHADER_STORAGE_BUFFER, 0, this.buffer.capacity(), GL_MAP_READ_BIT);
    }

    public float[] getBufferInArray() {
        float[] data = new float[this.buffer.limit()];
        this.buffer.get(data);
        return data;
    }
}
