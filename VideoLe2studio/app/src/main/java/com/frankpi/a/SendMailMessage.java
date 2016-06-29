package com.frankpi.a;

import android.util.Log;

class SendMailMessage {


    public void SendMessage(String message) {
        try {
            MailSenderInfo mailInfo = new MailSenderInfo();
            mailInfo.setMailServerHost("smtp.qq.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUserName("489697862@qq.com"); // 你的邮箱地址
            mailInfo.setPassword("");// 您的邮箱密码
            mailInfo.setFromAddress(""); // 发送的邮箱
            mailInfo.setToAddress("liujiapeng@yyhudong.com"); // 发到哪个邮件去
            mailInfo.setSubject("乐视云视频链接采集器"); // 邮件主题
            mailInfo.setContent(message); // 邮件文本
            // 这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            sms.sendTextMail(mailInfo);// 发送文体格式
            //sms.sendHtmlMail(mailInfo);//发送html格式

        } catch (Exception e) {
            Log.e("==SendMail", e.getMessage(), e);
        }

    }
}
