package org.guodman.game.ninjawall

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics

class Line(private var _start: MapPoint, private var _direction: MapPoint, private var length: Double) {
  private var _end = start.increment(direction, length)

  def render(container: GameContainer, g: Graphics): Unit = {
    g.drawLine(start.xf, start.yf, end.xf, end.yf)
  }

  def recalculate: Unit = {
    _end = start.increment(direction, length, true)
  }

  def start_=(s: MapPoint): Unit = {
    _start = s
    recalculate
  }

  def start: MapPoint = _start

  def direction_=(d: MapPoint): Unit = {
    _direction = d
    recalculate
  }

  def direction: MapPoint = _direction

  def end_=(e: MapPoint): Unit = {
    _direction = e
    length = start.distance(_direction)
    recalculate
  }

  def end: MapPoint = _end

  def slope: Double = (start.y - end.y) / (start.x - end.x) //y/x

  def intercept: Double = start.y - slope * start.x
}
