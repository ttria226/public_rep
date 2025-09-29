package com.xsrw.wms.kanban.domain.vo;


/**
 * 物料库存top返回参数类
 */
public class StockListVO {



    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 库存数量
     */
    private Integer libraryCount;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(Integer libraryCount) {
        this.libraryCount = libraryCount;
    }
}
