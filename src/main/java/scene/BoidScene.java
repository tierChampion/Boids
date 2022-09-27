package scene;

import boids.Boid;
import dataStructures.spacial.OcTree;
import camera.Camera;
import inputs.JoyStickListener;
import inputs.KeyListener;
import inputs.MouseListener;
import lighting.InfiniteLight;
import lighting.Light;
import models.RenderModel;
import org.joml.Vector3f;
import shaderProgram.PipelineShaderProgram;
import shaderProgram.Shader;
import shaderVariables.UniformFloat;
import shaderVariables.UniformMatrix;
import shaderVariables.UniformSampler;
import shaderVariables.UniformVec3;
import utils.File;
import utils.OpenGlUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * Scene for simple boid flight
 */
public class BoidScene extends Scene {

    private static final int BOID_COUNT = 200;
    private static final float ALIGN_FACTOR = 1;
    private static final float COHESION_FACTOR = 1;
    private static final float AVOID_FACTOR = 1;

    private static final Vector3f WORLD_BOUNDS = new Vector3f(20);
    private OcTree<Boid> tree;

    public BoidScene() {
        super();
    }

    /**
     * Move the boids
     */
    private void updateFlock() {
        for (Boid boid : super.models) {
            boid.update(tree);
        }
    }

    @Override
    protected void initShaderProgram() {
        Shader vertexShader = new Shader(GL_VERTEX_SHADER, new File("vertex.glsl", File.SHADER_FILE));
        vertexShader.passShaderInfoToGPU();
        Shader fragmentShader = new Shader(GL_FRAGMENT_SHADER, new File("fragment.glsl", File.SHADER_FILE));
        fragmentShader.passShaderInfoToGPU();
        super.pipelineShaderProgram = new PipelineShaderProgram();
        String[] inVariables = new String[]{"pos", "texCoords", "normal"};
        super.pipelineShaderProgram.prepareProgram(inVariables, vertexShader, fragmentShader);
        initUniforms();
    }

    @Override
    protected void initAssets() {
        Boid.setSettings(0.2f, WORLD_BOUNDS, 8, 2, 3, 1 / 60.0f,
                ALIGN_FACTOR, COHESION_FACTOR, AVOID_FACTOR);
        for (int i = 0; i < BOID_COUNT; i++) {
            super.models.add(new Boid(new File("testTexture.png", File.TEXTURE_FILE),
                    0.0f, 0.0f, 8f, 3f));
            super.models.get(i).placeRandomlyInWorld();
        }

        this.tree = new OcTree<>(super.models, 1, new Vector3f(0), WORLD_BOUNDS);

        List<Light> lights = new ArrayList<>();
        lights.add(new InfiniteLight(new Vector3f(1000, 1000, 1000)));
        super.lights = lights;
    }

    @Override
    protected void initUniforms() {
        UniformSampler modelTexture = new UniformSampler("modelTexture");
        UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
        UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
        UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
        UniformVec3 lightPosition = new UniformVec3("lightPosition");
        UniformFloat reflectivity = new UniformFloat("reflectivity");
        UniformFloat shineDamper = new UniformFloat("shineDamper");
        UniformVec3 attenuation = new UniformVec3("attenuation");
        super.pipelineShaderProgram.storeUniformLocations(modelTexture, projectionMatrix, viewMatrix,
                modelMatrix, lightPosition, reflectivity, shineDamper, attenuation);
        // Load initial uniforms
        super.pipelineShaderProgram.bind();
        modelTexture.loadTextureUnit(0);
        projectionMatrix.loadMatrix(Camera.getProjectionMatrix());
        viewMatrix.loadMatrix(Camera.getViewMatrix());
    }

    @Override
    protected void updateUniforms() {

        this.tree.update();

        ((UniformMatrix) this.pipelineShaderProgram.getUniformVariable("viewMatrix"))
                .loadMatrix(Camera.getViewMatrix());
        if (Camera.hasProjectionChanged()) {
            ((UniformMatrix) this.pipelineShaderProgram.getUniformVariable("projectionMatrix"))
                    .loadMatrix(Camera.getProjectionMatrix());
        }
        ((UniformVec3) this.pipelineShaderProgram.getUniformVariable("lightPosition"))
                .loadVec3(super.lights.get(0).getPosition());
        ((UniformVec3) this.pipelineShaderProgram.getUniformVariable("attenuation"))
                .loadVec3(super.lights.get(0).getAttenuation());
    }

    @Override
    public void trackInputs() {
        if (JoyStickListener.getButtonPress(GLFW_GAMEPAD_BUTTON_A) || KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            Camera.move(-1);
        }
        if (JoyStickListener.getButtonPress(GLFW_GAMEPAD_BUTTON_B) || KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            Camera.move(1);
        }

        ///////////////////////////////////////////////
        // JOYSTICKS / MOUSE                         //
        // HAVE A MINIMUM GIVE TO REMOVE DEAD-INPUTS //
        ///////////////////////////////////////////////
        if (Math.abs(JoyStickListener.getAxis(GLFW_GAMEPAD_AXIS_LEFT_X)) > 0.1) {
            Camera.changeYaw(-JoyStickListener.getAxis(GLFW_GAMEPAD_AXIS_LEFT_X) / 10);

        } else if (Math.abs(MouseListener.getDx()) > 0.1) {
            float delta = MouseListener.getDx() / 25;
            Camera.changeYaw(delta);
        }
        if (Math.abs(JoyStickListener.getAxis(GLFW_GAMEPAD_AXIS_LEFT_Y)) > 0.1) {
            Camera.changePitch(-JoyStickListener.getAxis(GLFW_GAMEPAD_AXIS_LEFT_Y) / 10);
        } else if (Math.abs(MouseListener.getDy()) > 0.1) {
            float delta = -MouseListener.getDy() / 25;
            Camera.changePitch(delta);
        }

        MouseListener.endFrame();
    }

    @Override
    public void renderScene() {
        this.updateFlock();
        this.pipelineShaderProgram.bind();
        trackInputs();
        updateUniforms();

        for (RenderModel model : models) {
            model.render(this.pipelineShaderProgram);
        }

        this.pipelineShaderProgram.unbind();
        OpenGlUtils.clearZBuffer();
    }
}
