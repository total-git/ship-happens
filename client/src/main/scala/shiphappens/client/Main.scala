package shiphappens.Client

import shiphappens.Client._
import scala.util.matching.Regex
import shiphappens.Types._
import shiphappens.Types.Ship._
import shiphappens.Types.Orientation._
import shiphappens.Types.Coordinates._

object client {
  def main(args: Array[String]) {
    val p = new Player(1)
    val response : String = p.getBoards()
    if(response.matches("\\APlayer\\s*\\d.*")) {
      val coord = p.shoot()
    }
    if(response.matches("\\A\\w*\\s*\\(\\d\\).*")) {
      var name = ""
      var num  = 0
      "\\w*".r findFirstIn response match {
        case Some(s) => name = s
        case _       =>
      }
      "\\d".r findFirstIn response match {
        case Some(s) => num = s.toInt
        case _       =>
      }
      val ship : Ship = new Ship(name, num)
      val (coord,orient) : (Coordinates,Orientation) = p.place(ship)
    }
  }
}
