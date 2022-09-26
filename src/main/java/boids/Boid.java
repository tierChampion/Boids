package boids;

import dataStructures.spacial.OcTree;
import models.RenderModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import shaderProgram.PipelineShaderProgram;
import shaderVariables.UniformFloat;
import shaderVariables.UniformMatrix;
import utils.File;

import java.util.List;
import java.util.Random;

/**
 * Boid. Agent that acts like a bird in a flock
 */
public class Boid extends RenderModel {

    private static float MODEL_SCALE;

    private static Vector3f WORLD_DIMENSIONS = new Vector3f(20);
    private static float MAX_SPEED = 8;
    private static float MIN_SPEED = 2;
    private static float MAX_FORCE = 3;
    private static float TIME_STEP = 1 / 60.0f;

    private static float ALIGNMENT_FACTOR = 1;
    private static float COHESION_FACTOR = 1;
    private static float AVOIDANCE_FACTOR = 1;

    private final Vector3f position;
    private final Vector3f velocity;

    private float viewRadius;
    private float avoidRadius;

    private Matrix4f currentModelMatrix = null;

    /**
     * Bird agent
     * @param textureFile .png image file for texture
     * @param shineDamper texture light dampening
     * @param reflectivity texture light reflectivity
     * @param viewRadius maximum distance to neighbour
     * @param avoidRadius distance at which to avoid collisions
     */
    public Boid(
            File textureFile,
            float shineDamper,
            float reflectivity,
            float viewRadius,
            float avoidRadius) {
        super(new File("baseBoid.obj", File.OBJ_FILE), textureFile, shineDamper, reflectivity);
        this.position = new Vector3f();
        this.velocity = new Vector3f();
        this.viewRadius = viewRadius;
        this.avoidRadius = avoidRadius;
    }

    /**
     * Set the parameters of all boids
     * @param modelScale Size of a single boid
     * @param worldDimensions World bounds
     * @param maxSpeed Maximum speed of a boid
     * @param minSpeed Minimum speed of a boid
     * @param maxForce Maximum steering force
     * @param timeStep Frame time step
     * @param alignmentFactor Strength of the alignment behaviour
     * @param cohesionFactor Strength of the cohesion behaviour
     * @param avoidanceFactor Strength of the avoidance befaviour
     */
    public static void setSettings(float modelScale, Vector3f worldDimensions, float maxSpeed, float minSpeed,
                                   float maxForce, float timeStep, float alignmentFactor, float cohesionFactor,
                                   float avoidanceFactor) {
        MODEL_SCALE = modelScale;
        WORLD_DIMENSIONS = worldDimensions;
        MAX_SPEED = maxSpeed;
        MIN_SPEED = minSpeed;
        MAX_FORCE = maxForce;
        TIME_STEP = timeStep;
        ALIGNMENT_FACTOR = alignmentFactor;
        COHESION_FACTOR = cohesionFactor;
        AVOIDANCE_FACTOR = avoidanceFactor;
    }

    /**
     * Set position to a random location inside the world bounds.
     */
    public void placeRandomlyInWorld() {
        Random random = new Random();

        this.position.x = random.nextFloat() * WORLD_DIMENSIONS.x;
        this.position.y = random.nextFloat() * WORLD_DIMENSIONS.y;
        this.position.z = random.nextFloat() * WORLD_DIMENSIONS.z;

        this.velocity.x = random.nextFloat() * 2 * MAX_SPEED - MAX_SPEED;
        this.velocity.y = random.nextFloat() * 2 * MAX_SPEED - MAX_SPEED;
        this.velocity.z = random.nextFloat() * 2 * MAX_SPEED - MAX_SPEED;

        if (this.velocity.lengthSquared() < MIN_SPEED * MIN_SPEED
                && this.velocity.lengthSquared() != 0) {
            this.velocity.normalize(MIN_SPEED);
        }
    }

