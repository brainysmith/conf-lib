package com.identityblitz.conf

import com.typesafe.config.{Config, ConfigFactory}
import java.net.URL
import scala.collection.JavaConverters._

/**
 *
 */

class NestedConf(name: Option[String], parentConf: Config) {

  //It is not right, but necessary to glue this configuration to the play framework configuration
  def conf = name.fold(parentConf)(parentConf.getConfig)

  def this(name: String)(nestedConf: NestedConf) = this(Some(name), nestedConf.conf)

  @inline private[this] def _safeUnwrap[B](func:(Config, String) => B)(implicit name2: String): Option[B] = conf.hasPath(name2) match {
    case true => Option(func(conf, name2))
    case false => None
  }

  def getString(name: String): String = conf.getString(name)
  def getOptString(implicit name: String): Option[String] = _safeUnwrap(_.getString(_))

  def getInt(name: String): Int = conf.getInt(name)
  def getOptInt(implicit name: String): Option[Int] = _safeUnwrap(_.getInt(_))

  def getLong(name: String): Long = conf.getLong(name)
  def getOptLong(implicit name: String): Option[Long] = _safeUnwrap(_.getLong(_))

  def getBoolean(name: String): Boolean = conf.getBoolean(name)
  def getOptBoolean(implicit name: String): Option[Boolean] = _safeUnwrap(_.getBoolean(_))

  def getConfig(name: String): Config = conf.getConfig(name)
  def getOptConfig(implicit name: String): Option[Config] = _safeUnwrap(_.getConfig(_))

  @inline private[this] def _toMapString(cnf: Config): Map[String, String] = cnf.entrySet().asScala.map(_.getKey match {case key => key -> cnf.getString(key)}).toMap

  def getMapString(name: String): Map[String, String] = getOptConfig(name).fold[Map[String, String]](Map.empty)(_toMapString)

  def getDeepMapString(name: String): Map[String, Map[String, String]] = getOptConfig(name).fold[Map[String, Map[String, String]]](Map.empty){c => {
    c.entrySet().asScala.map(_.getKey.split('.')(0)).map{case key => key -> _toMapString(c.getConfig(key))}.toMap
  }}

}

import BlitzConf.rootConf
class BlitzConf(private val appConf: Option[String], private val root: Config) extends NestedConf(appConf, root) {

  def this(confName: String) = this(Some(confName), rootConf)

  def this() = this(None, rootConf)

}

private object BlitzConf {

  @inline private def rootConf = Option(System.getProperty("blitzConfUrl")).fold[Config](
    throw new IllegalStateException("Property 'blitzConfUrl' is undefined.")
  )(path => {
    val conf = ConfigFactory.parseURL(new URL(path))
    if (conf.isEmpty) {
      throw new IllegalStateException(s"the specified config [$path] not found or it's empty")
    }
    conf.resolve()
  })

}
