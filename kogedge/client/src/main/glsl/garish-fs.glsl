#version 300 es

precision highp float;

uniform vec4 uniformColor;

out vec4 fragmentColor; //#vec4# A four-element vector [r,g,b,a].; Alpha is opacity, we set it to 1 for opaque.; It will be useful later for transparency.

void main(void) {
  fragmentColor = uniformColor; //#1, 1, 0, 1# solid yellow
}
