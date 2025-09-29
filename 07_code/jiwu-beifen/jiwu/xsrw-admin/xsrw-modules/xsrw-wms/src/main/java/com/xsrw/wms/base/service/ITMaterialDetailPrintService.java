package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TMaterialDetailPrint;
import com.xsrw.wms.base.domain.dto.TMaterialDetailPrintDTO;
import com.xsrw.wms.base.domain.vo.TMaterialDetailPrintVO;

/**
 * rfid打印记录Service接口
 *
 * @author wxr
 * @date 2023-11-09
 */
public interface ITMaterialDetailPrintService extends IService<TMaterialDetailPrint> {

    /**
     * 查询rfid打印记录列表
     *
     * @param tMaterialDetailPrint rfid打印记录
     * @return rfid打印记录集合
     */
    public List<TMaterialDetailPrintVO> selectTMaterialDetailPrintList(TMaterialDetailPrintVO tMaterialDetailPrint);

    /**
     * 查询rfid打印记录
     *
     * @param id rfid打印记录主键
     * @return rfid打印记录
     */
    public TMaterialDetailPrint selectTMaterialDetailPrintById(Long id);

    /**
     * 新增rfid打印记录
     *
     * @param tMaterialDetailPrint rfid打印记录
     * @return 结果
     */
    public AjaxResult insertTMaterialDetailPrint(TMaterialDetailPrint tMaterialDetailPrint);

    /**
     * 修改rfid打印记录
     *
     * @param tMaterialDetailPrint rfid打印记录
     * @return 结果
     */
    public AjaxResult updateTMaterialDetailPrint(TMaterialDetailPrint tMaterialDetailPrint);

    /**
     * 批量删除rfid打印记录
     *
     * @param ids 需要删除的rfid打印记录主键集合
     * @return 结果
     */
    public int deleteTMaterialDetailPrintByIds(Long[] ids);

    /**
     * 删除rfid打印记录信息
     *
     * @param id rfid打印记录主键
     * @return 结果
     */
    public int deleteTMaterialDetailPrintById(Long id);

    AjaxResult print(TMaterialDetailPrintDTO tMaterialDetailPrint);


    AjaxResult printErCode(TMaterialDetailPrint tMaterialDetailPrint);

    /**
     * 根据入库单据id删除打印信息
     * @param advanceRegistrationId
     * @return
     */
    AjaxResult deleteByDeliveryId(Long advanceRegistrationId);
}
