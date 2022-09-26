package utils;

import java.io.*;

public class File {

    private String path;
    private String name;

    //////////////////////////
    // FILE TYPES LOCATIONS //
    //////////////////////////

    public static final String SHADER_FILE = "src/main/java/shaders/";
    public static final String COMPUTE_FILE = "src/main/java/computeShaders/";
    public static final String OBJ_FILE = "src/main/resources/objects/";
    public static final String TEXTURE_FILE = "src/main/resources/textures/";

    public File(String name, String fileType) {
        this.name = name;
        this.path = fileType + name;
    }

    public String getPath() {
        return path;
    }

    /**
     * Get a buffered reader to read the file
     * @return buffered reader
     */
    public BufferedReader getReader() {
        try {
            return new BufferedReader(new FileReader(this.path));
        } catch (FileNotFoundException exception) {
            System.err.println("ERROR: File not found! \n\t path: " + this.path);
            System.exit(-1);
        }
        return null;
    }

    /**
     * Get the input stream to write to the file
     * @return input stream
     * @throws FileNotFoundException
     */
    public DataInputStream getInputStream() throws FileNotFoundException {

        FileInputStream fstream = new FileInputStream(this.path);
        DataInputStream in = new DataInputStream(fstream);
        return in;
    }

}
