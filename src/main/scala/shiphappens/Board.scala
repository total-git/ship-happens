package shiphappens.Board

import shiphappens.Types._

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

  def setShip(ship: Ship, coords: Coordinates, orient: Orientation): Boolean = false

  def getFullBoard(): Field = field
  def getVisibleBoard() : Field = field
}
