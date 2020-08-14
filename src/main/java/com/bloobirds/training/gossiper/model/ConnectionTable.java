package com.bloobirds.training.gossiper.model;

import com.bloobirds.training.gossiper.GossiperConfigurationProperties;
import com.bloobirds.training.gossiper.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@EnableConfigurationProperties({GossiperConfigurationProperties.class})
public class ConnectionTable {

    private final Set<Connection> connections;
    private final GossiperConfigurationProperties properties;
    private final Persistence persistenceService;

    public ConnectionTable(GossiperConfigurationProperties properties, Persistence persistenceService) {
        this.properties = properties;
        this.persistenceService = persistenceService;
        connections = Collections.synchronizedSet(new HashSet<>());
        if (properties.getSeedHostname() != null ) {
            connections.addAll(Collections.singleton(new Connection(properties.getSeedHostname())));
        }
        addConnections(persistenceService.loadFile());
    }

    public void addConnections( Collection<Connection> newConnections) {
        newConnections.forEach(newConnection -> {
            if (properties.getMyHostName().equals(newConnection.getHostname()) ){
                return;
            }
            boolean success = connections.add(newConnection);
            if (success) {
                log.info("Node {} joined", newConnection.getHostname());
            }
        });
    }

    public void remove(Connection connection) {
        boolean success = connections.remove(connection);
        if (success) {
            log.info("Node {} left", connection.getHostname());
        }
    }

    public List<Connection> get(int n) {
        List<Connection> copy = new ArrayList<>(connections);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(copy.size(), n));
    }

    public List<Connection> getAll() {
        return new ArrayList<>(connections);
    }

}
