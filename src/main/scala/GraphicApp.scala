import javax.swing._
import java.awt._
import tinyrenderer.parsers.WavefrontParser

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
