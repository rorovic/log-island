package com.hurence.logisland.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 04/07/16.
 */
public class ComponentConfiguration {

    private String component = "";
    private String version = "";
    private String documentation = "";
    private String type = "";

    private Map<String, String> configuration = new HashMap<>();

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return "ComponentConfiguration{" +
                "component='" + component + '\'' +
                ", version='" + version + '\'' +
                ", documentation='" + documentation + '\'' +
                ", type='" + type + '\'' +
                ", configuration=" + configuration +
                '}';
    }
}
