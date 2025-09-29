package com.xsrw.wms.base.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.domain.SysUser;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.vo.ExcelAreaVO;
import com.xsrw.wms.base.domain.vo.TAreaVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TAreaMapper;
import com.xsrw.wms.base.domain.TArea;
import com.xsrw.wms.base.service.ITAreaService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 区域Service业务层处理
 *
 * @author wxr
 * @date 2023-05-05
 */
@Service
public class TAreaServiceImpl extends ServiceImpl<TAreaMapper, TArea> implements ITAreaService {
    @Autowired
    private TAreaMapper tAreaMapper;


    /**
     * 查询区域列表
     *
     * @param tArea 区域
     * @return 区域
     */
    @Override
    public List<TAreaVO> selectTAreaList(TArea tArea) {
//        添加权限控制 1.获取当前登录人的id 2.通过id获取部门id
//        Long deptId = tAreaMapper.selectDeptIdByUserId(SecurityUtils.getUserId());
//        if (deptId == null) {
//            throw new ServiceException("用户部门为空!");
//        }
//        tArea.setDeptId(deptId);
        return tAreaMapper.selectTAreaList(tArea);
    }

    /**
     * 查询区域
     *
     * @param id 区域主键
     * @return 区域
     */
    @Override
    public TArea selectTAreaById(Long id) {
        return tAreaMapper.selectById(id);
    }

    /**
     * 新增区域
     *
     * @param tArea 区域
     * @return 结果
     */
    @Override
    public int insertTArea(TArea tArea) {
        return tAreaMapper.insert(tArea);
    }

    /**
     * 修改区域
     *
     * @param tArea 区域
     * @return 结果
     */
    @Override
    public int updateTArea(TArea tArea) {
        return tAreaMapper.updateById(tArea);
    }


    /**
     * 批量删除区域
     *
     * @param ids 需要删除的区域主键
     * @return 结果
     */
    @Override
    public int deleteTAreaByIds(Long[] ids) {
        return tAreaMapper.deleteTAreaByIds(ids);
    }

    /**
     * 删除区域信息
     *
     * @param id 区域主键
     * @return 结果
     */
    @Override
    public int deleteTAreaById(Long id) {
        return tAreaMapper.deleteTAreaById(id);
    }

    @Override
    public AjaxResult importUnit(MultipartFile file) throws Exception {

        if (file == null) {
            return AjaxResult.error("文件不可为空");
        }

        // 文件名称
        String fileName = file.getOriginalFilename();
        // 校验文件格式
        String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        if (fileType.contains("xlsx") || fileType.contains("xls")) {
            // 转换Excel数据
            ExcelUtil<ExcelAreaVO> util = new ExcelUtil<ExcelAreaVO>(ExcelAreaVO.class);
            List<ExcelAreaVO> areaList = util.importExcel(file.getInputStream());
            //校验excel 是否有重复信息,存在的话返回错误
            Set<String> collect = areaList.stream().map(ExcelAreaVO::getName).collect(Collectors.toSet());
            Boolean result = collect.size() == areaList.size();
            if (!result) {
                throw new ServiceException("Excel区域名称中有重复信息,请检查确认");
            }
            if (areaList.size() > 0) {
                int notNullCount = 0;
                int count = 0;

                //校验导入字段是否为空
                for (int i = 0; i < areaList.size(); i++) {
                    notNullCount = notNullCount + 1;
                    ExcelAreaVO areaVO = areaList.get(i);

                    if (StringUtils.isEmpty(areaVO.getName())) {
                        throw new ServiceException("第:" + notNullCount + "条区域名称不可为空");
                    }
                    if (StringUtils.isEmpty(areaVO.getStatus())) {
                        throw new ServiceException("第:" + notNullCount + "条区域状态不可为空");
                    }
                }
                //校验导入信息是否正确
                for (int i = 0; i < areaList.size(); i++) {
                    ExcelAreaVO areaVO = areaList.get(i);
                    //记录导入数据次数
                    count = count + 1;

                    //批量导入数据校验名称是否存在
                    QueryWrapper areaWrapper = new QueryWrapper();
                    areaWrapper.eq("name", areaVO.getName());
                    areaWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    Long size = tAreaMapper.selectCount(areaWrapper);

                    //校验数据库是否存在该名称信息,存在返回错误
                    if (1 <= size) {
                        throw new ServiceException("第:" + count + "条区域名称已存在,请检查区域信息");
                    } else {
                        TArea area = new TArea();
                        BeanUtils.copyProperties(areaVO, area);
                        try {
                            tAreaMapper.insert(area);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            throw new ServiceException("第:" + count + "条数据出现错误请检查信息:" + area);
                        }
                    }
                }

            } else {
                return AjaxResult.error("数据不可为空");
            }
        } else {
            return AjaxResult.error("文件格式错误");
        }
        return AjaxResult.success();
    }
}
