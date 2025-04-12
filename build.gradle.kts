plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.hantu"
version = "0.3"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
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
        relocate("org.mindrot.jbcrypt", "net.hantu.ralp.libs.jbcrypt")
    }
}