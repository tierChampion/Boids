package shaderVariables;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

/**
 * Generic uniform variable. A uniform variable is essentially a constant for shaders.
 */
public abstract class Uniform {

    private static final int NOT_FOUND = -1;

    //////////////////////////////////////////////
    // VARIABLES:                               //
    // NAME: NAME OF THE VARIABLE IN THE SHADER //
    // LOCATION: POINTER TO THE SHADER VARIABLE //
    //////////////////////////////////////////////
    private String name;
    private int location;

    protected Uniform(String name) {
        this.name = name;
    }

    /**
     * Get a pointer to the shader uniform variable
     * @param programID shader program
     */
    public void loadUniformToShader(int programID) {
        this.location = glGetUniformLocation(programID, this.name);
        if (this.location == NOT_FOUND) {
            System.err.println("No uniform variable called " + name + " found!");
        }
    }

    public String getName() {
        return this.name;
    }

    protected int getLocation() {
        return this.location;
    }
}
