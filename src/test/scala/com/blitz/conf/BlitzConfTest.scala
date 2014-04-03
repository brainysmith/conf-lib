package com.blitz.conf

import org.scalatest._

/**
 */

class TestAppConf extends BlitzConf("test-app-conf") {
  implicit val self = this

  val logger = new NestedConf("logger")(this) {

    val dirOfLogs = getString("dir-of-logs")

    val levels = getMapString("levels")

  }  
  
  val modules = getDeepMapString("modules")
  val flow = getOptString("flow")
}

trait ConfBehaviors extends Matchers { this: FlatSpec =>

  def blitzConf(conf: TestAppConf) {
    it should "contains non empty dataDirPath (mandatory parameter)" in {
      conf.dataDirPath should not be empty
    }
  }

  def testConf(conf: TestAppConf) {
    it should "contains non empty logger.dirOfLogs (mandatory parameter)" in {
      conf.logger.dirOfLogs should not be empty
    }
    it should "contains non empty logger.levels (map parameter)" in {
      conf.logger.dirOfLogs should not be empty
    }    
    
    it should "contains non empty modules map (deep map parameter)" in {
      conf.modules should not be empty
    }
    it should "not contains flow parameter (option parameter)" in {
      conf.flow shouldBe empty
    }
  }
}

class BlitzConfTest extends FlatSpec with Matchers with ConfBehaviors {

  behavior of "Negative cases"

  it should "throw IllegalStateException if a property 'blitzConfUrl' is undefined." in {
    System.clearProperty("blitzConfUrl")
    a [IllegalStateException] should be thrownBy {
      new TestAppConf
    }
  }

  it should "throw IllegalStateException if an configuration not found" in {
    System.setProperty("blitzConfUrl", "file:./wrongPath")
    a [IllegalStateException] should be thrownBy {
      new TestAppConf
    }
  }

  it should "throw IllegalStateException if an configuration is empty" in {
    System.setProperty("blitzConfUrl", "file:./src/test/resources/empty.conf")
    a [IllegalStateException] should be thrownBy {
      new TestAppConf
    }
  }

  lazy val testAppConf = {
    System.setProperty("blitzConfUrl", "file:./src/test/resources/test.conf")
    new TestAppConf
  }

  "A common blitz parameters" should behave like blitzConf(testAppConf)

  "An application specific parameters" should behave like testConf(testAppConf)
}
