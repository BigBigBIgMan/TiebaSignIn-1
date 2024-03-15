package top.srcrs;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import util.TellMsg;

import java.net.UnknownHostException;

public class Run {

    public static void main(String[] args) throws UnknownHostException {
        TimeInterval timer = DateUtil.timer();
        String title = YouDaoNote.YouDaoNoteDo();
        System.out.println("有道云签到耗时："+timer.intervalRestart());

//        String notice = AliYunDrive.aliYunDriveCheckin();
//        System.out.println(notice);
//        System.out.println("阿里云盘签到耗时："+timer.intervalRestart());

        String pcr532Msg = Pcr532.checkIn();
        System.out.println("PCR532推送耗时："+timer.intervalRestart());

        String tradePage = YKT.tradePage();
        System.out.println("有看投："+timer.intervalRestart());

        String content = YouDaoNote.content;
        TellMsg.tellMsg(title, content +"%0D%0A%0D%0A "+pcr532Msg+"%0D%0A%0D%0A "+tradePage);
        System.out.println("消息推送耗时："+timer.intervalRestart());

    }
}
