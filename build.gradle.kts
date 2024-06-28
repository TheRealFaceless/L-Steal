plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.dev.faceless"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    implementation(files("C:\\Users\\Faceless\\Desktop\\Minecraft DevKit\\libraries\\SwiftLib-1.0-dev.jar"))
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set("")
    val path = "Server/plugins"
    val default = "${layout.buildDirectory.get().asFile}/libs"

    if (file(path).exists()) destinationDirectory.set(file(path))
    else destinationDirectory.set(file(default))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

