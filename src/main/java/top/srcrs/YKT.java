package top.srcrs;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Date;
import java.util.TimeZone;

public class YKT {
    static TimeZone cnTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
    public static void main(String[] args) {
        String msg = tradePage();
        System.out.println(msg);
    }
    public static String tradePage(){
        String apikey ="ykt.html";
        String nonce =RandomUtil.randomInt(8)+"-e28e-11ee-b884-b32830c59fdd";
        String timestamp =System.currentTimeMillis()+"";
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String sign =md5.digestHex("".concat(nonce).concat(timestamp).concat("no.secret"));

        String body = HttpRequest.post("https://yktapi.emoney.cn/JinNang/Data/TradePage")
                .body("{\"jinNangId\":\"2011624335\",\"Filter\":1,\"direction\":1}")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Emapp-Apikey", apikey)
                .header("Emapp-Nonce", nonce)
                .header("Emapp-Sign", sign)
                .header("Emapp-Timestamp", timestamp)
                .execute().body();
        System.out.println(body);
        JSONObject jsonObject = JSONUtil.parseObj(body);
        JSONObject detailJson = jsonObject.getJSONObject("detail");
        JSONArray listJson = detailJson.getJSONArray("list");
        String info = "有看投：%0D%0A%0D%0A ";
        for (Object o : listJson) {
            JSONObject infoJson = JSONUtil.parseObj(o);
            String tradeTime = infoJson.getStr("tradeTime");
            Date date = DateUtil.date(Long.parseLong(tradeTime));
            DateTime tradeTimeDateTime = DateUtil.convertTimeZone(date, cnTimeZone);
            String tradeTimeStr = DateUtil.format(tradeTimeDateTime, "yyyy-MM-dd HH:mm:ss.SSS");
            String tradeType = infoJson.getStr("tradeType");
            String tradeTypeName = infoJson.getStr("tradeTypeName");
            String stockId = infoJson.getStr("stockId");
            String statusMsg = infoJson.getStr("statusMsg");
            String dealNumber = infoJson.getStr("dealNumber");
            String entrustAmt = infoJson.getStr("entrustAmt");
            String tradeMoney = infoJson.getStr("tradeMoney");
            String entrustPrice = infoJson.getStr("entrustPrice");
            String dealTime = infoJson.getStr("dealTime");
            Date dealTimeDate = DateUtil.date(Long.parseLong(dealTime));
            DateTime dealTimeDateTime = DateUtil.convertTimeZone(dealTimeDate, cnTimeZone);
            String  dealTimeStr = DateUtil.format(dealTimeDateTime, "yyyy-MM-dd HH:mm:ss.SSS");

            String entrustTime = infoJson.getStr("entrustTime");
            Date entrustTimeDate = DateUtil.date(Long.parseLong(entrustTime));
            DateTime entrustDateTime = DateUtil.convertTimeZone(entrustTimeDate, cnTimeZone);
            String  entrustTimeStr = DateUtil.format(entrustDateTime, "yyyy-MM-dd HH:mm:ss.SSS");

            String remark = infoJson.getStr("remark");
            info += tradeTimeStr+" ; "+tradeTypeName+" ; "+stockId+" ; "+statusMsg +" ;  "+entrustAmt+" ;  "+entrustPrice+" ; "+entrustTimeStr+" ; "+remark+"  "+"%0D%0A%0D%0A ";
        }
        System.out.println(info);
        return info;
    }

    public static String summary(){
        String apikey ="ykt.html";
//        96be85d2-e29f-11ee-8119-d712c536ee30
        String nonce =RandomUtil.randomInt(8)+"-e28e-11ee-b884-b32830c59fdd";
        String timestamp =System.currentTimeMillis()+"";
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String sign =md5.digestHex("".concat(nonce).concat(timestamp).concat("no.secret"));

        String body = HttpRequest.post("https://yktapi.emoney.cn/JinNang/Data/Summary")
                .body("{\"jinNangId\":\"2011624335\"}")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Emapp-Apikey", apikey)
                .header("Emapp-Nonce", nonce)
                .header("Emapp-Sign", sign)
                .header("Emapp-Timestamp", timestamp)
                .execute().body();
        System.out.println(body);
        JSONObject jsonObject = JSONUtil.parseObj(body);
        JSONObject detailJson = jsonObject.getJSONObject("detail");
        String latestTradeTime = detailJson.getStr("latestTradeTime");
        Date latestTradeTimeDate = DateUtil.date(Long.parseLong(latestTradeTime));
        String  latestTradeTimeStr = DateUtil.format(latestTradeTimeDate, "yyyy-MM-dd HH:mm:ss.SSS");
        return latestTradeTimeStr;
    }

}
