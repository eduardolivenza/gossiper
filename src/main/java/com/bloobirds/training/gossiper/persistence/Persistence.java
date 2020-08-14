package com.bloobirds.training.gossiper.persistence;

import com.bloobirds.training.gossiper.model.Connection;

import java.util.List;

public interface Persistence {

     void persists(List<Connection> connections) ;

     List<Connection> loadFile();

}
