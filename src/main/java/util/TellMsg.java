package util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import util.RedisDS;

public class TellMsg {



    public static void tellMsg(String title,String msg)
    {
        String code = fangTangPush(title,msg);


        if(!"0".equals(code)){
            code = PPush(title,msg);
        }
    }

    public static String  fangTangPush(String title,String msg)
    {
        String code = "";

        try {
            String url = "https://sctapi.ftqq.com/SCT207695TV2Mu3zwYCq2JN8dS1sGrSIBF.send?title="+title+"&desp="+msg;
            HttpRequest.setGlobalTimeout(5000);
            HttpResponse response  = HttpRequest.post(url).execute();
            String responseStr = response.body();
            JSONObject responseJson = JSONUtil.parseObj(responseStr);
            code = responseJson.getStr("code");
            System.out.println(responseStr);
//            {"code":0,"message":"","data":{"pushid":"136742982","readkey":"SCTU5pmCJ9UyGvh","error":"SUCCESS","errno":0}}
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
        }
        return code;
    }

    public static String PPush(String title,String msg)
    {
        String code = "";
        try {
            String purl  = "http://www.pushplus.plus/send?token=ebbee855fc8942708aa6f50c9e9cd66d&title="+title+"&content="+msg;
            HttpResponse responseP = HttpRequest.post(purl).execute();
            String responsePStr = responseP.body();
            JSONObject responsePJson = JSONUtil.parseObj(responsePStr);
            code = responsePJson.getStr("code");
            System.out.println(responsePStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }


}
