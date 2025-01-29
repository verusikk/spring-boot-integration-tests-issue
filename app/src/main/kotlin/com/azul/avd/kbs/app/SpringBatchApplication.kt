@file:Suppress("ForbiddenComment")

package com.azul.avd.kbs.app

//import com.azul.avd.kbs.app.models.DependencyRepositoryChecker
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBatchApplication {
}

fun main(args: Array<String>) {
    runApplication<SpringBatchApplication>(*args) {
        //addInitializers(DependencyRepositoryChecker())
    }
}
