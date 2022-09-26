package shaderProgram;

import utils.File;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int shaderID;
    private int type;
    private String sourceCode;
    private File file;

    public Shader(int type, File file) {
        this.type = type;
        this.file = file;
        this.sourceCode = readFile(file);
    }

    private String readFile(File file) {
        StringBuilder sourceCode = new StringBuilder();
        try {
            BufferedReader reader = file.getReader();
            String currentLine = reader.readLine();
            while (currentLine!= null) {
                sourceCode.append(currentLine).append("\n");
                currentLine = reader.readLine();
            }
        } catch (FileNotFoundException exception) {
            System.out.println("ERROR in Shader file loading\n\t" + file.getPath() + " not found.");
        } catch (IOException exception) {
            System.out.println("ERROR in Shader file loading\n\t" + file.getPath() + " : problems while trying to read it. ");
        }
        return sourceCode.toString();
    }

    public void passShaderInfoToGPU() {
        // Load pointer for shader
        this.shaderID = glCreateShader(this.type);
        // Load pointer for shader code
        glShaderSource(this.shaderID, this.sourceCode);
        // Compile shader
        glCompileShader(this.shaderID);

        // Check for errors in compilation
        int success = glGetShaderi(this.shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            // Needs length to print the info log
            int length = glGetShaderi(this.shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR IN: " + this.file + "\n\tShader compilation failed.");
            System.out.println(glGetShaderInfoLog(this.shaderID, length));
            assert false : "";
        }
    }

    public void delete() {
        glDeleteShader(this.shaderID);
    }

    public int getShaderID() {
        return this.shaderID;
    }
}
