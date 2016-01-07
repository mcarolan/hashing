package net.mcarolan.hashing

import scala.util.Random
import java.nio.ByteBuffer
import java.io.FileWriter
import com.google.common.hash.Hashing
import com.google.common.hash.HashFunction

object App {

  def identifier: String =
    Random.alphanumeric.take(15 + Random.nextInt(5)).mkString.toUpperCase()

  def identifiers(n: Int): List[String] =
    (0 to n).toList.map(_ => identifier)

  sealed trait Bucket
  case object A extends Bucket
  case object B extends Bucket

  sealed trait Hasher extends (String => Int)

  case class GuavaHasher(algo: HashFunction) extends Hasher {

    def apply(input: String): Int =
      algo.hashUnencodedChars(input).asInt
  }

  case object HashcodeHasher extends Hasher {

    def apply(input: String): Int =
      input.hashCode

  }

  def shard(hash: Int): Bucket =
    if (hash % 2 == 0)
      A
    else
      B

  def balanceUsing(identifiers: List[String])(hasher: Hasher): Int = {
    val (a, b) = identifiers partition { id => shard(hasher(id)) == A }
    Math.abs(a.size - b.size)
  }

  def main(args: Array[String]) {
    val algs = List(GuavaHasher(Hashing.murmur3_32),
                    GuavaHasher(Hashing.sha1),
                    GuavaHasher(Hashing.md5),
                    GuavaHasher(Hashing.crc32),
                    GuavaHasher(Hashing.adler32),
                    GuavaHasher(Hashing.sipHash24),
                    HashcodeHasher)
    (0 to 100) foreach { _ =>
      val balance = balanceUsing(identifiers(10000))_
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
