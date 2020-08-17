package com.bloobirds.training.gossiper.model;

import com.bloobirds.training.gossiper.GossiperConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@EnableConfigurationProperties({GossiperConfigurationProperties.class})
public class DataTable {

    private final Map<String, Data> dataStructure;

    public DataTable() {
        dataStructure = Collections.synchronizedMap(new HashMap<>());
    }

    public void createUpdateData(Collection<Data> dataList) {
        dataList.forEach(data -> {
            if (this.dataStructure.containsKey(data.getDataKey())){
                if  (this.dataStructure.get(data.getDataKey()).getDataDate().compareTo(data.getDataDate()) < 0 ) {
                    this.dataStructure.replace(data.getDataKey(), data);
                    log.info("Value for key " +  data.getDataKey() + " updated to " + data.getDataContent());
                }
                return;
            }
            else {
                this.dataStructure.put(data.getDataKey(), data);
                log.info("Inserted value for " +  data.getDataKey() + " with content: " + data.getDataContent());
            }
        });
    }

    public List<Data> getAll() {
        return new ArrayList<>(dataStructure.values());
    }

    public String getContentByKey(String key){
        return this.dataStructure.get(key).getDataContent();
    }

}
