package models.modelLoader;

import models.ModelData;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OBJFileReader {

    /**
     * Read raw mesh data from a .obj file
     * @param objFile Mesh file to read
     * @return Raw mesh data
     */
    public static ModelData loadOBJ(File objFile) {
        // Create Reader
        BufferedReader reader = objFile.getReader();
        String line;
        // Final Data
        List<Vertex> vertexList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();
        // OBJ File Data
        List<Vector3f> positionList = new ArrayList<>();
        List<Vector2f> textureList = new ArrayList<>();
        List<Vector3f> normalList = new ArrayList<>();

        try {
            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                // Vertex pos
                if (line.startsWith("v ")) {
                    positionList.add(
                            new Vector3f(
                                    Float.parseFloat(currentLine[1]),
                                    Float.parseFloat(currentLine[2]),
                                    Float.parseFloat(currentLine[3])));
                    // Vertex Texture Coordinate
                } else if (line.startsWith("vt ")) {
                    textureList.add(
                            new Vector2f(
                                    Float.parseFloat(currentLine[1]),
                                    Float.parseFloat(currentLine[2])));
                    // Vertex Normal
                } else if (line.startsWith("vn ")) {
                    normalList.add(
                            new Vector3f(
                                    Float.parseFloat(currentLine[1]),
                                    Float.parseFloat(currentLine[2]),
                                    Float.parseFloat(currentLine[3])));
                    // Faces
                } else if (line.startsWith("f ")) {
                    break;
                }
            }

            int pointer = 0;

            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                // Get vertex data (pos, tex, norm)
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                // Vertex index
                int index1 = Integer.parseInt(vertex1[0]) - 1;
                int index2 = Integer.parseInt(vertex2[0]) - 1;
                int index3 = Integer.parseInt(vertex3[0]) - 1;

                // Create vertex
                Vertex v0 =
                        new Vertex(
                                positionList.get(index1),
                                textureList.get(Integer.parseInt(vertex1[1]) - 1),
                                normalList.get(Integer.parseInt(vertex1[2]) - 1));
                vertexList.add(v0);

                Vertex v1 =
                        new Vertex(
                                positionList.get(index2),
                                textureList.get(Integer.parseInt(vertex2[1]) - 1),
                                normalList.get(Integer.parseInt(vertex2[2]) - 1));
                vertexList.add(v1);

                Vertex v2 =
                        new Vertex(
                                positionList.get(index3),
                                textureList.get(Integer.parseInt(vertex3[1]) - 1),
                                normalList.get(Integer.parseInt(vertex3[2]) - 1));
                vertexList.add(v2);
                indicesList.add(pointer++);
                indicesList.add(pointer++);
                indicesList.add(pointer++);

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException exception) {
            System.err.println("Couldn't read model file: " + objFile);
            System.exit(-1);
        }
        int[] indices = convertIndicesListToArray(indicesList);
        return new ModelData(vertexList, indices);
    }

    private static int[] convertIndicesListToArray(List<Integer> indicesList) {
        int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = indicesList.get(i);
        }
        return indices;
    }
}
