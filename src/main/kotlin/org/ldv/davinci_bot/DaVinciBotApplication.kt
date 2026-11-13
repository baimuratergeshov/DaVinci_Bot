package org.ldv.davinci_bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DaVinciBotApplication

fun main(args: Array<String>) {
    runApplication<DaVinciBotApplication>(*args)
}
