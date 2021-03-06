package SQLDumper

import java.sql.{PreparedStatement, ResultSet, SQLException, Timestamp, Connection}
import java.util.regex.Pattern
import scala.collection.mutable
 
class SqlQueryFactory extends QueryFactory {
  def apply(connectionFactory:ConnectionFactory, query:String, params: Any*) = {
    new SqlQuery(connectionFactory.connection, query, params: _*)
  }
}
 
class TooFewQueryParametersException extends Exception
class TooManyQueryParametersException extends Exception
 
class SqlQuery(connection: Connection, query: String, params: Any*) extends Query {
  val statement = buildStatement(connection, query, params: _*)
 
  
  def select[A](f: ResultSet => A): Seq[A] = {
    withStatement {
      statement.executeQuery()
      val rs = statement.getResultSet
      try {
        val finalResult = new mutable.ArrayBuffer[A]
        while (rs.next()) {
          finalResult += f(rs)
        }
        finalResult
      } finally {
        rs.close()
      }
    }
  }

 
  def execute() = {
    withStatement {
      statement.executeUpdate()
    }
  }
 
  def cancel() {
    try {
      statement.cancel()
      statement.close()
    } catch {
      case _ =>
    }
  }
 
  private def withStatement[A](f: => A) = {
    try {
      f
    } finally {
      try {
        statement.close()
      } catch {
        case _ =>
      }
    }
  }
 
  private def buildStatement(connection: Connection, query: String, params: Any*) = {
    val statement = connection.prepareStatement(expandArrayParams(query, params: _*))
    setBindVariable(statement, 1, params)
    statement
  }
 
  private def expandArrayParams(query: String, params: Any*) = {
    val p = Pattern.compile("\\?")
    val m = p.matcher(query)
    val result = new StringBuffer
    var i = 0
    while (m.find) {
      try {
        val questionMarks = params(i) match {
          case a: Array[Byte] => "?"
          case s: Seq[_] => s.map { arg=> "?" }.mkString(",")
          case _ => "?"
        }
        m.appendReplacement(result, questionMarks)
      } catch {
        case e: ArrayIndexOutOfBoundsException => throw new TooFewQueryParametersException
      }
      i += 1
    }
    m.appendTail(result)
    result.toString
  }
 
  private def setBindVariable[A](statement: PreparedStatement, startIndex: Int, param: A): Int = {
    var index = startIndex
 
    try {
      param match {
        case s: String =>
          statement.setString(index, s)
        case l: Long =>
          statement.setLong(index, l)
        case i: Int =>
          statement.setInt(index, i)
        case b: Array[Byte] =>
          statement.setBytes(index, b)
        case b: Boolean =>
          statement.setBoolean(index, b)
        case d: Double =>
          statement.setDouble(index, d)
        case t: Timestamp =>
          statement.setTimestamp(index, t)
        case is: Seq[_] =>
          for (i <- is) index = setBindVariable(statement, index, i)
          index -= 1
        case _ => throw new IllegalArgumentException("Unhandled query parameter type for " +
          param + " type " + param.asInstanceOf[Object].getClass.getName)
      }
      index + 1
    } catch {
      case e: SQLException => throw new TooManyQueryParametersException
    }
  }
}
 
