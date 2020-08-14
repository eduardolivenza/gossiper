package com.bloobirds.training.gossiper.persistence.util;


import com.bloobirds.training.gossiper.model.Connection;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileParser {

    public List<Connection> parseEntry(String inputFile) {
        List<Connection> connections = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null)   {
                connections.add(new Connection(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connections;
    }

}
