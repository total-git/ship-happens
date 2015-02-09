package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse

import models.PlayGame

/**
 * simple web front end, which is read-only, but allows watching the game
 * from a browser (with manual reloading). Address is player/<playerid>
 */
object Player extends Controller {

  def index(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens\n")
    else {
      Ok(views.html.PlayerMain(id, PlayGame.player(id)))
    }
  }

}
