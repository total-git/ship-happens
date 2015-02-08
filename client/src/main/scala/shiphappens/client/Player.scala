package shiphappens.Client

import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.response.Response
import java.net.URL

import shiphappens.Board._
import shiphappens.Types.Result._

class Player(val id: Int) {
  private val client = new HttpClient

  def getBoards(): String = {
    val response = client.get(new URL("http://localhost:9000/api/get/" + id.toString))
    response.body.asString
  }

}
