Configuration library
----------------------
This project aims to simplify working with configuration.

Example of the configuration
----------------------------
```
main-conf {
    data-dir = "/opt/git/data"

    logger {
        dir-of-logs = ${main-conf.data-dir}"/logs"

        levels {
     	    root = "INFO"
            core = "DEBUG"
            Wl = "TRACE"
        }
    }
}

test-conf {
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

Author
--------------------

