#version 120

const float OFFSET = 0.5;
const float PI     = 3.1415926535897932384626433832795;
const float TWO_PI = 6.2831853071795864769252867665590;
const vec3  BASE   = PI * vec3(0., 0.66666666666666, 1.33333333333333);

//uniform float speed;
uniform float scale;
uniform vec2  rotation;
uniform float time; //pre-multiplied by TWO_PI

uniform sampler2D texSampler;

void main() {
    if (gl_Color.a == 0.0)   {
        float pos = (gl_FragCoord.x * rotation.x + gl_FragCoord.y * rotation.y) * scale;
        gl_FragColor = vec4(OFFSET + sin(BASE + pos + time), 1.) * texture2D(texSampler, gl_TexCoord[0].xy);
    } else {
        gl_FragColor = gl_Color * texture2D(texSampler, gl_TexCoord[0].xy);
    }
}
