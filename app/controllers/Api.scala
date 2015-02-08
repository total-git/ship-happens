package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.libs.json._
import play.api.libs.json.Json._

import models.Game
import shiphappens.Types._
import shiphappens.Json._

import net.liftweb.json._
import net.liftweb.json.Serialization.write

object Api extends Controller {

  def getOwn(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest(toJson(Map("status" -> "error",
                            "message" -> "Player ID invalid, only two players can play ship-happens")))
    else {
      implicit val formats = Serialization.formats(NoTypeHints)
      Ok(write(Game.player(id).own))
    }
  }

//  def move(id: Int) = ???
}
