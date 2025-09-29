package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TReservoir;
import com.xsrw.wms.base.domain.vo.ExcelReservoirVO;
import com.xsrw.wms.base.domain.vo.TReservoirVO;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TReservoirMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITReservoirService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 库区Service业务层处理
 *
 * @author wxr
 * @date 2023-05-05
 */
@Service
public class TReservoirServiceImpl extends ServiceImpl<TReservoirMapper, TReservoir> implements ITReservoirService {

    @Autowired
    private TReservoirMapper tReservoirMapper;

    @Autowired
    private TLocationMapper tLocationMapper;

    @Autowired
    private ITCodeConfigService itCodeConfigService;


    /**
     * 查询库区列表
     *
     * @param tReservoir 库区
     * @return 库区
     */
    @Override
    public List<TReservoirVO> selectTReservoirList(TReservoir tReservoir) {
        return tReservoirMapper.selectTReservoirList(tReservoir);
    }

    /**
     * 查询库区
     *
     * @param id 库区主键
     * @return 库区
     */
    @Override
    public TReservoir selectTReservoirById(Long id) {
        return tReservoirMapper.selectById(id);
    }

    /**
     * 新增库区
     *
     * @param tReservoir 库区
     * @return 结果
     */
    @Override
    public int insertTReservoir(TReservoir tReservoir) {
        // 获取编号
        String code = itCodeConfigService.getCode("IKQ");
        if (StringUtils.isEmpty(code)) {
            throw new ServiceException("编号生成失败");
        }
        tReservoir.setCode(code);
        return tReservoirMapper.insert(tReservoir);
    }

    /**
     * 修改库区
     *
     * @param tReservoir 库区
     * @return 结果
     */
    @Override
    public int updateTReservoir(TReservoir tReservoir) {
        return tReservoirMapper.updateById(tReservoir);
    }


    /**
     * 批量删除库区
     *
     * @param ids 需要删除的库区主键
     * @return 结果
     */
    @Override
    public int deleteTReservoirByIds(Long[] ids) {
        return tReservoirMapper.deleteTReservoirByIds(ids);
    }

    /**
     * 删除库区信息
     *
     * @param id 库区主键
     * @return 结果
     */
    @Override
    public int deleteTReservoirById(Long id) {
        return tReservoirMapper.deleteTReservoirById(id);
    }

    /**
     * 导入库区列表
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public AjaxResult importReservoir(MultipartFile file) throws Exception {
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
            ExcelUtil<ExcelReservoirVO> util = new ExcelUtil<>(ExcelReservoirVO.class);
            List<ExcelReservoirVO> reservoirVOList = util.importExcel(inputStream);
            //校验excel库区名称是否有重复信息,存在的话返回错误
            Set<String> collect = reservoirVOList.stream().map(ExcelReservoirVO::getName).collect(Collectors.toSet());
            Boolean result = collect.size() == reservoirVOList.size() ? true : false;
            if (!result) {
                return AjaxResult.error("Excel名称中有重复信息,请检查确认");
            }

            if (reservoirVOList.size() > 0) {

                //校验导入字段是否为空
                int notNullCount = 0;
                for (int i = 0; i < reservoirVOList.size(); i++) {
                    notNullCount = notNullCount + 1;
                    ExcelReservoirVO reservoirVO = reservoirVOList.get(i);
                    if (StringUtils.isEmpty(reservoirVO.getName())) {
                        throw new ServiceException("第：" + notNullCount + "条库区名称不可为空");
                    }
                    if (StringUtils.isEmpty(reservoirVO.getAreaName())) {
                        throw new ServiceException("第：" + notNullCount + "条所属区域不可为空");
                    }
                }

                //校验导入信息是否正确
                int count = 0;
                for (int i = 0; i < reservoirVOList.size(); i++) {
                    count = count + 1;
                    ExcelReservoirVO reservoirVO = reservoirVOList.get(i);

                    //校验数据库是否存在该库区名称信息,存在返回错误
                    QueryWrapper reservoirWrapper = new QueryWrapper();
                    reservoirWrapper.eq("name", reservoirVO.getName());
                    reservoirWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    Long size = tReservoirMapper.selectCount(reservoirWrapper);

                    if (1 <= size) {
                        throw new ServiceException("第:" + count + "条库区名称已存在,请检查库区信息");
                    }
                    //导入
                    TReservoir reservoir = new TReservoir();
                    BeanUtils.copyProperties(reservoirVO, reservoir);

                    try {
                        tReservoirMapper.insert(reservoir);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("第:" + count + "条数据出现错误请检查信息:" + reservoir);
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

    /**
     * 库区禁用、启用
     * @param status
     * @param id
     * @return
     */
    @Override
    public int deleteTReservoirStatusByIds(String status, Long id) {
        if (Constants.DEL_FLAG_YES.equals(status)) {
            tLocationMapper.deleteTLocationStatusByReservoirIds(Constants.DEL_FLAG_NO, id);
        } else {
            tLocationMapper.deleteTLocationStatusByReservoirIds(Constants.DEL_FLAG_YES, id);
        }

        return tReservoirMapper.deleteTReservoirStatusByIds(status, id);
    }
    @Override
    public  List<Map<String,Object>>getReservoirList(Integer type,Integer areaId){
        List<Map<String,Object>>list=tReservoirMapper.getReservoirList(areaId);
        if(type==0){
            for (Map<String,Object> map:list) {
                //库位
                map.put("locationList",tLocationMapper.getLocationUsedInfo(Long.parseLong(map.get("id").toString())));
            }
        }
        return  list;
    }
}
