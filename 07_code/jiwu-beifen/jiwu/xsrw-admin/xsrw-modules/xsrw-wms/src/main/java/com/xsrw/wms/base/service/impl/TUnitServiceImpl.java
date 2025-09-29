package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.domain.SysUser;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.domain.vo.ExcelUnitVO;
import com.xsrw.wms.base.mapper.TUnitMapper;
import com.xsrw.wms.base.service.ITUnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 单位Service业务层处理
 *
 * @author lyx
 * @date 2023-05-05
 */
@Service
public class TUnitServiceImpl extends ServiceImpl<TUnitMapper, TUnit> implements ITUnitService
{
    @Autowired
    private TUnitMapper tUnitMapper;


    /**
     * 查询单位列表
     *
     * @param entity 单位
     * @return 单位
     */
    @Override
    public List<TUnit> selectTUnitList(TUnit entity)
    {
        return tUnitMapper.selectTUnitList(entity);
    }

    /**
     * 查询单位
     *
     * @param id 单位主键
     * @return 单位
     */
    @Override
    public TUnit selectTUnitById(Long id)
    {
        return tUnitMapper.selectById(id);
    }

    /**
     * 新增单位
     *
     * @param entity 单位
     * @return 结果
     */
    @Override
    public AjaxResult insertTUnit(TUnit entity)
    {
        entity.setCreateTime(DateUtils.getNowDate());
        entity.setCreateBy(SecurityUtils.getUsername());
        List<TUnit> unitList = tUnitMapper.selectList(Wrappers.lambdaQuery(TUnit.class)
                .eq(TUnit::getName,entity.getName())
                .eq(StringUtils.isNotNull(entity.getDeptId()),TUnit::getDeptId,entity.getDeptId())
                .eq(TUnit::getDelFlag,Constants.NO));
        if (StringUtils.isNotEmpty(unitList) && unitList.size() > 0) {
            return AjaxResult.error("该单位已存在");
        }
        return AjaxResult.success(tUnitMapper.insert(entity));
    }

    /**
     * 修改单位
     *
     * @param entity 单位
     * @return 结果
     */
    @Override
    public AjaxResult updateTUnit(TUnit entity)
    {
        entity.setUpdateTime(DateUtils.getNowDate());
        entity.setUpdateBy(SecurityUtils.getUsername());
        TUnit unit = tUnitMapper.selectById(entity.getId());
        if (!unit.getName().equals(entity.getName())) {
            List<TUnit> unitList = tUnitMapper.selectList(Wrappers.lambdaQuery(TUnit.class)
                    .eq(StringUtils.isNotNull(entity.getDeptId()),TUnit::getDeptId,entity.getDeptId())
                    .eq(TUnit::getName,entity.getName())
                    .eq(TUnit::getDelFlag,Constants.NO));
            if (StringUtils.isNotEmpty(unitList) && unitList.size() > 0) {
                return AjaxResult.error("该单位已存在");
            }
        }
        return AjaxResult.success(tUnitMapper.updateById(entity));
    }


    /**
     * 批量删除单位
     *
     * @param ids 需要删除的单位主键
     * @return 结果
     */
    @Override
    public int deleteTUnitByIds(Long[] ids)
    {
        return tUnitMapper.deleteTUnitByIds(ids);
    }

    /**
     * 删除单位信息
     *
     * @param id 单位主键
     * @return 结果
     */
    @Override
    public int deleteTUnitById(Long id)
    {
        return tUnitMapper.deleteTUnitById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        if (file == null){
            return AjaxResult.error("文件不可为空");
        }
        // 文件名称
        String fileName = file.getOriginalFilename();
        // 校验文件格式
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        if (fileType.contains("xlsx") || fileType.contains("xls")){
            InputStream inputStream = file.getInputStream();
            ExcelUtil<ExcelUnitVO> util = new ExcelUtil<ExcelUnitVO>(ExcelUnitVO.class);
            // 转换Excel数据
            List<ExcelUnitVO> unitList = util.importExcel(inputStream);

            //校验excel 是否有重复信息,存在的话返回错误
            Set<String> collect = unitList.stream().map(ExcelUnitVO::getName).collect(Collectors.toSet());
            boolean result = collect.size() == unitList.size() ? true : false;
            if (!result){
                throw new ServiceException("Excel单位名称有重复信息,请检查确认");
            }

            if (unitList.size()>0){
                int notNullCount = 0;
                int count = 0;

                for (int i = 0; i < unitList.size(); i++) {
                    ExcelUnitVO unitVO = unitList.get(i);
                    notNullCount = notNullCount + 1;
                    // 校验名称
                    if (StringUtils.isEmpty(unitVO.getName())){
                        throw new ServiceException("第:" + notNullCount + "条单位名称不可为空");
                    }
                }

                for (int i = 0; i < unitList.size(); i++) {
                    ExcelUnitVO unitVO = unitList.get(i);
                    count = count + 1;
                    // 校验是否存在单位名称
                    QueryWrapper<TUnit> unitQueryWrapper = new QueryWrapper<>();
                    unitQueryWrapper.eq("name",unitVO.getName());
                    unitQueryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    Long size = tUnitMapper.selectCount(unitQueryWrapper);
                    if (1<=size){
                        throw new ServiceException("第:"+count+"条单位名称已存在,请检查单位名称信息");
                    }
                    //批量插入
                    TUnit unit = new TUnit();
                    BeanUtils.copyProperties(unitVO,unit);
                    SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
                    if (sysUser.getDeptId() != null && sysUser.getDeptName() != null){
                        unit.setDeptId(sysUser.getDeptId());
                        unit.setDeptName(sysUser.getDeptName());
                    }else {
                        throw new ServiceException("获取组织信息失败");
                    }
                    try {
                        tUnitMapper.insert(unit);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("第:"+count+"条数据出现错误请检查信息:"+unit);
                    }
                }
            }else {
                return AjaxResult.error("数据不可为空");
            }
            return AjaxResult.success();
        }else {
            return AjaxResult.error("文件格式错误");
        }
    }
}
