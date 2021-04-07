package io.bitnews.news.service;

import io.bienews.common.helper.PageUtil;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.internal.BannerVo;
import io.bitnews.model.news.po.TBanner;
import io.bitnews.news.dao.TBannerDao;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/7/12.
 */
@Service
@CacheConfig(cacheNames = "banner")
public class BannerService {

    @Autowired
    private TBannerDao tBannerDao;

    /**
     * 分页查找
     * @return
     */
    public BNPageResponse<BannerVo> queryByPage(PageQuery<TBanner> query, String cacheKey) {
        tBannerDao.queryByPage(query);
        List<TBanner> tList = query.getList();
        List<BannerVo> vList = transfrom(tList);
        return PageUtil.createPage(vList,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(), query.getTotalRow());
    }

    /**
     * 通过类型查找
     * @param type
     * @return
     */
    public List<BannerVo> queryByType(String type) {
        List<TBanner> tBanners = tBannerDao.queryByType(type);
        return transfrom(tBanners);
    }


    private List<BannerVo> transfrom(List<TBanner> list) {
        if (list == null) {
            return null;
        }
        return list.stream().map(po ->
                transfrom(po)
        ).collect(Collectors.toList());
    }

    private BannerVo transfrom(TBanner po) {
        if (po == null) {
            return null;
        }
        BannerVo vo = new BannerVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
