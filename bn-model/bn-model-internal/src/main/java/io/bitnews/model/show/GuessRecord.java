package io.bitnews.model.show;

import io.bitnews.model.em.BetStatusEnum;
import io.bitnews.model.em.ChoiceEnum;
import io.bitnews.model.em.GuessStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2019/11/28.
 */
@Data
@NoArgsConstructor
public class GuessRecord {
    /*
	自增ID
	*/
    private Long id;

    /*
    竞猜主题
    */
    private String topic;
    /*
    竞猜盘id
    */
    private Long promoterId;
    /*
    竞猜盘主题id
    */
    private Long promoterTopicId;
    /*
	发布状态：0-开盘, 1-封盘, 2-结算
	*/
    private String status;
    private String statusStr;

    public String getStatusStr() {
        if (null != status) {
            return GuessStatusEnum.getName(Integer.parseInt(status));
        }
        return statusStr;
    }

    public String getGuessWinnerStr() {
        if (null != guessWinner) {
            return ChoiceEnum.getName(Integer.parseInt(guessWinner));
        }
        return guessWinnerStr;
    }

    /*
        猜胜方：0-能; 1-不能
        */
    protected String guessWinner;
    protected String guessWinnerStr;

    /*
    赔率
    */
    private BigDecimal odds;

    /*
	投入金额
	*/
    protected BigDecimal betsGold;

    /*
    收益
    */
    protected BigDecimal income;

    /*
	创建时间
	*/
    protected Date createTime;
}
