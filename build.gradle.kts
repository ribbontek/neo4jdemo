import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.liquibase.gradle") version "2.1.1"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.20"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    id("com.avast.gradle.docker-compose") version "0.14.3"
    id("nebula.integtest") version "9.6.3"
    id("pl.allegro.tech.build.axion-release") version "1.15.0"
    idea
}

group = "com.ribbontek"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.0")
    // Neo4j Embedded
    implementation("org.neo4j:neo4j:5.16.0") {
        exclude(group = "org.neo4j", module = "neo4j-slf4j-provider")
    }
    // DB liquibase
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("com.mysql:mysql-connector-j")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    testImplementation("org.hamcrest:hamcrest:2.2")

    // integ tests
    integTestImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

kotlin {
    allOpen {
        annotations(
            "javax.persistence.Entity",
            "javax.persistence.MappedSuperclass",
            "javax.persistence.Embedabble"
        )
    }
}

idea {
    module {
        testSourceDirs.plusAssign(project.sourceSets["integTest"].kotlin.srcDirs)
        testResourceDirs.plusAssign(project.sourceSets["integTest"].resources.srcDirs)
    }
}

liquibase {
    activities.register("main") {
        val dbUrl by project.extra.properties
        val dbUser by project.extra.properties
        val dbPassword by project.extra.properties

        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/liquibase/db.changelog-master.xml",
            "url" to (project.findProperty("DATABASE_URL")?.toString() ?: dbUrl),
            "username" to (project.findProperty("DATABASE_USERNAME")?.toString() ?: dbUser),
            "password" to (project.findProperty("DATABASE_PASSWORD")?.toString() ?: dbPassword),
            "driver" to "org.postgresql.Driver"
        )
    }
    runList = "main"
}

dockerCompose.isRequiredBy(tasks.getByName("integrationTest"))

val versionFile by tasks.registering {
    doLast {
        mkdir("${project.buildDir}/version")
        file("${project.buildDir}/version/version").writeBytes(scmVersion.version.toByteArray())
        project.version = scmVersion.version
    }
}

tasks.build {
    dependsOn(versionFile)
}

scmVersion {
    tag {
        // if no tags exists, this sets the starting position
        initialVersion { _, _ -> "1.0.0" }
    }
    // Example options to pass-through: incrementPatch, incrementMinor, incrementMajor (PredefinedVersionIncrementer)
    val incrementer: String = project.findProperty("release.scope")?.toString() ?: "incrementMinor"
    // Use minor, not patch by default. e.g. 1.0.0 -> 1.1.0
    versionIncrementer(incrementer)
    // Adds branch names to snapshots
    branchVersionCreator.putAll(
        mapOf(
            "feature/.*" to "versionWithBranch",
            "hotfix/.*" to "versionWithBranch"
        )
    )
}
