package com.xsrw.wms.base.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TOutStrategyMapper;
import com.xsrw.wms.base.domain.TOutStrategy;
import com.xsrw.wms.base.service.ITOutStrategyService;

/**
 * 拣货策略Service业务层处理
 *
 * @author wxr
 * @date 2023-05-06
 */
@Service
public class TOutStrategyServiceImpl extends ServiceImpl<TOutStrategyMapper, TOutStrategy> implements ITOutStrategyService {
    @Autowired
    private TOutStrategyMapper tOutStrategyMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;


    /**
     * 查询拣货策略列表
     *
     * @param tOutStrategy 拣货策略
     * @return 拣货策略
     */
    @Override
    public List<TOutStrategy> selectTOutStrategyList(TOutStrategy tOutStrategy) {
        return tOutStrategyMapper.selectTOutStrategyList(tOutStrategy);
    }

    /**
     * 查询拣货策略
     *
     * @param id 拣货策略主键
     * @return 拣货策略
     */
    @Override
    public TOutStrategy selectTOutStrategyById(Long id) {
        return tOutStrategyMapper.selectById(id);
    }

    /**
     * 新增拣货策略
     *
     * @param tOutStrategy 拣货策略
     * @return 结果
     */
    @Override
    public int insertTOutStrategy(TOutStrategy tOutStrategy) {
        return tOutStrategyMapper.insert(tOutStrategy);
    }

    /**
     * 修改拣货策略
     *
     * @param outStrategy 拣货策略
     * @return 结果
     */
    @Override
    public int updateTOutStrategy(TOutStrategy outStrategy) {
        outStrategy = tOutStrategyMapper.selectById(outStrategy.getId());

        if (Constants.NO.equals(outStrategy.getFlag())) {
            // 将启用的策略关闭
            LambdaUpdateWrapper<TOutStrategy> lambdaUpdateWrapper = new LambdaUpdateWrapper();
            lambdaUpdateWrapper.eq(TOutStrategy::getFlag, Constants.YES).set(TOutStrategy::getFlag, Constants.NO);
            tOutStrategyMapper.update(null, lambdaUpdateWrapper);

            // 更新当前启用数据
            outStrategy.setFlag(Constants.YES);
            tOutStrategyMapper.updateById(outStrategy);
        }
        return 1;
    }


    /**
     * 批量删除拣货策略
     *
     * @param ids 需要删除的拣货策略主键
     * @return 结果
     */
    @Override
    public int deleteTOutStrategyByIds(Long[] ids) {
        return tOutStrategyMapper.deleteTOutStrategyByIds(ids);
    }

    /**
     * 删除拣货策略信息
     *
     * @param id 拣货策略主键
     * @return 结果
     */
    @Override
    public int deleteTOutStrategyById(Long id) {
        return tOutStrategyMapper.deleteTOutStrategyById(id);
    }

    @Override
    public boolean getStrategyByIsBatch() {
        TOutStrategy outStrategy = tOutStrategyMapper.selectOne(new LambdaQueryWrapper<TOutStrategy>()
                .eq(TOutStrategy::getFlag, Constants.YES)
                .eq(TOutStrategy::getDelFlag, Constants.DEL_FLAG_NO)
                .eq(TOutStrategy::getStrategyType, Constants.BATCH));

        if (ObjectUtils.isNotNull(outStrategy)) {
            return true;
        }

        return false;
    }

    @Override
    public String getStrategy(Long materialId) {
        // 排序规则
        String strategySql = "";

        // 获取启用的拣货策略，并根据优先级排序
        TOutStrategy model = tOutStrategyMapper.selectOne(new LambdaQueryWrapper<TOutStrategy>()
                .eq(TOutStrategy::getFlag, Constants.YES)
                .eq(TOutStrategy::getDelFlag, Constants.DEL_FLAG_NO));

        if (ObjectUtils.isNotNull(model)) {
            // 先进先出
            if (Constants.FIFO.equals(model.getStrategyType())) {
                strategySql = " order by begin_date asc ";
            } else if (Constants.LIFO.equals(model.getStrategyType())) {
                //后进先出
                strategySql = " order by begin_date desc ";
            } else if (Constants.RANDOM.equals(model.getStrategyType())) {
                // 取消随机

            } else if (Constants.BATCH.equals(model.getStrategyType())) {
                // 批次
                // 获取该物料信息
                TMaterialVO material = tMaterialMapper.selectInfoById(materialId);

                // 是否启用批次,是否有批次属性ID
                if (Constants.YES.equals(material.getBatchFlag()) && ObjectUtils.isNotNull(material.getBatchAttrName())) {
                    //批次属性
                    if (Constants.MATERIAL_BATCH_TYPE_MAKE_DATE.equals(material.getBatchAttrName())
                            || Constants.MATERIAL_BATCH_TYPE_PRODUCT_DATE.equals(material.getBatchAttrName()) || Constants.MATERIAL_BATCH_TYPE_DAY_COUNT.equals(material.getBatchAttrName())) {
                        strategySql = " order by produced_date asc, begin_date asc ";
                    } else {
                        strategySql = " order by begin_date asc ";
                    }
                }
            }
        }

        // 默认排序先进先出
        if (StringUtils.isEmpty(strategySql)) {
            strategySql = " order by begin_date asc ";
        }

        return strategySql;
    }
}
