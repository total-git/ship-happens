package shiphappens.Board

import shiphappens.Types._
import shiphappens.Types.Orientation._

import scala.Array

case class Board(val width: Int, val height: Int) {
  // (ship, lives of ship)
  case class ShipEntry(ship: Ship, var lives: Int)
  // (probed by enemy, ship reference if any)
  case class FieldEntry(var probed: Boolean, var ship: Option[ShipEntry])
  type Field = Array[Array[FieldEntry]]

  private var field: Field = Array.ofDim[FieldEntry](width,height)
  private var ships: List[ShipEntry] = List()

  def probeSquare(coords: Coordinates): Result.Result = {
    val fe = field(coords.x)(coords.y)

    if (fe.ship.isEmpty)
      fe.probed = true
      return Result.Miss

    if (fe.probed)
      return Result.Miss

    fe.probed = true

    val ship = fe.ship.get

    ship.lives = ship.lives - 1
    if (ship.lives == 0)
      return Result.Sunk
    else
      return Result.Hit
  }

  def setShip(ship: Ship, coords: Coordinates, orient: Orientation): Boolean = {
    val fields = getShipFields(ship, coords, orient)
    // first check placement (no fields out of border)
    if (fields.foldLeft(true)(_ && isValidCoord(_)) == false)
      return false
    // check neighbour fields
    if (fields.foldLeft(true)(_ && isValidShipPlace(_)) == false)
      return false

    // store ship in ship list
    val se = new ShipEntry(ship, ship.length)
    ships ::= se
    for (f <- fields) field(f.x)(f.y).ship = Some(se)
    return true
  }

  def getFullBoard(): Field = field
  def getVisibleBoard() : Field = field

  private def isValidShipPlace(coords: Coordinates): Boolean = {
    (coords :: coords.getNeighbours(width,height))
      .map(c => field(c.x)(c.y).ship.isEmpty)
      .foldLeft(true)(_ && _)
  }

  private def getShipFields(ship: Ship, c: Coordinates, o: Orientation) = {
    if (o == Orientation.Horizontal)
      for (i <- 0 to ship.length-1) yield new Coordinates(c.x+i, c.y)
    else // vertical
      for (i <- 0 to ship.length-1) yield new Coordinates(c.x, c.y+i)
  }

  private def isValidCoord(c: Coordinates): Boolean = {
    if (c.x < 0 || c.y < 0) return false
    if (c.x >= width || c.y >= height) return false
    return true
  }
}
