package org.rss.oaboard.tools.pojo;

import java.io.InputStream;

/** Simple Pojo representing the current api entry to be uploaded */
public class AppRecord {

    private final String namespace;

    private final String apiName;

    private final String version;

    private final String url;

    private final InputStream descriptorFile;

    public AppRecord(final String namespace, final String apiName, final String version, final String url, final InputStream descriptorFile) {
        this.namespace = namespace;
        this.apiName = apiName;
        this.version = version;
        this.url = url;
        this.descriptorFile = descriptorFile;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getApiName() {
        return apiName;
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

    @Override
    public String toString() {
        return "ApiRecord{" + "namespace='" + namespace + '\'' + ", apiName='" + apiName + '\'' + ", version='" + version + '\'' + ", url='" + url + '\''
            + ", descriptorFile=" + descriptorFile + '}';
    }
}
