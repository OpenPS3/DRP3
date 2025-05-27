import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import edu.sc.seis.launch4j.tasks.DefaultLaunch4jTask
import org.gradle.kotlin.dsl.launch4j

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("edu.sc.seis.launch4j") version "3.0.6"
}

group = "net.openps3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Discord
    implementation("com.github.JnCrMx:discord-game-sdk4j:v1.0.0")

    // Scraping
    implementation("org.jsoup:jsoup:1.17.2")

    // Ktor
    implementation("io.ktor:ktor-client-core:3.1.3")
    implementation("io.ktor:ktor-client-cio:3.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("io.github.microutils:kotlin-logging:2.1.16")

    // Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("ps3rpc")
        archiveClassifier.set("")

        manifest {
            attributes(
                "Main-Class" to "net.openps3.drp3.DRP3Launcher"
            )
        }
    }
}

launch4j {
    outfile = "ps3rpc.exe"
    mainClassName = "net.openps3.drp3.DRP3Launcher"

}

tasks.withType<DefaultLaunch4jTask> {
    dependsOn("shadowJar")

    outfile.set("ps3rpc.exe")
    mainClassName.set("net.openps3.drp3.DRP3Launcher")
    productName.set("Discord Rich Presence for PlayStation 3")
    setJarFiles(files(tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile }))
    jreMinVersion.set("17")
    dontWrapJar.set(false)
    headerType.set("console")
}

kotlin {
    jvmToolchain(17)
}