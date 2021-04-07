package io.bitnews.admin.service;

import io.bienews.common.helper.PageUtil;
import io.bitnews.admin.beanmapper.NewsBeanMapper;
import io.bitnews.admin.dao.news.TBannerDao;
import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.BannerVo;
import io.bitnews.model.news.po.TBanner;
import io.bitnews.model.news.po.TPost;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywd on 2019/7/22.
 */
@Service
public class BannerService {

    @Autowired
    private TBannerDao bannerDao;

    public void issue(BannerVo bannerVo) {
        TBanner tBanner = NewsBeanMapper.INSTANCE.toTBanner(bannerVo);
        bannerDao.insert(tBanner);
    }

    public void delete(Long bannerId) {
        bannerDao.deleteById(bannerId);
    }

    public BNPageResponse<BannerVo> queryBanner(Integer pageNum, Integer pageSize) {

        PageQuery<TBanner> queryPage = bannerDao.createLambdaQuery()
                .orderBy("picture_order desc")
                .page(pageNum, pageSize);
        return PageUtil.createPage(queryPage).map(NewsBeanMapper.INSTANCE::toBannerVo);
    }
}
