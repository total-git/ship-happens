package shiphappens.Client

import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.response.Response
import uk.co.bigbeeconsultants.http.request.RequestBody
import java.net.URL

import shiphappens.Board._
import shiphappens.Types._
import shiphappens.Types.Result._
import shiphappens.Types.Coordinates._
import shiphappens.Types.Orientation._

class Player(val id: Int) {
  private val client = new HttpClient
  var state : String = ""

  def getBoards(): String = {
    val response = client.get(new URL("http://localhost:9000/api/get/" + id.toString))
    return response.body.asString
  }

  def getStatus(): String = {
    val response = client.get(new URL("http://localhost:9000/api/status/" + id.toString))
    return response.body.asString

  }

  def checkStatus() {
    val boards : String = getBoards()
    val status : String = getStatus()
    if(status.matches("\\APlayer\\s*\\d.*")) {
      val coord = RequestBody(Map("coord" -> shoot().toString))
      client.post(new URL("http://localhost:9000/api/shoot/" + id.toString), Some(coord))
    }
    if(status.matches("\\A\\w*\\s*\\(\\d\\).*")) {
      var name = ""
      var num  = 0
      "\\w*".r findFirstIn status match {
        case Some(s) => name = s
        case _       =>
      }
      "\\d".r findFirstIn status match {
        case Some(s) => num = s.toInt
        case _       =>
      }
      val ship : Ship = new Ship(name, num)
      val (coord,orient) : (Coordinates,Orientation) = place(ship)
      val placing = RequestBody(Map("coord" -> coord.toString, "orient" -> orient.toString))
      client.post(new URL("http://localhost:9000/api/place/" + id.toString), Some(placing))
    }
    val boards2 : String = getBoards()
    if(boards2 != boards) println(boards)
  }

  def shoot() : Coordinates = {
    var buffer : String = ""
    // only checks if the coordinates can be parsed, not if they are inside the boundaries or already bombed
    while(!buffer.matches("\\A[A-Z]\\d+\\s*\\z")) {
      buffer = readLine("Shoot which square? (e.g. A4): ")
    }
    val coord : Coordinates = new Coordinates(buffer)
    return coord
  }

  def place(ship: Ship) : (Coordinates,Orientation) = {
    var buffer : String = ""
    while(!buffer.matches("\\A[A-Z]\\d+\\s*\\z")) {
      buffer = readLine("Place %s where? Give coordinates of the top left square: ".format(ship))
    }
    val coord : Coordinates = new Coordinates(buffer)
    while(!(buffer.matches("\\A[hv]\\s*\\z"))) {
      buffer = readLine("And the orientation (h|v) for horizontal or vertical: ")
    }
    val orientation : Orientation = buffer match {
      case "h" => Horizontal
      case "v" => Vertical
    }
    return (coord, orientation)
  }
}
