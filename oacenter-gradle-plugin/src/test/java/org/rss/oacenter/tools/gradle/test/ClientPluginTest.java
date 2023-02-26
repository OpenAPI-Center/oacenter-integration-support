package org.rss.oacenter.tools.gradle.test;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.rss.oacenter.tools.gradle.OaCenterClientPlugin;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientPluginTest {

    @Test
    void testPlugin() {
        Project project = ProjectBuilder.builder().build();
//        project.getPluginManager().apply("org.rss.center.tools.gradle");
        project.getPluginManager().apply(OaCenterClientPlugin.class);

        Set<Task> task = project.getTasksByName("deployOpenApiDefinition", false);
        assertTrue(task.size() == 1);

        task.iterator().next().doLast(t -> {
            System.out.println("Last");
        });

        System.out.println("Done");

    }
}
