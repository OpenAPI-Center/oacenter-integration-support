package org.rss.oacenter.tools.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rss.oacenter.tools.client.OaCenterAgentClient;
import org.rss.oacenter.tools.pojo.ApiRecord;

@Tag("unit")
public class OaCenterAgentClientTest {

    @Mock
    Client client;

    @Mock
    WebTarget target;

    @Mock
    Invocation.Builder builder;

    OaCenterAgentClient tested;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        tested = new OaCenterAgentClient(client, target, System.out::println);

        when(target.path(anyString())).thenReturn(target);
        when(target.request(anyString())).thenReturn(builder);

    }

    @Test
    void invokeWithInvalidRecord() {
        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () ->
                tested.pushConfiguration(new ApiRecord(null, "name", "1.0", "http://address", null))),
            () -> assertThrows(IllegalArgumentException.class, () ->
                tested.pushConfiguration(new ApiRecord("Namespace", "", "", "http://address", stream))),
            () -> assertThrows(IllegalArgumentException.class, () ->
                tested.pushConfiguration(new ApiRecord("Namespace", null, null, "http://address", stream))),
            () -> assertThrows(IllegalArgumentException.class, () ->
                tested.pushConfiguration(new ApiRecord("SomeNs", "appName", "1.0", null, null)))
        );
    }

    @Test
    void invokeWithInvalidNames() {
        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->
                        tested.pushConfiguration(new ApiRecord("nsOk", "name/invalid", "1.0", "http://address", null))),
                () -> assertThrows(IllegalArgumentException.class, () ->
                        tested.pushConfiguration(new ApiRecord("Namespace-", "apiOk", "1.0", "http://address", stream)))
        );
    }

    @Test
    void checkResponse404() {
        when((builder.put(any()))).thenReturn(Response.status(404).build());
        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());

        assertThrows(RuntimeException.class,
                     () -> tested.pushConfiguration(new ApiRecord("test", "junit", "1.1", "http://localhost", stream)));
    }

    @Test
    void checkResponseValidationError() {
        when((builder.put(any()))).thenReturn(Response.status(409).entity("{'errorOrigin': 'oab', 'cause': 'testing purpose'}").build());
        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());

        assertThrows(RuntimeException.class,
                     () -> tested.pushConfiguration(new ApiRecord("test", "junit", "1.1", "http://localhost", stream)));
    }

    @Test
    void responseOk() {
        when((builder.put(any()))).thenReturn(Response.ok().build());

        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());
        tested.pushConfiguration(new ApiRecord("test", "junit", "1.1",
                                               "http://localhost", stream));
    }
}
