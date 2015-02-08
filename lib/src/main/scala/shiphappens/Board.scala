package shiphappens.Board

import shiphappens.Types._
import shiphappens.Types.Orientation._
import shiphappens.Types.Result._

case class ShipEntry(val ship: Ship, val lives: Int,
                     val coords: Coordinates, val orient: Orientation)

object Board {
  type PlayerField = Array[Array[(Boolean, Result)]]
  type EnemyField =  Array[Array[Option[Result]]]
}

// field is an 2D array with the inner array beeing horizontal
case class Board(field: Array[Array[Boolean]],
                 ships: List[ShipEntry]) {
  // constructs an empty field
  def this(width: Int, height: Int) =
    this(Array.fill(height,width)(false), List())
  // constructor with default size
  def this() = this(10,10)

  def height = field.size
  def width = field(0).size

  def full = { // : Board.PlayerField
    field zip (shipField map {_ map toResult}) map { case (a,b) => a zip b }
  }

  def visible : Board.EnemyField = {
    full map (_ map {
      case (true, r) => Some(r);
      case (false, x) => None;
    } )
  }

  def isProbed(c: Coordinates) = field(c.y)(c.x)
  def shipAt(c: Coordinates) : Option[ShipEntry] = {
    shipField(c.y)(c.x)
  }

  def probeSquare(c: Coordinates): (Board,Result) = {
    // check if coordinates are valid, otherwise move is wasted
    if (!isValidCoord(c))
      return (this,Invalid)
    // first prevent double hits on single field
    if (isProbed(c))
      return (this,AlreadyProbed)

    // replace the modified line
    var nfield = field.clone
    nfield(c.y) = field(c.y).clone
    nfield(c.y)(c.x) = true

    val ship = shipAt(c)

    if (ship == None)
      return (Board(nfield,ships), Miss)

    val nships = ships.map( s =>
      if (s == ship.get)
        new ShipEntry(s.ship, s.lives - 1, s.coords, s.orient)
      else
        s
    )

    if (ship.get.lives == 1) // old ship with lives before reduction
      return (Board(nfield, nships), Sunk)
    else
      return (Board(nfield, nships), Hit)
  }

  def setShip(ship: Ship, coords: Coordinates, orient: Orientation): Option[Board] = {
    val entry = new ShipEntry(ship, ship.length, coords, orient)

    val fields = getShipFields(entry)
    // first check placement (no fields out of border)
    if (fields.foldLeft(true)(_ && isValidCoord(_)) == false)
      return None
    // check neighbour fields
    if (fields.foldLeft(true)(_ && isValidShipPlace(_)) == false)
      return None

    // store ship in ship list
    val nships = entry :: ships
    return Some(Board(field, nships))
  }

  private def shipField : Array[Array[Option[ShipEntry]]] = {
    // build a list of tuples of coordinates and the corresponding ships
    val sfs : List[(Coordinates, ShipEntry)]
      = ships.map(getShipFields).zip(ships) // List[(List[Coordinates],Ships)]
          .flatMap { case (fs,s) => fs zip List.fill(fs.length)(s) }
    val sf : Array[Array[Option[ShipEntry]]] = Array.fill(width,height)(None)
    sf.map(_.zipWithIndex).zipWithIndex
      // first add the coordinates to every entry
      .map{ case (a,x) => a map { case (e,y) => (e,(x,y)) } } // A[A[(Opt[Ship],(Int,Int))]]
      // if the coordinates are in the ship list, add the ship, else None
      .map(_.map({ case (e,c) => sfs.find{ _._1 == c } map (_._2) })).transpose
  }

  private def toResult(ship: Option[ShipEntry]) : Result = ship match {
    case Some(s) => if (s.lives == 0) Sunk else Hit;
    case None    => Miss
  }

  private def isValidShipPlace(coords: Coordinates): Boolean = {
    (coords :: coords.getNeighbours(width,height))
      .map(shipAt(_).isEmpty)
      .foldLeft(true)(_ && _)
  }

  private def getShipFields(s: ShipEntry) = {
    if (s.orient == Horizontal)
      for (i <- 0 to s.ship.length-1) yield (s.coords + (i,0))
    else // vertical
      for (i <- 0 to s.ship.length-1) yield (s.coords + (0,i))
  }

  private def isValidCoord(c: Coordinates): Boolean = {
    if (c.x < 0 || c.y < 0) return false
    if (c.x >= width || c.y >= height) return false
    return true
  }
}
