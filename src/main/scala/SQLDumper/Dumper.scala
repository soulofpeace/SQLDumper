package SQLDumper

trait DumperFactory{
  def getDumper():Dumper
  def close(dumper:Dumper):Unit
}

trait Dumper{
  def write(values:Seq[Map[String, String]]):Unit
  def close():Unit
}
