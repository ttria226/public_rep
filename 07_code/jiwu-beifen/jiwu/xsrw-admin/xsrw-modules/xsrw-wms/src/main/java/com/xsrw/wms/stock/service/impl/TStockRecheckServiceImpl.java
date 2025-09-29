package com.xsrw.wms.stock.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSerachDTO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.stock.domain.dto.TStockRecheckDTO;
import com.xsrw.wms.stock.domain.vo.TStockRecheckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.stock.mapper.TStockRecheckMapper;
import com.xsrw.wms.stock.domain.TStockRecheck;
import com.xsrw.wms.stock.service.ITStockRecheckService;

import static com.xsrw.common.core.utils.PageUtils.startPage;

/**
 * 复检管理Service业务层处理
 *
 * @author wxr
 * @date 2023-06-21
 */
@Service
public class TStockRecheckServiceImpl extends ServiceImpl<TStockRecheckMapper, TStockRecheck> implements ITStockRecheckService {
    @Autowired
    private TStockRecheckMapper tStockRecheckMapper;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    /**
     * 查询复检管理列表
     *
     * @param tStockRecheck 复检管理
     * @return 复检管理
     */
    @Override
    public List<TStockRecheckVO> selectTStockRecheckList(TStockRecheckDTO tStockRecheck) {
        List<TStockRecheckVO> list = tStockRecheckMapper.selectTStockRecheckList(tStockRecheck);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(e -> {
                if (e.getFailCount() == 0) {
                    e.setRecheckResult("全部通过");
                } else {
                    e.setRecheckResult(e.getFailCount() + "个未通过");
                }
            });
        }
        return list;
    }

    /**
     * 查询复检管理
     *
     * @param id 复检管理主键
     * @return 复检管理
     */
    @Override
    public TStockRecheck selectTStockRecheckById(Long id) {
        return tStockRecheckMapper.selectById(id);
    }

    /**
     * 新增复检管理
     *
     * @param tStockRecheck 复检管理
     * @return 结果
     */
    @Override
    public int insertTStockRecheck(TStockRecheck tStockRecheck) {
        tStockRecheck.setStatus(Constants.INOUT_STATUS_NOT);
        tStockRecheck.setFailCount(0L);
        return tStockRecheckMapper.insert(tStockRecheck);
    }

    /**
     * 修改复检管理
     *
     * @param tStockRecheck 复检管理
     * @return 结果
     */
    @Override
    public int updateTStockRecheck(TStockRecheck tStockRecheck) {
        return tStockRecheckMapper.updateById(tStockRecheck);
    }


    /**
     * 批量删除复检管理
     *
     * @param ids 需要删除的复检管理主键
     * @return 结果
     */
    @Override
    public int deleteTStockRecheckByIds(Long[] ids) {
        return tStockRecheckMapper.deleteTStockRecheckByIds(ids);
    }

    /**
     * 删除复检管理信息
     *
     * @param id 复检管理主键
     * @return 结果
     */
    @Override
    public int deleteTStockRecheckById(Long id) {
        return tStockRecheckMapper.deleteTStockRecheckById(id);
    }

    /**
     * 复检完成
     *
     * @param tStockRecheck
     * @return
     */
    @Override
    public AjaxResult checkEnd(TStockRecheck tStockRecheck) {
        TStockRecheckDTO tStockRecheckDTO = new TStockRecheckDTO();
        tStockRecheckDTO.setId(tStockRecheck.getId());
        List<TStockRecheckVO> list = tStockRecheckMapper.selectTStockRecheckList(tStockRecheckDTO);
        if (CollectionUtils.isEmpty(list)) {
            return AjaxResult.error("未查询到相关信息");
        }
        TStockRecheckVO tStockRecheckVO = list.get(0);
        if (Constants.INOUT_STATUS_END.equals(tStockRecheckVO.getStatus())) {
            return AjaxResult.error();
        }
        Integer failCount = 0;
        TMaterialDetailSerachDTO tMaterialDetail = new TMaterialDetailSerachDTO();
        tMaterialDetail.setMaterialId(tStockRecheckVO.getMaterialId());
        tMaterialDetail.setBatchCode(tStockRecheckVO.getBatchCode());
        tMaterialDetail.setLocationId(tStockRecheckVO.getLocationId());
        tMaterialDetail.setDetectionFailType("2");
        List<TMaterialDetailVO> tMaterialDetailVOList = tMaterialDetailMapper.selectTMaterialDetailList(tMaterialDetail);
        if (CollectionUtils.isNotEmpty(tMaterialDetailVOList)) {
//            tMaterialDetailVOList = tMaterialDetailVOList.stream().filter(e -> StringUtils.isNotEmpty(e.getDetectionFailType())).collect(Collectors.toList());
            failCount = tMaterialDetailVOList.size();
        }
        tStockRecheck.setStatus(Constants.INOUT_STATUS_END);
        tStockRecheck.setFailCount(Long.valueOf(failCount));
        tStockRecheckMapper.updateById(tStockRecheck);
        return AjaxResult.success();
    }

    /**
     * 获取库存的物料详情列表
     * @param tStockRecheck
     * @return
     */
    @Override
    public List<TMaterialDetailVO> getMaterialDetailList(TStockRecheck tStockRecheck) {
        List<TMaterialDetailVO> tMaterialDetailVOList = new ArrayList<>();
        TStockRecheckDTO tStockRecheckDTO = new TStockRecheckDTO();
        tStockRecheckDTO.setId(tStockRecheck.getId());
        List<TStockRecheckVO> list = tStockRecheckMapper.selectTStockRecheckList(tStockRecheckDTO);
        if (CollectionUtils.isNotEmpty(list)) {
            TStockRecheckVO tStockRecheckVO = list.get(0);
            startPage();
            TMaterialDetailSerachDTO tMaterialDetail = new TMaterialDetailSerachDTO();
            tMaterialDetail.setMaterialId(tStockRecheckVO.getMaterialId());
            tMaterialDetail.setBatchCode(tStockRecheckVO.getBatchCode());
            tMaterialDetail.setLocationId(tStockRecheckVO.getLocationId());
            tMaterialDetailVOList = tMaterialDetailMapper.selectTMaterialDetailList(tMaterialDetail);
        }
        return tMaterialDetailVOList;
    }


}
