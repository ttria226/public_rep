package com.xsrw.wms.base.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TBom;

/**
 * bomMapper接口
 * 
 * @author zjj
 * @date 2023-06-10
 */
public interface TBomMapper extends BaseMapper<TBom>
{

    /**
     * 查询bom列表
     * 
     * @param tBom bom
     * @return bom集合
     */
    public List<TBom> selectTBomList(TBom tBom);


    /**
     * 删除bom
     * 
     * @param id bom主键
     * @return 结果
     */
    public int deleteTBomById(Long id);

    /**
     * 批量删除bom
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTBomByIds(Long[] ids);
}
