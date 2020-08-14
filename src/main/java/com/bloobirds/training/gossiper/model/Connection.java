package com.bloobirds.training.gossiper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Connection {

    private final String hostname;

    public Connection( @JsonProperty("hostname") String hostname) {
        this.hostname = hostname;
    }
}
