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
  private val _width = 600
  private val _height = 600
  
	override def getPreferredSize = new Dimension(_width, _height)

	override def paintComponent(g: Graphics) {
		super.paintComponent(g)
    
    val drawer = new Drawer(g, _width, _height)
    
    val parseStartTime = System.currentTimeMillis
    val faces = WavefrontParser.parse(io.Source.fromFile("african_head.obj"))
    val drawStartTime = System.currentTimeMillis
    drawer.drawModel(new Model(faces), drawer.DrawType.NORMAL, Color.GRAY)
    val finished = System.currentTimeMillis
    g.setColor(Color.GRAY)
    g.drawString(s"parsed by ${drawStartTime-parseStartTime}ms", 10, 20)
    g.drawString(s"${faces.length} faces", 10, 40)
    g.drawString(s"rendered by ${finished-drawStartTime}ms", 10, 60)
	} 
}
