package shaderProgram;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

/**
 * A shader program with inputs.
 */
public class PipelineShaderProgram extends ShaderProgram {

    public PipelineShaderProgram() {
        super();
    }

    public void prepareProgram(String[] inVariables, Shader... shaders) {
        super.linkProgram(shaders);
        bindAttributes(inVariables);
    }

    /**
     * Prepare inputs
     * @param inVariables inputs
     */
    private void bindAttributes(String[] inVariables) {
        for (int i = 0; i < inVariables.length; i++) {
            glBindAttribLocation(this.shaderProgramId, i, inVariables[i]);
        }
    }
}
