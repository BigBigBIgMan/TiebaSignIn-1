package top.srcrs;

import cn.hutool.http.HttpUtil;

public class Pcr532 {




    public static void main(String[] args)  {
        String notice = checkIn();
        System.out.println(notice);

    }

    public static String checkIn()  {
        String post = null;
        try {
            post = HttpUtil.post("https://www.rfidfans.com/upload/qiandao.php", "username=xukuan&passc=MTIzNDU2&USERID=MTAwMDIxNjM2Mw==",3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return post;
    }
}
