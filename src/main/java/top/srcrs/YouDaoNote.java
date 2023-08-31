package top.srcrs;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import util.RedisDS;
import util.TellMsg;

import java.net.UnknownHostException;

public class YouDaoNote {

    // 测试签到接⼝
    public static final String POST_URL_CHECKIN = "https://note.youdao.com/yws/mapi/user?method=checkin";
    public static final String POST_URL_ADPROMPT = "https://note.youdao.com/yws/mapi/user?method=adPrompt";
    public static final String POST_URL_ADPANDOMPROMPT = "https://note.youdao.com/yws/mapi/user?method=adRandomPrompt";

    public static final String[] USER = {"Note163_13171555760@163.com","Note163_xk@163.com","Note163_xu_kuan@yeah.net"};
    static RedisDS  redisDS = RedisDS.create();

    public static String content = "";


    public static void main(String[] args)  {
        YouDaoNoteDo();
    }

    public static String YouDaoNoteDo()  {
        String title = "签到结果：";
        for (int i = 0; i < USER.length; i++) {
            String name = USER[i];
            try {
                long toatl = postReq(name);
                title+=i+"："+toatl+";";

            } catch (Exception e) {
                e.printStackTrace();
                TellMsg.tellMsg(name+"异常",e.getMessage());
            }
        }
        redisDS.close();

        return title;
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
}