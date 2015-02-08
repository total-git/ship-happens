package Utils

import shiphappens.Types.Result._

object Utils {
  def printOwnField(f: (Boolean, Result)) = f match {
    case (b, Sunk)    => "#"
    case (true, Hit)  => "+"
    case (false, Hit) => "S"
    case (b, Miss)    => "~"
    case (_, _)       => "!"
  }

  def printEnemyField(f: Option[Result]) = f match {
    case Some(Sunk) => "#"
    case Some(Hit)  => "+"
    case Some(Miss) => "~"
    case None       => " "
    case Some(_)    => "!"
  }

}
