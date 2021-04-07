package io.bitnews.model.external;

import lombok.Data;

/**
 * Created by ywd on 2019/12/27.
 */
@Data
public class MarketSort {

    private String symbol;
    private Double price;
    private Double percent_change_24h;
}
