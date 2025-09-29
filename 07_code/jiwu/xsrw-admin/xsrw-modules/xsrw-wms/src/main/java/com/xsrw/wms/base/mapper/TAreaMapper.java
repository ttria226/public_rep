package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TArea;
import com.xsrw.wms.base.domain.vo.TAreaVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 区域Mapper接口
 *
 * @author wxr
 * @date 2023-05-05
 */
@Repository
public interface TAreaMapper extends BaseMapper<TArea> {

    /**
     * 查询区域列表
     *
     * @param tArea 区域
     * @return 区域集合
     */
    public List<TAreaVO> selectTAreaList(TArea tArea);


    /**
     * 删除区域
     *
     * @param id 区域主键
     * @return 结果
     */
    public int deleteTAreaById(Long id);

    /**
     * 批量删除区域
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAreaByIds(Long[] ids);

    long selectDeptIdByUserId(@Param("userId") Long userId);
}
