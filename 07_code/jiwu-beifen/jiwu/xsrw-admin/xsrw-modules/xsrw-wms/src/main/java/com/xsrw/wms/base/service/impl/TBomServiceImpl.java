package com.xsrw.wms.base.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TBomDetail;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.base.mapper.TBomDetailMapper;
import com.xsrw.wms.base.service.ITBomDetailService;
import com.xsrw.wms.base.service.ITCodeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TBomMapper;
import com.xsrw.wms.base.domain.TBom;
import com.xsrw.wms.base.service.ITBomService;
import org.springframework.transaction.annotation.Transactional;

/**
 * bomService业务层处理
 *
 * @author zjj
 * @date 2023-06-10
 */
@Service
public class TBomServiceImpl extends ServiceImpl<TBomMapper, TBom> implements ITBomService {
    @Autowired
    private TBomMapper tBomMapper;

    @Autowired
    private TBomDetailMapper tBomDetailMapper;
    @Autowired
    private ITBomDetailService bomDetailService;
    @Autowired
    private ITCodeConfigService itCodeConfigService;
    /**
     * 查询bom列表
     *
     * @param tBom bom
     * @return bom
     */
    @Override
    public List<TBom> selectTBomList(TBom tBom) {
        return tBomMapper.selectTBomList(tBom);
    }

    /**
     * 查询bom
     *
     * @param id bom主键
     * @return bom
     */
    @Override
    public TBom selectTBomById(Long id) {
        TBom tBom = tBomMapper.selectById(id);
        TBomDetail tBomDetail = new TBomDetail();
        tBomDetail.setBomId(tBom.getId());
        List<TBomDetail> tBomDetails = tBomDetailMapper.selectTBomDetailList(tBomDetail);
        tBom.setBomDetails(tBomDetails);
        return tBom;
    }

    /**
     * 新增bom
     *
     * @param tBom bom
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTBom(TBom tBom) {
        String code = itCodeConfigService.getCode(CodeEnum.BOM.getCodeName());
        tBom.setCode(code);
        int insert = tBomMapper.insert(tBom);
        List<TBomDetail> bomDetails = tBom.getBomDetails();
        bomDetails.forEach(e -> {
            if (e.getCount() == null || e.getMaterialId() == null) {
                throw new ServiceException("物料数量不可为空");
            }
            e.setBomId(tBom.getId());
        });
        bomDetailService.saveBatch(bomDetails);
        return insert;
    }

    /**
     * 修改bom
     *
     * @param tBom bom
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTBom(TBom tBom) {
        int i = tBomMapper.updateById(tBom);
        Long[] ids = new Long[1];
        ids[0] = tBom.getId();
        tBomDetailMapper.deleteTBomDetailByBomId(ids);
        List<TBomDetail> bomDetails = tBom.getBomDetails();
        bomDetails.forEach(e -> {
            e.setId(null);
            e.setBomId(tBom.getId());
        });
        bomDetailService.saveBatch(bomDetails);
        return i;
    }


    /**
     * 批量删除bom
     *
     * @param ids 需要删除的bom主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTBomByIds(Long[] ids) {
        tBomDetailMapper.deleteTBomDetailByBomId(ids);
        return tBomMapper.deleteTBomByIds(ids);
    }

    /**
     * 删除bom信息
     *
     * @param id bom主键
     * @return 结果
     */
    @Override
    public int deleteTBomById(Long id) {
        Long[] ids = new Long[1];
        ids[0] = id;
        tBomDetailMapper.deleteTBomDetailByBomId(ids);
        return tBomMapper.deleteTBomById(id);
    }

    /**
     * 根据bom-id获取物料信息列表
     *
     * @param id
     * @return
     */
    @Override
    public List<TMaterialVO> getMaterialListByBomId(Long id) {
        List<TMaterialVO> list = tBomDetailMapper.getMaterialListByBomId(id);
        if (!CollectionUtils.isEmpty(list)) {
            Date makeDate = DateUtils.getNowDate();
            Date finalMakeDate = makeDate;
            list.forEach(e -> {
                e.setBatchCode(itCodeConfigService.getBatchCode1(e.getId(), finalMakeDate));
            });
        }
        return list;
    }

    /**
     * 通过id获取bom详情库存信息列表
     *
     * @param id
     * @return
     */
    @Override
    public List<TMaterialVO> selectTBomDetailStockList(Long id) {
        return tBomDetailMapper.selectTBomDetailStockList(id, null);
    }

}
