package shaderProgram;

import shaderVariables.Uniform;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderProgram {

    protected final int shaderProgramId;
    protected List<Uniform> uniforms = new ArrayList<>();

    public ShaderProgram() {
        // Create pointer to program
        this.shaderProgramId = glCreateProgram();
    }

    public void linkProgram(Shader... shaders) {
        // Attach each shaders to the program
        for (Shader shader : shaders) {
            glAttachShader(this.shaderProgramId, shader.getShaderID());
        }
        // Link the program
        glLinkProgram(this.shaderProgramId);
        // Check if the link was successful
        checkForSuccess();
        // Delete shaders
        for (Shader shader : shaders) {
            shader.delete();
        }
    }

    private void bindAttributes(String[] inVariables) {
        // Bind all the in variables for use
        for (int i = 0; i < inVariables.length; i++) {
            glBindAttribLocation(this.shaderProgramId, i, inVariables[i]);
        }
    }

    public void storeUniformLocations(Uniform... uniforms) {
        for (Uniform uniform : uniforms) {
            uniform.loadUniformToShader(this.shaderProgramId);
            this.uniforms.add(uniform);
        }
        glValidateProgram(this.shaderProgramId);
    }

    public Uniform getUniformVariable(String name) {
        for (Uniform uniform : this.uniforms) {
            if (uniform.getName().equalsIgnoreCase(name)) {
                return uniform;
            }
        }
        return null;
    }

    public void bind() {
        glUseProgram(this.shaderProgramId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void delete() {
        glDeleteProgram(this.shaderProgramId);
    }

    protected void checkForSuccess() {
        int success = glGetProgrami(this.shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(this.shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Linking of shaders failed.");
            System.out.println(glGetProgramInfoLog(this.shaderProgramId, length));
            assert false : "";
        }
    }

}
