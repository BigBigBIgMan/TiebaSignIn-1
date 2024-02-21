package top.srcrs;

import cn.hutool.http.HttpUtil;

public class Pcr532 {




    public static void main(String[] args)  {
        String notice = checkIn();
        System.out.println(notice);

    }

    public static String checkIn()  {
        String post = "Not running";
        try {
               post = HttpUtil.post("https://www.rfidfans.com/upload/qiandao.php", "username=xukuan&passc=cGNyNTIxWHVrdWFuIQ==&USERID=MTAwMDgyNTg0OQ==",3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }
}
