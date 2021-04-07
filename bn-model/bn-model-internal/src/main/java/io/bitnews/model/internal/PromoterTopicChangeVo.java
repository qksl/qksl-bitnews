package io.bitnews.model.internal;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by ywd on 2019/8/26.
 */
@Data
public class PromoterTopicChangeVo {
    /*
   能
   */
    private BigDecimal yes;
    /*
	不能
	*/
    private BigDecimal no;
    /*
    能
    */
    private BigDecimal yesOdds;
    /*
	不能
	*/
    private BigDecimal noOdds;

    public PromoterTopicChangeVo(BigDecimal yes, BigDecimal no) {
        this.yes = yes;
        this.no = no;
        this.noOdds = yes.divide(no, 2, BigDecimal.ROUND_FLOOR);
        this.yesOdds = no.divide(yes, 2, BigDecimal.ROUND_FLOOR);
    }
}
