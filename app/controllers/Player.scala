package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse

import shiphappens.Board._

object Player extends Controller {

  def index(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens\n")
    else {
      val b = new Board()
      Ok("Hello player " + id + " to ship-happens\n" + views.html.DrawBoard(b.full)).as(HTML)
    }
  }

  def move(id: Int) = Action(parse.tolerantText) { request =>
    val b = new Board()
    Ok(views.html.DrawBoard(b.full))
  }

}
