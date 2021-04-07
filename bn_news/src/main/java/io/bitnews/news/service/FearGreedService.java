package io.bitnews.news.service;

import io.bienews.common.helper.StringUtil;
import io.bitnews.model.em.FearEnum;
import io.bitnews.model.internal.FearGreedVo;
import io.bitnews.model.news.po.TFearGreed;
import io.bitnews.news.dao.TFearGreedDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/11/27.
 */
@Service
public class FearGreedService {

    @Autowired
    private TFearGreedDao tFearGreedDao;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    boolean isSaveToDay(String time) {
        int i = tFearGreedDao.toTimeDays(time);
        return i == 1?true:false;
    }

    public List<FearGreedVo> latest(String limit) {
        List<FearGreedVo> rs = new ArrayList<>();
        if (StringUtil.isEmpty(limit)) {
            TFearGreed latest = tFearGreedDao.lastest();
            rs.add(transfrom(latest));
        }else {
            List<TFearGreed> lists = tFearGreedDao.query(Integer.parseInt(limit));
            rs = transfrom(lists);
        }
        return rs;
    }

    public void save(FearGreedVo fearGreedVo) {
        boolean saveToDay = isSaveToDay(formatter.format(fearGreedVo.getUpdateTime()));
        if (!saveToDay) {
            TFearGreed tFearGreed = new TFearGreed();
            BeanUtils.copyProperties(fearGreedVo, tFearGreed);
            tFearGreedDao.insert(tFearGreed);
        }
    }



    FearGreedVo transfrom(TFearGreed tFearGreed) {
        FearGreedVo fearGreedVo = new FearGreedVo();
        BeanUtils.copyProperties(tFearGreed, fearGreedVo);
        fearGreedVo.setValueClassification(FearEnum.getName(tFearGreed.getValueClassification()));
        return fearGreedVo;
    }

    List<FearGreedVo> transfrom(List<TFearGreed> tFearGreeds) {
        List<FearGreedVo> collect =
                tFearGreeds.stream().map(tFearGreed -> transfrom(tFearGreed)).collect(Collectors.toList());
        return collect;
    }
}
