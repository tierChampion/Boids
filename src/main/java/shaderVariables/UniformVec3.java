package shaderVariables;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL20.glUniform3f;

public class UniformVec3 extends Uniform {

    private float currentX;
    private float currentY;
    private float currentZ;
    private boolean used = false;

    public UniformVec3(String name) {
        super(name);
    }

    public void loadVec3(float x, float y, float z) {
        if (!this.used || currentX != x || currentY != y || currentZ != z) {
            glUniform3f(super.getLocation(), x, y, z);
            used = true;
            currentX = x;
            currentY = y;
            currentZ = z;
        }
    }

    public void loadVec3(Vector3f vector) {
        loadVec3(vector.x, vector.y, vector.z);
    }

}
