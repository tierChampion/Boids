package shaderVariables;

import static org.lwjgl.opengl.GL20.glUniform1i;

public class UniformSampler extends Uniform {

    private int currentValue;
    private boolean used = false;

    public UniformSampler(String name) {
        super(name);
    }

    /**
     * Load uniform texture location
     * @param textureUnit sampler2D unit to load
     */
    public void loadTextureUnit(int textureUnit) {
        if (!this.used || currentValue != textureUnit) {
            glUniform1i(super.getLocation(), textureUnit);
            used = true;
            currentValue = textureUnit;
        }
    }

}
