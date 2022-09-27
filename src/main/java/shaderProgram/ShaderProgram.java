package shaderProgram;

import shaderVariables.Uniform;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Generic shader program. A shader program is a program run on the gpu that contains one or more shaders
 */
public abstract class ShaderProgram {

    protected final int shaderProgramId;
    protected List<Uniform> uniforms = new ArrayList<>();

    public ShaderProgram() {
        // Create pointer to program
        this.shaderProgramId = glCreateProgram();
    }

    /**
     * List a lot of shaders to create a program
     * @param shaders list of shaders to link
     */
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

    /**
     * Prepare the inputs of the program
     * @param inVariables inputs of the program
     */
    private void bindAttributes(String[] inVariables) {
        // Bind all the in variables for use
        for (int i = 0; i < inVariables.length; i++) {
            glBindAttribLocation(this.shaderProgramId, i, inVariables[i]);
        }
    }

    /**
     * Set values of the uniform variables inside the program
     * @param uniforms list of shader constants
     */
    public void storeUniformLocations(Uniform... uniforms) {
        for (Uniform uniform : uniforms) {
            uniform.loadUniformToShader(this.shaderProgramId);
            this.uniforms.add(uniform);
        }
        glValidateProgram(this.shaderProgramId);
    }

    /**
     * Get a shader constant by name
     * @param name name of the constant
     * @return uniform variable
     */
    public Uniform getUniformVariable(String name) {
        for (Uniform uniform : this.uniforms) {
            if (uniform.getName().equalsIgnoreCase(name)) {
                return uniform;
            }
        }
        return null;
    }

    /////////////////////////////
    // CONTROLS OF THE PROGRAM //
    /////////////////////////////
    public void bind() {
        glUseProgram(this.shaderProgramId);
    }
    public void unbind() {
        glUseProgram(0);
    }
    public void delete() {
        glDeleteProgram(this.shaderProgramId);
    }

    /**
     * Check if the program was properly linked
     */
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
