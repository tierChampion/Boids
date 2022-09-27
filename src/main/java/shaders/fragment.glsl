#version 410 core

in vec2 passTextureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

layout (location = 0) out vec4 outColor;

uniform sampler2D modelTexture;
uniform float reflectivity;
uniform float shineDamper;
uniform vec3 attenuation;

void main() {

    // Attenuation
    float distance = length(toLightVector);
    float attenuationFactor = attenuation.x + (attenuation.y * distance) +
    (attenuation.z * distance * distance);

    // Calculate unit vectors to make dot products
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 unitLightVector = normalize(toLightVector);

    // How much the mormal faces the light, which is needed for diffuse lighting
    float normalFacingLightFactor = dot(unitNormal, unitLightVector);
    float diffuse = max(normalFacingLightFactor, 0);
    // Looks at reflected light from the light source and see how much it goes towards the camera
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    // Diffuse and specular factor
    diffuse = max(diffuse, 0.2) / attenuationFactor;
    float specular = (dampedFactor * reflectivity) / attenuationFactor;
    // Color for each effect
    vec4 textureColor = texture(modelTexture, passTextureCoordinates);
    vec4 diffuseColor = vec4(vec3(diffuse), 1.0);
    vec4 specularColor = vec4(vec3(specular), 1.0);
    // Mix them to get final color with ambiant
    outColor = diffuseColor * textureColor + specularColor + vec4(vec3(0.5), 1.0);

}
