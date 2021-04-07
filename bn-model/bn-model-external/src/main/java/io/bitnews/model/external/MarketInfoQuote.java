package io.bitnews.model.external;

import lombok.Data;

/**
 * Created by ywd on 2019/12/23.
 */
@Data
public class MarketInfoQuote {

    private String code_name;
    private String price;
    private String market_cap;
    private String percent_change_24h;
    private boolean flag;
}
