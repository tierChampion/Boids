#version 410 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 texCoords;
layout (location = 2) in vec3 normal;

out vec2 passTextureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition;

void main() {

    // Apply model matrix to have world space position
    vec4 worldPos = modelMatrix * vec4(pos, 1.0);
    // Calculate final render position
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    // Pass the texture coordinates to the fragment shader
    passTextureCoordinates = texCoords;
    // Get world space surface normal
    surfaceNormal = (modelMatrix * vec4(normal, 1.0)).xyz;
    // Vector from the vertex to the light
    toLightVector = lightPosition - worldPos.xyz;
    // Get camera position and subtract the world position from it
    // To get the vector from the vertex to the camera
    toCameraVector = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPos.xyz;
}
