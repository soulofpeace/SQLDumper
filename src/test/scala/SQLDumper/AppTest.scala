package SQLDumper

import org.junit._
import Assert._

@Test
class AppTest {

    @Test
    def testOK() {
      val sqlDumper = new SQLDumper()
      val mySQLConnectionFactory = new MySQLConnectionFactory()
      val dumperFactory = new CSVDumperFactory("test.txt")
      
    }

//    @Test
//    def testKO() = assertTrue(false)

}


