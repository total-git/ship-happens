package shiphappens.Types

import scala.Enumeration

object Orientation extends Enumeration {
  type Orientation = Value
  val Horizontal, Vertical = Value
}
import Orientation._

case class Ship(val length: Int)

object Result extends Enumeration {
  type Result = Value
  val Miss, Hit, Sunk = Value
}
import Result._

case class Move(val target: (Int,Int), val result: Result)

case class Coordinates(val x: Int, val y: Int) {
  def this(s: String) {
    this(s.head - 'A', s.tail.toInt - 1)
  }

  // mx: maxInXDirection my: maxInYDirection
  def getNeighbours(mxx: Int, myy: Int): List[Coordinates] = {
    val mx = mxx-1
    val my = myy-1
    var list = List[Coordinates]()
    if (x > 0  && y > 0)  list ::= new Coordinates(y-1, x-1)
    if (x > 0)            list ::= new Coordinates(y  , x-1)
    if           (y > 0)  list ::= new Coordinates(y-1, x  )
    if (x > 0  && y < my) list ::= new Coordinates(y+1, x-1)
    if (x < mx && y > 0)  list ::= new Coordinates(y-1, x+1)
    if (x < mx)           list ::= new Coordinates(y  , x+1)
    if           (y < my) list ::= new Coordinates(y+1, x  )
    if (x < mx && y < my) list ::= new Coordinates(y+x, x+1)
    return list
  }

  implicit def coords2Tuple(coords: Coordinates): (Int,Int) =
    (coords.x,coords.y)

  override def toString(): String =
    (x + 'A').toChar.toString +  (y+1).toString
}
