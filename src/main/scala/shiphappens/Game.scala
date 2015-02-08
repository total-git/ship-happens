package shiphappens.Game

import shiphappens.Board._

import shiphappens.Player._
import shiphappens.Types._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._


object Game {
  val ships : Array[Ship] = Array(Ship("Battleship", 5), Ship("Cruiser", 4), Ship("Cruiser", 4), Ship("Destroyer", 3), Ship("Destroyer", 3), Ship("Destroyer", 3), Ship("Submarine", 2), Ship("Submarine", 2), Ship("Submarine", 2), Ship("Submarine", 2))

  // Just for testing
  def main(args: Array[String]) {
    var b1 = new Board()
    var b2 = new Board()
    var p1 = new HumanPlayer(1)
    var p2 = new HumanPlayer(2)



  }
}
