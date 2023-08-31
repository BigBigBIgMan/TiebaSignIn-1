package top.srcrs;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import util.RedisDS;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class Run {

    // 测试签到接⼝
    public static final String POST_URL_CHECKIN = "https://note.youdao.com/yws/mapi/user?method=checkin";
    public static final String POST_URL_ADPROMPT = "https://note.youdao.com/yws/mapi/user?method=adPrompt";
    public static final String POST_URL_ADPANDOMPROMPT = "https://note.youdao.com/yws/mapi/user?method=adRandomPrompt";

    public static final String[] USER = {"Note163_13171555760@163.com","Note163_xk@163.com","Note163_xu_kuan@yeah.net"};
    static RedisDS  redisDS = RedisDS.create();

    public static String content = "";
    static TimeInterval timer = DateUtil.timer();

    public static void main(String[] args) throws UnknownHostException {
        String title = "签到结果：";
        for (int i = 0; i < USER.length; i++) {
            String name = USER[i];
            try {
                long toatl = postReq(name);
                title+=i+"："+toatl+";";

            } catch (Exception e) {
                e.printStackTrace();
                tellMsg(name+"异常",e.getMessage());
            }
        }
        tellMsg(title, content);
        redisDS.close();
        System.out.println(timer.interval());
    }

    public static long postReq(String name)
    {
        String cookie = redisDS.getStr(name);
        Long checkinSpace = postReq(POST_URL_CHECKIN,cookie);
        Long adpromptSpace = postReq(POST_URL_ADPROMPT,cookie);
        Long adpandompromptSpace = postReq(POST_URL_ADPANDOMPROMPT,cookie);
        long total = checkinSpace + adpromptSpace + adpandompromptSpace;
        String msg = "共获取："+total+"签到获取："+checkinSpace+"看广告："+adpromptSpace+"看视频广告："+adpandompromptSpace;
        System.out.println(msg);
        content += msg + "<br/>";
        return total;
    }

    public static Long postReq(String url,String cookie)
    {
        HttpResponse response = HttpRequest.post(url).header("Host","note.youdao.com").header("Cache-Control","no-cache").header("Accept","*/*").header("User-Agent","YNote").header("cookie",cookie).execute();
        String responseStr = response.body();
        JSONObject responseJson = JSONUtil.parseObj(responseStr);
        Long responseSpace = responseJson.getLong("space")/1024/1024;

        return responseSpace;
    }

    public static void tellMsg(String title,String msg)
    {
        String code = "";
        try {
            String url = "https://sctapi.ftqq.com/SCT207695TV2Mu3zwYCq2JN8dS1sGrSIBF.send?title="+title+"&desp="+msg;
            HttpRequest.setGlobalTimeout(2000);
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

        if(!"0".equals(code)){
            try {
                String purl  = "http://www.pushplus.plus/send?token=ebbee855fc8942708aa6f50c9e9cd66d&title="+title+"&content="+msg;
                HttpResponse responseP = HttpRequest.post(purl).execute();
                String responsePStr = responseP.body();
//                JSONObject responsePJson = JSONUtil.parseObj(responsePStr);
//                code = responsePJson.getStr("code");
                System.out.println(responsePStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
