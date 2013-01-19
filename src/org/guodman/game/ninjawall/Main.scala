package org.guodman.game.ninjawall

import org.newdawn.slick.AppGameContainer
import org.newdawn.slick.BasicGame
import org.newdawn.slick.Graphics
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Input
import org.newdawn.slick.SlickException
import org.newdawn.slick.tiled.TiledMap
import org.newdawn.slick.Color

object Main {
  val WIDTH = 1024
  val HEIGHT = 768

  def main(args: Array[String]) {
    val container: AppGameContainer = new AppGameContainer(
      new Main(), WIDTH, HEIGHT, false);
    container.start()
  }
}

class Main extends BasicGame("Ninja Wall") {
  var quit = false
  var info: GameInfo = null

  def init(container: GameContainer) = {
    container.setAlwaysRender(true)
    startGame() // throws SlickException
  }

  def startGame() = {
    info = new GameInfo
  }

  def render(container: GameContainer, g: Graphics) = {
    info.map.render(0, 0)
    info.player.render(container, g)
    info.move.render(container, g)
    //val intersect = findIntersect
    // println("x:" + intersect.x + " y:" + intersect.y)
    g.setColor(Color.red)
    //if (intersect != null) {
    //g.drawLine(intersect.xf, intersect.yf, intersect.xf + 1, intersect.yf + 1)
    //}
    g.setColor(Color.white)

    g.drawString("Last Click: " + info.clickX + "x" + info.clickY, 10, 25)
    g.drawString("Mouse Position: " + info.mouseX + "x" + info.mouseY, 10, 40)
  }

  def update(container: GameContainer, delta: Int) = {
    if (quit) {
      container.exit();
    }

    if (!info.paused) {
      info.player.update(container, delta)
      info.move.start = info.player.position
      info.move.direction = new MapPoint(info.mouseX, info.mouseY)
    }
  }

  override def keyPressed(key: Int, c: Char) = {
    // System.out.println("Someone pressed " + key);

    key match {
      case Input.KEY_ESCAPE =>
        quit = true;
      case Input.KEY_P =>
        info.paused = !info.paused
      case Input.KEY_SPACE =>
        try {
          startGame();
        } catch {
          case e: SlickException =>
            e.printStackTrace();
        }
      case _ =>
        println("unmapped key: " + c + " (" + key + ")")
    }
  }

  override def mousePressed(button: Int, x: Int, y: Int): Unit = {
    info.clickX = x
    info.clickY = y
    info.player.move(findIntersect)
  }

  override def mouseMoved(oldx: Int, oldy: Int, x: Int, y: Int): Unit = {
    info.mouseX = x
    info.mouseY = y
  }

  def findIntersect: MapPoint = {
    val layer = 0
    val s: Double = 0
    val i: Double = 60
    val x: Double = (i - info.move.intercept) / (info.move.slope - s)
    val y: Double = info.move.slope * x + info.move.intercept

    // determine if we are trying to move into a block
    var direction = if (info.move.start.x < info.move.end.x) { 1 } else { -1 }
    println(direction)
    val check: Boolean = try {
      val px: Int = (info.move.start.x + direction).toInt
      val py: Int = (info.move.slope * px + info.move.intercept).toInt
      info.map.getTileId(px / 32, py / 32, layer) != 0
    } catch {
      case e: Exception => false
    }
    if (check) {
      return null
    }

    var horizontals = (0 until (info.map.getHeight * info.map.getTileHeight) by info.map.getTileHeight).filter(j => {
      j > math.min(info.move.start.y, info.move.end.y) &&
        j < math.max(info.move.start.y, info.move.end.y)
    })
    if (info.move.start.y > info.move.end.y) {
      horizontals = horizontals.reverse
    }
    horizontals = horizontals.filter(i => {
      val px: Int = ((i - info.move.intercept) / info.move.slope).toInt
      val on = try {
        info.map.getTileId(px / 32, i / 32, layer) != 0
      } catch {
        case e: Exception => false
      }
      val adjacent = try {
        info.map.getTileId(px / 32, (i / 32) - 1, layer) != 0
      } catch {
        case e: Exception => false
      }
      on || adjacent
    })
    var horizontalPoint: MapPoint = null
    if (horizontals.length > 0) {
      horizontalPoint = new MapPoint((horizontals(0) - info.move.intercept) / info.move.slope, horizontals(0))
    }

    var verticals = (0 until (info.map.getWidth * info.map.getTileWidth) by info.map.getTileWidth).filter(j => {
      j > math.min(info.move.start.x, info.move.end.x) &&
        j < math.max(info.move.start.x, info.move.end.x)
    })
    if (info.move.start.x > info.move.end.x) {
      verticals = verticals.reverse
    }
    verticals = verticals.filter(i => {
      val py: Int = (i * info.move.slope + info.move.intercept).toInt
      val on = try {
        info.map.getTileId(i / 32, py / 32, layer) != 0
      } catch {
        case e: Exception => false
      }
      val adjacent = try {
        info.map.getTileId((i / 32) - 1, py / 32, layer) != 0
      } catch {
        case e: Exception => false
      }
      on || adjacent
    })
    var verticalPoint: MapPoint = null
    if (verticals.length > 0) {
      verticalPoint = new MapPoint(verticals(0), verticals(0) * info.move.slope + info.move.intercept)
    }

    if (horizontalPoint != null && verticalPoint != null) {
      val hdist = info.player.position.distance(horizontalPoint)
      val vdist = info.player.position.distance(verticalPoint)
      if (hdist < vdist) {
        return horizontalPoint
      } else {
        return verticalPoint
      }
    } else if (horizontalPoint != null && verticalPoint == null) {
      return horizontalPoint
    } else if (horizontalPoint == null && verticalPoint != null) {
      return verticalPoint
    } else {
      return null
    }
  }
}

class GameInfo {
  var paused = false
  var clickX = 0
  var clickY = 0
  var mouseX = 0
  var mouseY = 0
  var map: TiledMap = new TiledMap("/assets/maps/simple.tmx")
  val startX = map.getMapProperty("startX", "0").toInt
  val startY = map.getMapProperty("startY", "0").toInt
  var player = new Player(new MapPoint(startX, startY))
  var move: Line = new Line(player.position, player.position, 300)
}
