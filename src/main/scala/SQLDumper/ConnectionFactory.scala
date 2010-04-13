package SQLDumper

import java.sql.Connection

abstract class ConnectionFactory(val serverName:String,  val portNumber:String, val databaseName:String, val userName:String, val password:String){
  def connection:Connection
  def close(connection:Connection):Boolean
}
