package shiphappens.Player

import scala.util.matching.Regex
import scala.io.StdIn._

// import bloat
import shiphappens.Board._
import shiphappens.Board.Board._
import shiphappens.Types._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._
import shiphappens.Types.Player._
import shiphappens.Types.Result._
import shiphappens.Types.Shot._
import shiphappens.Types.Placed._

trait Player {
  def requestShot(field: Board.EnemyField): Coordinates
  def requestPlacing(field: Board.PlayerField, ship: Ship): (Coordinates,Orientation)
  // update function (for GUI, etc.)
  def update(ownField: Board.PlayerField, enemyField: Board.EnemyField, move: Move)

}

case class HumanPlayer(val id: Int)  extends Player {
  def requestShot(field: Board.EnemyField): Coordinates = {
    var buffer : String = ""
    // only checks if the coordinates can be parsed, not if they are inside the boundaries or already bombed
    while(!buffer.matches("\\A[A-Z]\\d+\\s*\\z")) {
      buffer = readLine("Shoot which square? (A to %s, 1 to %d, e.g. A4): ".format((field.size+'A').toChar.toString, field(0).size))
    }
    val coord : Coordinates = new Coordinates(buffer)
    return coord
  }
  def requestPlacing(ship: Ship, field: Board.PlayerField): (Coordinates,Orientation) = {
    var buffer : String = ""
    while(!buffer.matches("\\A[A-Z]\\d+\\s*\\z")) {
      buffer = readLine("Place %s (%d long) where? Give coordinates of the top left square: ".format(ship.name,ship.length))
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
    // TODO: notification for wrong coordinates when placing ship
    move match {
      case Shot(player, target, result) => print("%s shot at %s. Result = %s".format(player, target, result))
      case Placed(target, ship, orient) => print("Placed %s at %s with %s orientation".format(ship, target, orient))
    }
    printOwn(ownField)
    printEnemy(enemyField)
  }

  def printEnemy(f: Board.EnemyField) {
    var rowCount : Int = 0
    val validChars = ('A' to 'Z')
    print("Enemy: \n    ")
    // wraparound after z to a
    for(letterIndex <- 0 to f.size-1) print(" "+validChars(letterIndex%26))
    println("\n   +"+"-"*(f.size*2+1)+"+")
    for(row <- f) {
      print("%2d |".format(rowCount+1))
      for(col <- row) {
        col match {
          case Some(Miss) => print(" o")
          case Some(Hit)  => print(" x")
          case Some(Sunk) => print(" X")
          case _          => print("  ")
        }
      }
      rowCount+=1
      println(" |")
    }
    println("   +"+"-"*(f.size*2+1)+"+\n")
  }

  // TODO maybe reuse some code
  def printOwn(f: Board.PlayerField) {
    var rowCount : Int = 0
    val validChars = ('A' to 'Z')
    print("You: \n    ")
    // wraparound after z to a
    for(letterIndex <- 0 to f.size-1) print(" "+validChars(letterIndex%26))
    println("\n   +"+"-"*(f.size*2+1)+"+")
    for(row <- f) {
      print("%2d |".format(rowCount+1))
      for(col <- row) {
        col match {
          case (true, Miss) => print(" o")
          case (true, Hit)  => print(" x")
          case (true, Sunk) => print(" X")
          case _            => print("  ")
        }
      }
      rowCount+=1
      println(" |")
    }
    println("   +"+"-"*(f.size*2+1)+"+\n")
  }

}
