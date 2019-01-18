precision mediump float;

uniform vec3 u_objectColor;
uniform vec3 u_lightColor;

void main()
{
     float ambientStrength = 0.2;
     vec3 ambient = ambientStrength * u_lightColor;
     vec3 result = ambient * u_objectColor;
     gl_FragColor = vec4(result, 1.0);
}