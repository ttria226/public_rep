package com.xsrw.wms.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物料Mapper接口
 *
 * @author wxr
 * @date 2023-05-05
 */
@Repository
public interface TMaterialMapper extends BaseMapper<TMaterial> {

    /**
     * 查询物料列表
     *
     * @param tMaterial 物料
     * @return 物料集合
     */
    public List<TMaterialVO> selectTMaterialList(TMaterial tMaterial);


    /**
     * 删除物料
     *
     * @param id 物料主键
     * @return 结果
     */
    public int deleteTMaterialById(Long id);

    /**
     * 批量删除物料
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMaterialByIds(Long[] ids);

    /**
     * 查询批次列表
     * @param tMaterial
     * @return
     */
    List<TMaterialVO> getMaterialSelectList(TMaterialDTO tMaterial);

    /**
     * 根据id查询物料详情
     * @param materialId
     * @return
     */
    TMaterialVO selectInfoById(Long materialId);
}
