
uniform mat4 u_Matrix;
attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates1;
attribute vec2 a_TextureCoordinates2;

varying vec2 v_TextureCoordinates1;
varying vec2 v_TextureCoordinates2;

void main()
{
    v_TextureCoordinates1 = a_TextureCoordinates1;
    v_TextureCoordinates2 = a_TextureCoordinates2;
    gl_Position = u_Matrix * a_Position;
}