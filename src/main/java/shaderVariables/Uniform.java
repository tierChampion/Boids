package shaderVariables;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public abstract class Uniform {

    private static final int NOT_FOUND = -1;

    private String name;
    private int location;

    protected Uniform(String name) {
        this.name = name;
    }

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