    /**
     * Update the boid depending on the list of other boids
     * @param boids List of other boids
     */
    public void update(List<Boid> boids) {

        Vector3f acceleration = new Vector3f();

        int numPerceivedFlockMates = 0;

        Vector3f flockHeading = new Vector3f();
        Vector3f flockCenter = new Vector3f();
        Vector3f separationHeading = new Vector3f();

        for (Boid boid : boids) {
            if (!boid.equals(this)) {

                Vector3f offset = new Vector3f(boid.position).sub(this.position);
                float sqrDistance = offset.lengthSquared();

                // Check if neighbour
                if (sqrDistance < viewRadius * viewRadius) {
                    numPerceivedFlockMates++;
                    flockHeading.add(new Vector3f(boid.velocity).normalize());
                    flockCenter.add(new Vector3f(boid.position));

                    // Check if neighbour needs to be avoided
                    if (sqrDistance < avoidRadius * avoidRadius) {
                        separationHeading.sub(offset.div(sqrDistance));
                    }
                }
            }
        }

        // Apply forces depending on the neighbours
        if (numPerceivedFlockMates > 0) {
            flockCenter.div(numPerceivedFlockMates);
            Vector3f offsetToCenter = flockCenter.sub(this.position);

            Vector3f alignmentForce = steerTowards(flockHeading);
            Vector3f cohesionForce = steerTowards(offsetToCenter);
            Vector3f separationForce = steerTowards(separationHeading);

            acceleration.add(alignmentForce.mul(ALIGNMENT_FACTOR));
            acceleration.add(cohesionForce.mul(COHESION_FACTOR));
            acceleration.add(separationForce.mul(AVOIDANCE_FACTOR));

            this.velocity.add(acceleration.mul(TIME_STEP));
            float speed = this.velocity.lengthSquared();
            if (speed > MAX_SPEED * MAX_SPEED) {
                this.velocity.normalize(MAX_SPEED);
            } else if (speed < MIN_SPEED * MIN_SPEED && speed != 0) {
                this.velocity.normalize(MIN_SPEED);
            }
        }
        this.position.add(new Vector3f(this.velocity).mul(TIME_STEP));

        // Make boids wrap around the bounds
        keepInWorld();
    }

    /**
     * Update the boid depending on an Octree of all boids
     * @param boids Octree of the scene
     */
    public void update(OcTree<Boid> boids) {
        Vector3f acceleration = new Vector3f();

        int numPerceivedFlockmates = 0;

        Vector3f flockHeading = new Vector3f();
        Vector3f flockCenter = new Vector3f();
        Vector3f separationHeading = new Vector3f();

        List<Boid> neighbours = boids.query(this.position, this.viewRadius);

        // Apply neighbours effect
        for (Boid neighbour : neighbours) {
            if (!neighbour.equals(this)) {
                numPerceivedFlockmates++;
                flockHeading.add(new Vector3f(neighbour.velocity).normalize());
                flockCenter.add(new Vector3f(neighbour.position));
            }
        }

        // Avoid close neighbours
        for (Boid close : neighbours) {

            Vector3f offset = new Vector3f(close.position).sub(this.position);
            if (!close.equals(this) && offset.lengthSquared() < avoidRadius * avoidRadius) {
                separationHeading.sub(offset.div(offset.lengthSquared()));
            }
        }

        // Apply forces
        if (numPerceivedFlockmates > 0) {
            flockCenter.div(numPerceivedFlockmates);
            Vector3f offsetToCenter = flockCenter.sub(this.position);

            Vector3f alignmentForce = steerTowards(flockHeading);
            Vector3f cohesionForce = steerTowards(offsetToCenter);
            Vector3f separationForce = steerTowards(separationHeading);

            acceleration.add(alignmentForce.mul(ALIGNMENT_FACTOR));
            acceleration.add(cohesionForce.mul(COHESION_FACTOR));
            acceleration.add(separationForce.mul(AVOIDANCE_FACTOR));

            this.velocity.add(acceleration.mul(TIME_STEP));
            float speed = this.velocity.lengthSquared();
            if (speed > MAX_SPEED * MAX_SPEED) {
                this.velocity.normalize(MAX_SPEED);
            } else if (speed < MIN_SPEED * MIN_SPEED && speed != 0) {
                this.velocity.normalize(MIN_SPEED);
            }
        }
        this.position.add(new Vector3f(this.velocity).mul(TIME_STEP));

        // Make boids wrap around the bounds
        keepInWorld();
    }

