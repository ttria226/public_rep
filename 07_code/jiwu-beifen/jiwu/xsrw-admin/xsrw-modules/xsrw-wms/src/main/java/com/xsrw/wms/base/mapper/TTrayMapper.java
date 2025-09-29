package com.xsrw.wms.base.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TTrayDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 载具管理Mapper接口
 *
 * @author lyx
 * @date 2023-05-05
 */
@Repository
public interface TTrayMapper extends BaseMapper<TTray>
{

    /**
     * 查询载具管理列表
     *
     * @param tTray 载具管理
     * @return 载具管理集合
     */
    List<TTray> selectTTrayList(TTrayDTO tTray);

    /**
     * 获取载具信息列表
     * @param tTray
     * @return
     */
    List<TTrayApiVO> selectTTrayInfoList(TTrayDTO tTray);


    /**
     * 删除载具管理
     *
     * @param id 载具管理主键
     * @return 结果
     */
    int deleteTTrayById(Long id);

    /**
     * 批量删除载具管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTTrayByIds(Long[] ids);
    List<Map<String,Object>>getTrayListByTaskId(Long taskId);

    /**
     * 根据托盘ID获取是否已有相应库位
     * @param trayId
     * @return
     */
    Long getLocationInfoById(Long trayId);

    /**
     * 根据托盘ID获取是否已有相应库位
     * @param trayId
     * @return
     */
    TLocation getLocationById(Long trayId);

    /**
     * 获取载具信息
     * @param trayId
     * @return
     */
    TTray getLocationDetailById(Long trayId);

    /**
     * 获取详细信息
     * @param trayCode
     * @param trayId
     * @return
     */
    TTrayApiVO getTrayInfo(@Param("trayCode") String trayCode, @Param("trayId") Long trayId);

    /**
     * 获取载具编号
     * @param trayDTO
     * @return
     */
    List<String> getBatchCodeList(TTrayDTO trayDTO);

    /**
     * 选择上架载具列表
     * @param tTray
     * @return
     */
    List<TTrayApiVO> selectPutWayList(TTrayDTO tTray);
}
