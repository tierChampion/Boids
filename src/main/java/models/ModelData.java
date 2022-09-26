package models;

import models.modelLoader.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

/**
 * 3D model raw data
 */
public class ModelData {

    private final static int POSITION_SIZE = 3;
    private final static int TEXTURE_SIZE = 2;
    private final static int NORMAL_SIZE = 3;

    private final float[] vertexInformation;
    private final int[] indices;

    /**
     * Raw 3D model data
     * @param vertexList Raw data of vertices (position, normal, mapping)
     * @param indices Ordering of vertices
     */
    public ModelData(List<Vertex> vertexList, int[] indices) {
        this.vertexInformation = calculateVertexInformation(vertexList);
        this.indices = indices;
    }

    /**
     * Convert a list of vertex to its raw data
     * @param vertexList List of vertex to convert
     * @return Raw data
     */
    private float[] calculateVertexInformation(List<Vertex> vertexList) {
        float[] data = new float[vertexList.size() * (POSITION_SIZE + TEXTURE_SIZE + NORMAL_SIZE)];

        int dataIndex = 0;

        // For each Vertex
        for (int i = 0; i < vertexList.size(); i++) {
            Vertex currentVertex = vertexList.get(i);
            // For each Position
            Vector3f pos = currentVertex.getPos();
            for (int p = 0; p < POSITION_SIZE; p++) {
                data[dataIndex] = pos.get(p);
                dataIndex++;
            }
            // For each Texture Coordinate
            Vector2f texture = currentVertex.getTextureCoordinates();
            for (int t = 0; t < TEXTURE_SIZE; t++) {
                data[dataIndex] = texture.get(t);
                dataIndex++;
            }
            // For each Normal
            Vector3f normal = currentVertex.getNormal();
            for (int n = 0; n < NORMAL_SIZE; n++) {
                data[dataIndex] = normal.get(n);
                dataIndex++;
            }
        }

        return data;
    }

    public float[] getVertexInformation() {
        return vertexInformation;
    }
    public int[] getIndices() {
        return indices;
    }
}
