package com.bloobirds.training.gossiper.pinger;

import com.bloobirds.training.gossiper.GossiperConfigurationProperties;
import com.bloobirds.training.gossiper.model.*;
import com.bloobirds.training.gossiper.model.Connection;
import com.bloobirds.training.gossiper.persistence.Persistence;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
    private final Persistence persistenceService;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ConnectionTable connectionTable;
    private final DataTable dataTable;
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
        List<com.bloobirds.training.gossiper.model.Data> allData = dataTable.getAll();
        connections.forEach(connection -> executorService.execute(() -> this.ping(connection, allConnections, allData)));
        persistenceService.persists(connections);
    }

    private void ping(com.bloobirds.training.gossiper.model.Connection connection, List<Connection> allConnections, List<Data> allData) {
        Request req = null;
        try {
            req = new Request.Builder()
                .url("http://" + connection.getHostname() + "/ping")
                .post(RequestBody.create(MediaType.get("application/json"), objectMapper.writeValueAsString(new GossiperResponse(properties.getClusterName(), properties.getMyHostName(), allConnections, allData))))
                .build();
            Call call = okHttpClient.newCall(req);
            Response execute = call.execute();
            String responseBody;
            if (execute.body() != null) {
                responseBody = execute.body().string();
                GossiperResponse response = objectMapper.readValue(responseBody, GossiperResponse.class);
                connectionTable.addConnections( response.getConnections());
                dataTable.createUpdateData( response.getData());
                return;
            }
            connectionTable.remove(connection);
        } catch (IOException e) {
            connectionTable.remove(connection);
        }
    }

}
