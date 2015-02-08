package shiphappens.Client

import shiphappens.Player.HumanPlayer

object client {
  def main(args: Array[String]) {
    val p = new Player(1)
    val h = new HumanPlayer(1)
    val boards = p.getOwnBoard
    h.printOwn(boards)
//    h.printEnemy(boards._2)
  }
}
