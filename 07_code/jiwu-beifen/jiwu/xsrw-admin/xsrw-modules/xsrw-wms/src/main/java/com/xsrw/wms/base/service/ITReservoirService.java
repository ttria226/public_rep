package com.xsrw.wms.base.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TReservoir;
import com.xsrw.wms.base.domain.vo.TReservoirVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 库区Service接口
 *
 * @author wxr
 * @date 2023-05-05
 */
public interface ITReservoirService extends IService<TReservoir> {

    /**
     * 查询库区列表
     *
     * @param tReservoir 库区
     * @return 库区集合
     */
    public List<TReservoirVO> selectTReservoirList(TReservoir tReservoir);

    /**
     * 查询库区
     *
     * @param id 库区主键
     * @return 库区
     */
    public TReservoir selectTReservoirById(Long id);

    /**
     * 新增库区
     *
     * @param tReservoir 库区
     * @return 结果
     */
    public int insertTReservoir(TReservoir tReservoir);

    /**
     * 修改库区
     *
     * @param tReservoir 库区
     * @return 结果
     */
    public int updateTReservoir(TReservoir tReservoir);

    /**
     * 批量删除库区
     *
     * @param ids 需要删除的库区主键集合
     * @return 结果
     */
    public int deleteTReservoirByIds(Long[] ids);

    /**
     * 删除库区信息
     *
     * @param id 库区主键
     * @return 结果
     */
    public int deleteTReservoirById(Long id);

    /**
     * 导入库区列表
     * @param file
     * @return
     * @throws Exception
     */
    AjaxResult importReservoir(MultipartFile file) throws Exception;

    /**
     * 库区禁用、启用
     * @param status
     * @param id
     * @return
     */
    int deleteTReservoirStatusByIds(String status, Long id);
    List<Map<String,Object>>getReservoirList(Integer type,Integer areaId);
}
