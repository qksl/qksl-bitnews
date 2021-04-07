package io.bitnews.news.scheduled;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bienews.common.helper.IntervalUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.RedisConstant;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.model.external.DistributionInfo;
import io.bitnews.model.external.MarketInfo;
import io.bitnews.model.external.MarketSort;
import io.bitnews.model.news.po.TCoinMarket;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.news.dao.TCoinMarketDao;
import io.bitnews.news.service.CoinMarketService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class CmcFairValueJob {

    private final ObjectMapper objectMapper;
    private final String cmcApiHost;
    private final String cmcApiKey;
    private final String cmcSymbol;
    private static final String currenciesEndpoint = "/v1/cryptocurrency/quotes/latest";
    private static final String TOTAL_URI = "https://pro-api.coinmarketcap.com/v1/global-metrics/quotes/latest";

    private static final String LISTINGS_URI = "https://pro-api.coinmarketcap.com//v1/cryptocurrency/listings/latest";

    private static final String BTC = "BTC";
    private static final String ETH = "ETH";
    private static final String EOS = "EOS";
    private static final String LTC = "LTC";
    private static final String USD = "USD";

    @Autowired
    private CoinMarketService coinMarketService;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    public CmcFairValueJob(ObjectMapper objectMapper,
                           @Value("${btc.cmc.apiHost}") String cmcApiHost,
                           @Value("${btc.cmc.apiKey}") String cmcApiKey,
                           @Value("${btc.cmc.symbol}") String cmcSymbol) {
        this.objectMapper = objectMapper;
        this.cmcApiHost = cmcApiHost;
        this.cmcApiKey = cmcApiKey;
        this.cmcSymbol = cmcSymbol;
    }

    @Scheduled(cron = "${btc.cmc.exp}")
    public void syncFairValue() throws URISyntaxException {
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("convert","USD"));
        try {
            String makeAPICall = coinMarketService.makeAPICall(TOTAL_URI, pairs);
            JSONObject jsonObject = JSONObject.parseObject(makeAPICall);
            JSONObject data = jsonObject.getJSONObject("data").getJSONObject("quote").getJSONObject("USD");
            Double total_market_cap = data.getDouble("total_market_cap");
            TCoinMarket tCoinMarket = create(total_market_cap);
            coinMarketService.insert(tCoinMarket);
            log.info("存储当前市场代币总量"+ total_market_cap);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("存储市场代币总量", e);
        }
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void synMarketDistribution() {
        log.info("开始刷新涨跌分布");
        List<NameValuePair> paramer = new ArrayList<>();
        paramer.add(new BasicNameValuePair("limit", "400"));
        IntervalUtil a = new IntervalUtil();
        int[] rs = new int[12];
        try {
            String result = coinMarketService.makeAPICall(LISTINGS_URI, paramer);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray data = jsonObject.getJSONArray("data");
            Iterator<Object> iterator = data.iterator();
            while (iterator.hasNext()) {
                JSONObject em = (JSONObject) iterator.next();
                String percent = null;
                try {
                    percent = em.getJSONObject("quote").getJSONObject("USD").getDouble("percent_change_24h").toString();
                }catch (Exception e){
                    log.error("解析出错:" +em.toJSONString(), e);
                }
                if (percent == null) {
                    continue;
                }
                if (a.isInTheInterval(percent, "(-∞,-10]")){
                    rs[0] = rs[0] + 1;
                } else if(a.isInTheInterval(percent, "(-∞,-8]")) {
                    rs[1] = rs[1] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,-6]")) {
                    rs[2] = rs[2] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,-4]")) {
                    rs[3] = rs[3] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,-2]")) {
                    rs[4] = rs[4] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,0]")) {
                    rs[5] = rs[5] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,2]")) {
                    rs[6] = rs[6] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,4]")) {
                    rs[7] = rs[7] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,6]")) {
                    rs[8] = rs[8] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,8]")) {
                    rs[9] = rs[9] + 1;
                }else if(a.isInTheInterval(percent, "(-∞,10]")) {
                    rs[10] = rs[10] + 1;
                }else {
                    rs[11] = rs[11] + 1;
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int up=0;
        int down=0;
        for (int i=0; i<rs.length; i++) {
            if (i<6) {
                down = down + rs[i];
            }else {
                up = up + rs[i];
            }
        }
        if (up!=0 && down != 0) {
            DistributionInfo info = new DistributionInfo();
            info.setDownNum(down);
            info.setUpNum(up);
            info.setDistribution(rs);
            redisManager.set(RedisConstant.MARKET_DISTRIBUTION_INFO, info);
        }
        Object o = redisManager.get(RedisConstant.MARKET_DISTRIBUTION_INFO);
        if (o != null) {
            DistributionInfo info = (DistributionInfo) o;
            System.out.println(Arrays.toString(info.getDistribution()));
        }
    }

    @Scheduled(cron = "${btc.cmc.marketInfo}")
    public void synMarketinfo() {
        MarketInfo marketInfo = coinMarketService.getMarketInfo();
        if (null != marketInfo) {
            redisManager.set(RedisConstant.MARKET_CAP_INFO, marketInfo);
        }
    }


    public BigDecimal getPrice(String symbol) throws Exception {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 定义请求的参数
        URI uri = new URIBuilder(cmcApiHost + currenciesEndpoint)
                .setParameter("symbol", cmcSymbol).build();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader("X-CMC_PRO_API_KEY", cmcApiKey);
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        BigDecimal rs = BigDecimal.ZERO;
        if (response.getStatusLine().getStatusCode() == 200) {
            CmcResponse cmcResponse = objectMapper.readValue(entity.getContent(), CmcResponse.class);
            log.info("Result: {}", cmcResponse);
            String price = cmcResponse.data.get(symbol).quote.get(USD).price;
            rs = BigDecimal.valueOf(Double.parseDouble(price));
        } else {
            throw new CommonException("价格接口调用失败");
        }
        return rs;
    }

    @Data
    public static class CmcResponse {
        private Map<String, CmcData> data;
    }

    @Data
    public static class CmcData {
        private String name;
        private String symbol;
        private Map<String, CmcUSD> quote;
    }

    @Data
    public static class CmcUSD {
        private String price;
        private String volume_24h;
        private String percent_change_1h;
        private String percent_change_24h;
        private String percent_change_7d;
        private String market_cap;
        private String last_updated;

    }

    private TCoinMarket create(Double marketCap) {
        TCoinMarket tCoinMarket = new TCoinMarket();
        tCoinMarket.setCoinCode("ALL_MARKET_CAP");
        tCoinMarket.setMarketCap(BigDecimal.valueOf(marketCap));
        Date now = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        tCoinMarket.setUpdateTime(cal1.getTime());
        return tCoinMarket;
    }

}
