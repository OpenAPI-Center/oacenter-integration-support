package org.rss.oacenter.tools;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rss.oacenter.tools.client.OaCenterAgentClient;
import org.rss.oacenter.tools.pojo.ApiRecord;

/**
 * Goal to call remote server and send the given app information
 */
@Mojo(name = "send", requiresOnline = true)
public class AgentCallerMojo extends AbstractMojo {

    private static final String DEFAULT_NAMESPACE = "default";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(alias = "base-url", required = true, readonly = true)
    private String serverBaseUrl;

    @Parameter(alias = "username", readonly = true)
    private String serverUsername;

    @Parameter(alias = "password", readonly = true)
    private String serverPassword;

    @Parameter(alias = "target-namespace", readonly = true, required = true)
    private String namespace;

    @Parameter(property = "target-name", alias = "target-name", readonly = true, defaultValue = "${project.artifactId}")
    private String apiName;

    @Parameter(alias = "api-url", readonly = true, required = true)
    private String apiAddress;

    @Parameter(name = "api-version", readonly = true, defaultValue = "${project.version}")
    private String apiVersion;

    @Parameter(alias = "file", readonly = true, required = true)
    private String fileLocation;

    @Parameter(alias = "requires-auths", readonly = true)
    private String[] requiredAuthorities;

    public void execute() throws MojoExecutionException {
        getLog().info("Executing oaCenter:send");
        validateServer(serverBaseUrl);
        final OaCenterAgentClient client = new OaCenterAgentClient(serverBaseUrl, serverUsername, serverPassword, this::logDebug);

        try {
            client.pushConfiguration(new ApiRecord(namespace, apiName, apiVersion, apiAddress, loadFile(),
                    Arrays.stream(requiredAuthorities).collect(Collectors.toList())));
        } catch (RuntimeException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private InputStream loadFile() throws MojoExecutionException {
        if (fileLocation == null || Files.notExists(Paths.get(fileLocation))) {
            throw new MojoExecutionException("The file location is not valid, or the file is not accessble: " + fileLocation);
        }
        try {
            return Files.newInputStream(Paths.get(fileLocation));
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void validateServer(final String serverBaseUrl) throws MojoExecutionException {
        if (serverBaseUrl == null || serverBaseUrl.trim().isEmpty()) {
            throw new MojoExecutionException("Required configuration 'base-url' was not specified or it is empty.");
        }
    }

    private void logDebug(final String s) {
        getLog().debug("[oaCenter] " + s);
    }


}
