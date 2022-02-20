import org.khronos.webgl.Float32Array
import org.w3c.dom.HTMLCanvasElement
import vision.gears.webglmath.Mat4
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec4
import kotlin.js.Console
import kotlin.js.Date
import kotlin.properties.Delegates
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell

class Scene (
  val gl : WebGL2RenderingContext){
  var firstDraw = true
  var visible = true
  var solide = true
  var sumTranslation = Vec2(0.0f, 0.0f)
  var previouslyContained = false
  var timeAtLastFrame = Date().getTime()
  val vsIdle = Shader(gl, GL.VERTEX_SHADER, "idle-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val fsGarish = Shader(gl, GL.FRAGMENT_SHADER, "garish-fs.glsl")
  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsFirstTexture = Shader(gl, GL.FRAGMENT_SHADER, "firstTextureTask-fs.glsl")
  val modelMatrix = Mat4(Float32Array( arrayOf<Float>(
          1.0f, 0.0f, 0.0f, 0.1f,
          0.0f, 1.0f, 0.0f, 0.2f,
          0.0f, 0.0f, 1.0f, 0.0f,
          0.0f, 0.0f, 0.0f, 1.0f
  )))
  val solidProgram = Program(gl, vsIdle, fsSolid)
  val garishProgram = Program(gl, vsIdle, fsGarish)
  val firstTextureProgram = Program(gl, vsTrafo, fsFirstTexture)
  val quadGeometry = TexturedQuadGeometry(gl)
  val tirangleGeometry = TexturedTriangleGeometry(gl)
  var asteroidTexture: Texture2D = Texture2D(gl, "media/asteroid.png")

  fun moveTheTriangle(x: Int, y: Int, viewportWidth: Int, viewportHeight: Int){
    var newX: Float = 2.0f*x.toFloat() / viewportWidth.toFloat() - 1.0f
    var newY: Float = 2.0f*y.toFloat() / viewportHeight.toFloat() - 1.0f
    newY = -newY
    reset()
    modelMatrix.translate(Vec2(newX, newY))
    sumTranslation = Vec2(newX, newY)
    uploadModelMatrix()
  }

  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
  }

  fun uploadModelMatrix(){
    gl.uniformMatrix4fv(
            gl.getUniformLocation(
                    firstTextureProgram.glProgram,
                    "gameObject.modelMatrix"),
            false,
            modelMatrix.storage)
  }

  fun setUniformVec4(x: Float, y: Float, z: Float, a: Float){
    gl.uniform4f(
            gl.getUniformLocation(
                    garishProgram.glProgram,
                    "uniformColor"),
            x, y, z, a);
            // ide hozzateszek meg par sor kommentet, hogy lassam mi a helyzet
            val a: Int = 12
    println(a)
  }

  fun reset(){
    modelMatrix.translate(-sumTranslation)
    sumTranslation = Vec2(0.0f, 0.0f)
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>) {
    gl.clearColor(0.0f, 1.0f, 0.0f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    gl.uniform1i(gl.getUniformLocation(
            firstTextureProgram.glProgram, "material.colorTexture"), 0)
    gl.activeTexture(GL.TEXTURE0)
    gl.bindTexture(GL.TEXTURE_2D, asteroidTexture.glTexture)

    // gl.enable(GL.BLEND)
    // gl.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA)

    // quadGeometry.draw()


    val garishProgramUsed = false
    gl.useProgram(firstTextureProgram.glProgram)
    // gl.useProgram(garishProgram.glProgram)


    if (!garishProgramUsed) {
      if (firstDraw) {
        timeAtLastFrame = Date().getTime()
        firstDraw = false;
        gl.uniformMatrix4fv(
                gl.getUniformLocation(
                        firstTextureProgram.glProgram,
                        "gameObject.modelMatrix"),
                false,
                Float32Array(arrayOf<Float>(
                        1.0f, 0.0f, 0.0f, 0.1f,
                        0.0f, 1.0f, 0.0f, 0.2f,
                        0.0f, 0.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 0.0f, 1.0f
                )))
      } else {
        if (keysPressed.contains("M") ||
                keysPressed.contains("UP") ||
                keysPressed.contains("DOWN") ||
                keysPressed.contains("RIGHT") ||
                keysPressed.contains("LEFT")) {
          if (!previouslyContained) {
            timeAtLastFrame = Date().getTime()
          }
          val timeAtThisFrame = Date().getTime()
          val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
          timeAtLastFrame = timeAtThisFrame
          var velocity = Vec2(0.0f, 0.0f)
          if (keysPressed.contains("UP")) {
            velocity = velocity + Vec2(0.0f, 1.0f)
          }
          if (keysPressed.contains("DOWN")) {
            velocity = velocity + Vec2(0.0f, -1.0f)
          }
          if (keysPressed.contains("RIGHT")) {
            velocity = velocity + Vec2(1.0f, 0.0f)
          }
          if (keysPressed.contains("LEFT")) {
            velocity = velocity + Vec2(-1.0f, 0.0f)
          }

          modelMatrix.translate(velocity * dt)
          sumTranslation = sumTranslation + velocity * dt
          // modelMatrix.translate(Vec2(0.1f, 0.1f))
          // nem kell a .gradle es .build folderek
          gl.uniformMatrix4fv(
                  gl.getUniformLocation(
                          firstTextureProgram.glProgram,
                          "gameObject.modelMatrix"),
                  false,
                  modelMatrix.storage)
        }
      }
      if (keysPressed.contains("M") ||
              keysPressed.contains("UP") ||
              keysPressed.contains("DOWN") ||
              keysPressed.contains("RIGHT") ||
              keysPressed.contains("LEFT")) {
        previouslyContained = true
      } else {
        previouslyContained = false
      }
    }
    else{ // ez van ha garishProgramot hasznal az ember
      if (solide){
        setUniformVec4(1.0f, 0.0f, 0.0f, 1.0f)
      }
      else{
        setUniformVec4(0.0f, 0.0f, 1.0f, 1.0f)
      }
    }
    if(visible) {
      tirangleGeometry.draw()
    }
  }
}
