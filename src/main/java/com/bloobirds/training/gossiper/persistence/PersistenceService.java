package com.bloobirds.training.gossiper.persistence;

import com.bloobirds.training.gossiper.GossiperConfigurationProperties;
import com.bloobirds.training.gossiper.model.Connection;
import com.bloobirds.training.gossiper.persistence.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(GossiperConfigurationProperties.class)
public class PersistenceService implements Persistence{

    private final FileParser parser;
    private final OutputWritter writter;
    private final GossiperConfigurationProperties properties;

    public void persists(List<Connection> connections) {
        writter.writeOuptut("storage//" + properties.getOwnName()+ ".txt", connections);
    }

    public List<Connection> loadFile(){
        return parser.parseEntry("storage//" + properties.getOwnName()+ ".txt");
    };

}
