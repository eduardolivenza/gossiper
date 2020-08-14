package com.bloobirds.training.gossiper.pinger;

import com.bloobirds.training.gossiper.model.Connection;
import com.bloobirds.training.gossiper.model.ConnectionTable;
import com.bloobirds.training.gossiper.persistence.PersistenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bloobirds.training.gossiper.GossiperConfigurationProperties;
import com.bloobirds.training.gossiper.model.GossiperResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(GossiperConfigurationProperties.class)
public class PingerService {

    private ExecutorService executorService;
    private PersistenceService persistenceService;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ConnectionTable connectionTable;
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final ObjectMapper objectMapper;
    private final GossiperConfigurationProperties properties;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(properties.getPingAmount());
        scheduledExecutorService.scheduleAtFixedRate(this::ping, properties.getPingTime(), properties.getPingTime(), TimeUnit.SECONDS);
    }

    public void ping() {
        List<com.bloobirds.training.gossiper.model.Connection> connections = connectionTable.get(properties.getPingAmount());
        List<com.bloobirds.training.gossiper.model.Connection> allConnections = connectionTable.getAll();
        connections.forEach(connection -> executorService.execute(() -> this.ping(connection, allConnections)));
        //persistenceService.persists(connections);
    }

    private void ping(com.bloobirds.training.gossiper.model.Connection connection, List<Connection> allConnections) {
        Request req = null;
        try {
            req = new Request.Builder()
                .url("http://" + connection.getHostname() + "/ping")
                .post(RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(new GossiperResponse(properties.getClusterName(), properties.getMyHostName(), allConnections))))
                .build();
            Call call = okHttpClient.newCall(req);
            Response execute = call.execute();
            String responseBody;
            if (execute.body() != null) {
                responseBody = execute.body().string();
                GossiperResponse response = objectMapper.readValue(responseBody, GossiperResponse.class);
                connectionTable.addConnections( response.getConnections());
                return;
            }
            connectionTable.remove(connection);
        } catch (IOException e) {
            connectionTable.remove(connection);
        }
    }

}
