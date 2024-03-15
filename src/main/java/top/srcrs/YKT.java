package top.srcrs;


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

public class YKT {
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
            String tradeTimeStr = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss.SSS");
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
            String  dealTimeStr = DateUtil.format(dealTimeDate, "yyyy-MM-dd HH:mm:ss.SSS");
            String entrustTime = infoJson.getStr("entrustTime");
            Date entrustTimeDate = DateUtil.date(Long.parseLong(entrustTime));
            String  entrustTimeStr = DateUtil.format(entrustTimeDate, "yyyy-MM-dd HH:mm:ss.SSS");
            String remark = infoJson.getStr("remark");
            info += tradeTimeStr+" ; "+tradeTypeName+" ; "+stockId+" ; "+statusMsg +" ; "+dealNumber+" ; "+entrustAmt+" ; "+tradeMoney+" ; "+entrustPrice+" ; "+entrustTimeStr+" ; "+dealTimeStr+" ; "+remark+" ; "+"%0D%0A%0D%0A ";
        }

        return info;
    }

}
