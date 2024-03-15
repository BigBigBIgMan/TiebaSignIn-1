package top.srcrs;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

public class YKT {
    public static String tradePage(){
        String apikey ="ykt.html";
        String nonce =RandomUtil.randomInt(8)+"-e28e-11ee-b884-b32830c59fdd";
        String timestamp =System.currentTimeMillis()+"";
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String sign =md5.digestHex("".concat(nonce).concat(timestamp).concat("no.secret"));

        HttpResponse execute = HttpRequest.post("https://yktapi.emoney.cn/JinNang/Data/TradePage")
                .body("{\"jinNangId\":\"2011624335\",\"Filter\":1,\"direction\":1}")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Emapp-Apikey", apikey)
                .header("Emapp-Nonce", nonce)
                .header("Emapp-Sign", sign)
                .header("Emapp-Timestamp", timestamp)
                .execute();
        System.out.println(execute.body());
        return execute.body();
    }

}
