package com.bloobirds.training.gossiper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse {

    private Date date;
    private String dataKey;
    private String dataContent;

}
