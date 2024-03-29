
// vim: set ts=4 sw=4 et:

// ref: http://alvinalexander.com/scala/scala-execute-exec-external-system-commands-in-scala

import scala.sys.process._ 
import java.net.URL
import java.io._

object Photo {

  def fetch(url: String) = {
    val in = new URL(url).openStream
    org.apache.commons.io.IOUtils.toString( in )
  }
  
  def resize(url: String, width: Int, toFile: Boolean) = {
      
      val w = width.toString

      // The first hypen is a reference to STDIN, the `png:-` is a reference
      // to STDOUT, E.g. convert -strip -resize 100 - png:-
      val pb = Process("convert -strip -resize " + w + " - png:-")

      val image = fetch(url)

      val processIO = new ProcessIO(
            stdin => {
              stdin.write(image.getBytes); stdin.close
            },
            stdout => {
              toFile match {
                case true => {
                  val file = new java.io.FileOutputStream("/tmp/foo-1.png")
                  org.apache.commons.io.IOUtils.copy(stdout, file)
                }
                case _ => {
                  scala.io.Source.fromInputStream(stdout).getLines.foreach(println)
                }
              }

            },
            stderr => scala.io.Source.fromInputStream(stderr).getLines.foreach(println)
      )

      pb run processIO exitValue

  }

}

println(Photo.resize("http://localhost:4567/uk.jpg", 200, false))
