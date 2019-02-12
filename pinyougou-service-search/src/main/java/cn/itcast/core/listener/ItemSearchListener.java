package cn.itcast.core.listener;

import cn.itcast.core.service.itemSearch.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

//自定义消费者消息队列监听器,商品审核完后更新索引库
public class ItemSearchListener implements MessageListener{
    @Resource
    private ItemSearchService itemSearchService;

    //mq消费方
    @Override
    public void onMessage(Message message) {
        try {
            //获取到消息队列中的数据
            ActiveMQTextMessage activeMQTextMessage= (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("消费者itemsearch获得mq中的id:"+id);
            //消费消息
            itemSearchService.updateItemToSolr(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
