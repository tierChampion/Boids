package models;

import dataStructures.opengl.Vao;
import models.modelLoader.OBJFileReader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import shaderProgram.PipelineShaderProgram;
import shaderVariables.UniformFloat;
import shaderVariables.UniformMatrix;
import utils.File;
import utils.Maths;

import static org.lwjgl.opengl.GL11.*;

/**
 * Full opengl 3D model
 */
public class RenderModel {

    private ModelData modelData;
    protected ModelTexture modelTexture;
    private Vao vao;
    private Vector3f position = new Vector3f(0, 0, 0);
    private float xRotation = 0;
    private float yRotation = 0;

    /**
     * Opengl 3D model
     * @param modelFile Mesh file
     * @param textureFile Texture file
     * @param shineDamper Texture dampening parameter
     * @param reflectivity Texture reflectivity parameter
     */
    public RenderModel(File modelFile, File textureFile, float shineDamper, float reflectivity) {
        this.modelData = OBJFileReader.loadOBJ(modelFile);
        this.modelTexture = new ModelTexture(textureFile, shineDamper, reflectivity);
        this.vao = new Vao();
        this.vao.storeData(modelData.getVertexInformation(), modelData.getIndices());
        this.vao.generateVertexPointers();
    }

    public Matrix4f getModelMatrix() {
        return Maths.convertToModelMatrix(this.position, this.xRotation, this.yRotation, 1);
    }

    /**
     * Binds texture of the model
     */
    private void prepareTexture() {
        this.modelTexture.bindTexture(0);
    }

    /**
     * Update the uniform variables of the model
     * @param pipelineShaderProgram Program to modify the uniforms of
     */
    protected void updateIndividualUniforms(PipelineShaderProgram pipelineShaderProgram) {
        ((UniformMatrix) pipelineShaderProgram.getUniformVariable("modelMatrix"))
                .loadMatrix(getModelMatrix());
        ((UniformFloat) pipelineShaderProgram.getUniformVariable("reflectivity"))
                .loadFloat(this.modelTexture.getReflectivity());
        ((UniformFloat) pipelineShaderProgram.getUniformVariable("shineDamper"))
                .loadFloat(this.modelTexture.getShineDamper());
    }

    /**
     * Prepare and render model
     * @param pipelineShaderProgram Shader program to use
     */
    public void render(PipelineShaderProgram pipelineShaderProgram) {
        prepareTexture();
        updateIndividualUniforms(pipelineShaderProgram);

        this.vao.bind();
        this.vao.enableVertexAttributes(0, 1, 2);

        glDrawElements(GL_TRIANGLES, this.vao.getDrawCount(), GL_UNSIGNED_INT, 0);

        this.vao.disableVertexAttributes(0, 1, 2);
        this.vao.unbind();
    }
}
