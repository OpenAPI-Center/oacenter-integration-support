package org.rss.oacenter.tools.pojo;

import java.io.InputStream;
import java.util.List;

/** Simple Pojo representing the current api entry to be uploaded */
public class ApiRecord {

    private final String namespace;

    private final String apiName;

    private final String version;

    private final String url;

    private final InputStream descriptorFile;

    private final List<String> requiredAuthorities;

    public ApiRecord(final String namespace, final String apiName, final String version, final String url, final InputStream descriptorFile,
                     final List<String> requiredAuthorities) {
        this.namespace = namespace;
        this.apiName = apiName;
        this.version = version;
        this.url = url;
        this.descriptorFile = descriptorFile;
        this.requiredAuthorities = requiredAuthorities;
    }

    public ApiRecord(final String namespace, final String apiName, final String version, final String url, final InputStream descriptorFile) {
        this(namespace, apiName, version, url, descriptorFile, null);
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

    public List<String> getRequiredAuthorities() {
        return requiredAuthorities;
    }

    @Override
    public String toString() {
        return "ApiRecord{" + "namespace='" + namespace + '\'' + ", apiName='" + apiName + '\'' + ", version='" + version
                + '\'' + ", url='" + url + '\''
            + ", descriptorFile=" + descriptorFile + ", requiredAuths=" + requiredAuthorities  + '}';
    }
}
