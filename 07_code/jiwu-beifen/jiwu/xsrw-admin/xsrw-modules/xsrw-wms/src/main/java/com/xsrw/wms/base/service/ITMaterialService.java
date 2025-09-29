package com.xsrw.wms.base.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 物料Service接口
 *
 * @author wxr
 * @date 2023-05-05
 */
public interface ITMaterialService extends IService<TMaterial> {

    /**
     * 查询物料列表
     *
     * @param tMaterial 物料
     * @return 物料集合
     */
    public List<TMaterialVO> selectTMaterialList(TMaterial tMaterial);

    /**
     * 查询物料
     *
     * @param id 物料主键
     * @return 物料
     */
    public TMaterial selectTMaterialById(Long id);

    /**
     * 新增物料
     *
     * @param tMaterial 物料
     * @return 结果
     */
    public AjaxResult insertTMaterial(TMaterial tMaterial);

    /**
     * 修改物料
     *
     * @param tMaterial 物料
     * @return 结果
     */
    public int updateTMaterial(TMaterial tMaterial);

    /**
     * 批量删除物料
     *
     * @param ids 需要删除的物料主键集合
     * @return 结果
     */
    public int deleteTMaterialByIds(Long[] ids);

    /**
     * 删除物料信息
     *
     * @param id 物料主键
     * @return 结果
     */
    public int deleteTMaterialById(Long id);

    /**
     * 物料Excel导入
     *
     * @param file
     * @return
     */
    AjaxResult importUnit(MultipartFile file) throws Exception;

    /**
     * erp物料信息导入
     * @param file
     * @return
     * @throws Exception
     */
    AjaxResult importDataErp(MultipartFile file) throws Exception;

    /**
     * 批量设置物料库存上限、库存下限
     *
     * @param tMaterial
     * @return
     */
    int bacthStock(TMaterialDTO tMaterial);

    /**
     * 根据id获取编号
     * @param materialIds
     * @return
     */
    Map<Long, TMaterial> getCodeByIds(List<Long> materialIds);

    /**
     * 通过物料ids获取对应的物料重量
     * @param materialIds
     * @return
     */
    Map<Long, Double> getWeightByIds(List<Long> materialIds);

    TMaterial getCodeById(String materialCode);

    /**
     * 通过物料ids获取对应的推荐载具类型
     * @param ids
     * @return
     */
    AjaxResult getTrayTypeByMaterials(Long[] ids);

    /**
     * 物料选择列表
     * @param tMaterial
     * @return
     */
    List<TMaterialVO> getMaterialSelectList(TMaterialDTO tMaterial);

}
