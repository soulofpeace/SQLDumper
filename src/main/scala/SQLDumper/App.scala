package SQLDumper

import java.sql.{Connection, Statement, ResultSet, ResultSetMetaData}
//need to create a connection  trait for different db type eg mysql, oracle
//need to use factory to manufacture the connection
//subsquently using the connect to create statement
//running select
//dumping into csv file

class SQLDumper(val connectionFactory:ConnectionFactory, val dumperFactory:DumperFactory){
  def dump(queryString:String){
    val queryFactory: SqlQueryFactory = new SqlQueryFactory()
    val query:SqlQuery = queryFactory(connectionFactory, queryString)
    val dump:Seq[Map[String, String]] = query.select(getResults)
    //dump.foreach(println)
    val dumper:Dumper = dumperFactory.getDumper()
    dumper.write(dump)
    dumperFactory.close(dumper)
  }

  private def getResults(result:ResultSet):Map[String, String]={
    getColumnValues(result, result.getMetaData().getColumnCount(), 1)
  }
      

  private def getColumnValues(result:ResultSet, columnCount:Int, current:Int):Map[String, String]={
    if(current>columnCount){
      return Map()
    }
    else{
      return Map(result.getMetaData().getColumnName(current)->
                 (if(result.getString(current)==null) "null" 
                 else result.getString(current)))++getColumnValues(result, columnCount, current+1)
    }
  }
  
  
  
}

object SQLDumper{
  def main(args:Array[String]){
  }
}

