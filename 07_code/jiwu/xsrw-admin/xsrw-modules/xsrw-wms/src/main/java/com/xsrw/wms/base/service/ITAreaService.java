package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TArea;
import com.xsrw.wms.base.domain.vo.TAreaVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 区域Service接口
 *
 * @author wxr
 * @date 2023-05-05
 */
public interface ITAreaService extends IService<TArea> {

    /**
     * 查询区域列表
     *
     * @param tArea 区域
     * @return 区域集合
     */
    public List<TAreaVO> selectTAreaList(TArea tArea);

    /**
     * 查询区域
     *
     * @param id 区域主键
     * @return 区域
     */
    public TArea selectTAreaById(Long id);

    /**
     * 新增区域
     *
     * @param tArea 区域
     * @return 结果
     */
    public int insertTArea(TArea tArea);

    /**
     * 修改区域
     *
     * @param tArea 区域
     * @return 结果
     */
    public int updateTArea(TArea tArea);

    /**
     * 批量删除区域
     *
     * @param ids 需要删除的区域主键集合
     * @return 结果
     */
    public int deleteTAreaByIds(Long[] ids);

    /**
     * 删除区域信息
     *
     * @param id 区域主键
     * @return 结果
     */
    public int deleteTAreaById(Long id);

    AjaxResult importUnit(MultipartFile file) throws Exception ;
}
