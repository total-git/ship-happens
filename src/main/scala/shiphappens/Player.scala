package shiphappens.Player

// import bloat
import shiphappens.Board._
import shiphappens.Board.Board._
import shiphappens.Types._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._
import shiphappens.Types.Player._

trait Player {
  def bomb(field: Board.EnemyField): Coordinates
  def placeShip(ship: Ship, field: Board.PlayerField): (Coordinates,Orientation)
  // update function (for GUI, etc.)
  // TODO Should contain some information by whom the last move was.
  def update(ownField: Board.PlayerField, enemyField: Board.EnemyField,
             player: Player, move: Move)
}
