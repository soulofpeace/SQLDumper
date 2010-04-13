package SQLDumper

import org.junit._
import Assert._

@Test
class AppTest {

    @Test
    def testOK() {
      
      val mySQLConnectionFactory = new MySQLConnectionFactory("hostname.com", "3306", "db", "table", "password")
      val dumperFactory = new CSVDumperFactory("test3.txt")
      val sqlDumper = new SQLDumper(mySQLConnectionFactory, dumperFactory)
      sqlDumper.dump("select * from table")
    }

//    @Test
//    def testKO() = assertTrue(false)

}


