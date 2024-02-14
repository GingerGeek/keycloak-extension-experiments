package fyi.zed.kc.ext.gradle;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

public abstract class KeycloakDevPluginExtension {
    abstract public Property<String> getKeycloakVersion();
}
