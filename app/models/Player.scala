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

  def own = _own
  def enemy = _enemy
  def moves = _moves

  override def requestShot(field: Board.EnemyField): Coordinates = ???
  override def requestPlacing(field: Board.PlayerField, ship: Ship)
    : (Coordinates,Orientation) = ???
  override def update(ownField: Board.PlayerField, enemyField: Board.EnemyField,
             move: Move) = {
    _own = ownField
    _enemy = enemyField
    _moves ::= move
  }
}
