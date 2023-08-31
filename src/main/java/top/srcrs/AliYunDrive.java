package top.srcrs;

import cn.hutool.http.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import util.RedisDS;

import java.util.HashMap;
import java.util.List;

public class AliYunDrive {

    // 测试签到接⼝
    public static final String GET_TOKEN = "https://auth.aliyundrive.com/v2/account/token";
    public static final String SING_IN_LIST = "https://member.aliyundrive.com/v1/activity/sign_in_list";
    public static final String SING_IN_REWARD = "https://member.aliyundrive.com/v1/activity/sign_in_reward";


    public static void main(String[] args)  {
        String notice = aliYunDriveCheckin();
        System.out.println(notice);

    }

    public static String aliYunDriveCheckin()  {
        String aaccessToken = getAccessToken();
        Integer signInCount = checkIn(aaccessToken);
        String notice = getReward(aaccessToken,signInCount);
        return notice;
    }

    public static String  getAccessToken()
    {
        JSONObject param = JSONUtil.createObj();
        param.putOpt("grant_type","refresh_token");
        param.putOpt("refresh_token","b2a86e0267ca4160bce15efdd102cfe2");

        String result = HttpRequest.post(GET_TOKEN)
                .header(Header.CONTENT_TYPE, "application/json")
                .timeout(60000)
                .body(param.toString()).execute().body();
//        System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        String accessToken = responseJson.getStr("access_token");
        return accessToken;
    }

    public static Integer checkIn(String accessToken)
    {
        JSONObject param = JSONUtil.createObj();
        param.putOpt("isReward",false);
        param.putOpt("_rx-s","mobile");

        String result = HttpRequest.post(SING_IN_LIST)
                .header(Header.CONTENT_TYPE, "application/json")
                .header("Authorization", "Bearer "+accessToken)
                .timeout(60000)
                .body(param.toString()).execute().body();
//        System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        JSONObject resultJson = responseJson.getJSONObject("result");
        Integer  signInCount = resultJson.getInt("signInCount");
        return signInCount;
    }

    public static String getReward(String accessToken,Integer signInCount)
    {
        JSONObject param = JSONUtil.createObj();
        param.putOpt("signInDay",signInCount);
        param.putOpt("_rx-s","mobile");

        String result = HttpRequest.post(SING_IN_REWARD)
                .header(Header.CONTENT_TYPE, "application/json")
                .header("Authorization", "Bearer "+accessToken)
                .timeout(60000)
                .body(param.toString()).execute().body();
        //System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        JSONObject resultJson = responseJson.getJSONObject("result");
        String notice = resultJson.getStr("notice");
        return "阿里云盘签到结果："+notice;
    }

}
