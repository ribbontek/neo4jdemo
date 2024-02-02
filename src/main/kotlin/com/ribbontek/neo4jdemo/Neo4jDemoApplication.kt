package com.ribbontek.neo4jdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Neo4jDemoApplication

fun main(args: Array<String>) {
    runApplication<Neo4jDemoApplication>(*args)
}
