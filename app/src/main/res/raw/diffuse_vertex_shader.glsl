
uniform mat4 u_Matrix;
attribute vec4 a_Position;
attribute vec3 a_Normalize;

varying vec3 v_Normalize;

void main()
{
    gl_Position = u_Matrix * a_Position;
    v_Normalize = a_Normalize;
}