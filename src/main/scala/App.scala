package net.mcarolan.hashing

import scala.util.Random
import com.roundeights.hasher.{Hasher, Algo}
import java.nio.ByteBuffer
import java.io.FileWriter

object App {

  def identifier: String =
    Random.alphanumeric.take(15).mkString.toUpperCase()

  def identifiers(n: Int): List[String] =
    (0 to n).toList.map(_ => identifier)

  sealed trait Bucket
  case object A extends Bucket
  case object B extends Bucket

  def hash(algo: Algo, value: String): Int =
    ByteBuffer.wrap(algo(value).bytes).getInt

  def shard(hash: Int): Bucket =
    if (hash % 2 == 0)
      A
    else
      B

  def balanceUsing(identifiers: List[String])(algo: Algo): Int = {
    val (a, b) = identifiers partition { id => shard(hash(algo, id)) == A }
    Math.abs(a.size - b.size)
  }

  def main(args: Array[String]) {
    (0 to 100) foreach { _ =>
      val balance = balanceUsing(identifiers(10000))_
      val algs = List(Algo.crc32, Algo.md5, Algo.sha1)
      val res = algs.map(balance).mkString(",")
      val fw = new FileWriter("results.csv", true)
      try {
        fw.write(res + "\n")
      }
      finally {
        fw.close
      }
      println(res)
    }
  }
}
