# **Android GL ES**

Android case: draw anything by opengl es on glSurfaceView.

It need to set one renderer which implement GLSurfaceView.Renderer.

Most of out works are in MyRenderer.

MyRenderer has three callback functions. They are onSurfaceCreated, onSurfaceChanged, onDrawFrame separately.

That is android's content above.



### Project 1

#### Description: draw one red triangle

Prepare our vertex shader and fragment shader.

###### Filename: triangle_vertex_shader.glsl

```glsl
attribute vec4 a_Position;

void main()
{
    gl_Position = a_Position;
}
```

###### Filename: triangle_fragment_shader.glsl

```glsl
precision mediump float;

uniform vec4 u_Color;

void main()
{
    gl_FragColor = u_Color;
}
```

As shaders are prepared. Let's complete the remain in java world.

Usually, we set ClearColor in onSurfaceCreated. 

```java
@Override
public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
}
```

Take all screen as our canvas, so set viewport in onSurfaceChanged.

```java
@Override
public void onSurfaceChanged(GL10 gl, int width, int height) {
    GLES20.glViewport(0, 0, width, height);
}
```

Clear our screen when we draw everytime, so clear in onDrawFrame.

```java
@Override
public void onDrawFrame(GL10 gl) {
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
}
```

Next, let's create gl shaders for our shaders, compile them. In onSurfaceCreated.

```java
int vertexShaderId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
// TODO check create success or not
GLES20.glShaderSource(vertexShaderId, verCode);
GLES20.glCompileShader(vertexShaderId);
// TODO check compile success or not
```

The same as fragment shader.

```java
int fragmentShaderId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
// TODO check
GLES20.glShaderSource(fragmentShaderId, fragCode);
GLES20.glCompileShader(fragmentShaderId);
// TODO check
```

And then, if all pass, create program and attach shaders and link it.

```java
mGlProgram = GLES20.glCreateProgram();
// TODO check create success or not
GLES20.glAttachShader(mGlProgram, vertexShaderId);
GLES20.glAttachShader(mGlProgram, fragmentShaderId);
GLES20.glLinkProgram(mGlProgram);
// TODO check link success or not
// TODO validate program success or not
GLES20.glUseProgram(mGlProgram);
```

Now, we can get the variants in shader glsl.

```java
m_aPosition = GLES20.glGetAttribLocation(mGlProgram, A_POSITION);
m_uColor = GLES20.glGetUniformLocation(mGlProgram, U_COLOR);
```

Assign them some values.

```java
private static final int POSITION_COMPONENT_COUNT = 2;
float[] tableVertices = {
    0, 0,
    0, 0.7f,
    0.9f, 0.7f,
};

vertexFloatBuffer = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer()
    .put(tableVertices);
vertexFloatBuffer.position(0);

GLES20.glVertexAttribPointer(m_aPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
    false, 0, vertexFloatBuffer);
GLES20.glEnableVertexAttribArray(m_aPosition);

GLES20.glUniform4f(m_uColor, 1.0f, 0.0f, 0.0f, 1.0f);
```

Final, draw our triangle in onDrawFrame.

```java
GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
```

We get one red triangle as the below image.

<img src="./img/red_triangle.png" style="zoom:30%" />



