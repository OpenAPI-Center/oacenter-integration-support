package org.rss.oacenter.tools.test;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rss.oacenter.tools.client.OaCenterAgentClient;
import org.rss.oacenter.tools.pojo.ApiRecord;

import java.util.List;

@Tag("integration")
public class OaCenterAgentClientIntegrationTest {

    OaCenterAgentClient tested = new OaCenterAgentClient("http://localhost:8080/",
                                                       "agent",
                                                       "test00",
                                                       System.out::println);

    @Test
    void invokeRemoteService() {
        tested.pushConfiguration(new ApiRecord("Production", "Orders", "1.3",
                                               "http://someplace/app",
                                               getClass().getResourceAsStream("/test-descriptor.yaml")));
    }


    @Test
    void invokeRemoteWithAuthority() {
        tested = new OaCenterAgentClient("http://localhost:8080/",
                "blue",
                "accessBlueOnly",
                System.out::println);

        tested.pushConfiguration(new ApiRecord("Production", "Orders", "1.3",
                "http://someplace/app",
                getClass().getResourceAsStream("/test-descriptor.yaml"), List.of("BLUE_PERM")));
    }

    @Test
    void testConnection() {
        tested.testConnection();
    }
}
