#version 300 es

precision highp float;

uniform struct{
  sampler2D colorTexture;
} material;

in vec2 texColorCoord;
out vec4 fragmentColor; //#vec4# A four-element vector [r,g,b,a].; Alpha is opacity, we set it to 1 for opaque.; It will be useful later for transparency.

void main(void) {
  // fragmentColor = vec4(texColorCoord.x, texColorCoord.y, 0, 1);
  vec4 color = texture(material.colorTexture, texColorCoord);
  fragmentColor = color; //#1, 1, 0, 1# solid yellow
}
