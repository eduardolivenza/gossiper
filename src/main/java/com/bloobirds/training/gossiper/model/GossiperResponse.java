package com.bloobirds.training.gossiper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GossiperResponse {

    private String clusterName;
    private String hostName;
    private List<Connection> connections;

}
