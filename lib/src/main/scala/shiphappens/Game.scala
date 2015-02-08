package shiphappens.Game

import shiphappens.Board._

import shiphappens.Player._
import shiphappens.Types._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._
import shiphappens.Types.Result._


object Game {
  val ships : Array[Ship] = Array(Ship("Battleship", 5))//, Ship("Cruiser", 4), Ship("Cruiser", 4), Ship("Destroyer", 3), Ship("Destroyer", 3), Ship("Destroyer", 3), Ship("Submarine", 2), Ship("Submarine", 2), Ship("Submarine", 2), Ship("Submarine", 2))

  // request a placing from the player until it is valid (not colliding or adjacent to already placed ships)
  def getCorrectPlacing(player: Player, enemyField: Board.EnemyField, ownBoard: Board, ship: Ship) : (Board) = {
    val (tmpCoord: Coordinates, tmpOrient: Orientation) = player.requestPlacing(ownBoard.full, ship)
    val tmpBoard: Option[Board] = ownBoard.setShip(ship, tmpCoord, tmpOrient)
    println(tmpBoard)
    println(tmpCoord, tmpOrient)
    tmpBoard match {
      case None        => return getCorrectPlacing(player, enemyField, ownBoard, ship)
      case Some(b)     => {
        player.update(b.full, enemyField, new Placed(ship, tmpOrient, tmpCoord))
        return b
      }
    }
  }

  // call getCorrectPlacing for all ships
  def placeShips(player: Player, ownBoard: Board, enemyField: Board.EnemyField) : Board= {
    ships.foldLeft(ownBoard)(getCorrectPlacing(player, enemyField, _: Board, _: Ship))
  }

  // lets the player shoot until he misses or enters an invalid coordinate
  def shootLoop(ownPlayer: Player, enemyPlayer: Player, ownBoard: Board, enemyBoard: Board) : Boolean = {
    val coord : Coordinates = ownPlayer.requestShot(enemyBoard.visible)
    val probeResult = enemyBoard.probeSquare(coord)
    // update the player
    probeResult match {
      case (b, result) => {
        ownPlayer.update(ownBoard.full, b.visible, new Shot(ownPlayer, coord, result))
        enemyPlayer.update(b.full, ownBoard.visible, new Shot(ownPlayer, coord, result))
      }
    }
    // re-call the function if a ship was hit, let the other player shoot if not
    probeResult match {
      case (b, Sunk) => return shootLoop(ownPlayer, enemyPlayer, ownBoard, b)
      case (b, Hit) => return shootLoop(ownPlayer, enemyPlayer, ownBoard, b)
      // let the enemy shoot
      case (b, Miss) => return shootLoop(enemyPlayer, ownPlayer, b, ownBoard)
      case (b, Invalid) => return shootLoop(enemyPlayer, ownPlayer, b, ownBoard)
      case (b, AlreadyProbed) => return shootLoop(enemyPlayer, ownPlayer, b, ownBoard)
      case (_, Won) => return true
    }
  }
    

  // Just for testing
  def main(args: Array[String]) {
    var b1 = new Board()
    var b2 = new Board()
    var p1 = new HumanPlayer(1)
    var p2 = new HumanPlayer(2)
    val b1p = placeShips(p1, b1, b2.visible)
    val b2p = placeShips(p2, b2, b1.visible)
    shootLoop(p1, p2, b1p, b2p)
  }
}
