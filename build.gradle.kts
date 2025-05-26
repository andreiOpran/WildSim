plugins {
    id("java")
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
}

tasks.test {
    useJUnitPlatform()
}