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

    public static final String[] USER = {"Note163_13171555760@163.com","Note163_xk@163.com","Note163_xu_kuan@yeah.net"};
    static RedisDS  redisDS = RedisDS.create();

    public static String content = "";


    public static void main(String[] args)  {
        String notice = aliYunDriveCheckin();
        System.out.println(notice);

    }

    public static String aliYunDriveCheckin()  {
        String aaccessToken = postReq();
        Integer signInCount = postReq2(aaccessToken);
        String notice = postReq3(aaccessToken,signInCount);
        return notice;
    }

    public static String  postReq()
    {
        JSONObject param = JSONUtil.createObj();
        param.putOpt("grant_type","refresh_token");
        param.putOpt("refresh_token","b2a86e0267ca4160bce15efdd102cfe2");

        String result = HttpUtil.createPost(GET_TOKEN)
                .header(Header.CONTENT_TYPE, "application/json")
                .timeout(60000)
                .body(param.toString()).execute().body();
        System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        String accessToken = responseJson.getStr("access_token");
        return accessToken;
    }

    public static Integer postReq2(String accessToken)
    {
        JSONObject param = JSONUtil.createObj();
        param.putOpt("isReward",false);
        param.putOpt("_rx-s","mobile");

        String result = HttpUtil.createPost(SING_IN_LIST)
                .header(Header.CONTENT_TYPE, "application/json")
                .header("Authorization", "Bearer "+accessToken)
                .timeout(60000)
                .body(param.toString()).execute().body();
        System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        JSONObject resultJson = responseJson.getJSONObject("result");
        Integer  signInCount = resultJson.getInt("signInCount");
        return signInCount;
    }

    public static String postReq3(String accessToken,Integer signInCount)
    {
        JSONObject param = JSONUtil.createObj();
        param.putOpt("signInDay",signInCount);
        param.putOpt("_rx-s","mobile");

        String result = HttpUtil.createPost(SING_IN_REWARD)
                .header(Header.CONTENT_TYPE, "application/json")
                .header("Authorization", "Bearer "+accessToken)
                .timeout(60000)
                .body(param.toString()).execute().body();
        JSONObject responseJson = JSONUtil.parseObj(result);
        JSONObject resultJson = responseJson.getJSONObject("result");
        String notice = resultJson.getStr("notice");
        return "阿里云盘签到结果："+notice;
    }

}
