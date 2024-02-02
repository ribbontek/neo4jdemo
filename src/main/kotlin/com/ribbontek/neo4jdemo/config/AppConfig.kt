package com.ribbontek.neo4jdemo.config

import jakarta.persistence.EntityManagerFactory
import org.neo4j.configuration.GraphDatabaseSettings
import org.neo4j.configuration.connectors.BoltConnector
import org.neo4j.configuration.helpers.SocketAddress
import org.neo4j.dbms.api.DatabaseManagementService
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.driver.Driver
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import java.io.File
import kotlin.io.path.Path

@Configuration
@EnableNeo4jRepositories("com.ribbontek.neo4jdemo.graph.repository")
@EntityScan(basePackages = ["com.ribbontek.neo4jdemo.repository"])
@EnableJpaRepositories(basePackages = ["com.ribbontek.neo4jdemo.repository"])
@EnableFeignClients(basePackages = ["com.ribbontek.neo4jdemo.openai"])
class AppConfig(
    @Value("\${com.ribbontek.neo4j.port}") private val neo4jPort: Int
) {

    @Bean(destroyMethod = "shutdown")
    @Profile("!neo4jexternal") // we're not using an embedded neo4j instance for our GDS plugins
    fun databaseManagementService(): DatabaseManagementService {
        // Start on a fresh DB slate by removing any neo4j data files
        File("app/neo4j").takeIf { it.exists() }?.deleteRecursively()
        return DatabaseManagementServiceBuilder(Path("app/neo4j"))
            .setConfig(BoltConnector.enabled, true)
            .setConfig(BoltConnector.listen_address, SocketAddress("localhost", neo4jPort))
            // Couldn't get this config working with an embedded neo4j instance with gds plugins by unzipping the jar & adding under app/neo4j/plugins
            .setConfig(GraphDatabaseSettings.procedure_allowlist, listOf("apoc.coll.*", "apoc.load.*", "gds.*"))
            .setConfig(GraphDatabaseSettings.procedure_unrestricted, listOf("algo.*"))
            .build()
    }

    @Bean(name = ["neo4jTransactionManager"])
    fun neo4jTransactionManager(driver: Driver): Neo4jTransactionManager {
        return Neo4jTransactionManager(driver)
    }

    @Bean(name = ["transactionManager"])
    fun jpaTransactionManager(emf: EntityManagerFactory): JpaTransactionManager {
        return JpaTransactionManager(emf)
    }
}
