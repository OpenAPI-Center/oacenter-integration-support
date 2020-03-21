package org.rss.oaboard.tools.gradle;

import org.gradle.api.GradleException;
import org.gradle.api.GradleScriptException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.rules.RuleActionValidationException;
import org.rss.oaboard.tools.client.OaBoardAgentClient;
import org.rss.oaboard.tools.pojo.AppRecord;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OaBoardClientPlugin implements Plugin<Project> {

    public OaBoardClientPlugin() {
    }

    @Override
    public void apply(final Project project) {
        final PluginProperties extension = project.getExtensions().create("oab", PluginProperties.class);

        project.task("deployOpenApiDefinition", task -> {

            task.doLast(t -> {
                project.getLogger().info("Starting OaBoardGradlePlugin ");
                System.out.println("Starting");

                validateRequest(extension.server, extension.namespace);
                loadDefaultValues(extension, project);

                OaBoardAgentClient client = new OaBoardAgentClient(extension.applicationUrl,
                        extension.username, extension.password, project.getLogger()::debug);

                try {
                    client.pushConfiguration(new AppRecord(extension.namespace,
                            extension.applicationName, extension.version,
                            extension.applicationUrl,
                            loadFile(extension.descriptorPath)));
                } catch (RuntimeException e) {
                    throw new GradleException(e.getMessage(), e);
                }

            });
        });
    }

    private void loadDefaultValues(PluginProperties properties, Project project) {
        if (properties.applicationName == null) {
            properties.applicationName = project.getName();
        }
        if (properties.version == null) {
            properties.version = project.getVersion().toString();
        }
    }

    private InputStream loadFile(String descriptorPath) {
        if (descriptorPath == null || Files.notExists(Paths.get(descriptorPath))) {
            throw new RuleActionValidationException(
                    "The file location is not valid, or the file is not accessble: " + descriptorPath);
        }
        try {
            return Files.newInputStream(Paths.get(descriptorPath));
        } catch (IOException e) {
            throw new GradleScriptException(e.getMessage(), e);
        }
    }

    private void validateRequest(final String serverBaseUrl, String namespace) {
        if (serverBaseUrl == null || serverBaseUrl.trim().isEmpty()) {
            throw new RuleActionValidationException("Required configuration 'server' was not specified or it is empty.");
        }
        if (namespace == null || namespace.trim().isEmpty()) {
            throw new RuleActionValidationException("Required configuration 'namespace' was not specified or it is empty.");
        }
    }
}

class PluginProperties {

    protected String server;

    protected String applicationName;

    protected String namespace;

    protected String version;

    protected String applicationUrl;

    protected String descriptorPath;

    protected String username;

    protected String password;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public String getDescriptorPath() {
        return descriptorPath;
    }

    public void setDescriptorPath(String descriptorPath) {
        this.descriptorPath = descriptorPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PluginProperties() {
    }

    public PluginProperties(String server, String applicationName, String namespace, String version, String applicationUrl, String descriptorPath, String username, String password) {
        this.server = server;
        this.applicationName = applicationName;
        this.namespace = namespace;
        this.version = version;
        this.applicationUrl = applicationUrl;
        this.descriptorPath = descriptorPath;
        this.username = username;
        this.password = password;
    }
}