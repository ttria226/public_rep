package com.xsrw.wms.inout.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TOutRecheckVO;
import com.xsrw.wms.inout.mapper.*;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.service.ITOutRecheckService;

/**
 * 出库复核单Service业务层处理
 *
 * @author wxr
 * @date 2023-06-07
 */
@Service
public class TOutRecheckServiceImpl extends ServiceImpl<TOutRecheckMapper, TOutRecheck> implements ITOutRecheckService {
    @Autowired
    private TOutRecheckMapper tOutRecheckMapper;
    @Autowired
    private ITOutDeliveryService outDeliveryService;
    @Autowired
    private TOutDeliveryMapper outDeliveryMapper;
    @Autowired
    private TOutDeliveryDetailMapper outDeliveryDetailMapper;

    /**
     * 查询出库复核单列表
     *
     * @param tOutRecheck 出库复核单
     * @return 出库复核单
     */
    @Override
    public List<TOutRecheckVO> selectTOutRecheckList(TOutRecheck tOutRecheck) {
        return tOutRecheckMapper.selectTOutRecheckList(tOutRecheck);
    }

    /**
     * 查询出库复核单
     *
     * @param id 出库复核单主键
     * @return 出库复核单
     */
    @Override
    public TOutDelivery selectTOutRecheckById(Long id) {
        TOutDelivery tOutDelivery = new TOutDelivery();
        TOutRecheck tOutRecheck = tOutRecheckMapper.selectById(id);
        if (tOutRecheck != null && tOutRecheck.getOriginId() != null) {
            tOutDelivery = outDeliveryService.selectTOutDeliveryById(tOutRecheck.getOriginId());
        }
        tOutDelivery.setId(id);
        return tOutDelivery;
    }

    /**
     * 新增出库复核单
     *
     * @param tOutRecheck 出库复核单
     * @return 结果
     */
    @Override
    public AjaxResult insertTOutRecheck(TOutRecheck tOutRecheck) {
        Long count = this.getExistCountByOriginId(tOutRecheck.getOriginId());
        if (count > 0) {
            return AjaxResult.error("当前单据已添加，不可重复添加");
        }
        tOutRecheck.setStatus(Constants.INOUT_FORM_STATUS_NOT);
        tOutRecheckMapper.insert(tOutRecheck);
        return AjaxResult.success();
    }

    /**
     * 获取原单是否已存在
     *
     * @param originId
     * @return
     */
    public Long getExistCountByOriginId(Long originId) {
        QueryWrapper<TOutRecheck> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("origin_id", originId);
        queryWrapper.in("status", Constants.INOUT_FORM_STATUS_NOT, Constants.INOUT_FORM_STATUS_PART);
        return tOutRecheckMapper.selectCount(queryWrapper);
    }

    /**
     * 修改出库复核单
     *
     * @param tOutRecheck 出库复核单
     * @return 结果
     */
    @Override
    public int updateTOutRecheck(TOutRecheck tOutRecheck) {
        return tOutRecheckMapper.updateById(tOutRecheck);
    }


    /**
     * 批量删除出库复核单
     *
     * @param ids 需要删除的出库复核单主键
     * @return 结果
     */
    @Override
    public int deleteTOutRecheckByIds(Long[] ids) {
        return tOutRecheckMapper.deleteTOutRecheckByIds(ids);
    }

    /**
     * 删除出库复核单信息
     *
     * @param id 出库复核单主键
     * @return 结果
     */
    @Override
    public int deleteTOutRecheckById(Long id) {
        return tOutRecheckMapper.deleteTOutRecheckById(id);
    }
}
