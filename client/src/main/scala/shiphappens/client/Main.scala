package shiphappens.Client

import scala.util.matching.Regex

object client {

  def main(args: Array[String]) {
    val id : Int = readLine("Enter player id (1 or 2): ").toInt
    val p = new Player(id)

    while(p.checkStatus) {
      Thread sleep(500)
    }
  }
}
