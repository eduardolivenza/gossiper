package com.bloobirds.training.gossiper.endpoint;

import com.bloobirds.training.gossiper.model.ConnectionTable;
import com.bloobirds.training.gossiper.model.Data;
import com.bloobirds.training.gossiper.model.DataResponse;
import com.bloobirds.training.gossiper.model.DataTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@Slf4j
public class DataController {

    private final DataTable dataTable;

    @JsonPost("/data")
    public void inputData(@RequestBody DataResponse data) {
        dataTable.createUpdateData(Collections.singletonList(new Data(new Date(), data.getDataKey(), data.getDataContent())));
    }

    @JsonGet("/data")
    public String queryAnswer(@RequestParam String key) {
        return dataTable.getContentByKey(key);
    }

}
