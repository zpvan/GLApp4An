precision mediump float;

uniform vec3 u_objectColor;
uniform vec3 u_lightColor;

varying vec3 v_Normalize;

void main()
{
     float ambientStrength = 0.2;
     vec3 ambient = ambientStrength * u_lightColor;
     vec3 result = ambient * u_objectColor;
     gl_FragColor = vec4(result, 1.0);
}