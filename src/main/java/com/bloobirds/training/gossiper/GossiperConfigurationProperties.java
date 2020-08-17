package com.bloobirds.training.gossiper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("gossiper")
public class GossiperConfigurationProperties {

    private String clusterName;
    private String seedHostname;
    private String ownName;
    private String myHostName;
    private long pingTime = 5; // seconds
    private int pingAmount = 2;

}
