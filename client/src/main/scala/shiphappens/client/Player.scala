package shiphappens.Client

import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.response.Response
import java.net.URL

import shiphappens.Board._
import shiphappens.Types.Result._
import shiphappens.Json._

import net.liftweb.json._
import net.liftweb.json.Serialization.read

class Player(val id: Int) {
  private val client = new HttpClient

  def getOwnBoard(): Board.PlayerField = {
    val response = client.get(new URL("http://localhost:9000/api/player/" + id.toString))
    implicit val formats = Serialization.formats(NoTypeHints)
    read[Board.PlayerField](response.body.toString)
  }

}
