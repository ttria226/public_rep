package com.xsrw.wms.base.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TBomDetail;

/**
 * bom详情Service接口
 *
 * @author zjj
 * @date 2023-06-10
 */
public interface ITBomDetailService extends IService<TBomDetail>
{

    /**
     * 查询bom详情列表
     *
     * @param tBomDetail bom详情
     * @return bom详情集合
     */
    public List<TBomDetail> selectTBomDetailList(TBomDetail tBomDetail);

    /**
     * 查询bom详情
     *
     * @param id bom详情主键
     * @return bom详情
     */
    public TBomDetail selectTBomDetailById(Long id);

    /**
     * 新增bom详情
     *
     * @param tBomDetail bom详情
     * @return 结果
     */
    public int insertTBomDetail(TBomDetail tBomDetail);

    /**
     * 修改bom详情
     *
     * @param tBomDetail bom详情
     * @return 结果
     */
    public int updateTBomDetail(TBomDetail tBomDetail);

    /**
     * 批量删除bom详情
     *
     * @param ids 需要删除的bom详情主键集合
     * @return 结果
     */
    public int deleteTBomDetailByIds(Long[] ids);

    /**
     * 删除bom详情信息
     *
     * @param id bom详情主键
     * @return 结果
     */
    public int deleteTBomDetailById(Long id);
}
