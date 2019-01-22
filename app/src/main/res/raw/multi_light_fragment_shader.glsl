precision mediump float;

uniform vec3 u_objectColor;
uniform vec3 u_lightColor;

uniform vec3 u_lightPos;
uniform vec3 u_ViewPos;

varying vec3 v_Normalize;
varying vec3 v_FragPos;
varying vec2 v_TextureCoords;

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
};

uniform Material u_Material;

// 方向光
struct DirLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform DirLight u_DirLight;

// 计算方向光的影响
vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir) {
    vec3 lightDir = normalize(-light.direction);

    // 环境光
    vec3 ambient = light.ambient * vec3(texture2D(u_Material.diffuse, v_TextureCoords));

    // 漫反射
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * vec3(texture2D(u_Material.diffuse, v_TextureCoords));

    // 镜面高光
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), u_Material.shininess);
    vec3 specular = light.specular * spec * vec3(texture2D(u_Material.specular, v_TextureCoords));

    return ambient + diffuse + specular;
}

// 点光源
struct PointLight {
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
#define NR_POINT_LIGHTS 4
uniform PointLight u_PointLights[NR_POINT_LIGHTS];

// 计算点光源的影响
vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    // 环境光
    vec3 ambient = light.ambient * vec3(texture2D(u_Material.diffuse, v_TextureCoords));

    // 漫反射光
    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(light.position - v_FragPos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * vec3(texture2D(u_Material.specular, v_TextureCoords));

    // 衰减
    float distance = length(light.position - v_FragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    return ambient + diffuse + specular;
}

// 聚光灯
struct SpotLight {
    vec3 direction;
    vec3 position;
    float cutOff;
    // 外椎角
    float outerCutOff;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};
uniform SpotLight u_SpotLight;

// 计算聚光灯的影响
vec3 CalcPointLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    // 环境光
    vec3 ambient = light.ambient * vec3(texture2D(u_Material.diffuse, v_TextureCoords));

    // 漫反射光
    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(light.position - fragPos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * vec3(texture2D(u_Material.diffuse, v_TextureCoords));

    // 镜面高光
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), u_Material.shininess);
    vec3 specular = light.specular * spec * vec3(texture2D(u_Material.specular, v_TextureCoords));

    // 聚光灯
    // 计算片元角度的cos值
    float theta = dot(lightDir, normalize(-light.direction));
    // 计算epsilon的值, 用内椎角的cos值减去外椎角的cos值
    float epsilon = light.cutOff - light.outerCutOff;
    // 根据公式计算光照强度, 并限制结果的范围
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);

    diffuse *= intensity;
    specular *= intensity;

    // 衰减
    float distance = length(light.position - v_FragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    return ambient + diffuse + specular;
}

void main() {
    vec3 norm = normalize(v_Normalize);
    vec3 viewDir = normalize(u_ViewPos - v_FragPos);

    // 方向光
    vec3 result = CalcDirLight(u_DirLight, norm, viewDir);

    // 点光源
    for (int i = 0; i < NR_POINT_LIGHTS; ++i)
        result += CalcPointLight(u_PointLights[i], norm, v_FragPos, viewDir);

    // 聚光灯
    result += CalcSpotLight(u_SpotLight, norm, v_FragPos, viewDir);

    gl_FragColor = vec4(result, 1.0);
}