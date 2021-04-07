package io.bitnews.model.external;

import lombok.Data;

import java.util.List;

/**
 * Created by ywd on 2019/12/27.
 */
@Data
public class DistributionInfo {

    int[] distribution;
    int upNum;
    int downNum;
    List<List<MarketSort>> details;

}
