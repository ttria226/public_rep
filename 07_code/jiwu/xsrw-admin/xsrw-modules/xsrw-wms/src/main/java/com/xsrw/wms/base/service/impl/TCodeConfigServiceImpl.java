package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TCodeConfig;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.base.mapper.TCodeConfigMapper;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 编码配置Service业务层处理
 */
@Service
public class TCodeConfigServiceImpl extends ServiceImpl<TCodeConfigMapper, TCodeConfig> implements ITCodeConfigService {
    @Autowired
    private TCodeConfigMapper codeConfigMapper;

    @Autowired
    private RedisService redisService;
    @Autowired
    private TMaterialMapper tMaterialMapper;

    /**
     * 查询编码配置
     *
     * @param id 编码配置主键
     * @return 编码配置
     */
    @Override
    public TCodeConfig selectCodeConfigById(Long id) {
        return codeConfigMapper.selectById(id);
    }

    /**
     * 查询编码配置列表
     *
     * @param codeConfig 编码配置
     * @return 编码配置
     */
    @Override
    public List<TCodeConfig> selectCodeConfigList(TCodeConfig codeConfig) {
        return codeConfigMapper.selectCodeConfigList(codeConfig);
    }

    /**
     * 新增编码配置
     *
     * @param codeConfig 编码配置
     * @return 结果
     */
    @Override
    public AjaxResult insertCodeConfig(TCodeConfig codeConfig) {

        if (Constants.DATE_EMPTY.equals(codeConfig.getMiddleDate()) &&
                Constants.RULE_EMPTY.equals(codeConfig.getAfterNumberType())) {
            return AjaxResult.error("中间日期和后缀必须配置一种");
        }

        // 查询编码配置是否已存在
        TCodeConfig typeCode = codeConfigMapper.selectOne(new QueryWrapper<TCodeConfig>().eq("type_code", codeConfig.getTypeCode()));
        if (typeCode != null) {
            return AjaxResult.error("该配置已存在");
        }

//        codeConfig.setType(CodeEnum.getValue(codeConfig.getTypeCode()));
        return AjaxResult.success(codeConfigMapper.insert(codeConfig));
    }

    /**
     * 修改编码配置
     *
     * @param codeConfig 编码配置
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult updateCodeConfig(TCodeConfig codeConfig) {

        if (Constants.DATE_EMPTY.equals(codeConfig.getMiddleDate()) &&
                Constants.RULE_EMPTY.equals(codeConfig.getAfterNumberType())) {
            return AjaxResult.error("中间日期和后缀必须配置一种");
        }

        // 查询编码配置是否已存在
        TCodeConfig typeCode = codeConfigMapper.selectOne(new QueryWrapper<TCodeConfig>().eq("type_code", codeConfig.getTypeCode()));
        if (typeCode != null) {
            if (codeConfig.getId() - typeCode.getId() != 0) {
                return AjaxResult.error("该配置已存在");
            }
        }

//        codeConfig.setType(CodeEnum.getValue(codeConfig.getTypeCode()));

        return AjaxResult.success(codeConfigMapper.updateById(codeConfig));
    }

    /**
     * 批量删除编码配置
     *
     * @param ids 需要删除的编码配置主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteCodeConfigByIds(Long[] ids) {
        return codeConfigMapper.deleteCodeConfigByIds(ids);
    }

    /**
     * 删除编码配置信息
     *
     * @param id 编码配置主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteCodeConfigById(Long id) {
        return codeConfigMapper.deleteCodeConfigById(id);
    }

    /**
     * 根据编码配置生成编码
     *
     * @param modelName
     * @return
     */
    @Override
    public String getCode(String modelName) {
        QueryWrapper<TCodeConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_code", modelName).eq("del_flag", Constants.DEL_FLAG_NO);
        TCodeConfig codeConfig = codeConfigMapper.selectOne(queryWrapper);
        if (codeConfig == null) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        result.append(codeConfig.getBeforeCode());

        // 拼接日期
        if (Constants.DATE_DAY.equals(codeConfig.getMiddleDate())) {
            result.append(DateUtils.parseDateToStr("yyyyMMdd", new Date()));
        }
        if (Constants.DATE_SECOND.equals(codeConfig.getMiddleDate())) {
            result.append(DateUtils.parseDateToStr("yyyyMMddHHmmss", new Date()));
        }
        if (Constants.DATE_MILLISECOND.equals(codeConfig.getMiddleDate())) {
            result.append(System.currentTimeMillis());
        }

        // 拼接最后规则
        if (Constants.RULE_AUTO.equals(codeConfig.getAfterNumberType())) {
            // 自增值由redis维护
            int ruleValue = Integer.valueOf(codeConfig.getRuleValue());
            // 判断键值是否存在  不存在则从1开始
            Boolean hasKey = redisService.hasKey("wms:codecreate:" + modelName);
            if (hasKey == false) {
                StringBuffer str = new StringBuffer();
                str.append("1");
                for (int i = 0; i < ruleValue; i++) {
                    if (str.length() == ruleValue) {
                        break;
                    }
                    str.append("0");
                }
                String currentValue = str.reverse().toString();
                result.append(currentValue);

                codeConfig.setCurrentIndex(1L);
            } else {
                // redis中获取自增值
                Long currentIndex = redisService.getCacheObject("wms:codecreate:" + modelName);
                StringBuffer str = new StringBuffer();
                str.append(currentIndex + 1L);
                for (int i = 0; i < ruleValue - currentIndex.toString().length(); i++) {
                    if (str.length() == ruleValue) {
                        break;
                    }
                    str.insert(0,"0");
                }
                result.append(str);

                codeConfig.setCurrentIndex(currentIndex + 1L);
            }
            redisService.setCacheObject("wms:codecreate:" + modelName, codeConfig.getCurrentIndex());
        }
        if (Constants.RULE_ROUND.equals(codeConfig.getAfterNumberType())) {
            String uuid = UUID.randomUUID().toString();
            String upperCase = uuid.substring(0, Integer.valueOf(codeConfig.getRuleValue())).toUpperCase();
            result.append(upperCase);
        }
        return result.toString();

    }

