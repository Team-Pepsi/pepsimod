#version 110

void main(){
    //gl_Normal = gl_NormalMatrix * gl_Normal;
    //gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_Position = ftransform();
    gl_FrontColor = gl_Color;
    gl_BackColor = gl_Color;
}
