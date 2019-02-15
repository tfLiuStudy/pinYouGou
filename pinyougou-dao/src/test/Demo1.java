import org.junit.Test;

import java.util.Date;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/13$ 10:44$
 */
public class Demo1 {
    @Test
    public void test21(){
        String s="Mon Mar 30 00:00:00 CST 2015";
        String date = s;
        System.out.println(date);
        Date f = new Date(s);
        System.out.println(f);
    }

    @Test
    public void  test2(){
       String str = "safd(111)";
        System.out.println(str.split("\\(")[0]);
    }
}