    /**
     * 生成批次号-废弃
     *
     * @param materialId
     * @param producedDate
     * @return
     */
    @Override
    public String getBatchCode(Long materialId, Date producedDate) {
        // 批次号
        StringBuffer batchCode = new StringBuffer();

        // 当前物料信息
        TMaterialVO material = tMaterialMapper.selectInfoById(materialId);

        // 物料编码
        batchCode.append(material.getCode());
        batchCode.append("-");

        // 如果启用批次信息，生成批次号
        if (Constants.YES.equals(material.getBatchFlag())) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

            if (Constants.MATERIAL_BATCH_TYPE_MAKE_DATE.equals(material.getBatchAttrName())) {
                //制单日期
                if (!ObjectUtils.isEmpty(producedDate)) {
                    batchCode.append(sdf.format(producedDate));
                }
            } else if (Constants.MATERIAL_BATCH_TYPE_PRODUCT_DATE.equals(material.getBatchAttrName())) {
                //生产日期
                if (!ObjectUtils.isEmpty(producedDate)) {
                    batchCode.append(sdf.format(producedDate));
                }
            } else if (Constants.MATERIAL_BATCH_TYPE_DAY_COUNT.equals(material.getBatchAttrName())) {
                //剩余有效期天数
                if (material.getExpirationDate() > 0) {
                    Calendar c = Calendar.getInstance();

                    c.setTime(producedDate);

                    c.set(Calendar.DATE, Math.toIntExact(c.get(Calendar.DATE) + (material.getExpirationDate() - 1)));

                    batchCode.append(sdf.format(c.getTime()));
                } else {
                    // 如果有效期天数为0，默认无限期
                    batchCode.append("991231");
                }
            } else {
                // 入库日期
                Date d = new Date();
                batchCode.append(sdf.format(d));
            }

        } else {
            batchCode.append(String.format("%06d", 1));
        }
        return batchCode.toString();
    }

    /**
     * 生成批次号-
     *
     * @param materialId
     * @param producedDate
     * @return
     */
    @Override
    public String getBatchCode1(Long materialId, Date producedDate) {
        String redisKey = "wms:codecreate:batchcode:"+DateUtils.dateTimeNow("yy-MM-dd");
        StringBuffer batchCode = new StringBuffer();

        producedDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        batchCode.append(sdf.format(producedDate));

        Integer currentIndex = 1;
        Boolean hasKey = redisService.hasKey(redisKey);
        if (hasKey == false) {
            batchCode.append(String.format("%04d", currentIndex));
        } else {
            // redis中获取自增值
            currentIndex = redisService.getCacheObject(redisKey);
            batchCode.append(String.format("%04d", currentIndex));
        }
        // 设置过期时间
        redisService.setCacheObject(redisKey, currentIndex + 1,60 * 60 * 24L, TimeUnit.SECONDS);
        return batchCode.toString();
    }

    /**
     * 生成rfid编码
     *
     * @param materialCode
     * @return
     */
    @Override
    public String getRfIdCode(String materialCode, String batchCode) {
        String redisKey = "wms:codecreate:mrfid:code" + batchCode;
        StringBuffer code = new StringBuffer();
//        code.append(materialCode);
        code.append(batchCode);
        Integer currentIndex = 1;
        Boolean hasKey = redisService.hasKey(redisKey);
        if (hasKey == false) {
            code.append(String.format("%06d", currentIndex));
        } else {
            // redis中获取自增值
            currentIndex = redisService.getCacheObject(redisKey);
            code.append(String.format("%06d", currentIndex));
        }
        redisService.setCacheObject(redisKey, currentIndex + 1,2 * 60 * 60 * 24L, TimeUnit.SECONDS);//过期时间2天
        return code.toString();
    }


}
