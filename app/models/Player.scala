package models

import shiphappens._
import shiphappens.Orientation._
import shiphappens.PlayerId._
import shiphappens.Result._

class PlayPlayer(val id: Int) extends Player {
  private var _own: Board.PlayerField = Array.fill(10,10)((false,Miss))
  private var _enemy: Board.EnemyField = Array.fill(10,10)(None)
  private var _moves: List[Move] = List()
  private var _ourTurn: Boolean = false
  private var _ship: Option[Ship] = None

  def own = _own
  def enemy = _enemy
  def moves = _moves

  override def requestShot(field: Board.EnemyField) = {
    _enemy = field
    _ourTurn = true
  }

  override def requestPlacing(field: Board.PlayerField, ship: Ship) = {
    _own = field
    _ship = Some(ship)
  }

  override def update(ownField: Board.PlayerField,
                      enemyField: Board.EnemyField,
                      move: Move) = {
    _own = ownField
    _enemy = enemyField
    _moves ::= move
  }

  def placeShip(coords: Coordinates, orient: Orientation): Boolean = {
    if (_ship.isEmpty)
      return false

    // we can't reset the ship to None after placeShip, because a callback of
    // placeShip already sets _ship to the next ship. But if it fails we need
    // to restore the original ship, because we still have to place it
    val oldShip = _ship.get
    _ship = None
    PlayGame.game.placeShip(id, oldShip, coords, orient) match {
      case true => return true
      case false => _ship = Some(oldShip); return false
    }
  }

  def shoot(coords: Coordinates): Boolean = {
    PlayGame.game.makeMove(id, coords) match {
      case true  => _ourTurn = false; return true
      case false => return false
    }
  }

  def getStatus(): String = {
    if (!_ship.isEmpty)
      return _ship.get.toString

    // check if game is over
    if (!moves.isEmpty) {
      checkForEndOfGame(moves.head) match {
        case Some(Self)  => return "Won"
        case Some(Enemy) => return "Lost"
        case _           => ()
      }
    }

    // otherwise we are currently playing
    if (_ourTurn)
      "Player " + id
    else
      "Player " + (if (id == 1) 2 else 1)
  }

  private def checkForEndOfGame(m: Move) = m match {
    case Shot(p, _, Won) => Some(p)
    case e               => None
  }
}
