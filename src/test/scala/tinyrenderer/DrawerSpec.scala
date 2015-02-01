package tinyrenderer

import org.scalatest._
import io.Source
import java.awt._
import java.awt.image.BufferedImage


class DrawerSpec extends FlatSpec with Matchers {
  val B = Color.BLACK
  val R = Color.RED
  
  def loadTriangleBitmap(testName: String): BufferedImage = {
    val extracted = getClass.getResource("/triangle/" + testName + ".png")
    javax.imageio.ImageIO.read(extracted)
  }
  
  val testBitmaps = new collection.mutable.HashMap[String, BufferedImage]()
  for (testName <- Array("filled-sw-45", "filled-se-45", "filled-a", "mesh-sw-45", "mesh-se-45", "mesh-a")) {
    testBitmaps(testName) = loadTriangleBitmap(testName)
  }
  
  
  def performDrawing(w: Int, h: Int, proc: ((Drawer) => Unit)) = {
    val resultBitmap = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    val graphics = resultBitmap.createGraphics()
    proc(new Drawer(graphics, w, h))
    val tmpFile = java.io.File.createTempFile("bitmap", ".png")
    javax.imageio.ImageIO.write(resultBitmap, "png", tmpFile)
    new BufferedImageOps(tmpFile.getName, resultBitmap)
  }
  
  def testMeshTriangle(expectedName: String, p1: Point, p2: Point, p3: Point) {
    val resultBitmap = performDrawing(100, 100, (drawer) => {
        drawer.g.setColor(Color.RED)
        drawer.drawTriangleMesh(p1, p2, p3)
      })
      resultBitmap should be(testBitmaps(expectedName))
  }
  
  
  def testFilledTriangle(expectedName: String, p1: Point, p2: Point, p3: Point) {
    val resultBitmap = performDrawing(100, 100, (drawer) => {
        drawer.g.setColor(Color.RED)
        drawer.drawTriangleNormal(p1, p2, p3)
      })
      resultBitmap should be(testBitmaps(expectedName))
  }
  
  implicit def tuple2awtPoint(t: (Int, Int)): Point = new Point(t._1, t._2)
  
  "Drawer mesh" should "draw enclosed triangles" in {
    testMeshTriangle("mesh-sw-45", (0, 0), (0, 99), (99, 99))
    testMeshTriangle("mesh-se-45", (99, 0), (0, 99), (99, 99))
    testMeshTriangle("mesh-a", (20, 76), (24, 3), (86, 84))
  }
  
  it should "not depends on point pass order" in {
    val p1: Point = (20, 76)
    val p2: Point = (24, 3)
    val p3: Point = (86, 84)
    
    testMeshTriangle("mesh-a", p1, p2, p3)
    testMeshTriangle("mesh-a", p1, p3, p2)
    testMeshTriangle("mesh-a", p2, p1, p3)
    testMeshTriangle("mesh-a", p2, p3, p1)
    testMeshTriangle("mesh-a", p3, p1, p2)
    testMeshTriangle("mesh-a", p3, p2, p1)
  }
  
  "Drawer normal" should "draw filled triangles" in {
    testFilledTriangle("filled-sw-45", (0, 0), (0, 99), (99, 99))
    testFilledTriangle("filled-se-45", (99, 0), (0, 99), (99, 99))
    testFilledTriangle("filled-a", (20, 76), (24, 3), (86, 84))
  }
}

class BufferedImageOps(val filename: String, val image: BufferedImage) {
  override def equals(o: Any): Boolean = o match {
    case image2: BufferedImage => {
      if (image.getWidth == image2.getWidth && image.getHeight == image2.getHeight) {
        for(x <- 0 until image.getWidth; y <- 0 until image.getHeight) {
          if (image.getRGB(x, y) != image2.getRGB(x, y)) {
            println(s"$x $y")
            return false
          }
        }
        true
      } else false
    }
    case _ => false
  }
  
  override def toString = "BufferedImage(" + filename + ")"
}