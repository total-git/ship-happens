package shiphappens

import shiphappens._
import shiphappens.Result._
import shiphappens.PlayerId.{Self,Enemy}
import shiphappens.Orientation._

case class PlayerStruct(var player: Player,
                        var board: Board,
                        var ships: List[Ship])

object Game {
  // List of ships available for every player
  val startShips : List[Ship]
    = List(Ship("Battleship", 5),
           Ship("Cruiser", 4),
           Ship("Cruiser", 4),
           Ship("Destroyer", 3),
           Ship("Destroyer", 3),
           Ship("Destroyer", 3),
           Ship("Submarine", 2),
           Ship("Submarine", 2),
           Ship("Submarine", 2),
           Ship("Submarine", 2))
}

/**
 * Game wraps the controller of a ship-happens game. For every player it
 * owns a reference to that player, a Board and a List of ships to place.
 *
 * Players can play by using the functions placeShip and makeMove. They get
 * notified about updates via the functions defined in the Player trait.
 */
class Game(players: (Player, Player)) {
  private var _players: (PlayerStruct,PlayerStruct)
    = (PlayerStruct(players._1, new Board(), Game.startShips),
       PlayerStruct(players._2, new Board(), Game.startShips))
  // initialize with 0 so nobody can shoot until placing is done
  private var next: Int = 0

  // as last part of initialization tell both players to place their first
  // ships
  get(1).player.requestPlacing(get(1).board.full, get(1).ships.head)
  get(2).player.requestPlacing(get(2).board.full, get(2).ships.head)

  private def get(id: Int) = id match {
    case 1 => _players._1
    case 2 => _players._2
  }

  private def enemy(id: Int) = id match {
    case 1 => _players._2
    case 2 => _players._1
  }

  // places a ship at the given position
  def placeShip(id: Int, ship: Ship, coords: Coordinates,
                orient: Orientation): Boolean = {
    if (id < 1 || id > 2)
      return false

    var p = get(id)
    if (p.ships.isEmpty)
      return false

    if (ship == p.ships.head) {
      p.board.setShip(ship, coords, orient) match {
        case None    => return false;
        case Some(b) => {
          // valid move, update board & remove ship from List
          p.board = b
          p.ships = p.ships.tail
          p.player.update(p.board.full, enemy(id).board.visible,
                          Placed(ship, orient, coords))
          if (!p.ships.isEmpty) {
            p.player.requestPlacing(p.board.full, p.ships.head)
          } else if (enemy(id).ships.isEmpty) {
            // all ships are placed now notify player 1 to make the first move
            next = 1
            get(1).player.requestShot(get(2).board.visible)
          }
          return true
        }
      }
    } else {
      // ship is not first in list, player tries to set something else
      return false
    }
  }

  def makeMove(id: Int, coords: Coordinates): Boolean = {
    // make sure it's the players turn
    if (id != next || id < 1 || id > 2)
      return false

    val p = get(id)
    val e = enemy(id)
    val (b,r) = e.board.probeSquare(coords)
    e.board = b
    p.player.update(p.board.full, e.board.visible,
                    Shot(Self, coords, r))
    e.player.update(e.board.full, p.board.visible,
                    Shot(Enemy, coords, r))

    // if the game is not over notify the other player that it's his turn
    if (r == Won) {
      // we set this to 0, meaning nobody is allowed to make moves
      next = 0
    } else if (r == Hit || r == Sunk) {
      // all moves that allow an extra shot
      p.player.requestShot(e.board.visible)
    } else {
      // all moves that pass the right to shoot to the other player
      e.player.requestShot(p.board.visible)
      next = if (next == 1) 2 else 1
    }
    return true
  }

}
