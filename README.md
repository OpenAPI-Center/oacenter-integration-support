# oaboard-integration-support

Groups support tools to integrate with oaBoard

It is composed of tools/plugins to be used in a client application, in order to communicate with oaBoard server.  

## Modules

### oaboard-java-client

Standard Java application with common components for java plugins. 
Encapsulates logic to invoke the server and handles its response.

> Arch decision: All these modules for Java don't use Kotlin, to keep them smaller.

### oaboard-maven-plugin

Exposes the java-client to work with Maven.  

Use it with the goal `send`, invoke:

> mvn oaboard:send

**Parameters**:  

|    Name       | Description                     | Required      | Default value
|    base-url   | Base address of the server      |     yes       |
|    username   | Username, if required by server |    false      |
|    password   | Password, if required by server |    false      |
|    target-namespace   | Namespace to publish the app |    true      |
|    target-name   | App name to be pushed |    false      |  project.artifactId
|    app-url   | Address of the running application |    true      |  
|    app-version   | App version to be pushed |    false      |  project.version  
|    file   | Location of descriptor file to be uploaded |    true      |  

An **example** of its use can be found on test-pom.xml, in this project.

### oaboard-gradle-plugin

Integrates gradle with oaboard's java-client. 
It exposes one task `deployOpenApiDefinition`.

> gradlew deployOpenApiDefinition

**Properties**:

* extension.applicationUrl,
* extension.username, 
* extension.password
* extension.namespace,

* extension.applicationName, 
* extension.version,
* extension.applicationUrl

* extension.descriptorPath

> Their descriptions, required... are the same as on Maven's section. Check above!
 
 
