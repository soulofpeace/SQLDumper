package SQLDumper

import java.sql.Connection

trait ConnectionFactory{
  def apply(serverName:String, portNumber:String, databaseName:String, userName:String, password:String):Connection
  def close(connection:Connection):Boolean
}
