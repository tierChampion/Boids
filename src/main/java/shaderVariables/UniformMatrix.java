package shaderVariables;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class UniformMatrix extends Uniform {

    private static FloatBuffer currentValues = BufferUtils.createFloatBuffer(4 * 4);

    public UniformMatrix(String name) {
        super(name);
    }

    /**
     * Load matrix to uniform variable
     * @param matrix uniform value
     */
    public void loadMatrix(Matrix4f matrix) {
        matrix.get(currentValues);
        glUniformMatrix4fv(super.getLocation(), false, currentValues);
    }

}
