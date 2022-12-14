package scene;

import boids.Boid;
import camera.Camera;
import dataStructures.spacial.OcTree;
import lighting.InfiniteLight;
import lighting.Light;
import lighting.PointLight;
import org.joml.Vector3f;
import shaderProgram.PipelineShaderProgram;
import utils.File;
import utils.OpenGlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Represent all that gets displayed on the window.
 */
public abstract class Scene {

    protected PipelineShaderProgram pipelineShaderProgram;
    protected List<Boid> models = new ArrayList<>();
    protected List<Light> lights = new ArrayList<>();

    protected Scene(float cameraDist, int count, Vector3f bounds, String modelName, String textureName, float shine, float reflectivity,
                    float timeStep, Vector3f lightPos, float modelScale, float viewRad, float avoidRad,
                    float maxSpeed, float minSpeed, float maxForce,
                    float align, float cohesion, float avoid) {

        initShaderProgram();
        initAssets(cameraDist, count, bounds, modelName, textureName, shine, reflectivity,
                timeStep, lightPos, modelScale, viewRad, avoidRad,
                maxSpeed, minSpeed, maxForce,
                align, cohesion, avoid);
        OpenGlUtils.activateBackFaceCulling();
        OpenGlUtils.activateZBuffering();
    }

    /**
     * Prepare the pipeline shader program for rendering
     */
    protected abstract void initShaderProgram();

    /**
     * Prepare the models for rendering
     * @param count Number of boids in the scene
     * @param bounds World dimensions of the scene
     * @param modelName Name of the boid 3D model
     * @param textureName Name of the boid texture
     * @param shine Shine damper of the material of the model
     * @param reflectivity Reflectivity of the material of the model
     * @param timeStep Time per frame
     * @param lightPos Position of the infinite light
     * @param modelScale Scale of the 3D model
     * @param viewRad Distance at which boids are considered neighbours
     * @param avoidRad Distance at which boids are considered too close
     * @param maxSpeed Maximum speed of the boids
     * @param minSpeed Minimum speed of the boids
     * @param maxForce Maximum steering force of the boids
     * @param align Strength of the alignment behaviour of the boids
     * @param cohesion Strength of the cohesion behaviour of the boids
     * @param avoid Strength of the avoidance behaviour of the boids
     */
    protected void initAssets(float cameraDist, int count, Vector3f bounds, String modelName, String textureName,
                              float shine, float reflectivity,
                              float timeStep, Vector3f lightPos, float modelScale, float viewRad, float avoidRad,
                              float maxSpeed, float minSpeed, float maxForce,
                              float align, float cohesion, float avoid) {
        Boid.setSettings(modelScale, bounds, maxSpeed, minSpeed, maxForce, timeStep,
                align, cohesion, avoid);

        Camera.move(bounds.x / 2, bounds.y / 2, cameraDist);

        File modelFile = new File(modelName, File.OBJ_FILE);
        File textureFile = new File(textureName, File.TEXTURE_FILE);
        for (int i = 0; i < count; i++) {
            this.models.add(new Boid(modelFile, textureFile,
                    shine, reflectivity, viewRad, avoidRad));
            this.models.get(i).placeRandomlyInWorld();
        }
    }

    /**
     * Prepare the uniform variables for rendering
     */
    protected abstract void initUniforms();

    /**
     * Update the values of the uniform variables
     */
    protected abstract void updateUniforms();

    /**
     * Look at the inputs / coontrols
     */
    public abstract void trackInputs();

    /**
     * Call the shader program and render the scene
     */
    public abstract void renderScene();

    public Boid getModel(int index) {
        return this.models.get(index);
    }

    public Light getLight(int index) {
        return this.lights.get(index);
    }
}
