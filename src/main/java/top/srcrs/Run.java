package top.srcrs;

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
    public static final String cookie = " Hm_lvt_fcbf8a457b2c5ae9cc58b5bf4cb7cef1=1693235875;Hm_lpvt_fcbf8a457b2c5ae9cc58b5bf4cb7cef1=1693235875;__snaker__id=9YU0kgSeuzN4OqEY;___rl__test__cookies=1693235887199;JSESSIONID=D7E0597671016C41F4342E5A8C98DEDF.ynote-accountserver-docker-cwonline-3-ezf33-bmf3s-69558955sgdwb-8081;__yadk_uid=QFwt6pn46qJ62xIYbJO2Fb75MV768xg9;gdxidpyhxdE=fRDMdXC%5C%5CLvbtUEe0dzBmztQjclTlmU6ZRl7pkm7VqO0kgWjJ%5CsSYwh%2BAiLSbx3Apt0apgET4GxRPN%5C%2BTdVC47kPDDy1Jpo%5ClhqNKus6JNqIRm%2F2A9SWlcjhOI1mAqCjB2CER03v9x%2BESvISgf9mM0g7LVixZv6kRENWkHfeWR%2FaXTvT%3A1693236777857;YD00053006227227%3AWM_NI=NTnKqo4z%2BmGVH86UJnMKaFBbr0S0EnRq0LKsX1DGbFphv5wbo5a%2Fftgh7e9wuXEpNNNuNYGO8PyqDttAd%2BjT38rbMaODXC%2BB%2B29b6WQ1B17ahzRKaB5fK2IMp6fU449bUko%3D;YD00053006227227%3AWM_NIKE=9ca17ae2e6ffcda170e2e6ee89c13af6b982d2b67caceb8fb6d55b869f9f83c16697f1a88ef27085ebe5aab22af0fea7c3b92a8e8ca2acf465f8a6e58ec65e8ce8fab6c9668f95afa7c16ab2f09dd1c86692af00adca799cef8ca8e14eb4b8bad6f66aadebafd0e580a5b8f986bc7db7888b91cd468eab82bac645fc90b792c780b89faccce960f1e8be83d06b8d9c98a4aa4ae9eef783b55d93ef8ed2b66baca69eace44af7ba85d3cc47b4eaaab6b740fbbe9b8cee37e2a3;YD00053006227227%3AWM_TID=AtU19tzDayVFERURBFLViV7h3oJToqIz;YNOTE_SESS=v2|UGQ82QsqfVzmhLYf0fwLRkMkMY5hLOG0eKh4Qu6L6yReLPMTyhfpL0UW64qBkfeL0l50fJu64pBRPL0HQS64pu0Ul0LgLRHOfR;YNOTE_PERS=v2|urstoken||YNOTE||web||-1||1693235883316||20.168.158.183||m13171555760@163.com||w4kMOWkfqy0qZOMTy0MQK0wKhLYG6Mlm0TLPMJz0HTFReBnfYGnflY0eS6LJ4hLl50p4nHUWP4eLRYYhfwzhfQL0;YNOTE_LOGIN=3||1693235883324;YNOTE_CSTK=9AVCyPHm";
//    public static final String cookie ="YNOTE_SESS=v2|O7ZXvcT2emk5k4JK64eBRQShMUE0feL06uh4kWn4qu0zf0MUMh4TuRz56LpL6Mz50JzOMlA64g4RJLhMe4nMgLRkA6Mkm6LJBR";
//    public static final String cookie = "YNOTE_SESS=v2|8JgBNW2D-mQ46MzGh4qL0YGn46yhLlfRYfhfkfk4qK0kl0Hq4RHwu0k56LTBh4z50Q4P4p4kLUERkmh4PLhHOY0l5hMq4OMlMR";
//    public static final String cookie = "YNOTE_SESS=v2|0hqKrleHQmTKh4pFkfgz0UWh46FPLzf0TFnfqukMY50OWPMYfOMqF0JS0MllhfzMReuRHPuhMO5ROfOfJukfkM06zO4TBkLkMR";

    /**
     * 接⼝调⽤  POST
     */
    public static String httpURLConnectionPOST(String POST_URL,String cookie) {
        String str = "error";
        try {
            URL url = new URL(POST_URL);
// 将url 以 open⽅法返回的urlConnection  连接强转为HttpURLConnection连接  (标识⼀个url所引⽤的远程对象连接)
// 此时cnnection只是为⼀个连接对象,待连接中
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
// 设置连接输出流为true,默认false (post 请求是以流的⽅式隐式的传递参数)
            connection.setDoOutput(true);
// 设置连接输⼊流为true
            connection.setDoInput(true);
// 设置请求⽅式为post
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Host", "note.youdao.com");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("User-Agent", "YNote");

// 建⽴连接 (请求未开始,直到connection.getInputStream()⽅法调⽤时才发起,以上各个参数设置需在此⽅法之前进⾏)
            connection.connect();
// 创建输⼊输出流,⽤于往连接⾥⾯输出携带的参数,(输出内容为?后⾯的内容)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
// 输出完成后刷新并关闭流
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
//System.out.println(connection.getResponseCode());
// 连接发起请求,处理服务器响应  (从连接获取到输⼊流并包装为bufferedReader)
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder(); // ⽤来存储响应数据
// 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
//sb.append(bf.readLine());
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
            connection.disconnect(); // 销毁连接
            System.out.println(sb.toString());
            str=sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

     public static String postReq(String cookie)
    {
        String str1 = httpURLConnectionPOST(POST_URL_CHECKIN,cookie);
        String str2 = httpURLConnectionPOST(POST_URL_ADPROMPT,cookie);
        String str3 = httpURLConnectionPOST(POST_URL_ADPANDOMPROMPT,cookie);
        return str1+str2+str3;
    }
    
    public static void main(String[] args) throws UnknownHostException {
     
        RedisDS redisDS = RedisDS.create();
        String note131 = redisDS.getStr("Note163_13171555760@163.com");
        String notexk = redisDS.getStr("Note163_xk@163.com");
        String notexu = redisDS.getStr("Note163_xu_kuan@yeah.net");
        String str1 = postReq(note131);
        String str2 = postReq(notexk);
        String str3 = postReq(notexu);
        //https://sctapi.ftqq.com/SCT207695TV2Mu3zwYCq2JN8dS1sGrSIBF.send?title=messagetitle&desp=desp
         redisDS.close();
    }
}
