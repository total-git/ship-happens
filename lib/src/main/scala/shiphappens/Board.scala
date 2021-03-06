package shiphappens

import shiphappens.Orientation._
import shiphappens.Result._

case class ShipEntry(val ship: Ship, val lives: Int,
                     val coords: Coordinates, val orient: Orientation)

object Board {
  type PlayerField = Array[Array[(Boolean, Result)]]
  type EnemyField =  Array[Array[Option[Result]]]

  def printFull(f: PlayerField) : String = {
    var rowCount : Int = 0
    val validChars = ('A' to 'Z')
    var out = "You:\n    "
    // wraparound after z to a
    for(letterIndex <- 0 to f.size-1)
      out += " "+validChars(letterIndex%26)
    out += "\n   +"+"-"*(f.size*2+1)+"+\n"
    for(row <- f) {
      out += "%2d |".format(rowCount+1)
      for(col <- row) {
        col match {
          case (true, Miss) => out += " o"
          case (true, Hit)  => out += " x"
          case (true, Sunk) => out += " X"
          case (false, Hit) => out += " ■"
          case _            => out += "  "
        }
      }
      rowCount+=1
      out += " |\n"
    }
    out += "   +"+"-"*(f.size*2+1)+"+\n"
    out
  }

  def printVisible(f: EnemyField) : String = {
    var rowCount : Int = 0
    val validChars = ('A' to 'Z')
    var out = "Enemy:\n    "
    // wraparound after z to a
    for(letterIndex <- 0 to f.size-1)
      out += (" "+validChars(letterIndex%26))
    out += "\n   +"+"-"*(f.size*2+1)+"+\n"
    for(row <- f) {
      out += "%2d |".format(rowCount+1)
      for(col <- row) {
        col match {
          case Some(Miss) => out += " o"
          case Some(Hit)  => out += " x"
          case Some(Sunk) => out += " X"
          case _          => out += "  "
        }
      }
      rowCount+=1
      out += " |\n"
    }
    out += "   +"+"-"*(f.size*2+1)+"+\n"
    out
  }
}

/**
 * Class that handles all inforation regarding the placement of ships and
 * shots. Doesn't know anything about Playern, just has full and visible as
 * representation functions for the different players.
 * Game is responsible to check that only the correct player is allowed to
 * modify the game state.
 *
 * The whole class is immutable, changing functions return a new game with as
 * much as possible shared between the new and the old game.
 *
 * field is an 2D array with the inner array beeing horizontal
 * ships is a list containing all the ships placed on the Board
 */
case class Board(field: Array[Array[Boolean]],
                 ships: List[ShipEntry]) {
  // constructs an empty field
  def this(width: Int, height: Int) =
    this(Array.fill(height,width)(false), List())
  // constructor with default size
  def this() = this(10,10)

  def height = field.size
  def width = field(0).size

  // returns a representation of the full board to be given to the player
  // owning the board
  def full = { // : Board.PlayerField
    field zip (shipField map {_ map toResult}) map { case (a,b) => a zip b }
  }

  // returns a representation of the board to be given to the enemy player
  def visible : Board.EnemyField = {
    full map (_ map {
      case (true, r) => Some(r);
      case (false, x) => None;
    } )
  }

  // helper functions so the don't have to create bugs by using the wrong
  // index in the array
  def isProbed(c: Coordinates) = field(c.y)(c.x)
  def shipAt(c: Coordinates) : Option[ShipEntry] = {
    shipField(c.y)(c.x)
  }

  // probes the square, i.e. tries to shoot at the given coordinate
  // returns a tuple of the updates board and the result code for that shot
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

    if (ship.get.lives == 1) {// old ship with lives before reduction
      for(tmpShip <- nships) {
        // still at least one ship alive
        if(tmpShip.lives != 0) return (Board(nfield, nships), Sunk)
      }
      // all ships destroyed
      return (Board(nfield, nships), Won)
    }
    else
      return (Board(nfield, nships), Hit)
  }

  // places a ship at the given position with the given orientation.
  // if the placement is valid Some() updated board is returned, otherwise
  // None.
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

  /** internal functions begin here **/

  // this functions builds a 2D array similar to the field array which
  // has on every occupied field a reference to the ship at that place
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
