#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

//declare uniforms
uniform float resolution;
uniform float radius;
uniform vec2 dir;
uniform float diffuse;

void addPoint(inout vec4 sum, vec4 color);

void main() {
	//this will be our RGBA sum
	vec4 sum = vec4(0.0);
	
	//our original texcoord for this fragment
	vec2 tc = v_texCoords;
	
	//the amount to blur, i.e. how far off center to sample from 
	//1.0 -> blur by one pixel
	//2.0 -> blur by two pixels, etc.
	float blur = radius/resolution; 
    
    //the direction of our blur
    //(1.0, 0.0) -> x-axis blur
    //(0.0, 1.0) -> y-axis blur
	float hstep = dir.x;
	float vstep = dir.y;
	
    //apply blurring, using a 9-tap filter with predefined gaussian weights
    addPoint(sum, texture2D(u_texture, vec2(tc.x - 4.0*(blur)*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162);
	addPoint(sum, texture2D(u_texture, vec2(tc.x - 3.0*(blur)*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541);
	addPoint(sum, texture2D(u_texture, vec2(tc.x - 2.0*(blur)*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216);
	addPoint(sum, texture2D(u_texture, vec2(tc.x - 1.0*(blur)*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946);
	
	addPoint(sum, texture2D(u_texture, vec2(tc.x, tc.y)) * 0.2270270270);
	
	addPoint(sum, texture2D(u_texture, vec2(tc.x + 1.0*(blur)*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946);
	addPoint(sum, texture2D(u_texture, vec2(tc.x + 2.0*(blur)*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216);
	addPoint(sum, texture2D(u_texture, vec2(tc.x + 3.0*(blur)*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541);
	addPoint(sum, texture2D(u_texture, vec2(tc.x + 4.0*(blur)*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162);
	
	sum.a = sum.a * 2.0;

	gl_FragColor = v_color * sum * vec4(diffuse);
}

#define color_limit 0.0015

void addPoint(inout vec4 sum, vec4 color) {
    sum += step(vec4(color_limit), color) * color;
}
