package Utils

import shiphappens.Types.Result._

object Utils {
  def printField(f: (Boolean, Result)) = f match {
    case (b, Sunk)    => "#"
    case (true, Hit)  => "+"
    case (false, Hit) => "S"
    case (b, Miss)    => "~"
  }

}
