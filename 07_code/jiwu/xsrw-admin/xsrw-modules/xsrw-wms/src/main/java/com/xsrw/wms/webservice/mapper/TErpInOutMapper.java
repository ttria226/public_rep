package com.xsrw.wms.webservice.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.webservice.domain.TErpInOut;
import com.xsrw.wms.webservice.domain.vo.wms2erp.TErpInOutInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * ERP-出入库信息Mapper接口
 */
@Repository
public interface TErpInOutMapper extends BaseMapper<TErpInOut> {
    /**
     * 查询ERP-出入库信息
     *
     * @param id ERP-出入库信息主键
     * @return ERP-出入库信息
     */
    public TErpInOut selectTErpInOutById(Long id);

    /**
     * 查询ERP-出入库信息列表
     *
     * @param tErpInOut ERP-出入库信息
     * @return ERP-出入库信息集合
     */
    public List<TErpInOut> selectTErpInOutList(TErpInOut tErpInOut);

    /**
     * 新增ERP-出入库信息
     *
     * @param tErpInOut ERP-出入库信息
     * @return 结果
     */
    public int insertTErpInOut(TErpInOut tErpInOut);

    /**
     * 修改ERP-出入库信息
     *
     * @param tErpInOut ERP-出入库信息
     * @return 结果
     */
    public int updateTErpInOut(TErpInOut tErpInOut);

    /**
     * 删除ERP-出入库信息
     *
     * @param id ERP-出入库信息主键
     * @return 结果
     */
    public int deleteTErpInOutById(Long id);

    /**
     * 批量删除ERP-出入库信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTErpInOutByIds(Long[] ids);

    /**
     * 根据出入库任务id获取物料详情
     * @param wcsId
     * @return
     */
    List<TErpInOutInfoVO> selectTTaskInErpById(@Param("taskType") String taskType, @Param("wcsId") Long wcsId, @Param("zzdjbm") String zzdjbm, @Param("zzdjhh") String zzdjhh);

    int selectTTaskErpquantity(@Param("wcsId")Long wcsId);
}
