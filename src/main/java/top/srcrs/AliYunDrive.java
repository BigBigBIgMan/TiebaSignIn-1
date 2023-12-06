package top.srcrs;

import cn.hutool.http.*;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import util.RedisDS;

import java.util.HashMap;
import java.util.List;

import static cn.hutool.poi.excel.sax.AttributeName.t;

public class AliYunDrive {

    // 测试签到接⼝
    public static final String GET_TOKEN = "https://auth.aliyundrive.com/v2/account/token";
//    public static final String SING_IN_LIST = "https://member.aliyundrive.com/v1/activity/sign_in_list";
    public static final String SING_IN_LIST = "https://member.aliyundrive.com/v2/activity/sign_in_list";
    public static final String SING_IN_REWARD = "https://member.aliyundrive.com/v1/activity/sign_in_reward";
    public static final String SING_IN_TASK_REWARD = "https://member.aliyundrive.com/v2/activity/sign_in_task_reward";
    static RedisDS  redisDS = RedisDS.create();

    public static void main(String[] args)  {
        String notice = aliYunDriveCheckin();
        System.out.println(notice);

    }

    public static String aliYunDriveCheckin()  {


        String notice = null;
        try {
            String aaccessToken = getAccessToken();
            Integer signInCount = checkIn(aaccessToken);
            notice = getReward(aaccessToken,signInCount);
            notice += "%0D%0A%0D%0A "+getDailyTask(aaccessToken,signInCount);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return notice;
    }

    public static String  getAccessToken()
    {
        //JSON.parse(localStorage.token).refresh_token
        String refresh_token = redisDS.getStr("refresh_token");

        JSONObject param = JSONUtil.createObj();
        param.putOpt("grant_type","refresh_token");
        param.putOpt("refresh_token",refresh_token);

        String result = HttpRequest.post(GET_TOKEN)
                .header(Header.CONTENT_TYPE, "application/json")
                .timeout(10000)
                .body(param.toString()).execute().body();
//        System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        String accessToken = responseJson.getStr("access_token");
        redisDS.close();
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
                .timeout(10000)
                .body(param.toString()).execute().body();
//        System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        JSONObject resultJson = responseJson.getJSONObject("result");
        Integer  signInCount = resultJson.getInt("signInCount");
        JSONArray signInInfos = resultJson.getJSONArray("signInInfos");
        JSONObject todayJson = signInInfos.getJSONObject(signInCount - 1);
        JSONArray rewardsJson = todayJson.getJSONArray("rewards");
        for (int i = 0; i < rewardsJson.size(); i++) {
            JSONObject jsonObject = rewardsJson.getJSONObject(i);
            System.out.println(jsonObject);
//            System.out.println(jsonObject.getStr("type"));
        }
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
                .timeout(10000)
                .body(param.toString()).execute().body();
//        System.out.println(result);
        JSONObject responseJson = JSONUtil.parseObj(result);
        JSONObject resultJson = responseJson.getJSONObject("result");
        String notice = resultJson.getStr("notice");
        return "阿里云盘签到结果："+notice;
    }

    public static String getDailyTask(String accessToken,Integer signInCount)
    {
        JSONObject param = JSONUtil.createObj();
        param.putOpt("signInDay",signInCount);
        param.putOpt("_rx-s","mobile");

        String result = HttpRequest.post(SING_IN_TASK_REWARD)
                .header(Header.CONTENT_TYPE, "application/json")
                .header("Authorization", "Bearer "+accessToken)
                .timeout(10000)
                .body(param.toString()).execute().body();
        System.out.println(result);
        try {
        JSONObject responseJson = JSONUtil.parseObj(result);
        JSONObject resultJson = responseJson.getJSONObject("result");
        String notice = resultJson.getStr("notice");
            return "阿里云盘每日任务签到结果："+notice;
        }catch (Exception e){
            return "阿里云盘每日任务签到结果："+result;
        }

    }

}
