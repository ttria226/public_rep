package com.xsrw.wms.base.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.vo.TClassesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TClassesMapper;
import com.xsrw.wms.base.domain.TClasses;
import com.xsrw.wms.base.service.ITClassesService;

/**
 * 班次管理Service业务层处理
 *
 * @author wxr
 * @date 2023-06-12
 */
@Service
public class TClassesServiceImpl extends ServiceImpl<TClassesMapper, TClasses> implements ITClassesService {
    @Autowired
    private TClassesMapper tClassesMapper;


    /**
     * 查询班次管理列表
     *
     * @param tClasses 班次管理
     * @return 班次管理
     */
    @Override
    public List<TClassesVO> selectTClassesList(TClasses tClasses) {
        return tClassesMapper.selectTClassesList(tClasses);
    }

    /**
     * 查询班次管理
     *
     * @param id 班次管理主键
     * @return 班次管理
     */
    @Override
    public TClasses selectTClassesById(Long id) {
        return tClassesMapper.selectById(id);
    }

    /**
     * 新增班次管理
     *
     * @param tClasses 班次管理
     * @return 结果
     */
    @Override
    public AjaxResult insertTClasses(TClasses tClasses) {
        int existCount = tClassesMapper.getExistCount(tClasses);
        if(existCount > 0){
            return AjaxResult.error("当前人员，同一时间已有排班");
        }
        tClassesMapper.insert(tClasses);
        return AjaxResult.success();
    }

    /**
     * 修改班次管理
     *
     * @param tClasses 班次管理
     * @return 结果
     */
    @Override
    public AjaxResult updateTClasses(TClasses tClasses) {
        int existCount = tClassesMapper.getExistCount(tClasses);
        if(existCount > 0){
            return AjaxResult.error("当前人员，同一时间已有排班");
        }
        tClassesMapper.updateById(tClasses);
        return AjaxResult.success();
    }


    /**
     * 批量删除班次管理
     *
     * @param ids 需要删除的班次管理主键
     * @return 结果
     */
    @Override
    public int deleteTClassesByIds(Long[] ids) {
        return tClassesMapper.deleteTClassesByIds(ids);
    }

    /**
     * 删除班次管理信息
     *
     * @param id 班次管理主键
     * @return 结果
     */
    @Override
    public int deleteTClassesById(Long id) {
        return tClassesMapper.deleteTClassesById(id);
    }
}
