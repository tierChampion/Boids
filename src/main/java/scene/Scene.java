package scene;

import boids.Boid;
import lighting.Light;
import shaderProgram.PipelineShaderProgram;
import utils.OpenGlUtils;

import java.util.ArrayList;
import java.util.List;

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

    protected abstract void initShaderProgram();

    protected abstract void initAssets();

    protected abstract void initUniforms();

    protected abstract void updateUniforms();

    public abstract void trackInputs();

    public abstract void renderScene();

    public Boid getModel(int index) {
        return this.models.get(index);
    }

    public Light getLight(int index) {
        return this.lights.get(index);
    }


}
