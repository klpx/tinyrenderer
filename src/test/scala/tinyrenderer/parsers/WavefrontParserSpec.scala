package tinyrenderer.parsers

import org.scalatest._
import tinyrenderer.primitives._
import io.Source

class WavefrontParserSpec extends FlatSpec with Matchers {

  "WavefrontParser" should "understand simple face" in {
    val faces = WavefrontParser.parse(Source.fromString("""
        |v 1 2 3
        |v 4 5 6
        |v 7 8 9
        |f 1// 2// 3//
      |""".stripMargin))
    
    faces should be(Array(
      Face(
        Vertex(1, 2, 3),
        Vertex(4, 5, 6),
        Vertex(7, 8, 9)    
      )    
    ))
  }
  
  it should "understand different notations of doubles" in {
    val faces = WavefrontParser.parse(Source.fromString("""
        |v 1 2.0 0.3
        |v 4e-5 -5 -0.6
        |v -7e+4 -8.0 9
        |f 1// 2// 3//
      |""".stripMargin))
    
    faces should be(Array(
      Face(
        Vertex(1, 2, 0.3),
        Vertex(0.00004, -5, -0.6),
        Vertex(-70000, -8, 9)    
      )    
    ))
  }
}