package io.bitnews.model.external;

import lombok.Data;

import java.util.List;

/**
 * Created by ywd on 2019/12/23.
 */
@Data
public class MarketInfo {

//    private String total_market_cap;
//    private String total_market_percent;
    private List<MarketInfoQuote> list;
}
