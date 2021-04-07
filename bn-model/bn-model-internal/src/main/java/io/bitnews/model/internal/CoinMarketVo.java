package io.bitnews.model.internal;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2019/7/12.
 */
@NoArgsConstructor
@Data
public class CoinMarketVo {

    /*
	ID
	*/
    private Long id ;
    /*
    币种代码: BTC
    */
    private String coinCode ;
    /*
    行情价格，单位USDT
    */
    private BigDecimal price ;
    /*
    更新时间
    */
    private Date updateTime ;
}
