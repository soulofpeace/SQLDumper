package SQLDumper

import java.io.{FileWriter, IOException}


class CSVDumper (fileName:String) extends Dumper{
  
  private val fileWriter:FileWriter = new FileWriter(fileName)

  def write(values:List[Map[String, String]]){
    try{
      val keys = (values(0).keys).toList
      fileWriter.write(keys.mkString(",")+"\n")
      values.foreach(arg=>writeRow(keys ,arg))
    }
    catch{
      case e:IOException=>{
        e.printStackTrace()
        println("IOException occurs while writing")
      }
      
    }
  }
  
  private def writeRow(keys:List[String], maps:Map[String, String]){
    for(key<-keys){
      fileWriter.write(maps.get(key).get()+",")
    }
    fileWriter.write("\n")
   
  }

  def close(){
    try{
      fileWriter.flush()
      fileWriter.close()
    }
    catch{
      case e:IOException=>{
        e.printStackTrace()
        println("IOException occurs while writing")
      }
    }
  }
}
