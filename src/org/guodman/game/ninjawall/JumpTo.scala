package org.guodman.game.ninjawall

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image

class JumpTo {
  var position: MapPoint = null
  val image = new Image("/assets/images/jump-to.png")

  def render(container: GameContainer, g: Graphics) = {
    if (position != null) {
      image.draw(position.xf - image.getWidth / 2, position.yf - image.getHeight / 2)
    }
  }
}
