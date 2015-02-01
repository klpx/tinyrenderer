package tinyrenderer.primitives

import math._

case class Vertex(val x: Double, val y: Double, val z: Double, val w: Double = 1.0) {
  lazy val length = sqrt(x*x + y*y + z*z)
  
  def -(b: Vertex) = Vertex(x-b.x, y-b.y, z-b.z)
  
  def ^(b: Vertex) = Vertex(y*b.z - z*b.y, z*b.x - x*b.z, x*b.y - y*b.x)

  def *(b: Vertex) = x*b.x + y*b.y + z*b.z
  
  def normalized = Vertex(x/length, y/length, z/length)
}

case class TextureVertex(val u: Double, val v: Double, val w: Double = 0.0)

case class ParameterVertex(val u: Double, val v: Double, val w: Double)

case class Face(val v1: Vertex, val v2: Vertex, val v3: Vertex)

class Model(val faces: Array[Face])