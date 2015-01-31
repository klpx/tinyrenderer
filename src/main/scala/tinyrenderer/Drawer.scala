package tinyrenderer

import primitives._
import java.awt.{Graphics, Color}

class Drawer(private val g: Graphics, private val width: Int, private val height: Int, private val bgColor: Color = Color.BLACK) {
  
  g.setColor(bgColor)
  g.fillRect(0, 0, width-1, height-1)
  
  val halfWidth: Double = width / 2.0
  val halfHeight: Double = height / 2.0
    
  def normalizeX(x: Double): Int = ((x+1) * halfWidth).toInt
  def normalizeY(y: Double): Int = height - ((y+1) * halfHeight).toInt
  
  def drawMesh(model: Model, color: Color = Color.RED) {
    g.setColor(color)
    for (face <- model.faces) {
      for (edge <- face.edges) {
        val x0: Int = normalizeX(edge._1.x)
        val y0: Int = normalizeY(edge._1.y)
        val x1: Int = normalizeX(edge._2.x)
        val y1: Int = normalizeY(edge._2.y)
        g.drawLine(x0, y0, x1, y1)
      }
    }
  }
}