import org.apache.poi.ss.usermodel.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/13$ 18:44$
 */
public class PoiDemo1 {


    @Test
    public void readWorkBook() throws Exception {
        // poi读取excel
        //创建要读入的文件的输入流
        InputStream inp = new FileInputStream("E:\\workbookText.xls");

        //根据上述创建的输入流 创建工作簿对象
        Workbook wb = WorkbookFactory.create(inp);
        //得到第一页 sheet
        //页Sheet是从0开始索引的
        Sheet sheet = wb.getSheetAt(0);
        //利用foreach循环 遍历sheet中的所有行
        for (Row row : sheet) {
            //遍历row中的所有方格
            for (Cell cell : row) {
                //输出方格中的内容，以空格间隔
                System.out.print(cell.toString() + "  ");
            }
            //每一个行输出之后换行
            System.out.println();
        }
        //关闭输入流
        inp.close();
    }
}
