package com.xsrw.wms.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.check.domain.TCheckResult;
import com.xsrw.wms.check.domain.dto.CheckResultDTO;
import com.xsrw.wms.check.domain.vo.CheckResultVO;

import java.util.List;

/**
 * 盘点差异Mapper接口
 * 
 * @author lyx
 * @date 2023-05-09
 */
public interface TCheckResultMapper extends BaseMapper<TCheckResult>
{

    /**
     * 查询盘点差异列表
     * 
     * @param tCheckResult 盘点差异
     * @return 盘点差异集合
     */
    List<TCheckResult> selectTCheckResultList(TCheckResult tCheckResult);


    /**
     * 删除盘点差异
     * 
     * @param id 盘点差异主键
     * @return 结果
     */
    int deleteTCheckResultById(Long id);

    /**
     * 批量删除盘点差异
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTCheckResultByIds(Long[] ids);

    /**
     * 查询盘点差异报表列表
     *
     * @param checkResult 盘点差异报表
     * @return 盘点差异报表集合
     */
    List<CheckResultVO> selectCheckResultList(CheckResultDTO checkResult);
}
