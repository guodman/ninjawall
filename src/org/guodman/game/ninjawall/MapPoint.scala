package org.guodman.game.ninjawall

class MapPoint(val x: Double, val y: Double) {
  def xf: Float = return x.toFloat
  def yf: Float = return y.toFloat

  def asString: String = "x:" + x + " y:" + y

  def increment(other: MapPoint, length: Double, fullLength: Boolean = false): MapPoint = {
    val dir = this.direction(other)
    if (distance(other) > length || fullLength) {
      return new MapPoint(x - length * math.sin(dir),
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
  def centerRadians(rad: Double): Double = {
    // r is not final because there's not much point in making it final
    // the whole point of this is to modify r
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
