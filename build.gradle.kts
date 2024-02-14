plugins {
    id("java")
    id("fyi.zed.kc.ext.gradle")
}

group = "fyi.zed.kc.ext"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Actually work on the extension later...

}

keycloakExt {
    keycloakVersion = "23.0.6"
}