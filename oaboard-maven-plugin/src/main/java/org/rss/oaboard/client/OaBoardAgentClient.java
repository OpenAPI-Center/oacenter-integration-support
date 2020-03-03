package org.rss.oaboard.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.rss.oaboard.pojo.AppRecord;

/** Client for Agent endpoint on oaBoard Server */
public class OaBoardAgentClient {

    private final Client client;
    private final WebTarget baseTarget;

    public OaBoardAgentClient(final String serverUrl, final String username, final String password) {
        this.client = ClientBuilder
            .newBuilder()
            .register(MultiPartFeature.class)
            .newClient();
        this.baseTarget = client.target(serverUrl);
    }

    public void pushConfiguration(final AppRecord record) {
        validateRecord(record);
        
        WebTarget path = baseTarget.path(record.getNamespace()).path(record.getAppName());

        Response response;
        FormDataMultiPart multipart = new FormDataMultiPart();
        try {
            multipart
                .field("version", record.getVersion())
                .field("url", record.getUrl())
                .bodyPart(new StreamDataBodyPart("file", record.getDescriptorFile()));

            response = path.request(MediaType.APPLICATION_FORM_URLENCODED)
                                    .buildPost(Entity.entity(multipart, multipart.getMediaType()))
                                    .invoke();
        } finally {
            try {
                if (multipart != null) {
                    multipart.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }

        handleResponse(response);
    }

    private void validateRecord(final AppRecord record) {
    }

    private void handleResponse(final Response response) {
        // TODO
        System.out.println(response.getStatus());
        System.out.println(response.getEntity());
    }
}
