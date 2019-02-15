package cn.itcast.core.export;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.export.Export;
import cn.itcast.core.service.goods.GoodsService;
import cn.itcast.core.service.user.UserManagerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/2/14$ 19:38$
 */
@Component
public class GoodsExport implements Export {

    private String tb_order = "tb_order";

    @Reference
    private UserManagerService userManagerService;

    @Reference
    private GoodsService goodsService;

    @Override
    public boolean suport(String tableName) {
        return tb_order.equals(tableName);
    }

    @Override
    public Workbook getWorkBook() {
        //获取数据结果集
        List<Goods> goodsList = goodsService.searchByMarketAll();
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
        titleCell.setCellValue("上架商品表");
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

        for (Goods goods : goodsList) {
            Row containRow = getContainRow(goods, sheet, wb, cellStyle, i++);
        }

        return wb;
    }

    private Row getContainRow(Goods goods,Sheet sheet, Workbook wb, CellStyle cellStyle, int i) {
        int c=0;
        Row row1 = sheet.createRow(i);
        //1
        Cell id = row1.createCell(c++);
        id.setCellValue(goods.getId());
        id.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //1
        Cell sellerId = row1.createCell(c++);
        sellerId.setCellValue(goods.getSellerId());
        sellerId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //2
        Cell payMentType = row1.createCell(c++);
        payMentType.setCellValue(goods.getGoodsName());
        payMentType.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //3
        Cell status = row1.createCell(c++);
        status.setCellValue(goods.getAuditStatus());
        status.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //4
        Cell createTime = row1.createCell(c++);
        createTime.setCellValue(goods.getIsMarketable());
        createTime.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //5 订单跟新时间
        Cell updateTime = row1.createCell(c++);
        updateTime.setCellValue(goods.getBrandId());
        updateTime.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //6  用户ID
        Cell caption = row1.createCell(c++);
        caption.setCellValue(goods.getCaption());
        caption.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //7 买家留言
        Cell buyerMessage = row1.createCell(c++);
        buyerMessage.setCellValue(goods.getCategory1Id());
        buyerMessage.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //8 收货人地区名称
        Cell receiverAreaName = row1.createCell(c++);
        receiverAreaName.setCellValue(goods.getCategory2Id());
        receiverAreaName.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //9 收货人手机
        Cell receiverMobile = row1.createCell(c++);
        receiverMobile.setCellValue(goods.getCategory3Id());
        receiverMobile.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //10 收货人
        Cell receiver = row1.createCell(c++);
        receiver.setCellValue(goods.getSmallPic());
        receiver.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //11 商家ID sellerId
        Cell price = row1.createCell(c++);
        price.setCellValue(goods.getPrice()+"");
        price.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //12
        Cell typeTemplateId = row1.createCell(c++);
        typeTemplateId.setCellValue(goods.getTypeTemplateId());
        typeTemplateId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //13
        Cell isEnableSpec = row1.createCell(c++);
        isEnableSpec.setCellValue(goods.getIsEnableSpec());
        isEnableSpec.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        //14
        Cell isDelete = row1.createCell(c++);
        isDelete.setCellValue(goods.getIsDelete());
        isDelete.setCellStyle(cellStyle);
        sheet.autoSizeColumn(c-1);
        return row1;
    }

    private Row getTitleFilde(Sheet sheet,Workbook wb,CellStyle cellStyle) {

        //设置标题
        Row row1 = sheet.createRow(1);
        row1.setHeightInPoints(25);

        int i=0;
        //1
        Cell id = row1.createCell(i++);
        id.setCellValue("主键");
        id.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //2  商家ID
        Cell sellerId = row1.createCell(i++);
        sellerId.setCellValue("商品Id");
        sellerId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //3 SPU名
        Cell goodsName = row1.createCell(i++);
        goodsName.setCellValue("SPU名");
        goodsName.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //4 状态
        Cell auditStatus = row1.createCell(i++);
        auditStatus.setCellValue("状态");
        auditStatus.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //5 是否上架
        Cell isMarketable = row1.createCell(i++);
        isMarketable.setCellValue("是否上架");
        isMarketable.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //6 品牌
        Cell brandId = row1.createCell(i++);
        brandId.setCellValue("品牌");
        brandId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //7 副标题
        Cell caption = row1.createCell(i++);
        caption.setCellValue("副标题");
        caption.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //8  一级类目
        Cell category1Id = row1.createCell(i++);
        category1Id.setCellValue("一级类目");
        category1Id.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //9 二级类目
        Cell category2Id = row1.createCell(i++);
        category2Id.setCellValue("二级类目");
        category2Id.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //10 三级类目
        Cell category3Id = row1.createCell(i++);
        category3Id.setCellValue("三级类目");
        category3Id.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //11 小图
        Cell smallPic = row1.createCell(i++);
        smallPic.setCellValue("小图");
        smallPic.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //12 商城价
        Cell price = row1.createCell(i++);
        price.setCellValue("商城价");
        price.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        //13 分类模板ID 分类模板ID
        Cell typeTemplateId = row1.createCell(i++);
        typeTemplateId.setCellValue("分类模板ID");
        typeTemplateId.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);

        //14 商家ID 是否启用规格
        Cell isEnableSpec = row1.createCell(i++);
        isEnableSpec.setCellValue("是否启用规格");
        isEnableSpec.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);

        //15 是否删除 是否删除
        Cell isDelete = row1.createCell(i++);
        isDelete.setCellValue("是否删除");
        isDelete.setCellStyle(cellStyle);
        sheet.autoSizeColumn(i-1);
        return row1;
    }
}
