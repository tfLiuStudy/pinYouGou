package cn.itcast.core.export;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.export.Export;
import cn.itcast.core.service.user.UserManagerService;
import cn.itcast.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/1/31$ 11:09$
 */
@Component
public class OrderExport implements Export {

    private String tb_order = "tb_order";

    @Reference
    private UserManagerService userManagerService;

    @Override
    public boolean suport(String tableName) {
        return tb_order.equals(tableName);
    }

    @Override
    public Workbook getWorkBook() {
        //获取数据结果集
        List<Order> orderList = userManagerService.queryAll();
        //构造wb对象
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");
        //构造标题栏
        Row title = sheet.createRow(0); //标题行
        title.setHeightInPoints(36);  //高度
        Cell titleCell = title.createCell(0); //创建一个单元格
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                0, //first column (0-based)
                11  //last column  (0-based)
        ));
        //设置字体
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)28);
        font.setFontName("Courier New");
        font.setItalic(true);

        // Fonts are set into a style so create a new one to use.
        CellStyle style = wb.createCellStyle();
        style.setFont(font);
        //设置背景色
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //设置对齐样式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCell.setCellValue("用户订单");
        titleCell.setCellStyle(style);

        //设置对齐样式
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
/*        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("m/d/yy h:mm"));*/
        //构造字段兰
        Row titleFilde = getTitleFilde(sheet,wb,cellStyle);
        int i = 2;
        //存储数据
        for (Order order : orderList) {
            Row containRow = getContainRow(order,sheet,wb,cellStyle,i++);
        }

        return wb;
    }

    private Row getContainRow(Order order,Sheet sheet, Workbook wb, CellStyle cellStyle, int i) {
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle cellDateStyle = wb.createCellStyle();
        cellDateStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

        int c=0;
        Row row1 = sheet.createRow(i);
        Cell orderId = row1.createCell(c++);
        orderId.setCellValue(order.getOrderId());
        orderId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //1
        Cell payMent = row1.createCell(c++);
        payMent.setCellValue(order.getPayment()+"");
        payMent.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //2
        Cell payMentType = row1.createCell(c++);
        String type ;
        if ("1".equals(order.getPaymentType())){
            type = "在线付款";
        }else if ("2".equals(order.getPaymentType())){
            type = "货到付款";
        }else{
            type = "未知";
        }
        payMentType.setCellValue(type);
        payMentType.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //3
        Cell status = row1.createCell(c++);
        status.setCellValue(order.getStatus());
        status.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //4
        Cell createTime = row1.createCell(c++);
        createTime.setCellValue(order.getCreateTime());
        createTime.setCellStyle(cellDateStyle);
        sheet.autoSizeColumn(c-1);
        //5 订单跟新时间
        Cell updateTime = row1.createCell(c++);
        updateTime.setCellValue(order.getUpdateTime());
        updateTime.setCellStyle(cellDateStyle);
        sheet.autoSizeColumn(c-1);
        //6  用户ID
        Cell userID = row1.createCell(c++);
        userID.setCellValue(order.getUserId());
        userID.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //7 买家留言
        Cell buyerMessage = row1.createCell(c++);
        buyerMessage.setCellValue(order.getBuyerMessage());
        buyerMessage.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //8 收货人地区名称
        Cell receiverAreaName = row1.createCell(c++);
        receiverAreaName.setCellValue(order.getReceiverAreaName());
        receiverAreaName.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //9 收货人手机
        Cell receiverMobile = row1.createCell(c++);
        receiverMobile.setCellValue(order.getReceiverMobile());
        receiverMobile.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //10 收货人
        Cell receiver = row1.createCell(c++);
        receiver.setCellValue(order.getReceiver());
        receiver.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //11 商家ID sellerId
        Cell sellerId = row1.createCell(c++);
        sellerId.setCellValue(order.getSellerId());
        sellerId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        return row1;
    }

    private Row getTitleFilde(Sheet sheet,Workbook wb,CellStyle cellStyle) {

        //设置标题
        Row row1 = sheet.createRow(1);
        row1.setHeightInPoints(25);

        int i=0;
        Cell orderId = row1.createCell(i++);
        orderId.setCellValue("订单Id");
        orderId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //1
        Cell payMent = row1.createCell(i++);
        payMent.setCellValue("实付金额");
        payMent.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //2
        Cell payMentType = row1.createCell(i++);
        payMentType.setCellValue("支付类型");
        payMentType.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //3
        Cell status = row1.createCell(i++);
        status.setCellValue("状态");
        status.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //4
        Cell createTime = row1.createCell(i++);
        createTime.setCellValue("订单创建时间");
        createTime.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //5 订单跟新时间
        Cell updateTime = row1.createCell(i++);
        updateTime.setCellValue("订单跟新时间");
        updateTime.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //6  用户ID
        Cell userID = row1.createCell(i++);
        userID.setCellValue("用户Id");
        userID.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //7 买家留言
        Cell buyerMessage = row1.createCell(i++);
        buyerMessage.setCellValue("买家留言");
        buyerMessage.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //8 收货人地区名称
        Cell receiverAreaName = row1.createCell(i++);
        receiverAreaName.setCellValue("收货人地区名称");
        receiverAreaName.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //9 收货人手机
        Cell receiverMobile = row1.createCell(i++);
        receiverMobile.setCellValue("收货人手机");
        receiverMobile.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //10 收货人
        Cell receiver = row1.createCell(i++);
        receiver.setCellValue("收货人");
        receiver.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //11 商家ID sellerId
        Cell sellerId = row1.createCell(i++);
        sellerId.setCellValue("商家ID");
        sellerId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        return row1;
    }
}
