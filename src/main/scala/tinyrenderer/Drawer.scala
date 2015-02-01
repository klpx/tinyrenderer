package tinyrenderer

import primitives._
import java.awt.{Graphics, Color, Point}

case class Line(p1: Point, p2: Point) {
    val a = (p2.y - p1.y).toDouble / (p2.x - p1.x)
    val b = p1.y - a * p1.x
    
    def getYByX(x: Int) = (a * x + b).round.toInt
}

class Drawer(val g: Graphics, private val width: Int, private val height: Int, private val bgColor: Color = Color.BLACK) {
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
  
  def drawModel(model: Model, drawType: DrawType, color: Color = Color.RED) {
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
  
  def drawTriangleMesh(p1: Point, p2: Point, p3: Point) {
    for ((_p1, _p2) <- Seq((p1, p2), (p2, p3), (p3, p1))) {
      g.drawLine(_p1.x, _p1.y, _p2.x, _p2.y)
    }
  }
  
  def separateXBaseAndAngle(p1: Point, p2: Point, p3: Point): (Line, (Line, Line)) = {
    val px = Array(p1, p2, p3)
    val baseP1 = px.minBy(_.x)
    val notP1Points = px.filter(_ ne baseP1)
    val baseP2 = notP1Points.maxBy(_.x)
    val angleP = notP1Points.filter(_ ne baseP2)(0)
    
    (Line(baseP1, baseP2), (Line(baseP1, angleP), Line(angleP, baseP2)))
  }
  
  def drawTriangleNormal(p1: Point, p2: Point, p3: Point) {
    val (base, (roof1, roof2)) = separateXBaseAndAngle(p1, p2, p3)
    for (x <- base.p1.x to base.p2.x) {
      val baseY = base getYByX x
      val roof = (if (x <= roof1.p2.x) roof1 else roof2)
      if (roof.p1.x == roof.p2.x) {
        g.drawLine(x, roof.p1.y, x, roof.p2.y)
      } else {
        val roofY = roof getYByX x
        g.drawLine(x, baseY, x, roofY)
      }
    }
  }
}