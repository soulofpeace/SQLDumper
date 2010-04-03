package SQLDumper

import java.sql.{Connection, Statement, ResultSet, ResultSetMetaData}
//need to create a connection  trait for different db type eg mysql, oracle
//need to use factory to manufacture the connection
//subsquently using the connect to create statement
//running select
//dumping into csv file

class SQLDumper{
  def apply(serverName:String, portNumber:String, databaseName:String, userName:String, password:String, tableName:String, connectionFactory:ConnectionFactory, dumperFactory:DumperFactory){
    val connection:Connection = connectionFactory(serverName, portNumber, databaseName, userName, password)
    val statement:Statement = connection.createStatement()
    val query:String = "select * from "+tableName
    val result:ResultSet = statement.executeQuery(query)
    val columnCount: int = result.getMetaData().getColumnCount()
    val dump = getResults(result, columnCount)
    //dump.foreach(println)
    val dumper:Dumper = dumperFactory.getDumper()
    dumper.write(dump)
    connectionFactory.close(connection)
    dumperFactory.close(dumper)
  }

  private def getResults(result:ResultSet, columnCount:int):List[Map[String, String]]={
    if(!result.next()){
      return Nil
    }
    else{
     val columnIds = columnCount to 1
     return getColumnValue(result, columnCount, 1)::getResults(result, columnCount)
    }
      
  }

  private def getColumnValue(result:ResultSet, columnCount:Int, current:Int):Map[String, String]={
    if(current>columnCount){
      return Map()
    }
    else{
      return Map(result.getMetaData().getColumnName(current)->
                 (if(result.getString(current)==null) "null" 
                 else result.getString(current)))++getColumnValue(result, columnCount, current+1)
    }
  }
  
  
  
}

object SQLDumper{
  def main(args:Array[String]){
  }
}

