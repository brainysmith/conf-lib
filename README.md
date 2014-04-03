CONF-LIB
----------------------
This project aims to simplify working with configuration.
A configuration file's URL specified by the system property 'blitzConfUrl'
For example: file:./src/test/resources/empty.conf.
If the property no found or the URL is wrong throws IllegalStateException.

Example of the configuration
----------------------------
```json
test-app-conf {
    data-dir = "/opt/git/data"
    logger {
        dir-of-logs = ${test-app-conf.data-dir}"/logs"
        levels {
            com.blitz = "INFO"
            com.blitz.scs = "DEBUG"
            com.blitz.crypt = "TRACE"
        }
    }
    modules {
        module1 = {
            order = 1
            param1 = value 1
            param2 = value 2
        }
        module2 = {
            order = 2
        }
    }
}
```

Requires
---------------
* [sbt 0.13.1](http://www.scala-sbt.org/)
* [typesafe config 1.2.0] (https://github.com/typesafehub/config)

Use
---------------
* Scala
```scala
class TestAppConf extends BlitzConf("test-app-conf") {
  implicit val self = this
  val logger = new NestedConf("logger")(this) {
    val dirOfLogs = getString("dir-of-logs")
    val levels = getMapString("levels")
  }
  val modules = getDeepMapString("modules")
  val flow = getOptString("flow")
}
```
* Java
TODO

Author
--------------------

