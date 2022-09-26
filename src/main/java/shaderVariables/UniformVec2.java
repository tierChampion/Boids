package shaderVariables;

import static org.lwjgl.opengl.GL20.glUniform2f;

public class UniformVec2 extends Uniform {

    private float currentX;
    private float currentY;
    private boolean used = false;

    public UniformVec2(String name) {
        super(name);
    }

    public void loadVec2(float x, float y, float z) {
        if (!this.used || currentX != x || currentY != y) {
            glUniform2f(super.getLocation(), x, y);
            used = true;
            currentX = x;
            currentY = y;
        }
    }
}
