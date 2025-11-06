package dev.behindthescenery.core.system.rendering.assimp.shaders;

import dev.behindthescenery.core.system.rendering.TextureType;
import dev.behindthescenery.core.system.rendering.shader.BasicInstancingShaderProgram;
import dev.behindthescenery.core.system.rendering.shader.errorListener.ShaderError;
import dev.behindthescenery.core.system.rendering.shader.errorListener.ShaderErrorListener;
import dev.behindthescenery.core.system.rendering.shader.uniform.ShaderUniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Pair;
import org.intellij.lang.annotations.Language;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ColorModelShader extends BasicInstancingShaderProgram {

    @Language("glsl")
    private static final String VERTEX =
"""
#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec2 TexCoord;
layout (location = 2) in vec3 Normal;
layout (location = 3) in ivec4 BoneIndices;
layout (location = 4) in vec4 BoneWeights;

layout (location = 5) in vec4 instanceModelMatrix0;
layout (location = 6) in vec4 instanceModelMatrix1;
layout (location = 7) in vec4 instanceModelMatrix2;
layout (location = 8) in vec4 instanceModelMatrix3;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 boneTransforms[64];

uniform bool hasmodel;

uniform bool useInstancing;

out vec2 fragTexCoord;
out vec3 fragNormal;
out vec3 fragPos;

void main() {
    vec4 localPos = vec4(Position, 1.0);
    vec3 localNormal = Normal;
    bool hasBones = false;

//    if (BoneIndices[0] >= 0) {
//        localPos = vec4(0.0);
//        localNormal = vec3(0.0);
//        hasBones = true;
//
//        for (int i = 0; i < 4; i++) {
//            if (BoneIndices[i] >= 0 && BoneWeights[i] > 0.0) {
//                mat4 boneTransform = boneTransforms[BoneIndices[i]];
//                localPos += BoneWeights[i] * (boneTransform * vec4(Position, 1.0));
//                localNormal += BoneWeights[i] * (mat3(boneTransform) * Normal);
//            }
//        }
//    }

    mat4 instanceModel = mat4(1.0);
    if (useInstancing) {
        instanceModel = mat4(instanceModelMatrix0, instanceModelMatrix1, instanceModelMatrix2, instanceModelMatrix3);
    }

    vec4 worldPos = useInstancing ? (instanceModel * localPos) : (model * localPos);
    
    gl_Position = projection * view * worldPos;
    fragPos = worldPos.xyz;
    
    fragNormal = normalize(mat3(useInstancing ? instanceModel : model) * localNormal);
    fragTexCoord = TexCoord;
}
""";

    @Language("glsl")
    private static final String FRAGMENT =
"""
#version 330 core
 
 in vec2 fragTexCoord;
 in vec3 fragNormal;
 in vec3 fragPos;
 
 uniform sampler2D diffuseMap;
 uniform sampler2D normalMap;
 
 // ORM (AO/Roughness/Metallic)
 uniform sampler2D ormMap;
 uniform bool hasormMap;
 
 // Отдельные карты
 uniform sampler2D aoMap;
 uniform sampler2D roughnessMap;
 uniform sampler2D metallicMap;
 uniform bool hasaoMap;
 uniform bool hasroughnessMap;
 uniform bool hasmetallicMap;
 uniform bool hasnormalMap;
 
 uniform vec3 lightPos;
 uniform vec3 viewPos;
 uniform vec3 lightColor;
 
 out vec4 fragColor;
 
 const float PI = 3.14159265359;
 
 float distributionGGX(vec3 N, vec3 H, float roughness) {
     float a = roughness * roughness;
     float a2 = a * a;
     float NdotH = max(dot(N, H), 0.0);
     float NdotH2 = NdotH * NdotH;
 
     float nom = a2;
     float denom = (NdotH2 * (a2 - 1.0) + 1.0);
     denom = PI * denom * denom;
 
     return nom / denom;
 }
 
 float geometrySchlickGGX(float NdotV, float roughness) {
     float r = (roughness + 1.0);
     float k = (r * r) / 8.0;
     float nom = NdotV;
     float denom = NdotV * (1.0 - k) + k;
 
     return nom / denom;
 }
 
 float geometrySmith(vec3 N, vec3 V, vec3 L, float roughness) {
     float NdotV = max(dot(N, V), 0.0);
     float NdotL = max(dot(N, L), 0.0);
     float ggx2 = geometrySchlickGGX(NdotV, roughness);
     float ggx1 = geometrySchlickGGX(NdotL, roughness);
 
     return ggx1 * ggx2;
 }
 
 vec3 fresnelSchlick(float cosTheta, vec3 F0) {
     return F0 + (1.0 - F0) * pow(clamp(1.0 - cosTheta, 0.0, 1.0), 5.0);
 }
 
 void main() {
     vec3 albedo = texture(diffuseMap, fragTexCoord).rgb;
     vec3 normal = vec3(1, 1, 1);
     
     if(hasnormalMap)
        normal = texture(normalMap, fragTexCoord).rgb;
     
     normal = normalize(normal * 2.0 - 1.0);
     vec3 N = normalize(fragNormal);
 
     // --- Получаем значения AO / Roughness / Metallic ---
     float ao = 1.0;
     float roughness = 1.0;
     float metallic = 0.0;
 
     if (hasormMap) {
         vec3 orm = texture(ormMap, fragTexCoord).rgb;
         ao = orm.r;
         roughness = orm.g;
         metallic = orm.b;
     } else {
         if (hasaoMap) ao = texture(aoMap, fragTexCoord).r;
         if (hasroughnessMap) roughness = texture(roughnessMap, fragTexCoord).r;
         if (hasmetallicMap) metallic = texture(metallicMap, fragTexCoord).r;
     }
 
     vec3 V = normalize(viewPos - fragPos);
     vec3 L = normalize(lightPos - fragPos);
     vec3 H = normalize(V + L);
 
     vec3 F0 = vec3(0.04);
     F0 = mix(F0, albedo, metallic);
 
     float NDF = distributionGGX(N, H, roughness);
     float G = geometrySmith(N, V, L, roughness);
     vec3 F = fresnelSchlick(max(dot(H, V), 0.0), F0);
 
     vec3 numerator = NDF * G * F;
     float denominator = 4.0 * max(dot(N, V), 0.0) * max(dot(N, L), 0.0) + 0.0001;
     vec3 specular = numerator / denominator;
 
     vec3 kS = F;
     vec3 kD = vec3(1.0) - kS;
     kD *= 1.0 - metallic;
     vec3 diffuse = kD * albedo / PI;
 
     float distance = length(lightPos - fragPos);
     float attenuation = 1.0 / (distance * distance);
     vec3 radiance = lightColor * attenuation;
 
     vec3 Lo = (diffuse + specular) * radiance * max(dot(N, L), 0.0);
 
     vec3 ambient = vec3(0.03) * albedo * ao;
 
     vec3 color = ambient + Lo;
 
     color = color / (color + vec3(1.0));
     color = pow(color, vec3(1.0/2.2));
 
     fragColor = vec4(color, 1.0);
 }
""";

    public ColorModelShader() {
        this(VERTEX, FRAGMENT);
    }


    public ColorModelShader(String vertex, String fragment) {
        super(vertex, fragment,
                DEFAULT_MATRIX | CAMERA_POSITION | GAME_TIME_VANILLA,
                VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);

    }

    public ColorModelShader(String vertexCode, String fragmentCode, ShaderErrorListener shaderErrorListener) {
        super(vertexCode, fragmentCode,
                DEFAULT_MATRIX | CAMERA_POSITION | GAME_TIME_VANILLA,
                VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, shaderErrorListener);
    }

    @Override
    public void registerCustomUniforms() {
        registerUniformOptional("model", new Matrix4f().identity(), ShaderUniformType.MATRIX4);
        registerUniform("boneTransforms", new Matrix4f[64], ShaderUniformType.MATRIX4_ARRAY);

        registerUniform("lightPos", new Vector3f(0f, 10.0f, 0f), ShaderUniformType.VEC3);
        registerUniform("lightColor", new Vector3f(1, 1, 1), ShaderUniformType.VEC3);

        registerUniform("debugLightScale", 2.0f, ShaderUniformType.FLOAT);


        registerUniformOptional(TextureType.DIFFUSE.getShaderId(), null, null, ShaderUniformType.SAMPLER2D_0);
        registerUniformOptional(TextureType.NORMAL.getShaderId(), null, null, ShaderUniformType.SAMPLER2D_1);
        registerUniformOptional(TextureType.METALLIC.getShaderId(), null, null, ShaderUniformType.SAMPLER2D_2);
        registerUniformOptional(TextureType.ROUGHNESS.getShaderId(), null, null, ShaderUniformType.SAMPLER2D_3);
        registerUniformOptional(TextureType.AO.getShaderId(), null, null, ShaderUniformType.SAMPLER2D_4);
        registerUniformOptional(TextureType.ORM.getShaderId(), null, null, ShaderUniformType.SAMPLER2D_5);

        registerUniform("uProgress", 1f, ShaderUniformType.FLOAT);
    }

    public final void setTexture(Object value, TextureType textureType) {
        setUniformValue(textureType.getShaderId(), value);
    }

    public final void setTextures(Pair<Object, TextureType>... textures) {
        for (Pair<Object, TextureType> texture : textures) {
            setUniformValue(texture.getRight().getShaderId(), texture.getLeft());
        }
    }

    public static class Error implements ShaderErrorListener {

        public boolean hasError = false;
        public String message = "";

        @Override
        public void onShaderError(ShaderError shaderError, String shaderErrorInfo) {

            System.out.println("Shader ERROR: " + shaderError.name());

            hasError = true;
            this.message = shaderErrorInfo;
        }
    }
}
