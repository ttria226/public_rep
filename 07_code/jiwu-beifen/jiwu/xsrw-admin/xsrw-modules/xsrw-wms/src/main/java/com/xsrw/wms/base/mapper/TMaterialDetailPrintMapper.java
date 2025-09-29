package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TMaterialDetailPrint;
import com.xsrw.wms.base.domain.vo.TMaterialDetailPrintVO;
import org.springframework.stereotype.Repository;

/**
 * rfid打印记录Mapper接口
 *
 * @author wxr
 * @date 2023-11-09
 */
@Repository
public interface TMaterialDetailPrintMapper extends BaseMapper<TMaterialDetailPrint> {

    /**
     * 查询rfid打印记录列表
     *
     * @param tMaterialDetailPrint rfid打印记录
     * @return rfid打印记录集合
     */
    public List<TMaterialDetailPrintVO> selectTMaterialDetailPrintList(TMaterialDetailPrintVO tMaterialDetailPrint);


    /**
     * 删除rfid打印记录
     *
     * @param id rfid打印记录主键
     * @return 结果
     */
    public int deleteTMaterialDetailPrintById(Long id);

    /**
     * 批量删除rfid打印记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMaterialDetailPrintByIds(Long[] ids);
    /**
     * 通过入库单详情id删除
     * @param id
     * @return
     */
    public int deletePrintByRegisterId(Long id);

    /**
     * 通过入库单详情id删除
     * @param ids
     * @return
     */
    public int deletePrintByRegisterIds(Long[] ids);
}
