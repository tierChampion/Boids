package shaderProgram;

import static org.lwjgl.opengl.GL42.GL_SHADER_IMAGE_ACCESS_BARRIER_BIT;
import static org.lwjgl.opengl.GL42.glMemoryBarrier;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BARRIER_BIT;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

/**
 *
 */
public class ComputeShaderProgram extends ShaderProgram {

    public ComputeShaderProgram() {

    }

    public void use(int x, int y, int z) {
        glDispatchCompute(x, y, z);
    }

    public void checkImageMemoryBarrier() {
        glMemoryBarrier(GL_SHADER_IMAGE_ACCESS_BARRIER_BIT);
    }

    public void checkSsboMemoryBarrier() {glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT);}

    public int getId() {
        return super.shaderProgramId;
    }
}
