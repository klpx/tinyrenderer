import javax.swing._
import tinyrenderer.Drawer
import tinyrenderer.primitives.Model
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
    
    val drawer = new Drawer(g, _width, _height)
    
    val faces = WavefrontParser.parse(io.Source.fromFile("african_head.obj"))
    g.drawString(s"drawing ${faces.length} faces", 10, 20)
    drawer.drawMesh(new Model(faces), drawer.DrawType.NORMAL, Color.RED)
	} 
}
