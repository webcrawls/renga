plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "cafe.navy.renga"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:4.6.4")
    implementation("io.pebbletemplates:pebble:3.1.5")
    implementation("org.thymeleaf:thymeleaf:3.0.12.RELEASE")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation("com.google.code.gson:gson:2.9.1")
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    jar {
        manifest {
            attributes(
                "Main-Class" to "sh.kaden.renga.RengaApp"
            )
        }
    }

    shadowJar {
        archiveBaseName.set("renga")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}