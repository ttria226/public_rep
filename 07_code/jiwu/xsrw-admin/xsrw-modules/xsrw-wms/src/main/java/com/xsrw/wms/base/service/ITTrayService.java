package com.xsrw.wms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TTrayDTO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 载具管理Service接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface ITTrayService extends IService<TTray>
{

    /**
     * 查询载具管理列表
     *
     * @param tTray 载具管理
     * @return 载具管理集合
     */
    List<TTrayApiVO> selectTTrayList(TTrayDTO tTray);

    /**
     * 获取载具详情列表
     * @param tTray
     * @return
     */
    List<TTrayApiVO> selectTTrayInfoList(TTrayDTO tTray);

    /**
     * 查询载具管理
     *
     * @param id 载具管理主键
     * @return 载具管理
     */
    TTray selectTTrayById(Long id);

    /**
     * 新增载具管理
     *
     * @param tTray 载具管理
     * @return 结果
     */
    AjaxResult insertTTray(TTray tTray);

    /**
     * 修改载具管理
     *
     * @param tTray 载具管理
     * @return 结果
     */
    AjaxResult updateTTray(TTray tTray);

    /**
     * 批量删除载具管理
     *
     * @param ids 需要删除的载具管理主键集合
     * @return 结果
     */
    int deleteTTrayByIds(Long[] ids);

    /**
     * 删除载具管理信息
     *
     * @param id 载具管理主键
     * @return 结果
     */
    int deleteTTrayById(Long id);

    /**
     * 导入载具管理
     * @param file
     * @return
     */
    AjaxResult importUnit(MultipartFile file) throws Exception;

    /**
     * 查询条码所对应的托盘信息
     *
     * @param tTray
     * @return
     */
    List<TTray> selectListBylabelTemplateId(TTray tTray);

    /**
     * 根据载具id获取对应编码
     * @param trays
     * @return
     */
    Map<Long, String> getTrayCodeByIds(List<Long> trays);
    /**
     * 根据载具id获取对应载具信息
     * @param trays
     * @return
     */
    Map<Long, TTray> getTrayByIds(List<Long> trays);

    /**
     * 根据编号查询托盘信息
     * @param trayCode
     * @return
     */
    TTray selectTTrayByCode(String trayCode);

    /**
     * 托盘出库
     * @param id
     * @return
     */
    AjaxResult outStockByTrayId(Long id);
    List<Map<String,Object>>getTrayListByTaskId(Long taskId);

    /**
     * 载具出库
     * @param tTray
     * @return
     */
    AjaxResult takeOut(TTray tTray);


    /**
     * 载具出库 -- 盘点使用
     * @param tTray
     * @return
     */
    AjaxResult takeOutCheck(TTrayDTO tTray);

    /**
     * 载具出库 -- 盘点使用（新）
     * @param tTray
     * @return
     */
    AjaxResult takeOutCheckNew(TTrayDTO tTray);


    /**
     * 托盘回库
     * @param tTray
     * @return
     */
    AjaxResult recycle(TTray tTray);

    /**
     * 出库任务--托盘强制回库
     * @param tTray
     * @return
     */
    AjaxResult recycleOut(TTray tTray);

    /**
     * 载具出库/回库回调
     * @param taskWcsVO
     */
    int completeTrayBack(TTaskWcsVO taskWcsVO);

    /**
     * 根据载具编码获取组盘详情信息
     * @param trayCode
     * @return
     */
    AjaxResult getDeliveryByTrayCode(String trayCode);

    /**
     * 批量打印标签
     * @param trayDTO
     * @return
     */
    AjaxResult batchCreate(TTrayDTO trayDTO);
    /**
     * 批量打印标签
     * @param trayDTO
     * @return
     */
    List<String>  getBatchList(TTrayDTO trayDTO);

    /**
     * 选择上架载具列表
     * @param tTray
     * @return
     */
    List<TTrayApiVO> selectPutWayList(TTrayDTO tTray);

    /**
     * 根据载具编号查询入库可用状态
     * @param trayCode
     * @return
     */
    AjaxResult getStatusByCode(String trayCode);

    /**
     * 载具表解除绑定库位
     * @param id
     * @return
     */
    AjaxResult relieveLocation(Long id);
}
