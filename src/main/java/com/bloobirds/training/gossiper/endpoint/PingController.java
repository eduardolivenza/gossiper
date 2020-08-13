package com.bloobirds.training.gossiper.endpoint;

import com.bloobirds.training.gossiper.model.Connection;
import com.bloobirds.training.gossiper.model.ConnectionTable;
import com.bloobirds.training.gossiper.GossiperConfigurationProperties;
import com.bloobirds.training.gossiper.model.GossiperResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@EnableConfigurationProperties(GossiperConfigurationProperties.class)
@Slf4j
public class PingController {

    private final GossiperConfigurationProperties properties;
    private final ConnectionTable connectionTable;

    @JsonPost("/ping")
    public @ResponseBody
    GossiperResponse answer(@RequestBody GossiperResponse newConnections, ServletRequest servletRequest) {
        //log.info("RECEIVED ping from " + newConnections.getName());
        connectionTable.addConnections(newConnections.getConnections());
        connectionTable.addConnections(Collections.singletonList(new Connection(newConnections.getName(), servletRequest.getServerName() + ":" + newConnections.getPort())));
        return new GossiperResponse(properties.getOwnName(), properties.getPort(),  connectionTable.getAll());
    }

}
