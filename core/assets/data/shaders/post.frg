#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

#define PI 3.14159265
#define TWOPI PI*PI
#define one vec3(1.0)

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float elapsedTime;
uniform int slowed;

#define distortion -0.6

float rand(vec2 co) {
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec2 tex = v_texCoords;
    vec2 cc = tex - vec2(0.5);
    float dist = dot(cc, cc) * distortion;

    if (slowed == 1) {
        tex = tex + cc * (1.0 - dist) * dist;
    }

	gl_FragColor = v_color * texture2D(u_texture, tex);
		
	if(gl_FragColor.x > 0.0) gl_FragColor.x *= 5.0;
    if(gl_FragColor.y > 0.0) gl_FragColor.y *= 5.0;
    if(gl_FragColor.z > 0.0) gl_FragColor.z *= 5.0;

    if(all(lessThan(gl_FragColor.xyz, vec3(0.99)))) {

        vec2 uv = -v_texCoords;

        vec2 p0 = uv - vec2(0.5 + 0.5 * sin(1.4 * TWOPI * uv.x + 2.8 * elapsedTime), 0.5);

        vec3 wave = vec3(0.5 * (cos(sqrt(dot(p0, p0)) * 5.6) + 1.0), cos(4.62 * dot(uv, uv) + elapsedTime), cos(distance(uv, vec2(1.6 * cos(elapsedTime * 2.0), 1.0 * sin(elapsedTime * 1.7))) * 1.3));

        float color = dot(wave, one) / 16.6;

        vec4 generated = vec4(0.1);
        generated.r = 0.5 * (sin(TWOPI * color + elapsedTime * 3.45) + 1.0);
        generated.g = 0.5 * (sin(TWOPI * color + elapsedTime * 3.15) + 1.0);
        generated.b = 0.4 * (sin(TWOPI * color + elapsedTime * 1.26) + 1.0);

        if (slowed == 1) {
            generated /= 2.5;
        } else {
            generated /= 5.0;
        }

        gl_FragColor += generated;
        gl_FragColor = clamp(gl_FragColor, 0., 1.);
    }

    if (slowed == 1) {
        gl_FragColor = vec4(gl_FragColor.xyz * 1.0 - 0.14 * rand(vec2(elapsedTime, tan(elapsedTime))), 1.0);
    }
}
