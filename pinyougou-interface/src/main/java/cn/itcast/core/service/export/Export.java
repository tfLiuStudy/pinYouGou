package cn.itcast.core.service.export;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @Description:
 * @Author: zc
 * @CreateDate: 2019/1/31$ 11:02$
 */
public interface Export {

    /**m
     * 是否支持导出
     * @param tableName
     * @return
     */
    public boolean suport(String tableName);

    public Workbook getWorkBook();
}
