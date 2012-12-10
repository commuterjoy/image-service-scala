
// vim: set ts=4 sw=4 et:

// ref: http://alvinalexander.com/scala/scala-execute-exec-external-system-commands-in-scala

import scala.sys.process._ 
import java.net.URL
import java.io._

object Photo {

  def fetch(url: String) = {
    val in = new URL(url).openStream;
    org.apache.commons.io.IOUtils.toString( in )
  }
  
  def resize(url: String, width: Int) = {
      
      val w = width.toString

      val file = new java.io.FileOutputStream("/tmp/foo.png")

      // The first hypen is a reference to STDIN, the `png:-` is a reference to STDOUT, E.g. convert -strip -resize 100 - png:-
      val pb = Process("convert -strip -resize " + w + " uk.jpg png:-")

      val body = fetch(url)

      val processIO = new ProcessIO(
            stdin => (), // TODO body getBytes "UTF-8"
            stdout => org.apache.commons.io.IOUtils.copy(stdout, file),
            stderr => scala.io.Source.fromInputStream(stderr).getLines.foreach(println))

      pb run processIO exitValue
    
  }

}

println(Photo.resize("http://localhost:4567/uk.jpg", 200))
