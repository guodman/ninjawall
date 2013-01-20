package org.guodman.game.ninjawall

import org.newdawn.slick.tiled.TiledMap

class MapPoint(val map: TiledMap, val x: Double, val y: Double) {
  def xf: Float = return x.toFloat
  def yf: Float = return y.toFloat

  def asString: String = "x:" + x + " y:" + y

  def touchesWall: MapPoint.WallDirection = {
    import MapPoint.WallDirection._
    val tileHeight: Int = map.getTileHeight
    val tileWidth: Int = map.getTileWidth
    val layer: Int = 0
    if (map.getTileId(((x) / tileWidth).toInt, ((y - tileHeight / 2) / tileHeight).toInt, layer) != 0 &&
        map.getTileId(((x - 1) / tileWidth).toInt, ((y - tileHeight / 2) / tileHeight).toInt, layer) != 0 &&
        map.getTileId(((x + 1) / tileWidth).toInt, ((y - tileHeight / 2) / tileHeight).toInt, layer) != 0) {
      return Above
    } else if (map.getTileId(((x) / tileWidth).toInt, ((y + tileHeight / 2) / tileHeight).toInt, layer) != 0 &&
        map.getTileId(((x - 1) / tileWidth).toInt, ((y + tileHeight / 2) / tileHeight).toInt, layer) != 0 &&
        map.getTileId(((x + 1) / tileWidth).toInt, ((y + tileHeight / 2) / tileHeight).toInt, layer) != 0) {
      return Below
    } else if (map.getTileId(((x - tileWidth / 2) / tileWidth).toInt, ((y) / tileHeight).toInt, layer) != 0) {
      return Left
    } else if (map.getTileId(((x + tileWidth / 2) / tileWidth).toInt, ((y) / tileHeight).toInt, layer) != 0) {
      return Right
    } else if (map.getTileId(((x) / tileWidth).toInt, ((y - tileHeight / 2) / tileHeight).toInt, layer) != 0) {
      return Above
    } else if (map.getTileId(((x) / tileWidth).toInt, ((y + tileHeight / 2) / tileHeight).toInt, layer) != 0) {
      return Below
    } else {
      return No
    }
  }

  def increment(other: MapPoint, length: Double, fullLength: Boolean = false): MapPoint = {
    val dir = this.direction(other)
    if (distance(other) > length || fullLength) {
      return new MapPoint(map, x - length * math.sin(dir),
        y + length * math.cos(dir))
    } else {
      return other
    }
  }

  def distance(other: MapPoint): Double = {
    return math.sqrt(math.pow(x - other.x, 2.0) + math.pow(y - other.y, 2.0))
  }

  def direction(other: MapPoint): Double = {
    var direction: Double = 0;
    if (x == other.x) {
      if (y > other.y) {
        // go left
        direction = math.Pi * 3f / 4f;
      } else if (y < other.y) {
        // go right
        direction = math.Pi / 4f;
      }
    } else if (y == other.y) {
      if (x > other.x) {
        // go up
        direction = math.Pi;
      } else if (x < other.x) {
        // go down
        direction = 0
      }
    } else if (y < other.y) {
      direction = -math.atan((x - other.x).toDouble
        / (y - other.y).toDouble)
    } else if (y > other.y) {
      direction = math.Pi - math.atan((x - other.x).toDouble
        / (y - other.y).toDouble)
    }
    direction = MapPoint.centerRadians(direction)
    return direction;
  }
}

object MapPoint {
  sealed trait WallDirection {}
  object WallDirection {
    case object Above extends WallDirection
    case object Below extends WallDirection
    case object Left extends WallDirection
    case object Right extends WallDirection
    case object No extends WallDirection
  }

  def centerRadians(rad: Double): Double = {
    var r = rad
    while (r > math.Pi) {
      r -= math.Pi * 2
    }
    while (r <= -math.Pi) {
      r += math.Pi * 2
    }
    return r
  }
}
