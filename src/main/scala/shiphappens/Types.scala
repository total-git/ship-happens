package shiphappens.Types

import scala.Enumeration

object Orientation extends Enumeration {
  type Orientation = Value
  val Horizontal, Vertical = Value
}
import Orientation._

case class Ship(val length: Int, val orientation: Orientation)

object Result extends Enumeration {
  type Result = Value
  val Miss, Hit, Sunk = Value
}
import Result._

case class Move(val target: (Int,Int), val result: Result)

case class Coordinates(val x: Int, val y: Int) {
  def this(s: String) {
    this(s.head - 'A', s.charAt(1) - '1')
  }

  implicit def coords2Tuple(coords: Coordinates): (Int,Int) =
    (coords.x,coords.y)

  override def toString(): String =
    (x + 'A').toChar.toString +  (y + '1').toChar.toString
}
