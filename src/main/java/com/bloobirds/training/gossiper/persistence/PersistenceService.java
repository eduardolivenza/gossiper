package com.bloobirds.training.gossiper.persistence;

import com.bloobirds.training.gossiper.model.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PersistenceService {

    public void persists(List<Connection> connections){
        log.info("persisting "+ connections.size());
    }

    public void loadFile(String filename){};

}
