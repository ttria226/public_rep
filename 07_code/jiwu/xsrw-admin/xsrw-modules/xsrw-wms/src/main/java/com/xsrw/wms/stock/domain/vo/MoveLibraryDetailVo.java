package com.xsrw.wms.stock.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.stock.domain.TMoveLibraryDetail;

/**
 * 移库详情对象 t_move_library_detail
 *
 */
public class MoveLibraryDetailVo extends TMoveLibraryDetail {

    /** 物料编码  */
    @Excel(name = "物料编码",sort = 2)
    private String materialCode;

    /** 物料名称  */
    @Excel(name = "物料名称",sort = 3)
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号",sort = 4)
    private String specifications;

    /** 单位名称 */
    @Excel(name = "单位",sort = 5)
    private String unitName;

    /*库存物料标识*/
    private String tsMaterialId;

    /*库存批次号*/
    private String tsBatchCode;

    /*库存数量*/
    private String tsCount;

    public String getTsMaterialId() {
        return tsMaterialId;
    }

    public void setTsMaterialId(String tsMaterialId) {
        this.tsMaterialId = tsMaterialId;
    }

    public String getTsBatchCode() {
        return tsBatchCode;
    }

    public void setTsBatchCode(String tsBatchCode) {
        this.tsBatchCode = tsBatchCode;
    }

    public String getTsCount() {
        return tsCount;
    }

    public void setTsCount(String tsCount) {
        this.tsCount = tsCount;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    public String getUnitName()
    {
        return unitName;
    }
}