package com.xsrw.wms.equipment.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import com.xsrw.wms.equipment.utils.GenerateNumberUtil;
import com.xsrw.wms.equipment.utils.WeekDayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.DInspectionItemsMapper;
import com.xsrw.wms.equipment.domain.DInspectionItems;
import com.xsrw.wms.equipment.service.IDInspectionItemsService;

/**
 * 巡检标准Service业务层处理
 *
 * @author zjj
 * @date 2023-05-08
 */
@Service
public class DInspectionItemsServiceImpl extends ServiceImpl<DInspectionItemsMapper, DInspectionItems> implements IDInspectionItemsService
{
    @Autowired
    private DInspectionItemsMapper dInspectionItemsMapper;
    @Autowired
    private GenerateNumberUtil generateNumberUtil;

    @Autowired
    private WmsEquipmentMapper wmsEquipmentMapper;


    /**
     * 查询巡检标准列表
     *
     * @param dInspectionItems 巡检标准
     * @return 巡检标准
     */
    @Override
    public List<DInspectionItems> selectDInspectionItemsList(DInspectionItems dInspectionItems)
    {
        if (dInspectionItems.getDepId()!=null){

        }
        List<DInspectionItems> dInspectionItems1 = dInspectionItemsMapper.selectDInspectionItemsList(dInspectionItems);
        dInspectionItems1.forEach(dInspectionItems2 -> {
            String[] split = dInspectionItems2.getEquipmentId().split(",");
            StringBuffer sb  = new StringBuffer();
            for (String str : split) {
                sb.append(wmsEquipmentMapper.selectById(str).getName());
                sb.append(",");
            }
            dInspectionItems2.setEquipmentName(sb.substring(0,sb.length()-1));
        });
        return dInspectionItems1;
    }

    /**
     * 查询巡检标准
     *
     * @param id 巡检标准主键
     * @return 巡检标准
     */
    @Override
    public DInspectionItems selectDInspectionItemsById(Long id)
    {
        return dInspectionItemsMapper.selectById(id);
    }

    /**
     * 新增巡检标准
     *
     * @param dInspectionItems 巡检标准
     * @return 结果
     */
    @Override
    public AjaxResult insertDInspectionItems(DInspectionItems dInspectionItems)
    {
//        String xb = generateNumberUtil.generateNum("XB", 4);
//        dInspectionItems.setItemNo(xb);
        String[] split = dInspectionItems.getEquipmentId().split(",");
        List<String> list = new ArrayList<>();
        for (String str : split) {//已添加过标准的设备无法重复添加
            List<DInspectionItems> isadd = dInspectionItemsMapper.isadd(str);
            WmsEquipment wmsEquipment = wmsEquipmentMapper.selectById(str);
            if (isadd.size()>0){
                list.add(wmsEquipment.getName());
            }
        }
        if (list.size()>0){
            return AjaxResult.error("设备:"+list+"已经添加标准，请勿重复添加");
        }
        dInspectionItemsMapper.insert(dInspectionItems);
        return AjaxResult.success();
    }

    /**
     * 修改巡检标准
     *
     * @param dInspectionItems 巡检标准
     * @return 结果
     */
    @Override
    public int updateDInspectionItems(DInspectionItems dInspectionItems)
    {
        return dInspectionItemsMapper.updateById(dInspectionItems);
    }


    /**
     * 批量删除巡检标准
     *
     * @param ids 需要删除的巡检标准主键
     * @return 结果
     */
    @Override
    public int deleteDInspectionItemsByIds(Long[] ids)
    {
        return dInspectionItemsMapper.deleteDInspectionItemsByIds(ids);
    }

    /**
     * 删除巡检标准信息
     *
     * @param id 巡检标准主键
     * @return 结果
     */
    @Override
    public int deleteDInspectionItemsById(Long id)
    {
        return dInspectionItemsMapper.deleteDInspectionItemsById(id);
    }

    @Override
    public List<DInspectionItems> isadd(String equipmentid) {
        return dInspectionItemsMapper.isadd(equipmentid);
    }


}
