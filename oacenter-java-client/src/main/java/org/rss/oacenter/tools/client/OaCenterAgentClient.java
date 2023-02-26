package org.rss.oacenter.tools.client;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.rss.oacenter.tools.pojo.ApiRecord;
import org.rss.oacenter.tools.pojo.OaCenterValidationError;

/** Client for Agent endpoint on oaCenter Server */
public class OaCenterAgentClient {

    private static final Pattern PATTERN = Pattern.compile("\\w+(([-.])\\w+)*");

    private final Client client;
    private final WebTarget baseTarget;
    private final Consumer<String> logger;

    public OaCenterAgentClient(final String serverUrl, final String username, final String password,
                               final Consumer<String> logger) {
        this.logger = logger;
        this.client = ClientBuilder
            .newBuilder()
            .register(JacksonFeature.class)
            .register(MultiPartFeature.class)
            .register(HttpAuthenticationFeature.basic(username, password))
            .build();

        this.baseTarget = client.target(serverUrl);
        logger.accept("Setting oaCenter client with target: " + this.baseTarget.toString());
    }

    public OaCenterAgentClient(final Client client, final WebTarget baseTarget, final Consumer<String> logger) {
        this.client = client;
        this.baseTarget = baseTarget;
        this.logger = logger;
    }

    public void pushConfiguration(final ApiRecord record) {
        validateRecord(record);
        logger.accept("Pushing " + record);
        
        final WebTarget path = baseTarget
            .path("namespaces")
            .path(record.getNamespace())
            .path("apis")
            .path(record.getApiName());

        final String requiredAuths = String.join(",",
                (record.getRequiredAuthorities() != null) ? record.getRequiredAuthorities() : List.of());

        try (FormDataMultiPart multipart = new FormDataMultiPart()) {
            multipart
                .field("version", record.getVersion())
                .field("url", record.getUrl() != null ? record.getUrl() : "")
                .field("requiredAuths", requiredAuths)
                .bodyPart(new StreamDataBodyPart("file", record.getDescriptorFile()));

            final Response response = path.request(MediaType.APPLICATION_JSON)
                                    .put(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA));

            handleResponse(response);
        } catch (IOException | WebApplicationException e) {
            logger.accept(e.getMessage());
            throw new RuntimeException("Error calling remote service. ", e);
        }
    }

    public void testConnection() {
        Response result = baseTarget.path("test").path("ping").request(MediaType.APPLICATION_JSON).get();

        switch (result.getStatusInfo().toEnum()) {
        case NOT_FOUND:
            throw new RuntimeException("The path to the agent was not found on the server. Check your configuration and remove extra parts on the path ");
        case INTERNAL_SERVER_ERROR:
            throw new RuntimeException("An unexpected error happened on the server while trying to access the endpoint. "
                                           + result.getEntity());
        }

        if (result.getStatus() >= 400) {
            throw new RuntimeException("Unspecified error: " + result.getEntity());
        }

    }

    private void validateRecord(final ApiRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("ApiRecord must be non null");
        }
        if (record.getNamespace() == null || record.getNamespace().isEmpty()) {
            throw new IllegalArgumentException("Target namespace is mandatory, but current value is empty");
        }
        if (record.getApiName() == null || record.getApiName().isEmpty()) {
            throw new IllegalArgumentException("Api name is mandatory, but current value is empty");
        }
        if (!PATTERN.matcher(record.getNamespace()).matches() || !PATTERN.matcher(record.getApiName()).matches()) {
            throw new IllegalArgumentException("Namespace and API name must be valid, containing only text, dash or dot");
        }
        if (record.getDescriptorFile() == null) {
            throw new IllegalArgumentException("Descriptor file is required. Indicate the location of a text file.");
        }
        if (record.getUrl() == null) {
            throw new IllegalArgumentException("App URL file is mandatory, but current value is empty");
        }
    }

    private void handleResponse(final Response response) {
        if (response.getStatusInfo() != null) {
            logger.accept("Response: " + response.getStatusInfo().getReasonPhrase());
        } else {
            logger.accept("Got response with no payload and status " + response.getStatus());
        }

        if (response.getStatus() >= 400 && response.getStatus() < 500) {
            String message = "The request failed with the message: " + response.getStatusInfo().getReasonPhrase();
            switch (response.getStatus()) {
            case 400:
                message = "The request failed due to a bad request. This usually indicates that some parameter was incorrect, or the request does not match "
                    + "what is expected on server side";
                break;
            case 409:
                message = tryGettingAppMessage(response);

                break;
            }
            throw new RuntimeException(message);
        }
        if (response.getStatus() >= 500) {
            throw new RuntimeException("The server could not process the request due to some internal error. Check the server health status");
        }

        logger.accept("Request successfully processed. ");
    }

    private String tryGettingAppMessage(final Response response) {
        try {
            final OaCenterValidationError validationError = response.readEntity(OaCenterValidationError.class);
            if (validationError.getRApp() == null) {
                throw new IllegalStateException();
            }

            return "The request failed due to a conflict with the server, with cause: " + validationError.getCause();
        } catch (ProcessingException | IllegalStateException e) {
            return "The request failed due to a conflict with the server. This usually indicates that given entry can not be processed because it "
                + "violates some validation. ";
        }
    }
}
