package shiphappens.Player

// import bloat
import shiphappens.Board._
import shiphappens.Board.Board._
import shiphappens.Types._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._
import shiphappens.Types.Player._
import shiphappens.Types.Result._

trait Player {
  def bomb(field: Board.EnemyField): Coordinates
  def placeShip(ship: Ship, field: Board.PlayerField): (Coordinates,Orientation)
  // update function (for GUI, etc.)
  // TODO Should contain some information by whom the last move was.
  def update(ownField: Board.PlayerField, enemyField: Board.EnemyField,
             player: Player, move: Move)

}

case class HumanPlayer(val id: Int)  extends Player {
  def bomb(field: Board.EnemyField): Coordinates = {
    // TODO handle wrong formatting
    print("Shoot which square? (A to %s, 1 to %d, e.g. A4): ".format((field.size+'A').toChar.toString, field(0).size))
    val coord : Coordinates = new Coordinates(readLine())
    return coord
  }
  def placeShip(ship: Ship, field: Board.PlayerField): (Coordinates,Orientation) = {
    ???
  }
  def update(ownField: Board.PlayerField, enemyField: Board.EnemyField,
             player: Player, move: Move) = {
    ???
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
