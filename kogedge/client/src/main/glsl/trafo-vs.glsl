#version 300 es

uniform struct{
  mat4 modelMatrix;
} gameObject;

in vec4 vertexPosition; //#vec4# A four-element vector [x,y,z,w].; We leave z and w alone.; They will be useful later for 3D graphics and transformations. #vertexPosition# attribute fetched from vertex buffer according to input layout spec
in vec2 vertexTexCoord;
out vec2 texColorCoord;

void main(void) {
  vec4 kimenetiVector = vertexPosition;
  texColorCoord = vertexTexCoord;
  gl_Position = kimenetiVector*gameObject.modelMatrix; //#gl_Position# built-in output, required
}