package com.bloobirds.training.gossiper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Date;

@Value
public class Data {

    private final Date dataDate;
    private final String dataKey;
    private final String dataContent;

    public Data(@JsonProperty("dataDate") Date dataDate, @JsonProperty("dataKey") String dataKey, @JsonProperty("dataContent") String dataContent) {
        this.dataDate = dataDate;
        this.dataKey = dataKey;
        this.dataContent = dataContent;
    }
}
