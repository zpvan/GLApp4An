
precision mediump float;

uniform sampler2D u_TextureUnit1;
uniform sampler2D u_TextureUnit2;

varying vec2 v_TextureCoordinates;

void main()
{
    vec4 tex1 = texture2D(u_TextureUnit1, v_TextureCoordinates);
    vec4 tex2 = texture2D(u_TextureUnit2, v_TextureCoordinates);
    gl_FragColor = tex1 * tex2;
}