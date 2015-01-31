import javax.swing._
import java.awt._

object GraphicApp extends App {
	SwingUtilities.invokeLater(new Runnable() {
			def run() {
				val frame = new JFrame("Swing Paint Demo")
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
				frame.add(new Viewport)
				frame.pack()
				frame.setVisible(true)
			}
		})
}

class Viewport extends JPanel {
  private val _width = 400
  private val _height = 400
  
	override def getPreferredSize = new Dimension(_width, _height)

	override def paintComponent(g: Graphics) {
		super.paintComponent(g)
		// g.drawPoint(new Point(50, 50), RED)
    
    def normalizeX(x: Double): Int = ((x+1) * _width / 2).toInt
    def normalizeY(y: Double): Int = _height - ((y+1) * _height / 2).toInt
    
    val faces = WavefrontParser.parse(io.Source.fromFile("african_head.obj"))
    g.drawString(s"drawing ${faces.length} faces", 10, 20)
    for(face <- faces) {
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

class Vertex(val x: Double, val y: Double, val z: Double, val w: Double = 1.0)

class TextureVertex(val u: Double, val v: Double, val w: Double = 0.0)

class ParameterVertex(val u: Double, val v: Double, val w: Double)

class Face(val v1: Vertex, val v2: Vertex, val v3: Vertex) {
  val edges = Array((v1, v2), (v2, v3), (v3, v1))
}

object WavefrontParser {
	import collection.mutable.ArrayBuffer
	import io.Source
  import scala.util.parsing.combinator._
  

//  object Parsers extends RegexParsers {
//    def doubleNumber: Parser[Double] = """\d+(\.\d*)?""".r ^^ { _.toDouble } 
//    val vertex: Parser[Vertex] = ("v " ~ doubleNumber ~ " " ~ doubleNumber ~ " " ~ doubleNumber) ^^ {
//      case "v " ~ x ~ " " ~ y ~ " " ~ z => new Vertex(x, y, z)
//    }
//  }
	private val number = """-?\d+(?:\.\d*)?(?:e[+\-]\d+)?"""
	private val vertexMatcher = s"""v ($number) ($number) ($number)""".r

	private val faceVerticle = """(\d+)\/\d+\/\d+"""
	private val faceMatcher = s"""f $faceVerticle $faceVerticle $faceVerticle""".r

	def parse(source: Source): Array[Face] = {
		val vertexes = new ArrayBuffer[Vertex]()
    val faces = new ArrayBuffer[Face]()
    
    println(vertexMatcher)

		for (line <- source.getLines) line match {
			case vertexMatcher(x, y, z) => vertexes += new Vertex(x.toDouble, y.toDouble, z.toDouble)
      case faceMatcher(v1Id, v2Id, v3Id) =>
        faces += new Face(vertexes(v1Id.toInt - 1), vertexes(v2Id.toInt - 1), vertexes(v3Id.toInt - 1))
			case _ if line.startsWith("f ") => println(line)
      case _ =>
		}
    println(vertexes.length)
    
    faces.toArray
	}
}