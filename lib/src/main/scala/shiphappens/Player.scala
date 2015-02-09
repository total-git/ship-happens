package shiphappens

trait Player {
  // set of signals which must be supported by all Players
  def requestShot(field: Board.EnemyField)
  def requestPlacing(field: Board.PlayerField, ship: Ship)
  // update function (for GUI, etc.)
  def update(ownField: Board.PlayerField, enemyField: Board.EnemyField, move: Move)

}
