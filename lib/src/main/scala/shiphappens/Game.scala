package shiphappens.Game

import shiphappens.Board._

import shiphappens.Player._
import shiphappens.Types._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._


object Game {
  val ships : Array[Ship] = Array(Ship("Battleship", 5), Ship("Cruiser", 4), Ship("Cruiser", 4), Ship("Destroyer", 3), Ship("Destroyer", 3), Ship("Destroyer", 3), Ship("Submarine", 2), Ship("Submarine", 2), Ship("Submarine", 2), Ship("Submarine", 2))

  def getCorrectPlacing(player: Player, enemyField: Board.EnemyField, board1: Board, ship: Ship) : (Board) = {
    val (tmpCoord: Coordinates, tmpOrient: Orientation) = player.requestPlacing(board1.full, ship)
    val tmpBoard: Option[Board] = board1.setShip(ship, tmpCoord, tmpOrient)
    println(tmpBoard)
    println(tmpCoord, tmpOrient)
    tmpBoard match {
      case None        => return getCorrectPlacing(player, enemyField, board1, ship)
      case Some(b)     => {
        player.update(b.full, enemyField, new Placed(ship, tmpOrient, tmpCoord))
        return b
      }
    }
  }

  def placeShips(player: Player, board1: Board, enemyField: Board.EnemyField) = {
    ships.foldLeft(board1)(getCorrectPlacing(player, enemyField, _: Board, _: Ship))
  }

  // Just for testing
  def main(args: Array[String]) {
    var b1 = new Board()
    var b2 = new Board()
    var p1 = new HumanPlayer(1)
    var p2 = new HumanPlayer(2)
    placeShips(p1, b1, b2.visible)
    placeShips(p2, b2, b1.visible)
  }
}
