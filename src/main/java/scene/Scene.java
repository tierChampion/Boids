package scene;

import boids.Boid;
import lighting.Light;
import shaderProgram.PipelineShaderProgram;
import utils.OpenGlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent all that gets displayed on the window.
 */
public abstract class Scene {

    protected PipelineShaderProgram pipelineShaderProgram;
    protected List<Boid> models = new ArrayList<>();
    protected List<Light> lights = new ArrayList<>();

    protected Scene() {
        init();
    }

    private void init() {
        initShaderProgram();
        initAssets();
        OpenGlUtils.activateBackFaceCulling();
        OpenGlUtils.activateZBuffering();
    }

    /**
     * Prepare the pipeline shader program for rendering
     */
    protected abstract void initShaderProgram();

    /**
     * Prepare the models for rendering
     */
    protected abstract void initAssets();

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
