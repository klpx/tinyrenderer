package tinyrenderer

import primitives._
import java.awt.{Graphics, Color, Point}

class Drawer(private val g: Graphics, private val width: Int, private val height: Int, private val bgColor: Color = Color.BLACK) {
  object DrawType extends Enumeration {
    type DrawType = Value
    val MESH, NORMAL = Value
  }
  
  import DrawType._
  
  g.setColor(bgColor)
  g.fillRect(0, 0, width-1, height-1)
  
  val halfWidth: Double = width / 2.0
  val halfHeight: Double = height / 2.0
    
  def normalizeX(x: Double): Int = ((x+1) * halfWidth).toInt
  def normalizeY(y: Double): Int = height - ((y+1) * halfHeight).toInt
  
  def drawMesh(model: Model, drawType: DrawType, color: Color = Color.RED) {
    val triangleDrawer: (Point, Point, Point) => Unit = drawType match {
      case DrawType.MESH => drawTriangleMesh
      case DrawType.NORMAL => drawTriangleNormal
    }
    g.setColor(color)
    for (face <- model.faces) {
      val p1 = new Point(normalizeX(face.v1.x), normalizeY(face.v1.y))
      val p2 = new Point(normalizeX(face.v2.x), normalizeY(face.v2.y))
      val p3 = new Point(normalizeX(face.v3.x), normalizeY(face.v3.y))
      triangleDrawer(p1, p2, p3)
    }
  }
  
  private def drawTriangleMesh(p1: Point, p2: Point, p3: Point) {
    for ((_p1, _p2) <- Seq((p1, p2), (p2, p3), (p3, p1))) {
      g.drawLine(_p1.x, _p1.y, _p2.x, _p2.y)
    }
  }
  
  private def drawTriangleNormal(p1: Point, p2: Point, p3: Point) {
    g.setColor(new Color((math.random*255).toInt, (math.random*255).toInt, (math.random*255).toInt))
    val ax = Array(p1.x, p2.x, p3.x)
    val ay = Array(p1.y, p2.y, p3.y)
    g.drawRect(ax.min, ay.min, ax.max-ax.min, ay.max-ay.min)
    
  }
}