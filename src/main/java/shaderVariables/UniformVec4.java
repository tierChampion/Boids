package shaderVariables;

import static org.lwjgl.opengl.GL20.glUniform4f;

public class UniformVec4 extends Uniform {

    private float currentX;
    private float currentY;
    private float currentZ;
    private float currentW;
    private boolean used = false;

    public UniformVec4(String name) {
        super(name);
    }

    /**
     * Load Vector4f to uniform variable
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public void loadVec4(float x, float y, float z, float w) {
        if (!this.used || currentX != x || currentY != y || currentZ != z || currentW != w) {
            glUniform4f(super.getLocation(), x, y, z, w);
            used = true;
            currentX = x;
            currentY = y;
            currentZ = z;
            currentW = w;
        }
    }
}
