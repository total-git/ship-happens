package models

import shiphappens.Player._
import shiphappens.Types._
import shiphappens.Types.Orientation._
import shiphappens.Types.Result._
import shiphappens.Board._

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

    PlayGame.game.placeShip(id, _ship.get, coords, orient)
  }

  def getStatus(): String = _ship match {
    case Some(s) => "Ship " + s.length
    case None    => if (_ourTurn) "Player " + id
                      else "Player " + (id+1)%2
  }
}
