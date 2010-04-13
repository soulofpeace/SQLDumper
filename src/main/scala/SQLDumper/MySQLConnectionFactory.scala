package SQLDumper

import java.sql.{DriverManager, Connection, SQLException}

class MySQLConnectionFactory  (serverName:String, portNumber:String, databaseName:String, userName:String, password:String) extends ConnectionFactory (serverName, portNumber, databaseName, userName, password){
    def connection:Connection={
      try{
        Class.forName("com.mysql.jdbc.Driver")
        val url ="jdbc:mysql://"+serverName+":"+portNumber+"/"+databaseName
        DriverManager.getConnection(url, userName, password)
      }
      catch{
        case e:ClassNotFoundException=>{
          e.printStackTrace()
          println("Cannot Find Database Connector Class=> Check Classpath")
          null
        }
      
        case e:SQLException=>{
          e.printStackTrace()
          println("Cannot Connect to database")
          null
        }
      }
     
    }

  def close(connection:Connection)={
    try{
      connection.close()
      true
    }
    catch{
      case e:Exception=>false
      
    }
  }
  
}
