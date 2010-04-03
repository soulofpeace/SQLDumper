package SQLDumper

class CSVDumperFactory(val fileName:String) extends DumperFactory{
  def getDumper():Dumper={
    val csvDumper = new CSVDumper(fileName)
    csvDumper
  }
  
  def close(dumper:Dumper):Unit={
    dumper.close()
  }
  
}
