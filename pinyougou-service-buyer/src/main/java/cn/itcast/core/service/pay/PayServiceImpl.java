package cn.itcast.core.service.pay;

import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.utils.http.HttpClient;
import cn.itcast.core.utils.uniquekey.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.sun.xml.internal.bind.v2.TODO;
import org.opensaml.xml.signature.Y;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {

    @Value("${appid}")
    private String appid;       //微信公众账号或开放平台APP的唯一标识
    @Value("${partner}")
    private String partner;     //财付通平台的商户账号
    @Value("${partnerkey}")
    private String partnerkey;  //财付通平台的商户密钥
    @Value("${notifyurl}")
    private String notifyurl;   //回调地址

    @Resource
    private IdWorker idWorker;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private PayLogDao payLogDao;

    @Resource
    private OrderDao orderDao;

    /**
     * 生成支付二维码
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> createNative(String username) throws Exception {
        String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
        //从redis获取到订单生成时候的流水号
        PayLog payLog = (PayLog) redisTemplate.boundHashOps("payLog").get(username);
        String out_trade_no = payLog.getOutTradeNo();
        Long totalFee = payLog.getTotalFee();
//        long out_trade_no = idWorker.nextId();
        Map<String,String> data=new HashMap<>();
//        公众账号ID 	appid 	是 	String(32) 	wxd678efh567hg6787 	微信支付分配的公众账号ID（企业号corpid即为此appId）
        data.put("appid",appid);
//        商户号 	mch_id 	是 	String(32) 	1230000109 	微信支付分配的商户号
        data.put("mch_id",partner);
//        随机字符串 	nonce_str 	是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	随机字符串，长度要求在32位以内。推荐随机数生成算法
        data.put("nonce_str", WXPayUtil.generateNonceStr());
//        签名 	sign 	是 	String(32) 	C380BEC2BFD727A4B6845133519F3AD6 	通过签名算法计算得出的签名值，详见签名生成算法
        //TODO data.put("sign","");
//        商品描述 	body 	是 	String(128) 	腾讯充值中心-QQ会员充值
        data.put("body","品优购支付");
//        商户订单号 	out_trade_no 	是 	String(32) 	20150806125346 	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号
        data.put("out_trade_no",out_trade_no);
//        标价金额 	total_fee 	是 	Int 	88 	订单总金额，单位为分，详见支付金额
//        data.put("total_fee",String.valueOf(totalFee));
        data.put("total_fee","1");
//        终端IP 	spbill_create_ip 	是 	String(64) 	123.12.12.123 	支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
        data.put("spbill_create_ip","192.168.11.11");
//        通知地址 	notify_url 	是 	String(256) 	http://www.weixin.qq.com/wxpay/pay.php 	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        data.put("notify_url",notifyurl);
//        交易类型 	trade_type 	是 	String(16) 	JSAPI
        data.put("trade_type","NATIVE");
        //将map转成xml数据
        String xmlParam = WXPayUtil.generateSignedXml(data, partnerkey);
        //模拟浏览器发送请求
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);
        httpClient.setXmlParam(xmlParam);
        httpClient.post();
        //接收响应数据
        String content = httpClient.getContent();
        Map<String, String> map = WXPayUtil.xmlToMap(content);
        map.put("total_fee",String.valueOf(totalFee));                                 //设置总金额(分)
        map.put("out_trade_no",out_trade_no);     //设置流水号
        return map;
    }

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String out_trade_no,String username) throws Exception {
        String url="https://api.mch.weixin.qq.com/pay/orderquery";
        Map<String,String> data=new HashMap<>();
//        公众账号ID 	appid 	是 	String(32) 	wxd678efh567hg6787 	微信支付分配的公众账号ID（企业号corpid即为此appId）
        data.put("appid",appid);
//        商户号 	mch_id 	是 	String(32) 	1230000109 	微信支付分配的商户号
        data.put("mch_id",partner);
//        微信订单号 	transaction_id 	二选一 	String(32) 	1009660380201506130728806387 	微信的订单号，建议优先使用
//        商户订单号 	out_trade_no 	String(32) 	20150806125346 	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 详见商户订单号
        data.put("out_trade_no",out_trade_no);
//        随机字符串 	nonce_str 	是 	String(32) 	C380BEC2BFD727A4B6845133519F3AD6 	随机字符串，不长于32位。推荐随机数生成算法
        data.put("nonce_str",WXPayUtil.generateNonceStr());
//        签名 	sign 	是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	通过签名算法计算得出的签名值，详见签名生成算法
        //设置签名并且将map转为xml数据
        String xmlParam = WXPayUtil.generateSignedXml(data, partnerkey);
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);
        httpClient.setXmlParam(xmlParam);
        httpClient.post();
        //接收返回数据
        String content = httpClient.getContent();
        Map<String, String> map = WXPayUtil.xmlToMap(content);
        //支付完成之后更新支付日志
        String trade_state = map.get("trade_state");
        if ("SUCCESS".equals(trade_state)){
            PayLog payLog = new PayLog();
            payLog.setOutTradeNo(out_trade_no);
            payLog.setPayTime(new Date());
            payLog.setTransactionId(map.get("transaction_id"));  //第三方交易流水号
            payLog.setTradeState("1");
            payLogDao.updateByPrimaryKeySelective(payLog);      //更新
            // 删除redis中的支付日志
            redisTemplate.boundHashOps("payLog").delete(username);
            // 支付成功更新订单表
            PayLog OrderPayLog = payLogDao.selectByPrimaryKey(out_trade_no);
            //查询本次支付日志的所有商家订单
            String orderList = OrderPayLog.getOrderList();
            String[] orderIds = orderList.split(",");
            if (orderIds!=null && orderIds.length>0){
                for (String orderId : orderIds) {
                    Order order = orderDao.selectByPrimaryKey(Long.parseLong(orderId));
                    order.setStatus("2");
                    order.setUpdateTime(new Date());
                    order.setPaymentTime(new Date());
                    order.setEndTime(new Date());
                    orderDao.updateByPrimaryKeySelective(order);
                }
            }
        }
        return map;
    }


}
