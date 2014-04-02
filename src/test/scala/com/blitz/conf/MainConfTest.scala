package com.blitz.conf

import org.scalatest._

/**
 */

class TestConf extends MainConf("test-conf") {
  implicit val self = this

  val modules = getDeepMapString("modules")
  val flow = getOptString("flow")
}

trait ConfBehaviors extends Matchers { this: FlatSpec =>

  def mainConf(conf: TestConf) {
    it should "contains non empty dataDirPath (mandatory parameter)" in {
      conf.main.dataDirPath should not be empty
    }
    it should "contains non empty logger.dirOfLogs (mandatory parameter)" in {
      conf.main.logger.dirOfLogs should not be empty
    }
    it should "contains non empty logger.levels (map parameter)" in {
      conf.main.logger.dirOfLogs should not be empty
    }
  }

  def testConf(conf: TestConf) {
    it should "contains non empty modules map (deep map parameter)" in {
      conf.modules should not be empty
    }
    it should "not contains flow parameter (option parameter)" in {
      conf.flow shouldBe empty
    }
  }
}

class MainConfTest extends FlatSpec with Matchers with ConfBehaviors {

  it should "throw IllegalStateException if a property 'blitzConfUrl' is undefined." in {
    System.clearProperty("blitzConfUrl")
    a [IllegalStateException] should be thrownBy {
      new TestConf
    }
  }

  it should "throw IllegalStateException if an configuration not found" in {
    System.setProperty("blitzConfUrl", "file:./wrongPath")
    a [IllegalStateException] should be thrownBy {
      new TestConf
    }
  }

  it should "throw IllegalStateException if an configuration is empty" in {
    System.setProperty("blitzConfUrl", "file:./src/test/resources/empty.conf")
    a [IllegalStateException] should be thrownBy {
      new TestConf
    }
  }

  lazy val testConf = {
    System.setProperty("blitzConfUrl", "file:./src/test/resources/test.conf")
    new TestConf
  }

  "A test.conf (main-conf part)" should behave like mainConf(testConf)

  "A test.conf (test-conf part)" should behave like testConf(testConf)
}
