// Vertex shader for program with compute shader
#version 430

layout (location = 0) in vec3 vertexPos;
layout (location = 1) in vec2 vertexTexture;
layout (location = 2) in vec3 vertexNormal;

out vec2 passTextureCoordinates;

void main() {

    gl_Position = vec4(vertexPos, 1.0);
    passTextureCoordinates = vertexTexture;

}

