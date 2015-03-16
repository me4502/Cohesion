#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
			
void main() {
	//if(texture2D(u_texture, v_texCoords).x > 0f && texture2D(u_texture, v_texCoords).x == texture2D(u_texture, v_texCoords).y && texture2D(u_texture, v_texCoords).y == texture2D(u_texture, v_texCoords).z) {
	//	gl_FragColor = vec4(1.0,1.0,1.0,1.0);
	//} else {
		gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	//}
}