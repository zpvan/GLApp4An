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

struct Light {
    vec3 direction;
    vec3 position;
    float cutOff;
    // 外椎角
    float outerCutOff;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform Light u_Light;

void main() {

    // 环境光
    vec3 ambient = u_Light.ambient * vec3(texture2D(u_Material.diffuse, v_TextureCoords));

    // 漫反射
    vec3 norm = normalize(v_Normalize);
    vec3 lightDir = normalize(u_Light.position - v_FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = u_Light.diffuse * (diff * vec3(texture2D(u_Material.diffuse, v_TextureCoords)));

    // 镜面高光
    float specularStrength = 0.5;
    vec3 viewDir = normalize(u_ViewPos - v_FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), u_Material.shininess);
    vec3 specular = u_Light.specular * (spec * vec3(texture2D(u_Material.specular, v_TextureCoords)));

    // 计算片元角度的cos值
    float theta = dot(lightDir, normalize(-u_Light.direction));
    // 计算epsilon的值，用内锥角的cos值减去外锥角的cos值
    float epsilon = u_Light.cutOff - u_Light.outerCutOff;
    // 根据公式计算光照强度，并限制结果的范围
    float intensity = clamp((theta - u_Light.outerCutOff) / epsilon, 0.0, 1.0);

    diffuse *= intensity;
    specular *= intensity;

    if (theta > u_Light.cutOff) {
        // 在照射范围内
        vec3 result = (ambient + diffuse + specular);
        gl_FragColor = vec4(result, 1.0);
    } else {
        // gl_FragColor = vec4(u_Light.ambient * vec3(texture2D(u_Material.diffuse, v_TextureCoords)), 1.0);
    }
}