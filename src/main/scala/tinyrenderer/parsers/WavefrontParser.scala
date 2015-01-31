package tinyrenderer.parsers

import tinyrenderer.primitives._

object WavefrontParser {
  import collection.mutable.ArrayBuffer
  import io.Source
  import util.matching.Regex
  
  private val number = """-?\d+(?:\.\d*)?(?:e[+\-]\d+)?"""
  private val vertexMatcher = s"""v ($number) ($number) ($number)""".r

  private val faceVerticle = """(\d+)\/\d*\/\d*"""
  private val faceMatcher = s"""f $faceVerticle $faceVerticle $faceVerticle""".r

  def parse(source: Source): Array[Face] = {
    val vertexes = new ArrayBuffer[Vertex]()
    val faces = new ArrayBuffer[Face]()

    for (line <- source.getLines) line match {
      case vertexMatcher(x, y, z) => vertexes += Vertex(x.toDouble, y.toDouble, z.toDouble)
      case faceMatcher(v1Id, v2Id, v3Id) =>
        faces += Face(vertexes(v1Id.toInt - 1), vertexes(v2Id.toInt - 1), vertexes(v3Id.toInt - 1))
      case _ if line.startsWith("f ") => println(line)
      case _ =>
    }
    
    faces.toArray
  }
}