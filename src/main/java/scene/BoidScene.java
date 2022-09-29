package scene;

import boids.Boid;
import dataStructures.spacial.OcTree;
import camera.Camera;
import inputs.JoyStickListener;
import inputs.KeyListener;
import inputs.MouseListener;
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

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * Scene for simple boid flight
 */
public class BoidScene extends Scene {

    private static final String VERTEX_SHADER = "vertex.glsl";
    private static final String FRAGMENT_SHADER = "fragment.glsl";

    private OcTree<Boid> tree;

    /**
     * General scene for the 3D boid simulation.
     * Use of a traditional shader pipeline (vertex -> fragment)
     * and on cpu calculations for the boid dynamics
     *
     * @param count Number of boids in the scene
     * @param bounds World dimensions of the scene
     * @param modelName Name of the boid 3D model
     * @param textureName Name of the boid texture
     * @param shine Shine damper of the material of the model
     * @param reflectivity Reflectivity of the material of the model
     * @param timeStep Time per frame
     * @param lightDir Direction of the infinite light
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
    public BoidScene(int count, Vector3f bounds, String modelName, String textureName, float shine, float reflectivity,
                     float timeStep, Vector3f lightDir, float modelScale, float viewRad, float avoidRad,
                     float maxSpeed, float minSpeed, float maxForce,
                     float align, float cohesion, float avoid) {

        super(count, bounds, modelName, textureName, shine, reflectivity,
                timeStep, lightDir, modelScale, viewRad, avoidRad,
                maxSpeed, minSpeed, maxForce,
                align, cohesion, avoid);
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
        Shader vertexShader = new Shader(GL_VERTEX_SHADER, new File(VERTEX_SHADER, File.SHADER_FILE));
        vertexShader.passShaderInfoToGPU();
        Shader fragmentShader = new Shader(GL_FRAGMENT_SHADER, new File(FRAGMENT_SHADER, File.SHADER_FILE));
        fragmentShader.passShaderInfoToGPU();
        super.pipelineShaderProgram = new PipelineShaderProgram();
        String[] inVariables = new String[]{"pos", "texCoords", "normal"};
        super.pipelineShaderProgram.prepareProgram(inVariables, vertexShader, fragmentShader);
        initUniforms();
    }

    @Override
    protected void initAssets(int count, Vector3f bounds, String modelName, String textureName, float shine, float reflectivity,
                              float timeStep, Vector3f lightDir, float modelScale, float viewRad, float avoidRad,
                              float maxSpeed, float minSpeed, float maxForce,
                              float align, float cohesion, float avoid) {

        super.initAssets(count, bounds, modelName, textureName, shine, reflectivity,
                timeStep, lightDir, modelScale, viewRad, avoidRad,
                maxSpeed, minSpeed, maxForce,
                align, cohesion, avoid);
        this.tree = new OcTree<>(super.models, 1, new Vector3f(0), bounds);
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
            Camera.changeYaw(JoyStickListener.getAxis(GLFW_GAMEPAD_AXIS_LEFT_X) / 10);

        } else if (Math.abs(MouseListener.getDx()) > 0.1) {
            float delta = MouseListener.getDx() / 25;
            Camera.changeYaw(delta);
        }
        if (Math.abs(JoyStickListener.getAxis(GLFW_GAMEPAD_AXIS_LEFT_Y)) > 0.1) {
            Camera.changePitch(JoyStickListener.getAxis(GLFW_GAMEPAD_AXIS_LEFT_Y) / 10);
        } else if (Math.abs(MouseListener.getDy()) > 0.1) {
            float delta = MouseListener.getDy() / 25;
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
