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
	override def getPreferredSize = new Dimension(320, 240)

	override def paintComponent(g: Graphics) {
		super.paintComponent(g)
		g.drawString("here I am", 10, 20)
		// g.drawPoint(new Point(50, 50), RED)
	}
}

class Vertex(val x: Double, val y: Double, val z: Double, val w: Double = 1.0)

class TextureVertex(val u: Double, val v: Double, val w: Double = 0.0)

class ParameterVertex(val u: Double, val v: Double, val w: Double)

class Face(val v1: Vertex, val v2: Vertex, val v3: Vertex)

object WavefrontParser {
	import collection.mutable.ArrayBuffer
	import io.Source
	import util.matching.Regex

	val number = """\d+(\.\d*)?"""
	val vertexMatcher = s"""v ($number) ($number) ($number)""".r

	val faceVerticle = """(d+)/(d+)/(d+)"""
	val faceMatcher = """f ($faceVerticle) ($faceVerticle) ($faceVerticle)""".r

	implicit private def string2double(s: String) = s.toDouble

	def parse(source: Source) {
		val vertexes = new ArrayBuffer[Vertex]()

		for (line <- source.getLines) line match {
			case vertexMatcher(x, y, z) => vertexes += new Vertex(x, y, z)
			case _ => ()
		}
	}
}