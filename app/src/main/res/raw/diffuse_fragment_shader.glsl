precision mediump float;

uniform vec3 u_objectColor;
uniform vec3 u_lightColor;

uniform vec3 u_lightPos;

varying vec3 v_Normalize;
varying vec3 v_FragPos;

void main()
{
    float ambientStrength = 0.2;
    vec3 ambient = ambientStrength * u_lightColor;

    vec3 norm = normalize(v_Normalize);
    vec3 lightDir = normalize(u_lightPos - v_FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * u_lightColor;

    vec3 result = (ambient + diffuse) * u_objectColor;
    gl_FragColor = vec4(result, 1.0);
}