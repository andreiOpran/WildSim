plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // mongodb driver for java
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")

    // SLF4J API
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("com.wildsim.Main")
}

tasks.test {
    useJUnitPlatform()
}