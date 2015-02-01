package tinyrenderer

import java.awt.Color

class ColorOps(c: Color) {
  def *(alpha: Float) =
    new Color(c.getRed, c.getGreen, c.getBlue, (c.getAlpha * alpha).round)
}

object ColorOps {
  implicit def Color2ColorOps(from: Color) = new ColorOps(from)
}