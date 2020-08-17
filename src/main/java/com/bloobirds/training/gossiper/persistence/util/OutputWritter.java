package com.bloobirds.training.gossiper.persistence.util;

import com.bloobirds.training.gossiper.model.Connection;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class OutputWritter {

    public void writeOuptut(String filename, List<Connection> connections){
        File file = new File(filename);
        file.getParentFile().mkdirs();
        try (FileWriter fr = new FileWriter(file);
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));)
        {
            for (Connection m: connections){
                writer.write(m.getHostname());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
