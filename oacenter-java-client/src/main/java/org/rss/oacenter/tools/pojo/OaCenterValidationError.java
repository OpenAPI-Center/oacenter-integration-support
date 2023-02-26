package org.rss.oacenter.tools.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Reflects AppValidationError on Server side, which holds details when on error come */
public class OaCenterValidationError {

    @JsonProperty("rapp")
    private String rApp;

    private String cause;

    private Integer code;

    public String getRApp() {
        return rApp;
    }

    public void setRApp(final String rApp) {
        this.rApp = rApp;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(final String cause) {
        this.cause = cause;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }
}
