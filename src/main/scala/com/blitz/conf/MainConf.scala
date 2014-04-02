package com.blitz.conf

import com.typesafe.config.{Config, ConfigFactory}
import java.net.URL
import scala.collection.JavaConverters._

/**
 *
 */

class NestedConf(name: String, parentConf: Config) {

  //It is not right, but necessary to glue this configuration to the play framework configuration
  def conf = parentConf.getConfig(name)

  def this(name: String)(nestedConf: NestedConf) = this(name, nestedConf.conf)

  @inline private[this] def _safeUnwrap[B](func:(Config, String) => B)(implicit name2: String): Option[B] = conf.hasPath(name2) match {
    case true => Option(func(conf, name2))
    case false => None
  }

  def getString(name: String) = conf.getString(name)
  def getOptString(implicit name: String) = _safeUnwrap(_.getString(_))

  def getInt(name: String) = conf.getInt(name)
  def getOptInt(implicit name: String) = _safeUnwrap(_.getInt(_))

  def getLong(name: String) = conf.getLong(name)
  def getOptLong(implicit name: String) = _safeUnwrap(_.getLong(_))

  def getBoolean(name: String) = conf.getBoolean(name)
  def getOptBoolean(implicit name: String) = _safeUnwrap(_.getBoolean(_))

  def getConfig(name: String) = conf.getConfig(name)
  def getOptConfig(implicit name: String) = _safeUnwrap(_.getConfig(_))

  @inline private[this] def _toMapString(cnf: Config): Map[String, String] = cnf.entrySet().asScala.map(_.getKey match {case key => key -> cnf.getString(key)}).toMap

  def getMapString(name: String): Map[String, String] = getOptConfig(name).fold[Map[String, String]](Map.empty)(_toMapString _)

  def getDeepMapString(name: String): Map[String, Map[String, String]] = getOptConfig(name).fold[Map[String, Map[String, String]]](Map.empty){c => {
    c.entrySet().asScala.map(_.getKey.split('.')(0)).map{case key => key -> _toMapString(c.getConfig(key))}.toMap
  }}

}


/**
 * Example:
 * main-conf {
 *
 *   data-dir = "/opt/git/data"
 *
 *   logger {
 *      dir-of-logs = ${main-conf.data-dir}"/logs"
 *      levels {
 *        com.blitz = "INFO"
 *        com.blitz.scs = "DEBUG"
 *        com.blitz.crypt = "TRACE"
 *      }
 *   }
 * }
 */
class MainConf(private val appConf: String, private val root: Config = Option(System.getProperty("blitzConfUrl")).fold[Config](
  throw new IllegalStateException("Property 'blitzConfUrl' is undefined.")
)(path => {
  val conf = ConfigFactory.parseURL(new URL(path))
  if (conf.isEmpty) {
    throw new IllegalStateException(s"the specified config [$path] not found or it's empty")
  }
  conf.resolve()
})) extends NestedConf(appConf, root) {

  val main = new NestedConf("main-conf", root) {

    val dataDirPath = getString("data-dir")

    val logger = new NestedConf("logger")(this) {

      val dirOfLogs = getString("dir-of-logs")

      val levels = getMapString("levels")

    }
  }

  private def lookupConfig(key: String) = root.hasPath(key) match {
    case true => Option(root.getString(key))
    case false => None
  }

}
