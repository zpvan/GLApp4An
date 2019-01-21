
uniform mat4 u_Model;
uniform mat4 u_View;
uniform mat4 u_Projection;
uniform mat4 u_NormalizeModel;

attribute vec4 a_Position;
attribute vec3 a_Normalize;

varying vec3 v_Normalize;
varying vec3 v_FragPos;

void main()
{
    gl_Position = u_Projection * u_View * u_Model * a_Position;
    v_FragPos = vec3(u_Model * a_Position);
    v_Normalize = a_Normalize;
    // v_Normalize = vec3(u_Model * vec4(a_Normalize, 1.0));
    // v_Normalize = vec3(u_NormalizeModel * vec4(a_Normalize, 1.0));
    // v_Normalize = mat3(transpose(inverse(u_Model)) * a_Normalize);
}