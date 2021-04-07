package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.math.BigDecimal;
import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-03
 */
@Table(name = "t_coin_market")
@NoArgsConstructor
@Data
public class TCoinMarket {

    /*
    ID
    */
    private Long id;
    /*
    币种代码: BTC
    */
    private String coinCode;
    /*
    行情价格，单位USDT
    */
    private BigDecimal price;
    /*
    市场总量，单位亿USDT
    */
    private BigDecimal marketCap;
    /*
    更新时间
    */
    private Date updateTime;
}
