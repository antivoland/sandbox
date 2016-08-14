package antivoland.dirctltest

import org.slf4j.LoggerFactory

object BigBrother {
  def log = LoggerFactory.getLogger(BigBrother.getClass)

  def main(args: Array[String]) {
    spyOnThePerson("Alice", (0, 0), 10, 5000, null)
  }

  def spyOnThePerson(id: String, point: (Double, Double), radius: Int, timeoutMillis: Int, callback: () => String): Unit = {
    log.debug(s"Spying on $id inside the circle with radius $radius and center at (${point._1}, ${point._2}) within the next $timeoutMillis milliseconds")
    sys.error("Not implemented yet")
  }
}
