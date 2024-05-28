plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "org.dzmitrykashlach"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.5")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.5:models")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.5:models-english")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.5:models-english-kbp")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
