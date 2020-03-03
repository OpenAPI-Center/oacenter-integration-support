package org.rss.oaboard.pojo;

import java.io.InputStream;

/** Simple Pojo representing the current app entry to be uploaded */
public class AppRecord {

    private final String namespace;

    private final String appName;

    private final String version;

    private final String url;

    private final InputStream descriptorFile;

    public AppRecord(final String namespace, final String appName, final String version, final String url, final InputStream descriptorFile) {
        this.namespace = namespace;
        this.appName = appName;
        this.version = version;
        this.url = url;
        this.descriptorFile = descriptorFile;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getAppName() {
        return appName;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public InputStream getDescriptorFile() {
        return descriptorFile;
    }
}
