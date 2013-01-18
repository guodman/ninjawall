package org.guodman.game.ninjawall

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image

class Player(var position: MapPoint) {
  val image = new Image("/assets/images/ninja-wall.png")
  var goto: MapPoint = null
  val speed: Double = 0.1

  def render(container: GameContainer, g: Graphics) = {
    image.draw(position.xf, position.yf)
  }

  def update(container: GameContainer, delta: Int) = {
    if (goto != null) {
      val next = position.increment(goto, speed * delta)
      position = next
      if (position.distance(goto) <= speed * delta) {
        goto = null
      }
    }
  }

  def move(next: MapPoint) {
    if (next != null) {
      goto = next
    }
  }
}
