package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.libs.json._
import play.api.libs.json.Json._

import models.Game
import shiphappens.Types._

object Api extends Controller {

  def get(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest(toJson(Map("status" -> "error",
                            "message" -> "Player ID invalid, only two players can play ship-happens")))
    else {
      Ok(toJson(Map("status" -> toJson("ok"),
                    "own" -> serializeOwn(id),
                    "enemy" -> serializeEnemy(id)
                   )))
    }
  }

//  def move(id: Int) = ???

  private def serializeOwn(id: Int): JsValue = {
    val o = Game.player(id).own.map(_.toList).toList.map(_.map{
      case (a,b) => Map("probed" -> toJson(a),
                        "result" -> serializeResult(b))
    })
    Json.toJson(o)
  }

  private def serializeEnemy(id: Int): JsValue = {
    val o = Game.player(id).enemy.map(_.toList).toList.map(_.map{
      case None => toJson("None")
      case Some(r) => serializeResult(r)
    })
    Json.toJson(o)
  }

  private def serializeResult(r: shiphappens.Types.Result.Result): JsValue =
    toJson(r.toString.toLowerCase)

}
