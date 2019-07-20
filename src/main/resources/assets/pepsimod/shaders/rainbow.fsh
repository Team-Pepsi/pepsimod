#version 110

const float OFFSET = 0.5;
const float PI = 3.14159265358979323846;
const float SPEED = 159.15494309;
const vec3 BASE = PI * vec3(0., 0.66666666666666, 1.33333333333333);

//settings index 0: speed
//settings index 1: scale
//settings index 2: rotationX
//settings index 3: rotationY
//settings index 4: time
uniform float settings[5];
//uniform int time;

uniform sampler2D texSampler;

void main() {
    if (gl_Color.a == 0.0)   {
        gl_FragColor = vec4(1., 0., 0., 1.);
    } else if (gl_Color.a < 0.3)   {
        gl_FragColor = texture2D(0, gl_TexCoord[0].st);
    } else if (gl_Color.a < 0.6)   {
        float pos = (gl_FragCoord.x * settings[2] - gl_FragCoord.y * settings[3]) * settings[1];
        float scaledTime = settings[4];//float(time) * (0.001 / SPEED) * settings[0];
        gl_FragColor = vec4(OFFSET + sin(BASE + scaledTime + pos), 1.);
    } else {
        gl_FragColor = gl_Color;
    }
}
