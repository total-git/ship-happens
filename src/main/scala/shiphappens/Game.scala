package shiphappens.Game

import shiphappens.Board._

import shiphappens.Player._
import shiphappens.Types._

object Game {
  // Just for testing
  def main(args: Array[String]) {
    var b = new Board()
    var p = new HumanPlayer(0)
    p.bomb(b.visible)
    //p.printEnemy(b.visible)
    //p.printOwn(b.full)
    //val s = new Ship("Battleship", 5)
    //p.placeShip(s, b.full)
  }
}
