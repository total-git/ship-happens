package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Results._
import play.api.libs.json._
import play.api.libs.json.Json._

import models.PlayGame
import shiphappens.Types._
import shiphappens.Board._

object Api extends Controller {

  // return the own game
  def get(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens")
    else {
      Ok(Board.printFull(PlayGame.player(id).own)
         + Board.printVisible(PlayGame.player(id).enemy)
        )
    }
  }

  def status(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens")
    else {
      Ok(PlayGame.player(id).getStatus)
    }
  }

  def shoot(id: Int) = Action(parse.tolerantText) { request =>
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens")
    Ok("Todo")
    // parse some data

    /*
    val coords = (0,0)
    PlayGame.player(id).shoot(coords) match {
      case true => Ok("Shot placed succesfully")
      case false => BadRequest("Failed to shoot")

    }
    */
  }

  def place(id: Int) = Action(parse.tolerantText) { request =>
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens")
    // parse some data
    val coords = (0,0)
    val orient = Orientation.Horizontal
    PlayGame.player(id).placeShip(coords,orient) match {
      case true => Ok("Ship placed succesfully")
      case false => BadRequest("Failed to place ship")
    }
  }
}
