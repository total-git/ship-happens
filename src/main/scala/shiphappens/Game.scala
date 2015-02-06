package shiphappens.Game

import shiphappens.Board._

import shiphappens.Player._

object Game {
  // Just for testing
  def main(args: Array[String]) {
    var b = new Board()
    var p = new HumanPlayer(0)
    //p.bomb(b.visible)
    p.printEnemy(b.visible)
    p.printOwn(b.full)
  }
}
