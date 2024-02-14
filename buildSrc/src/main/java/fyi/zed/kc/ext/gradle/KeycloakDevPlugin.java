package fyi.zed.kc.ext.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.JavaExec;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeycloakDevPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        KeycloakDevPluginExtension keycloakExt = target.getExtensions().create("keycloakExt", KeycloakDevPluginExtension.class);
        keycloakExt.getKeycloakVersion().convention("latest");
        Configuration keycloakRuntime = target.getConfigurations()
                .create("keycloakRuntime", configuration -> {
                    configuration.setVisible(true);
                    configuration.setCanBeResolved(true);
                    configuration.setCanBeConsumed(true);
                    configuration.setDescription("Development Keycloak");
                    configuration.defaultDependencies(d -> {
                        //d.add(target.getDependencies().create("org.keycloak:keycloak-quarkus-server:"+keycloakExt.getKeycloakVersion().get()));
                        d.add(target.getDependencies().create("org.keycloak:keycloak-quarkus-server-app:"+keycloakExt.getKeycloakVersion().get()));
                        d.add(target.getDependencies().create("org.keycloak:keycloak-quarkus-server-deployment:"+keycloakExt.getKeycloakVersion().get()));
                        //d.add(target.getDependencies().create("org.keycloak:keycloak-quarkus-server:"+keycloakExt.getKeycloakVersion().get()))
                    });
                });
        target.getTasks().register("runKeycloakDevMode", JavaExec.class, run -> {
            run.setGroup("keycloak");
            run.setDescription("Run Keycloak Dev");
            ConfigurableFileCollection files = target.files(keycloakRuntime.getFiles());
            System.out.println("+ CLASSPATH START +");
            for (File file : files) {
                System.out.println(file.toString());
            }
            System.out.println("+ CLASSPATH END +");

            // Adapted and pulled from kc.sh https://github.com/keycloak/keycloak/blob/main/quarkus/dist/src/main/content/bin/kc.sh


            // TODO setup default conf etc


            //run.environment("QUARKUS_LAUNCH_DEVMODE", "true");
            run.setWorkingDir("work");
            run.setSystemProperties(
                    Map.of(
                            "file.encoding", "UTF-8",
                            "sun.stdout.encoding", "UTF-8",
                            "sun.err.encoding", "UTF-8",
                            "stdout.encoding", "UTF-8",
                            "stderr.encoding", "UTF-8",
                            "java.security.egd", "file:/dev/urandom"

                    )

            );
            run.setJvmArgs(List.of(
                    "-XX:MetaspaceSize=96M",
                    "-XX:MaxMetaspaceSize=256m",
                    "-XX:+ExitOnOutOfMemoryError",
                    "-XX:+UseParallelGC",
                    "-XX:MinHeapFreeRatio=10",
                    "-XX:MaxHeapFreeRatio=20",
                    "-XX:GCTimeRatio=4",
                    "-XX:AdaptiveSizePolicyWeight=90",
                    "-XX:FlightRecorderOptions=stackdepth=512",
                    "--add-opens=java.base/java.util=ALL-UNNAMED",
                    "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
                    "--add-opens=java.base/java.security=ALL-UNNAMED"

            ));
            run.setMaxHeapSize("512m");
            run.setMinHeapSize("64m");
            run.setClasspath(files);
            //run.getMainClass().set("org.keycloak.quarkus.runtime.KeycloakMain");
            run.getMainClass().set("org.keycloak.quarkus._private.IDELauncher");
            //run.getMainClass().set("io.quarkus.bootstrap.runner.QuarkusEntryPoint");
            run.setArgs(List.of("start-dev"));
        });
    }
}