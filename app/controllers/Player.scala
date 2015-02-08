package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse

import shiphappens.Board._
import shiphappens.Types._
import shiphappens.Types.Orientation._

object Player extends Controller {

  def index(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens\n")
    else {
      var b = new Board()
      b = b.setShip(Ship("Testship", 3), (0,4), Horizontal).get
      Ok(views.html.PlayerMain(id, b.full, b.visible))
    }
  }

  def move(id: Int) = Action(parse.tolerantText) { request =>
    val b = new Board()
    Ok(views.html.DrawOwnBoard(b.full))
  }

}
