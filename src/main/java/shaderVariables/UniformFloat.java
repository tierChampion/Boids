package shaderVariables;

import static org.lwjgl.opengl.GL20.glUniform1f;

public class UniformFloat extends Uniform {

    private float currentValue;
    private boolean used = false;

    public UniformFloat(String name) {
        super(name);
    }

    public void loadFloat(float value) {
        if (!used || currentValue != value) {
            glUniform1f(super.getLocation(), value);
            this.currentValue = value;
            used = true;
        }
    }
}
