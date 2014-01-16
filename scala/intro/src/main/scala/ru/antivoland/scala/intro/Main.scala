/**
 * @author antivoland
 */
package ru.antivoland.scala.intro

object Main {
  def main(args: Array[String]) {
    val x = List(1, 2, 3)
    val y = List(3, 4, 5)
    val z = x.++(y)
    println(z)
  }
}
