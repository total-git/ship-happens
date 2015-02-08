package models

import shiphappens.Game

object PlayGame {
  private var _players : (PlayPlayer, PlayPlayer) = (new PlayPlayer(1),
                                                     new PlayPlayer(2))

  def player(id: Int) = id match {
    case 1 => _players._1
    case 2 => _players._2
  }

}
