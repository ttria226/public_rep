package com.xsrw.wms.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;

import java.util.List;

/**
 * 库内移位Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITMoveLibraryService extends IService<TMoveLibrary>
{

    /**
     * 查询库内移位列表
     *
     * @param moveLibrary 库内移位
     * @return 库内移位集合
     */
    List<MoveLibraryVo> selectTMoveLibraryList(MoveLibraryVo moveLibrary);

    /**
     * 查询库内移位
     *
     * @param id 库内移位主键
     * @return 库内移位
     */
    MoveLibraryVo selectTMoveLibraryById(Long id);

    /**
     * 新增库内移位
     *
     * @param tMoveLibrary 库内移位
     * @return 结果
     */
    int insertTMoveLibrary(TMoveLibrary tMoveLibrary);

    /**
     * 修改库内移位
     *
     * @param tMoveLibrary 库内移位
     * @return 结果
     */
    int updateTMoveLibrary(TMoveLibrary tMoveLibrary);

    /**
     * 批量删除库内移位
     *
     * @param ids 需要删除的库内移位主键集合
     * @return 结果
     */
    AjaxResult deleteTMoveLibraryByIds(Long[] ids);

    /**
     * 删除库内移位信息
     *
     * @param id 库内移位主键
     * @return 结果
     */
    int deleteTMoveLibraryById(Long id);

    /**
     * 库内移位添加数据
     * @param stockId 库存id
     * @param locationInId 转入库位ID
     * @return
     */
    AjaxResult shift(Long stockId, Long locationInId);

    /**
     * 库内移位，直接生成移库任务
     * @param stockId
     * @param locationInId
     * @return
     */
    AjaxResult shiftMoveLibrary(Long stockId, Long locationInId);


    /**
     * 库内移位审核
     * @param id
     * @return
     */
    AjaxResult updateAuditor(Long id);

    /**
     * 生成移库任务
     * @param id
     * @return
     */
    AjaxResult move(Long id);
}
