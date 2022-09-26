package shaderProgram;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

public class PipelineShaderProgram extends ShaderProgram {

    public PipelineShaderProgram() {
        super();
    }

    public void prepareProgram(String[] inVariables, Shader... shaders) {
        super.linkProgram(shaders);
        bindAttributes(inVariables);
    }

    private void bindAttributes(String[] inVariables) {
        // Bind all the in variables for use
        for (int i = 0; i < inVariables.length; i++) {
            glBindAttribLocation(this.shaderProgramId, i, inVariables[i]);
        }
    }
}
