package com.xsrw.wms.stock.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.util.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.stock.mapper.TMailForewarningMapper;
import com.xsrw.wms.stock.domain.TMailForewarning;
import com.xsrw.wms.stock.service.ITMailForewarningService;

/**
 * 预警邮件配置Service业务层处理
 *
 * @author wxr
 * @date 2023-06-19
 */
@Service
public class TMailForewarningServiceImpl extends ServiceImpl<TMailForewarningMapper, TMailForewarning> implements ITMailForewarningService {
    @Autowired
    private TMailForewarningMapper tMailForewarningMapper;
    @Autowired
    private SendEmailUtil sendEmailUtil;

    /**
     * 查询预警邮件配置列表
     *
     * @param tMailForewarning 预警邮件配置
     * @return 预警邮件配置
     */
    @Override
    public List<TMailForewarning> selectTMailForewarningList(TMailForewarning tMailForewarning) {
        List<TMailForewarning> tMailForewarnings = tMailForewarningMapper.selectTMailForewarningList(tMailForewarning);
        return tMailForewarnings;
    }

    /**
     * 查询预警邮件配置
     *
     * @param id 预警邮件配置主键
     * @return 预警邮件配置
     */
    @Override
    public TMailForewarning selectTMailForewarningById(Long id) {
        return tMailForewarningMapper.selectById(id);
    }

    /**
     * 新增预警邮件配置
     *
     * @param tMailForewarning 预警邮件配置
     * @return 结果
     */
    @Override
    public int insertTMailForewarning(TMailForewarning tMailForewarning) {
        return tMailForewarningMapper.insert(tMailForewarning);
    }

    /**
     * 修改预警邮件配置
     *
     * @param tMailForewarning 预警邮件配置
     * @return 结果
     */
    @Override
    public int updateTMailForewarning(TMailForewarning tMailForewarning) {
        return tMailForewarningMapper.updateById(tMailForewarning);
    }


    /**
     * 批量删除预警邮件配置
     *
     * @param ids 需要删除的预警邮件配置主键
     * @return 结果
     */
    @Override
    public int deleteTMailForewarningByIds(Long[] ids) {
        return tMailForewarningMapper.deleteTMailForewarningByIds(ids);
    }

    /**
     * 删除预警邮件配置信息
     *
     * @param id 预警邮件配置主键
     * @return 结果
     */
    @Override
    public int deleteTMailForewarningById(Long id) {
        return tMailForewarningMapper.deleteTMailForewarningById(id);
    }

    /**
     * 根据类型发送邮件
     *
     * @param tMailForewarning
     * @return
     */
    @Override
    public AjaxResult sendEmail(TMailForewarning tMailForewarning) {
        List<String> address = tMailForewarningMapper.selectEmailAddressByType(tMailForewarning.getType());
        if (StringUtils.isEmpty(address)) {
            return AjaxResult.error("未查询到邮箱发送地址，请先配置");
        }
        String title = "";
        String content = "";
        if ("1".equals(tMailForewarning.getType())) {
            title = "库存预警";
            content = "【仓储】您的库存数量已超过预警下线";
        } else if ("1".equals(tMailForewarning.getType())) {
            title = "有效期预警";
            content = "【仓储】您的物料有效期已超过有效期";
        } else {
            title = "呆滞品预警";
            content = "【仓储】您的物料入库时间已久";
        }
        String[] emailAddress = address.toArray(new String[0]);
        sendEmailUtil.sendEmail(emailAddress, title, content);
        return AjaxResult.success();
    }
}
