package io.bitnews.model.external;

import io.bitnews.model.em.ChoiceEnum;
import io.bitnews.model.em.GuessStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2019/12/23.
 */
@Data
@ApiModel
public class GuessInfo {

    /*
	自增ID
	*/
    private Long id;
    /*
    竞猜主题
    */
    private String topic;
    /*
	内容
	*/
    private String context;
    /*
	发起人
	*/
    private Long createUserId;
    private String createUserName;
    private String createUserImage;
    private Date createTime;
    /*
	讨论ID
	*/
    private Long discussionId;
    private String title;
    /*
	发布状态：0-开盘, 1-封盘, 2-结算
	*/
    @ApiModelProperty(notes = "发布状态：0-开盘, 1-封盘, 2-结算")
    private String status;
    private String statusStr;
    /*
    竞猜币种
    */
    private String tokenType;
    /*
    预测价格
    */
    private BigDecimal guessGold;
    /*
    竞猜类型: 1-自动收益, 2-水友开盘
    */
    private String type;
    /*
	猜胜方：0-支持; 1-反对
	*/
    private String guessWinner;
    private String guessWinnerStr;
    /*
    投注截止时间
    */
    private Date permitTime;
    /*
	结算时间
	*/
    private Date settlementTime;
    /*
    能
    */
    private BigDecimal yes;
    private Integer yesNumber;
    private BigDecimal yesOdds;
    /*
	不能
	*/
    private BigDecimal no;
    private Integer noNumber;
    private BigDecimal noOdds;



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

}
