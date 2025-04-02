plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.yourname"
version = "0.2"

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.5-R0.1-SNAPSHOT")
    implementation("org.mindrot:jbcrypt:0.4")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set("RaLP-${version}.jar")
        relocate("org.mindrot.jbcrypt", "me.yourname.ralp.libs.jbcrypt")
    }
}