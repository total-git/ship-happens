package shiphappens.Client

import shiphappens.Client._
import scala.util.matching.Regex
import shiphappens.Types._
import shiphappens.Types.Ship._
import shiphappens.Types.Orientation._
import shiphappens.Types.Coordinates._

object client {

  def main(args: Array[String]) {
    val id : Int = readLine("Enter player id (1 or 2): ").toInt
    val p = new Player(id)

    while(p.checkStatus) {
      Thread sleep(2000)
    }
  }
}