    /**
     * Wrap boids around the world
     */
    private void keepInWorld() {
        if (this.position.x < 0) {
            this.position.x += WORLD_DIMENSIONS.x;
        } else if (this.position.x > WORLD_DIMENSIONS.x) {
            this.position.x -= WORLD_DIMENSIONS.x;
        }
        if (this.position.y < 0) {
            this.position.y += WORLD_DIMENSIONS.y;
        } else if (this.position.y > WORLD_DIMENSIONS.y) {
            this.position.y -= WORLD_DIMENSIONS.y;
        }
        if (this.position.z < 0) {
            this.position.z += WORLD_DIMENSIONS.z;
        } else if (this.position.z > WORLD_DIMENSIONS.z) {
            this.position.z -= WORLD_DIMENSIONS.z;
        }
    }

    /**
     * Calculate force required to steer towards another vector
     * @param vector Direction to steer towards
     * @return Force to apply to boid
     */
    private Vector3f steerTowards(Vector3f vector) {
        Vector3f v = new Vector3f();
        if (vector.lengthSquared() != 0) {
            v = vector.normalize().mul(MAX_SPEED).sub(this.velocity);
            if (v.lengthSquared() >= MAX_FORCE * MAX_FORCE) {
                v.normalize(MAX_FORCE);
            }
        }
        return v;
    }

    public Vector3f getPosition() { return new Vector3f(this.position); }
    public Vector3f getVelocity() { return new Vector3f(this.velocity); }

    @Override
    protected void updateIndividualUniforms(PipelineShaderProgram pipelineShaderProgram) {
        ((UniformMatrix) pipelineShaderProgram.getUniformVariable("modelMatrix"))
                .loadMatrix(getModelMatrix());
        ((UniformFloat) pipelineShaderProgram.getUniformVariable("reflectivity"))
                .loadFloat(super.modelTexture.getReflectivity());
        ((UniformFloat) pipelineShaderProgram.getUniformVariable("shineDamper"))
                .loadFloat(super.modelTexture.getShineDamper());
    }

    @Override
    public Matrix4f getModelMatrix() {
        boolean speedIsNull = this.velocity.equals(new Vector3f(0, 0, 0));
        if (this.currentModelMatrix == null || !speedIsNull) {

            Matrix4f modelMatrix = new Matrix4f();

            if (speedIsNull) {
                modelMatrix.scale(MODEL_SCALE);
                this.currentModelMatrix = modelMatrix;
                return this.currentModelMatrix;
            }

            Vector3f upVector;
            Vector3f heading = new Vector3f(this.velocity).normalize();

            // Find up vector of the boid
            if (!heading.equals(new Vector3f(0, 1f, 0))
                    && !heading.equals(new Vector3f(0, -1f, 0))) {
                upVector = new Vector3f(0, 1, 0);

            } else if (heading.y == 1) {
                upVector = new Vector3f(1, 0, 0);
            } else {
                upVector = new Vector3f(-1, 0, 0);
            }

            modelMatrix.translate(this.position);
            modelMatrix.rotateTowards(this.velocity, upVector);
            modelMatrix.scale(MODEL_SCALE);

            this.currentModelMatrix = modelMatrix;
        }
        return this.currentModelMatrix;
    }

    public float getViewRadius() {
        return this.viewRadius;
    }
    public float getAvoidRadius() {
        return this.avoidRadius;
    }
}
