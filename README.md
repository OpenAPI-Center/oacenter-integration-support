# oacenter-integration-support

Groups support tools for integration with oaCenter

It is composed of tools/plugins to be used in a client application, in order to communicate with oaCenter server.  

## Modules

### oacenter-java-client

Standard Java application with common components for java plugins. 
Encapsulates logic to invoke the server and handles its response.

Usually, users won't use this component directly. Instead, it'll likely be used through one of below plugins.

> Arch decision: All these modules for Java don't use Kotlin, to keep them smaller.

### oacenter-maven-plugin

Exposes the java-client to work with Maven.  

Use it with the goal `send`, invoke:

> mvn oacenter:send

**Parameters**:  


| Name             | Description                                         | Required | Default value | 
|------------------|-----------------------------------------------------|----------|---|
| base-url         | Base address of the server                          | yes      | |
| username         | Username, if required by server                     | false    | |
| password         | Password, if required by server                     | false    | |
| target-namespace | Namespace to publish the API                        | true     | |
| target-name      | App name to be pushed                               | false    |  project.artifactId |
| api-url          | Address of the running application                  | true     | |  
| api-version      | API version to be pushed                            | false    |  project.version |  
| file             | Location of descriptor file to be uploaded          | true     | |  
| requires-auths   | List (<value>) of Permissions required for this API | false    | |  

An **example** of its use can be found on test-pom.xml, in this project.

### oacenter-gradle-plugin

Integrates gradle with oacenter's java-client. 
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

> Their descriptions, required fields... are the same as on Maven's section. Check above!
 
 
