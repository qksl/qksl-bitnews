package io.bitnews.news.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bienews.common.helper.IntervalUtil;
import io.bitnews.common.constants.RedisConstant;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.model.external.DistributionInfo;
import io.bitnews.model.external.MarketInfo;
import io.bitnews.model.external.MarketInfoQuote;
import io.bitnews.model.external.MarketSort;
import io.bitnews.model.internal.CoinMarketVo;
import io.bitnews.model.news.po.TCoinMarket;
import io.bitnews.news.dao.TCoinMarketDao;
import io.bitnews.news.scheduled.CmcFairValueJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/8/12.
 */
@Service
@Slf4j
public class CoinMarketService {

    @Autowired
    private TCoinMarketDao tCoinMarketDao;

    @Autowired
    private RedisManager redisManager;

    @Value("${btc.cmc.apiKey}")
    private String cmcApiKey;

    @Value("${btc.cmc.symbol}")
    private String cmcSymbol;

    private String ALL_MARKET_CAP = "ALL_MARKET_CAP";
    private int TIMEBEFORE = 24;

    private static final String LATEST_URI = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";
    private static final String TOTAL_URI = "https://pro-api.coinmarketcap.com/v1/global-metrics/quotes/latest";

    private static final String LISTINGS_URI = "https://pro-api.coinmarketcap.com//v1/cryptocurrency/listings/latest";

    IntervalUtil intervalUtil = new IntervalUtil();

    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public List<CoinMarketVo> queryLatestQuotes(String coinCode, String start, String end) {
        List<TCoinMarket> tCoinMarkets = tCoinMarketDao.queryQuotes(coinCode, start, end);
        return transfrom(tCoinMarkets);
    }

    private List<CoinMarketVo> transfrom(List<TCoinMarket> tCoinMarkets) {
        if (tCoinMarkets == null) {
            return null;
        }
       return  tCoinMarkets.stream().map(po ->
                transfrom(po)
        ).collect(Collectors.toList());
    }

    private CoinMarketVo transfrom(TCoinMarket po) {
        if (po == null) {
            return null;
        }
        CoinMarketVo vo = new CoinMarketVo();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }

    public MarketInfo getMarketInfoFromCache(){
        return (MarketInfo) redisManager.get(RedisConstant.MARKET_CAP_INFO);
    }

    public MarketInfo getMarketInfo()  {
        MarketInfo marketInfo = new MarketInfo();
        List<NameValuePair> paratmers = new ArrayList<>();
        paratmers.add(new BasicNameValuePair("symbol",cmcSymbol));
        paratmers.add(new BasicNameValuePair("aux","market_cap_by_total_supply"));
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("convert","USD"));
            String makeTotalCall = makeAPICall(TOTAL_URI, pairs);
            String totalMarketCap = getTotalMarketCap(getTotalMarketCapReturnDouble(makeTotalCall));
            List<MarketInfoQuote> list = new ArrayList<>();
            MarketInfoQuote mAll = new MarketInfoQuote();
            mAll.setCode_name("总市值");
            mAll.setMarket_cap(totalMarketCap);
            TCoinMarket tCoinMarket = queryDayBeforMarketCap();
            if (null == tCoinMarket) {
                mAll.setPercent_change_24h("+0.00%");
                mAll.setFlag(true);
            }else {
                double totalMarketCapReturnDouble = getTotalMarketCapReturnDouble(makeTotalCall);
                BigDecimal tmcb = BigDecimal.valueOf(totalMarketCapReturnDouble);
                double percent = tmcb.subtract(tCoinMarket.getMarketCap())
                        .divide(tCoinMarket.getMarketCap(), 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
                StringBuffer sb = new StringBuffer();
                if (percent>0) {
                    sb.append("+");
                    mAll.setFlag(true);
                }
                sb.append(percent);
                sb.append("%");
                mAll.setPercent_change_24h(sb.toString());
            }
            list.add(mAll);

            String result = makeAPICall(LATEST_URI, paratmers);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            String[] splitSymbol = cmcSymbol.split(",");
            for (int i=0; i<splitSymbol.length; i++) {
                MarketInfoQuote marketInfoQuote = new MarketInfoQuote();
                JSONObject symbolObject = data.getJSONObject(splitSymbol[i]);
                JSONObject usd = symbolObject.getJSONObject("quote").getJSONObject("USD");
                double price = usd.getDouble("price");
                marketInfoQuote.setCode_name(splitSymbol[i]);
                marketInfoQuote.setPrice(Double.valueOf(toBigDecimal(price)).toString());
                double percent_change_24h = usd.getDouble("percent_change_24h");
                StringBuffer sb = new StringBuffer();
                double percent_change_24h_v = toBigDecimal(percent_change_24h);
                if (percent_change_24h_v>0) {
                    sb.append("+");
                }
                sb.append(decimalFormat.format(percent_change_24h_v));
                sb.append("%");
                marketInfoQuote.setPercent_change_24h(sb.toString());
                double market_cap = usd.getDouble("market_cap");
                //转换单位亿
                double v = market_cap / 100000000;
                marketInfoQuote.setMarket_cap(decimalFormat.format(toBigDecimal(v))+"亿");
                marketInfoQuote.setFlag(percent_change_24h>=0?true:false);
                list.add(marketInfoQuote);
            }

            marketInfo.setList(list);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("cannont access content",e);
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log.error("Error: Invalid URL",e);
            return null;
        }
        String mark = JSONObject.toJSONString(marketInfo);
        redisManager.set(RedisConstant.MARKET_CAP_INFO, mark, 60);
        return marketInfo;
    }

    public String getTotalMarketCap(double makeTotalCall) {
        BigDecimal rs = BigDecimal.valueOf(makeTotalCall / 100000000).setScale(2, BigDecimal.ROUND_HALF_UP);
        return decimalFormat.format(rs.doubleValue()) +"亿";
    }

    public double getTotalMarketCapReturnDouble(String makeTotalCall) {
        JSONObject jsonObject = JSONObject.parseObject(makeTotalCall);
        JSONObject data = jsonObject.getJSONObject("data").getJSONObject("quote").getJSONObject("USD");
        Double total_market_cap = data.getDouble("total_market_cap");
        return toBigDecimal(total_market_cap);
    }


    private static double toBigDecimal(double num) {
        BigDecimal b = new BigDecimal(num);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", cmcApiKey);

        CloseableHttpResponse response = client.execute(request);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        return response_content;
    }

    public DistributionInfo getMarketDistribution() {
        return (DistributionInfo) redisManager.get(RedisConstant.MARKET_DISTRIBUTION_INFO);
    }

    public void insert(TCoinMarket tCoinMarket) {
        tCoinMarketDao.insert(tCoinMarket);
    }

    public TCoinMarket queryDayBeforMarketCap() {
        for (int i=24; i<48;) {
            TCoinMarket tCoinMarket = tCoinMarketDao.queryAllMarketCapByTime(ALL_MARKET_CAP, i);
            if (null != tCoinMarket) {
                return tCoinMarket;
            }
            i = i + 2;
        }
        return null;
    }
}
