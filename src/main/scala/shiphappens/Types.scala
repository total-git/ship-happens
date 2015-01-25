package shiphappens.Types

import scala.Enumeration

object Orientation extends Enumeration {
  type Orientation = Value
  val Horizontal, Vertical = Value
}
import Orientation._

case class Ship(val name: String, val length: Int)

object Result extends Enumeration {
  type Result = Value
  val Miss, Hit, Sunk, AlreadyProbed = Value
}
import Result._

abstract class Move
case class Shot(val target: Coordinates, val result: Result) extends Move
case class Place(val target: Coordinates, val ship: Ship, val orient: Orientation) extends Move

case class Coordinates(val x: Int, val y: Int) {
  def this(s: String) {
    this(s.head - 'A', s.tail.toInt - 1)
  }

  // width and height of board
  def getNeighbours(width: Int, height: Int): List[Coordinates] = {
    val mx = width-1
    val my = height-1
    var list = List[Coordinates]()
    if (x > 0  && y > 0)  list ::= new Coordinates(x-1, y-1)
    if (x > 0)            list ::= new Coordinates(x-1, y  )
    if           (y > 0)  list ::= new Coordinates(x  , y-1)
    if (x > 0  && y < my) list ::= new Coordinates(x-1, y+1)
    if (x < mx && y > 0)  list ::= new Coordinates(x+1, y-1)
    if (x < mx)           list ::= new Coordinates(x+1, y  )
    if           (y < my) list ::= new Coordinates(x  , y+1)
    if (x < mx && y < my) list ::= new Coordinates(x+1, y+1)
    return list
  }


  def +(c: Coordinates) =
    new Coordinates(x + c.x, y + c.y)

  def +(c: (Int,Int)) =
    new Coordinates(x + c._1, y + c._2)

  def ==(c: Coordinates) =
    (x == c.x) && (y == c.y)

  def ==(c: (Int,Int)) =
    (x == c._1) && (y == c._2)

  override def toString(): String =
    (x + 'A').toChar.toString +  (y+1).toString
}

object Coordinates {
  implicit def coords2Tuple(coords: Coordinates): (Int,Int) =
    (coords.x,coords.y)

  implicit def tuple2Coords(t: (Int,Int)): Coordinates =
    Coordinates(t._1, t._2)
}
