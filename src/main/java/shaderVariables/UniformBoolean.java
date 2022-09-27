package shaderVariables;

import static org.lwjgl.opengl.GL20.glUniform1f;

public class UniformBoolean extends Uniform {

    private boolean currentValue;
    private boolean used = false;

    public UniformBoolean(String name) {
        super(name);
    }

    /**
     * Load boolean value to uniform variable
     * @param value boolean value
     */
    public void loadBoolean(boolean value) {
        if (!used || currentValue != value) {
            glUniform1f(super.getLocation(), value ? 1.0f : 0.0f);
            this.currentValue = value;
            used = true;
        }
    }

}
