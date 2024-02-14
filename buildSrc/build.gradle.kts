plugins {
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("keycloak-dev") {
            id = "fyi.zed.kc.ext.gradle"
            implementationClass = "fyi.zed.kc.ext.gradle.KeycloakDevPlugin"
        }
    }
}