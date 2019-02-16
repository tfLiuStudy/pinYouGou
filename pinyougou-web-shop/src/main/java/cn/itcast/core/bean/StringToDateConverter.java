package cn.itcast.core.bean;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 时间转换器
 * @Author: zc
 * @CreateDate: 2019/2/13$ 10:23$
 */
public class StringToDateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        SimpleDateFormat simpleDateFormat = null ;
        if (source.contains("/")){
            simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:s");
        }else if (source.contains("-")){
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }else{
            Date date = new Date(source);
            return date;
        }
        try {
            Date parse = simpleDateFormat.parse(source);
            return parse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}