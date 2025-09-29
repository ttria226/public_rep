package com.xsrw.wms.base.domain.dto;

import com.xsrw.wms.base.domain.TMaterialDetailPrint;

/**
 * @Description:
 * @Author XMING
 * @Date 2023-11-30
 */
public class TMaterialDetailPrintDTO extends TMaterialDetailPrint {

    /**
     * 打印机位置 1一层、2二层
     */
    private String printFloor;

    public String getPrintFloor() {
        return printFloor;
    }

    public void setPrintFloor(String printFloor) {
        this.printFloor = printFloor;
    }
}
