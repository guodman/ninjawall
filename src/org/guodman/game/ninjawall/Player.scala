package org.guodman.game.ninjawall

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image
import org.guodman.game.ninjawall.MapPoint.WallDirection._

class Player(var position: MapPoint) {
  val imageCeiling = new Image("/assets/images/ninja-ceiling.png")
  val imageFloor = new Image("/assets/images/ninja-floor.png")
  val imageLeftWall = new Image("/assets/images/ninja-wall-left.png")
  val imageRightWall = new Image("/assets/images/ninja-wall-right.png")
  val imageAir = new Image("/assets/images/ninja-air.png")
  var goto: MapPoint = null
  val speed: Double = 0.5

  def isMoving: Boolean = {
    return goto != null
  }

  def render(container: GameContainer, g: Graphics) = {
    import MapPoint.WallDirection._
    position.touchesWall match {
      case Above => imageCeiling.draw(position.xf - imageCeiling.getWidth / 2, position.yf)
      case Below => imageFloor.draw(position.xf - imageFloor.getWidth / 2, position.yf - imageFloor.getHeight)
      case Left => imageLeftWall.draw(position.xf, position.yf - imageLeftWall.getHeight / 2)
      case Right => imageRightWall.draw(position.xf - imageRightWall.getWidth, position.yf - imageRightWall.getHeight / 2)
      case _ => imageAir.draw(position.xf - imageAir.getWidth / 2, position.yf - imageAir.getHeight / 2)
    }
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
    if (next != null && !isMoving) {
      goto = next
    }
  }
}
