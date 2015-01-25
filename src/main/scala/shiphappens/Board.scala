package shiphappens.Board

import shiphappens.Types._
import shiphappens.Types.Orientation._
import shiphappens.Types.Result._

import scala.collections.immutable.Vector

// (ship, lives of ship)
case class ShipEntry(ship: Ship, val lives: Int)
// (probed by enemy, ship reference if any)
case class FieldEntry(val probed: Boolean, val ship: Option[ShipEntry])

case class Board(field: Array[Array[FieldEntry]],
                 ships: List[ShipEntry]) {
  // constructs an empty field
  def this(width: Int, height: Int) {
    this(Array.ofDim[FieldEntry](width,height), List())
  }

  type VisibleField = Array[Array[Option[Result]]]

  def width = field.size
  def height = field(0).size

  def full = field
  def visible : VisibleField = field.map(_.map(toVisibleField))

  def at(c: Coordinates) : FieldEntry = {
    field(c.x)(c.y)
  }

  def probeSquare(c: Coordinates): (Board,Result) = {
    // first prevent double hits on single field
    if (at(c).probed)
      return (this,AlreadyProbed)
    // replace the modified line
    var nfield = field.clone
    nfield(c.x) = field(c.x).clone
    nfield(c.x)(c.y) = new FieldEntry(true, at(c).ship)

    if (at(c).ship.isEmpty)
      return (Board(nfield,ships), Miss)

    val nships = ships.map( s => 
      if (s == at(c).ship.get)
        new ShipEntry(s.ship, s.lives - 1)
      else
        s
    )
    val ship = at(c).ship.get

    if (ship.lives == 1)
      return (Board(nfield, nships), Sunk)
    else
      return (Board(nfield, nships), Hit)
  }

  def setShip(ship: Ship, coords: Coordinates, orient: Orientation): Option[Board] = {
    val fields = getShipFields(ship, coords, orient)
    // first check placement (no fields out of border)
    if (fields.foldLeft(true)(_ && isValidCoord(_)) == false)
      return None
    // check neighbour fields
    if (fields.foldLeft(true)(_ && isValidShipPlace(_)) == false)
      return None

    // store ship in ship list
    val se = new ShipEntry(ship, ship.length)
    val nships = se :: ships
    var nfield = field.clone
    for (f <- fields) nfield(f.x)(f.y).ship = Some(se)
    return Some(Board(nfield, nships))
  }


  private def toVisibleField(f: FieldEntry): Option[Result] = {
    if (!f.probed)
      None
    else {
      if (f.ship.isEmpty) {
        Some(Miss)
      } else {
        if (f.ship.get.lives == 0)
          Some(Sunk)
        else
          Some(Hit)
      }
    }
  }

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
