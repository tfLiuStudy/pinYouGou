package cn.itcast.core.listener;

import cn.itcast.core.service.staticPage.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

//自定义消费者消息队列监听器,静态页面生成
public class PageListener implements MessageListener{

    @Resource
    private StaticPageService staticPageService;

    @Override
    public void onMessage(Message message) {
        try {
            ActiveMQTextMessage activeMQTextMessage= (ActiveMQTextMessage) message;
            //获取消息队列的id
            String id = activeMQTextMessage.getText();
            System.out.println("消费者staticpage获得id:"+id);
            //消费消息
            staticPageService.getPage(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
