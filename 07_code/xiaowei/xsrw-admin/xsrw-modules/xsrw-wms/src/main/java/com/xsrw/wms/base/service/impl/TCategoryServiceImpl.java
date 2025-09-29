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
import com.xsrw.wms.base.domain.TCategory;
import com.xsrw.wms.base.domain.vo.ExcelCategoryVO;
import com.xsrw.wms.base.mapper.TCategoryMapper;
import com.xsrw.wms.base.service.ITCategoryService;
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
 * 物料类别Service业务层处理
 *
 * @author lyx
 * @date 2023-05-05
 */
@Service
public class TCategoryServiceImpl extends ServiceImpl<TCategoryMapper, TCategory> implements ITCategoryService
{
    @Autowired
    private TCategoryMapper tCategoryMapper;


    /**
     * 查询物料类别列表
     *
     * @param entity 物料类别
     * @return 物料类别
     */
    @Override
    public List<TCategory> selectTCategoryList(TCategory entity)
    {
        return tCategoryMapper.selectTCategoryList(entity);
    }

    /**
     * 查询物料类别
     *
     * @param id 物料类别主键
     * @return 物料类别
     */
    @Override
    public TCategory selectTCategoryById(Long id)
    {
        return tCategoryMapper.selectById(id);
    }

    /**
     * 新增物料类别
     *
     * @param entity 物料类别
     * @return 结果
     */
    @Override
    public AjaxResult insertTCategory(TCategory entity)
    {
        entity.setCreateTime(DateUtils.getNowDate());
        entity.setCreateBy(SecurityUtils.getUsername());
        List<TCategory> categoryList = tCategoryMapper.selectList(Wrappers.lambdaQuery(TCategory.class)
                .eq(TCategory::getName,entity.getName())
                .eq(StringUtils.isNotNull(entity.getDeptId()),TCategory::getDeptId,entity.getDeptId())
                .eq(TCategory::getDelFlag,Constants.NO));
        if (StringUtils.isNotEmpty(categoryList) && categoryList.size() > 0) {
            return AjaxResult.error("该物料类别已存在");
        }
        return AjaxResult.success(tCategoryMapper.insert(entity));
    }

    /**
     * 修改物料类别
     *
     * @param entity 物料类别
     * @return 结果
     */
    @Override
    public AjaxResult updateTCategory(TCategory entity)
    {
        entity.setUpdateTime(DateUtils.getNowDate());
        entity.setUpdateBy(SecurityUtils.getUsername());

        TCategory category = tCategoryMapper.selectById(entity.getId());
        if (!category.getName().equals(entity.getName())) {
            List<TCategory> unitList = tCategoryMapper.selectList(Wrappers.lambdaQuery(TCategory.class)
                    .eq(StringUtils.isNotNull(entity.getDeptId()),TCategory::getDeptId,entity.getDeptId())
                    .eq(TCategory::getName,entity.getName())
                    .eq(TCategory::getDelFlag,Constants.NO));
            if (StringUtils.isNotEmpty(unitList) && unitList.size() > 0) {
                return AjaxResult.error("该物料类别已存在");
            }
        }
        return AjaxResult.success(tCategoryMapper.updateById(entity));
    }


    /**
     * 批量删除物料类别
     *
     * @param ids 需要删除的物料类别主键
     * @return 结果
     */
    @Override
    public int deleteTCategoryByIds(Long[] ids)
    {
        return tCategoryMapper.deleteTCategoryByIds(ids);
    }

    /**
     * 删除物料类别信息
     *
     * @param id 物料类别主键
     * @return 结果
     */
    @Override
    public int deleteTCategoryById(Long id)
    {
        return tCategoryMapper.deleteTCategoryById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        if (file == null) {
            return AjaxResult.error("文件不可为空");
        }
        // 文件名称
        String fileName = file.getOriginalFilename();
        // 校验文件格式
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        if (fileType.contains("xlsx") || fileType.contains("xls")) {
            InputStream inputStream = file.getInputStream();
            // 转换Excel数据
            ExcelUtil<ExcelCategoryVO> util = new ExcelUtil<ExcelCategoryVO>(ExcelCategoryVO.class);
            List<ExcelCategoryVO> categoryList = util.importExcel(file.getInputStream());
            //校验excel 是否有重复信息,存在的话返回错误
            Set<String> collect = categoryList.stream().map(ExcelCategoryVO::getName).collect(Collectors.toSet());
            boolean result = collect.size() == categoryList.size() ? true : false;
            if (!result){
                throw new ServiceException("Excel物料类别名称有重复信息,请检查确认");
            }
            if (categoryList.size()>0){
                int notNullCount = 0 ;
                int count = 0 ;

                for (int i = 0; i < categoryList.size(); i++) {
                    ExcelCategoryVO excelCategoryVO = categoryList.get(i);
                    notNullCount = notNullCount + 1 ;
                    if (StringUtils.isEmpty(excelCategoryVO.getName())){
                        throw new ServiceException("第:" + notNullCount + "条名称不可为空");
                    }
                }

                // 校验导入信息是否正确
                for (int i = 0; i < categoryList.size(); i++) {
                    ExcelCategoryVO excelCategoryVO = categoryList.get(i);
                    count = count + 1 ;
                    // 名称是否存在
                    QueryWrapper<TCategory> wrapper = new QueryWrapper<>();
                    wrapper.eq("name",excelCategoryVO.getName());
                    wrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    Long num = tCategoryMapper.selectCount(wrapper);
                    if (num>=1){
                        throw new ServiceException("第:"+count+"条数据已存在,请检查名称信息");
                    }
                    // 批量导入
                    TCategory category  = new TCategory();
                    BeanUtils.copyProperties(excelCategoryVO,category);
                    SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
                    if (sysUser.getDeptId() != null && sysUser.getDeptName() != null){
                        category.setDeptId(sysUser.getDeptId());
                        category.setDeptName(sysUser.getDeptName());
                    }else {
                        throw new ServiceException("获取组织信息失败");
                    }
                    try {
                        int insert = tCategoryMapper.insert(category);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("第:"+count+"条数据出现错误请检查信息:"+category);
                    }
                }
            }else {
                return AjaxResult.error("数据不可为空");
            }
        }else {
            return AjaxResult.error("文件格式错误");
        }
        return AjaxResult.success();
    }
}
