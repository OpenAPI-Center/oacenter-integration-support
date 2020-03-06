package org.rss.oaboard.tools.test;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rss.oaboard.tools.client.OaBoardAgentClient;
import org.rss.oaboard.tools.pojo.AppRecord;

@Tag("integration")
public class OaBoardAgentClientIntegrationTest {

    OaBoardAgentClient tested = new OaBoardAgentClient("http://localhost:8080/",
                                                       null,
                                                       null,
                                                       System.out::println);

    @Test
    void invokeRemoteService() {
        tested.pushConfiguration(new AppRecord("production", "oaboard-test", "1.0-SNAPSHOT",
                                               null,
                                               getClass().getResourceAsStream("/test-descriptor.yaml")));
    }

    @Test
    void testConnection() {
        tested.testConnection();
    }
}
