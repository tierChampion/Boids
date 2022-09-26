// Fragment shader for program with compute shader
#version 430

in vec2 passTextureCoordinates;

layout (location = 0) out vec4 outColor;

uniform sampler2D computeShaderResult;

void main() {

    outColor = texture(computeShaderResult, passTextureCoordinates);

}
