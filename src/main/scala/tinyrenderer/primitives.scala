package tinyrenderer.primitives

case class Vertex(val x: Double, val y: Double, val z: Double, val w: Double = 1.0)

case class TextureVertex(val u: Double, val v: Double, val w: Double = 0.0)

case class ParameterVertex(val u: Double, val v: Double, val w: Double)

case class Face(val v1: Vertex, val v2: Vertex, val v3: Vertex) {
  val edges = Array((v1, v2), (v2, v3), (v3, v1))
}

class Model(val faces: Array[Face])