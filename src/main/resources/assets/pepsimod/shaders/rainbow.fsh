#version 120

const float OFFSET = 0.5;
const vec3  BASE = 3.14159265358979323846 * vec3(0., 0.66666666666666, 1.33333333333333);

uniform float speed = 0.2;
uniform float scale = 0.03;
uniform vec2  rotation = vec2(0.068485271, 0.997652128);
uniform float time;

uniform sampler2D texSampler;

void main() {
    if (gl_Color.a == 0.0)   {
        //gl_FragColor = texture2D(texSampler, gl_TexCoord[0].xy);
        float pos = (gl_FragCoord.x * rotation.x - gl_FragCoord.y * rotation.y) * scale;
        gl_FragColor = vec4(OFFSET + sin(BASE + float(time) / 159.15494309 * speed + pos), 1.) * texture2D(texSampler, gl_TexCoord[0].xy);
    } else {
        gl_FragColor = gl_Color * texture2D(texSampler, gl_TexCoord[0].xy);
    }
}
