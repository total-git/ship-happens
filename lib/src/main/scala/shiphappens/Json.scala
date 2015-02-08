package shiphappens.Json

import shiphappens.Types._
import shiphappens.Types.Result._
import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}

class ResultSerializer extends Serializer[Result] {
  private val ResultClass = classOf[Result]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Result] = {
    case (TypeInfo(ResultClass, _), json) => json match {
      case JString("miss") => Miss
      case JString("hit") => Hit
      case JString("sunk") => Sunk
      case JString("already probed") => AlreadyProbed
      case JString("invalid") => Invalid
      case x => throw new MappingException("Can't convert " + x + " to Interval")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case x: Result => JString(x.toString)
  }

}
