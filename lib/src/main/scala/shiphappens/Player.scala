package shiphappens.Player

import scala.util.matching.Regex
import scala.io.StdIn._

// import bloat
import shiphappens.Board._
import shiphappens.Board.Board._
import shiphappens.Types._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._
import shiphappens.Types.Result._
import shiphappens.Types.Shot._
import shiphappens.Types.Placed._

trait Player {
  // set of signals which must be supported by all Players
  def requestShot(field: Board.EnemyField)
  def requestPlacing(field: Board.PlayerField, ship: Ship)
  // update function (for GUI, etc.)
  def update(ownField: Board.PlayerField, enemyField: Board.EnemyField, move: Move)

}

case class HumanPlayer(val id: Int)  extends Player {
  override def toString(): String =
    "Player " + id.toString
  def requestShot(field: Board.EnemyField): Coordinates = {
    var buffer : String = ""
    // only checks if the coordinates can be parsed, not if they are inside the boundaries or already bombed
    while(!buffer.matches("\\A[A-Z]\\d+\\s*\\z")) {
      buffer = readLine("Shoot which square? (A to %s, 1 to %d, e.g. A4): ".format((field.size+'A').toChar.toString, field(0).size))
    }
    val coord : Coordinates = new Coordinates(buffer)
    return coord
  }
  def requestPlacing(field: Board.PlayerField, ship: Ship): (Coordinates,Orientation) = {
    var buffer : String = ""
    while(!buffer.matches("\\A[A-Z]\\d+\\s*\\z")) {
      buffer = readLine("Place %s where? Give coordinates of the top left square: ".format(ship))
    }
    val coord : Coordinates = new Coordinates(buffer)
    while(!(buffer.matches("\\A[hv]\\s*\\z"))) {
      buffer = readLine("And the orientation (h|v) for horizontal or vertical: ")
    }
    val orientation = buffer match {
      case "h" => Horizontal
      case "v" => Vertical
      //case _   => Vertical
    }
    return (coord, orientation)
  }

  def update(ownField: Board.PlayerField, enemyField: Board.EnemyField, move: Move) = {
    move match {
      case Shot(player, target, result) => println(Shot(player, target, result))
      case Placed(ship, orient, target) => println(Placed(ship, orient, target))
    }
    println(Board.printFull(ownField))
    println(Board.printVisible(enemyField))
  }

}
