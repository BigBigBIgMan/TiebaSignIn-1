package top.srcrs;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Pcr532 {




    public static void main(String[] args)  {
        String notice = checkIn();
        System.out.println(notice);

    }

    public static String checkIn()  {
        String post = HttpUtil.post("https://www.rfidfans.com/upload/qiandao.php", "username=xukuan&passc=MTIzNDU2&USERID=MTAwMDIxNjM2Mw==");
        return post;
    }
}
