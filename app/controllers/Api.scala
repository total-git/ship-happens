package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Results._
import play.api.libs.json._
import play.api.libs.json.Json._

import scala.util.matching.Regex

import models.PlayGame
import shiphappens._

/**
 * Controller with access functions for the client
 *
 * Accesses the singleton PlayGame and the information stored in it
 */
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

  def latest(id: Int) = Action {
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens")
    else {
      Ok(PlayGame.player(id).moves.take(10).mkString("\n"))
    }
  }

  def shoot(id: Int) = Action(parse.urlFormEncoded) { request =>
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens")
    else if (request.body("coord").length != 1) {
      BadRequest("Input is invalid. Not a valid coordinate to shoot at")
    } else {
      val coords = request.body("coord").head

      if (!coords.matches("""\A\w+\d+\Z""")) {
        BadRequest("Invalid coordinates")
      } else {
        PlayGame.player(id).shoot(coords) match {
          case true => Ok("Shot placed succesfully")
          case false => BadRequest("Failed to shoot")
        }
      }
    }
  }

  def place(id: Int) = Action(parse.urlFormEncoded) { request =>
    if (id < 1 || id > 2)
      BadRequest("Player ID invalid, only two players can play ship-happens")
    else {
      val c = request.body("coord")
      val o = request.body("orient")
      if (c.length != 1 || o.length != 1
          || !c.head.matches("""\A\w+\d+\Z""")
          || !o.head.matches("(Horizontal|Vertical)"))
        BadRequest("Invalid parameters")
      else {
        val coords = c.head
        val orient = Orientation.withName(o.head)

        PlayGame.player(id).placeShip(coords,orient) match {
          case true => Ok("Ship placed succesfully")
          case false => BadRequest("Failed to place ship")
        }
      }
    }
  }
}
