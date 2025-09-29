package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TCategory;
import com.xsrw.wms.base.domain.TGoodShelf;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TReservoir;
import com.xsrw.wms.base.domain.dto.DemandCheckDTO;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.vo.DemandCheckVO;
import com.xsrw.wms.base.domain.vo.ExcelLocationVO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.base.mapper.TCategoryMapper;
import com.xsrw.wms.base.mapper.TGoodShelfMapper;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TReservoirMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.mapper.TStockMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 库位Service业务层处理
 *
 * @author wxr
 * @date 2023-05-05
 */
@Service
public class TLocationServiceImpl extends ServiceImpl<TLocationMapper, TLocation> implements ITLocationService {

    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private TReservoirMapper tReservoirMapper;
    @Autowired
    private TCategoryMapper tCategoryMapper;
    @Autowired
    private TGoodShelfMapper tGoodShelfMapper;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Resource
    private TStockMapper stockMapper;

    /**
     * 查询库位列表
     *
     * @param tLocation 库位
     * @return 库位
     */
    @Override
    public List<TLocationVO> selectTLocationList(TLocationDTO tLocation) {
        List<TLocationVO> data = tLocationMapper.selectTLocationList(tLocation);
        if(CollectionUtils.isNotEmpty(data)){
            data.forEach(e -> {
                if(e.getExtentionType() != null && e.getExtentionType() == 2){
                    TLocation firstVO = tLocationMapper.selectByFirstNodeId(e);
                    if(firstVO != null){
                        e.setFirstGoodsAllocationStatus(firstVO.getGoodsAllocationStatus());
                    }
                }
            });
        }
        return data;
    }

    /**
     * 查询库位
     *
     * @param id 库位主键
     * @return 库位
     */
    @Override
    public TLocation selectTLocationById(Long id) {
        return tLocationMapper.selectById(id);
    }

    /**
     * 新增库位
     *
     * @param tLocation 库位
     * @return 结果
     */
    @Override
    public int insertTLocation(TLocation tLocation) {
        if (tLocation.getGoodShelfId() != null) {
            TGoodShelf tGoodShelf = tGoodShelfMapper.selectById(tLocation.getGoodShelfId());
            if (tGoodShelf != null) {
                //编码：货架编号排列层+库位编码
                tLocation.setCode("KW" + tGoodShelf.getCode() + tLocation.getLocationRow() + tLocation.getLocationColumn() + tLocation.getLocationPlies() + codeConfigService.getCode(CodeEnum.IKW.getCodeName()));
            }
        } else {
            //编码：排列层+库位编码
            tLocation.setCode("KW" + tLocation.getLocationRow() + tLocation.getLocationColumn() + tLocation.getLocationPlies() + codeConfigService.getCode(CodeEnum.IKW.getCodeName()));
        }
        return tLocationMapper.insert(tLocation);
    }

    /**
     * 修改库位
     *
     * @param tLocation 库位
     * @return 结果
     */
    @Override
    public int updateTLocation(TLocation tLocation) {
        if (tLocation.getGoodShelfId() == null) {
            //设置为空
            tLocationMapper.updateNullById(tLocation);
        }
        return tLocationMapper.updateById(tLocation);
    }


    /**
     * 批量删除库位
     *
     * @param ids 需要删除的库位主键
     * @return 结果
     */
    @Override
    public int deleteTLocationByIds(Long[] ids) {
        return tLocationMapper.deleteTLocationByIds(ids);
    }

    /**
     * 删除库位信息
     *
     * @param id 库位主键
     * @return 结果
     */
    @Override
    public int deleteTLocationById(Long id) {
        return tLocationMapper.deleteTLocationById(id);
    }

    /**
     * 库位批量修改
     *
     * @param location
     * @return
     */
    @Override
    @Transactional
    public AjaxResult plcUpdate(TLocationDTO location) {
        // 批量修改校验  库位上有托盘则无法修改
        Long startRow = location.getStartRow();
        Long endRow = location.getEndRow();

        Long startColumn = location.getStartColumn();
        Long endColumn = location.getEndColumn();

        Long startPlies = location.getStartPlies();
        Long endPlies = location.getEndPlies();

        QueryWrapper<TLocation> queryWrapper = new QueryWrapper<>();

        if (ObjectUtils.isNotEmpty(startRow)) {
            queryWrapper.ge("location_row", startRow);
        }
        if (ObjectUtils.isNotEmpty(endRow)) {
            queryWrapper.le("location_row", endRow);
        }
        if (ObjectUtils.isNotEmpty(startColumn)) {
            queryWrapper.ge("location_column", startColumn);
        }
        if (ObjectUtils.isNotEmpty(endColumn)) {
            queryWrapper.le("location_column", endColumn);
        }
        if (ObjectUtils.isNotEmpty(startPlies)) {
            queryWrapper.ge("location_plies", startPlies);
        }
        if (ObjectUtils.isNotEmpty(endPlies)) {
            queryWrapper.le("location_plies", endPlies);
        }

//        List<TLocation> locationList = tLocationMapper.selectList(queryWrapper);

//        List<TLocation> collect = locationList.stream().filter(e -> StringUtils.isNotEmpty(e.getPalletNum())).collect(Collectors.toList());
//        if (collect.size() > 0){
//            return AjaxResult.error("选择的库位存在托盘绑定，无法修改");
//        }

        tLocationMapper.plcUpdate(location);

        return AjaxResult.success();
    }

    /**
     * 导入库位信息
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        //转换类型
        List<TLocation> list = new ArrayList<>();

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
            ExcelUtil<ExcelLocationVO> util = new ExcelUtil<ExcelLocationVO>(ExcelLocationVO.class);
            List<ExcelLocationVO> locationVOList = util.importExcel(file.getInputStream());
            //校验excel 是否有重复信息,存在的话返回错误
            Set<String> collect = locationVOList.stream().map(ExcelLocationVO::getName).collect(Collectors.toSet());
            Boolean result = collect.size() == locationVOList.size() ? true : false;
            if (!result) {
                throw new ServiceException("Excel名称中有重复信息,请检查确认");
            }

            if (locationVOList.size() > 0) {
                int notNullCount = 0;
                int count = 0;

                //校验导入字段是否为空
                for (int i = 0; i < locationVOList.size(); i++) {
                    notNullCount = notNullCount + 1;
                    ExcelLocationVO excelLocationVO = locationVOList.get(i);
                    if (StringUtils.isEmpty(excelLocationVO.getName())) {
                        throw new ServiceException("第:" + notNullCount + "条库位名称不可为空");
                    }
                    if (StringUtils.isEmpty(excelLocationVO.getAreaName())) {
                        throw new ServiceException("第:" + notNullCount + "条所属区域不可为空");
                    }
                    if (StringUtils.isEmpty(excelLocationVO.getReservoirName())) {
                        throw new ServiceException("第:" + notNullCount + "条所属库区不可为空");
                    }
//                    if (StringUtils.isEmpty(excelLocationVO.getCategoryName())) {
//                        return AjaxResult.error("第:" + notNullCount + "条存放物料类别不可为空");
//                    }
//                    if (StringUtils.isEmpty(excelLocationVO.getUnitName())) {
//                        return AjaxResult.error("第:" + notNullCount + "条存放包装方式不可为空");
//                    }
//                    if (StringUtils.isEmpty(excelLocationVO.getSameMaterialFlag())) {
//                        return AjaxResult.error("第:" + notNullCount + "条是否允许混物料存放不可为空");
//                    }
//                    if (StringUtils.isEmpty(excelLocationVO.getSameBatchFlag())) {
//                        return AjaxResult.error("第:" + notNullCount + "条是否允许混批次不可为空");
//                    }
//                    if (StringUtils.isEmpty(excelLocationVO.getContactsUnitName())) {
//                        return AjaxResult.error("第:" + notNullCount + "条默认往来单位不可为空");
//                    }
//                    if (excelLocationVO.getUpperLimit() == null ) {
//                        return AjaxResult.error("第:" + notNullCount + "条存放上限不可为空");
//                    }
                    if (excelLocationVO.getLocationRow() == null) {
                        throw new ServiceException("第:" + notNullCount + "条排不可为空");
                    }
                    if (excelLocationVO.getLocationColumn() == null) {
                        throw new ServiceException("第:" + notNullCount + "条列不可为空");
                    }
                    if (excelLocationVO.getLocationPlies() == null) {
                        throw new ServiceException("第:" + notNullCount + "条层不可为空");
                    }
//                    if (StringUtils.isEmpty(excelLocationVO.getGoodsAllocationType())) {
//                        return AjaxResult.error("第:" + notNullCount + "条货位类型不可为空");
//                    }
//                    if (StringUtils.isEmpty(excelLocationVO.getNarrowAisleNum())) {
//                        return AjaxResult.error("第:" + notNullCount + "条巷道号不可为空");
//                    }
                }

                //校验导入信息是否正确
                for (int i = 0; i < locationVOList.size(); i++) {
                    ExcelLocationVO excelLocationVO = locationVOList.get(i);
                    //记录导入数据次数
                    count = count + 1;

                    // 校验库区是否在区域下
                    QueryWrapper<TReservoir> reservoirQueryWrapper = new QueryWrapper<>();
                    reservoirQueryWrapper.eq("name", excelLocationVO.getReservoirName());
                    reservoirQueryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    reservoirQueryWrapper.eq("area_id", excelLocationVO.getAreaId());
                    TReservoir reservoir = tReservoirMapper.selectOne(reservoirQueryWrapper);
                    if (reservoir != null) {
                        excelLocationVO.setReservoirId(reservoir.getId());
                    } else {
                        throw new ServiceException("第:" + count + "条数据库区不存在或该"
                                + excelLocationVO.getAreaName() + "区域中没有" + excelLocationVO.getReservoirName() + "库区信息" +
                                ",请检查库区信息");
                    }
                    // 校验名称是否存在 //
                    QueryWrapper<TLocation> locationQueryWrapper = new QueryWrapper<>();
                    locationQueryWrapper.eq("area_id", excelLocationVO.getAreaId());
                    locationQueryWrapper.eq("reservoir_id", excelLocationVO.getReservoirId());
                    locationQueryWrapper.eq("name", excelLocationVO.getName());
                    reservoirQueryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    Long size = tLocationMapper.selectCount(locationQueryWrapper);
                    if (1 <= size) {
                        throw new ServiceException("第:" + count + "条" + excelLocationVO.getWarehouseName()
                                + "中库位名称" + excelLocationVO.getName() + "已存在,请检查信息");
                    }
                    // 校验存放物料类别
                    if (StringUtils.isNotEmpty(excelLocationVO.getCategoryName())) {
                        QueryWrapper<TCategory> categoryQueryWrapper = new QueryWrapper<>();
                        categoryQueryWrapper.eq("name", excelLocationVO.getCategoryName());
                        categoryQueryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                        TCategory category = tCategoryMapper.selectOne(categoryQueryWrapper);
                        if (category != null) {
                            excelLocationVO.setCategoryId(category.getId());
                            excelLocationVO.setDepositCategoryId(category.getId());
                            System.out.println(excelLocationVO.getCategoryId());
                            System.out.println(category.getId());
                            System.out.println(category.getName());
                        } else {
                            throw new ServiceException("第:" + count + "条存放物料类别不存在,请检查存放物料类别信息");
                        }
                    }
                    // 校验存放物料包装方式
//                    QueryWrapper<Unit> unitQueryWrapper = new QueryWrapper<>();
//                    unitQueryWrapper.eq("name",excelLocationVO.getUnitName());
//                    unitQueryWrapper.eq("del_flag",CimsConstants.DEL_FLAG_NO);
//                    Unit unit = unitMapper.selectOne(unitQueryWrapper);
//                    if (unit != null){
//                        excelLocationVO.setUnitId(unit.getId());
//                    }else {
//                        return AjaxResult.error("第:"+count+"条存放物料包装方式不存在,请检查存放物料包装方式信息");
//                    }
                    // 校验是否混物料存放
//                    if (!CimsConstants.MATERIAL_SAMEMATERIALFLAG_NO.equals(excelLocationVO.getSameMaterialFlag())
//                        && !CimsConstants.MATERIAL_SAMEMATERIALFLAG_YES
//                        .equals(excelLocationVO.getSameMaterialFlag())){
//                        return AjaxResult.error("第:"+count+"条数据是否混物料错误,请检查(是/否)");
//                    }
                    // 校验是否混批次存放
//                    if (!CimsConstants.MATERIAL_SAMEBATCHFLAG_NO.equals(excelLocationVO.getSameBatchFlag())
//                        && !CimsConstants.MATERIAL_SAMEBATCHFLAGG_YES
//                        .equals(excelLocationVO.getSameBatchFlag())){
//                        return AjaxResult.error("第:"+count+"条数据是否混批次错误,请检查(是/否)");
//                    }
                    // 校验往来单位
//                    QueryWrapper<ContactsUnit> contactsUnitQueryWrapper = new QueryWrapper<>();
//                    contactsUnitQueryWrapper.eq("name",excelLocationVO.getContactsUnitName());
//                    contactsUnitQueryWrapper.eq("del_flag",CimsConstants.DEL_FLAG_NO);
//                    ContactsUnit contactsUnit = contactsUnitMapper.selectOne(contactsUnitQueryWrapper);
//                    if (contactsUnit != null){
//                        excelLocationVO.setContactsUnitId(contactsUnit.getId());
//                    }else {
//                        return AjaxResult.error("第:"+count+"条默认往来单位不存在,请检查默认往来单位信息");
//                    }
                    // 校验存放上线  排 列 层
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("location_row", excelLocationVO.getLocationRow());
                    wrapper.eq("location_column", excelLocationVO.getLocationColumn());
                    wrapper.eq("location_plies", excelLocationVO.getLocationPlies());
                    Long siziLocation = tLocationMapper.selectCount(wrapper);
                    if (siziLocation >= 1) {
                        throw new ServiceException("第:" + count + "条排列层已存在,请检查排列层信息");
                    }
                    // 校验货位类型
//                    if (!CimsConstants.LOCATION_GOODS_ALLOCATION_TYPE_1.equals(excelLocationVO.getGoodsAllocationType())
//                        && !CimsConstants.LOCATION_GOODS_ALLOCATION_TYPE_2
//                        .equals(excelLocationVO.getGoodsAllocationType())
//                        && !CimsConstants.LOCATION_GOODS_ALLOCATION_TYPE_3
//                        .equals(excelLocationVO.getGoodsAllocationType())
//                        && !CimsConstants.LOCATION_GOODS_ALLOCATION_TYPE_4
//                        .equals(excelLocationVO.getGoodsAllocationType())
//                        &&!CimsConstants.LOCATION_GOODS_ALLOCATION_TYPE_5
//                        .equals(excelLocationVO.getGoodsAllocationType())
//                        &&!CimsConstants.LOCATION_GOODS_ALLOCATION_TYPE_6
//                        .equals(excelLocationVO.getGoodsAllocationType())){
//                        return AjaxResult.error("第:"+count+"条数据货位类型错误,只能输入(货位/轨道/入库口/出库口/提升机/充电位)");
//                    }

                    // 批量插入
                    TLocation location = new TLocation();
                    BeanUtils.copyProperties(excelLocationVO, location);
                    // 状态绑定 默认空闲
                    location.setStatus(Constants.LOCATION_STATUS_YES);
                    // 绑定货位类型
                    location.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
                    try {
                        tLocationMapper.insert(location);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("第:" + count + "条数据出现错误请检查信息:" + location);
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
     * 启用禁用
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public AjaxResult updateStatusById(Long id, String status) {
        TLocation location = tLocationMapper.selectById(id);
        if (location != null) {
            String locationStatus = location.getStatus();
            //货位状态(0:禁用,1:启用)
            if (Constants.LOCATION_STATUS_NO.equals(status)) {
                // 只有无货的库位才可以禁用
                if (!Constants.LOCATION_STATUS_YES.equals(locationStatus)) {
                    return AjaxResult.error("改库位状态已被更改，请刷新后重试");
                }
                // 禁用
                location.setStatus(status);
            } else if (Constants.LOCATION_STATUS_YES.equals(status)) {
                // 只有禁用的库位才可以被启用
                if (!Constants.LOCATION_STATUS_NO.equals(locationStatus)) {
                    return AjaxResult.error("改库位状态已被更改，请刷新后重试");
                }
                // 无货
                location.setStatus(status);
            }
            tLocationMapper.updateById(location);
        }
        return AjaxResult.success();
    }

    /**
     * 获取最大的排列层数
     *
     * @param location
     * @return
     */
    @Override
    public Map<String, Integer> plcCount(TLocation location) {
        return tLocationMapper.plcCount(location);
    }

    @Override
    public int pinsertTLocation(TLocationDTO locationDto) {
        if (locationDto.getStartRow() > locationDto.getEndRow()) {
            throw new ServiceException("起始排需要小于结束排");
        } else if (locationDto.getStartColumn() > locationDto.getEndColumn()) {
            throw new ServiceException("起始列需要小于结束列");
        } else if (locationDto.getStartPlies() > locationDto.getEndPlies()) {
            throw new ServiceException("起始层需要小于结束层");
        }
//        if(locationDto.getEndRow()>2){
//            throw new ServiceException("结束排不能大于2");
//        }
        long rowcount = locationDto.getEndRow() - locationDto.getStartRow();
        long columncount = locationDto.getEndColumn() - locationDto.getStartColumn();
        long pliescount = locationDto.getEndPlies() - locationDto.getStartPlies();
        int totalCount = 0;
        for (long i = 0; i < rowcount + 1; i++) {
            for (long i2 = 0; i2 < columncount + 1; i2++) {
                for (long i3 = 0; i3 < pliescount + 1; i3++) {
                    QueryWrapper<TLocation> queryWrapper = new QueryWrapper<>();
                    queryWrapper
                            //.eq("reservoir_id",locationDto.getReservoirId())
                            .eq("location_row", locationDto.getStartRow() + i)
                            .eq("location_column", locationDto.getStartColumn() + i2).eq("location_plies", locationDto.getStartPlies() + i3);
                    Long count = tLocationMapper.selectCount(queryWrapper);
                    if (count == 0) {
                        TLocation location = new TLocation();
                        location.setAreaId(locationDto.getAreaId());
                        location.setReservoirId(locationDto.getReservoirId());
                        String name = (locationDto.getStartRow() + i) + "排" + (locationDto.getStartColumn() + i2) + "列" + (locationDto.getStartPlies() + i3) + "层";
                        location.setName(name);
                        location.setStatus(Constants.YES);
                        location.setRemark("批量生成");
                        location.setLocationRow(locationDto.getStartRow() + i);
                        location.setLocationColumn(locationDto.getStartColumn() + i2);
                        location.setLocationPlies(locationDto.getStartPlies() + i3);
                        location.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
                        location.setLocationType("0");
                        //编码：排列层+库位编码
//                        location.setCode("RA-" + location.getLocationRow() +"-"+ location.getLocationColumn() +"-"+ location.getLocationPlies());
                        location.setCode("KW" + location.getLocationRow() + location.getLocationColumn() + location.getLocationPlies() + codeConfigService.getCode(CodeEnum.IKW.getCodeName()));
                        long narrowAisleNum = 0;
                        long row = locationDto.getStartRow() + i;
                        if (row % 2 == 0) {
                            narrowAisleNum = row / 2;
                        } else if (row % 2 == 1) {
                            narrowAisleNum = (row / 2) + 1;
                        }
                        tLocationMapper.insert(location);
                        totalCount++;
                    }
                }
            }
        }
        if (totalCount == 0) {
            throw new ServiceException("所选范围库位已存在！");
        }
        return totalCount;
    }

    /**
     * 通过区域、库区查询库位
     *
     * @param areaId      区域
     * @param reservoirId 库区
     * @return
     */
    @Override
    public List<TLocation> locationList(Long areaId, Long reservoirId) {
        return this.list(Wrappers.lambdaQuery(TLocation.class)
                .eq(TLocation::getAreaId, areaId)
                .eq(TLocation::getReservoirId, reservoirId)
                .eq(TLocation::getDelFlag, Constants.NO));
    }

    /**
     * @param categoryId
     * @return java.util.List<cn.haiwei.cims.domain.Location>
     * @description: 推荐库位--非固定托盘
     * @author XMING
     * @date 2022-07-29
     */
    @Override
    public List<TLocation> recommendLoactionMove(Long categoryId) {

        /**
         *       根据物料类别取得该物料类别下面的所有的空闲库位
         *              如果取得空闲库位为空
         *                    那么需要根据巷道号从小到大查询空闲库位（没有限制物料类别的），返回这些库位，为空就说明满了
         *              如果取得空闲库位不为空
         *                    返回这些空闲库位
         */
        List<TLocation> result = new ArrayList<>();

        // 物料类别取得该物料类别下面的所有的空闲库位
        List<TLocation> locationList = tLocationMapper.selectList(new QueryWrapper<TLocation>()
                .eq("deposit_category_id", categoryId)
                .eq("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_1)
                .eq("status", Constants.YES)
                .and(wq -> {
                    wq.isNull("pallet_num")
                            .or()
                            .eq("pallet_num", "");
                })
                .orderByAsc("location_plies")
                .orderByAsc("location_column")
                .orderByAsc("location_row"));
        if (locationList.size() > 0) {
            result.addAll(locationList);
        } else {
            List<TLocation> locationListEmpty = tLocationMapper.selectList(new QueryWrapper<TLocation>()
                    .eq("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_1)
                    .eq("status", Constants.YES)
                    .and(wq -> {
                        wq.isNull("pallet_num")
                                .or()
                                .eq("pallet_num", "");
                    })
                    .orderByAsc("narrow_aisle_num")
                    .orderByAsc("location_plies")
                    .orderByAsc("location_column")
                    .orderByAsc("location_row"));
            result.addAll(locationListEmpty);
        }
        return result;
    }

    /**
     * 获取空闲库位列表
     *
     * @return
     */
    @Override
    public List<TLocation> getOtherLocation(Long locationId) {
        TLocation tLocation = tLocationMapper.selectById(locationId);
        return tLocationMapper.selectOtherList(tLocation.getAreaId());
    }

    /**
     * 库位使用情况
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getLocationUsedInfo(Long reservoirId) {
        return tLocationMapper.getLocationUsedInfo(reservoirId);
    }

    @Override
    public List<Map<String, Object>> getLocationCurrentDetail(Integer locationRow, Long reservoirId) {
        List<Map<String, Object>> list = tLocationMapper.getLocationCurrentDetail(locationRow, reservoirId);
        for (Map<String, Object> map : list) {
            //物料
            map.put("materialList", tLocationMapper.getLocationStock(Long.parseLong(map.get("id").toString())));
        }
        return list;
    }

    /**
     * 获取库位上物料信息
     *
     * @param locationId
     * @return
     */
    @Override
    public List<Map<String, Object>> getLocationStock(Long locationId) {
        return tLocationMapper.getLocationStock(locationId);
    }

    @Override
    public List<Map<String, Object>> getLocationListByReservoirId(Long reservoirId) {
        return tLocationMapper.getLocationListByReservoirId(reservoirId);
    }

    /**
     * 查询库位列表
     *
     * @param tLocation
     * @return
     */
    @Override
    public List<TLocation> selectSimpleList(TLocation tLocation) {
        QueryWrapper<TLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.orderByDesc("create_time");
        return tLocationMapper.selectList(queryWrapper);
    }

    /**
     * 获取库位列表
     * @param tLocation
     * @return
     */
    @Override
    public List<TLocationVO> getLocationList(TLocationDTO tLocation) {
        return tLocationMapper.getLocationList(tLocation);
    }

    /**
     * 通过ids批量获取库位信息
     * @param locations
     * @return
     */
    @Override
    public Map<Long, TLocationVO> getLocationByIds(List<Long> locations) {
        Map<Long, TLocationVO> resMap = new HashMap<>();
        TLocationDTO tLocationDTO = new TLocationDTO();
        tLocationDTO.setIds(locations);
        List<TLocationVO> list = tLocationMapper.getLocationList(tLocationDTO);
        if (list != null) {
            resMap = list.stream().collect(Collectors.toMap(TLocation::getId, Function.identity()));
        }
        return resMap;
    }

    /**
     * 需盘点列表
     * @param request
     * @return
     */
    @Override
    public List<DemandCheckVO> demandCheckList(DemandCheckDTO request) {
        return tLocationMapper.demandCheckList(request);
    }

    /**
     * 修改货位状态(标记有货/标记无货)
     * @param tLocation
     * @return
     */
    @Override
    public AjaxResult updateGoodsAllocationStatus(TLocation tLocation) {
        //校验库存条数
        Long stockCount = stockMapper.selectCount(Wrappers.<TStock>lambdaQuery()
                .eq(TStock::getLocationId, tLocation.getId())
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));
        if (Constants.LOCATION_GOODS_ALLOCATION_STATUS_1.equals(tLocation.getGoodsAllocationStatus())&&stockCount>0){
            return AjaxResult.error("库存条数大于0,不能标记无货");
        }
        if (Constants.LOCATION_GOODS_ALLOCATION_STATUS_2.equals(tLocation.getGoodsAllocationStatus())&&stockCount==0){
            return AjaxResult.error("库存条数等于0,不能标记有货");
        }
        this.update(Wrappers.<TLocation>lambdaUpdate()
                .set(TLocation::getUpdateBy,SecurityUtils.getLoginUser().getUsername())
                .set(TLocation::getUpdateTime,new Date())
                .set(TLocation::getGoodsAllocationStatus,tLocation.getGoodsAllocationStatus())
                .eq(TLocation::getId,tLocation.getId()));
        return AjaxResult.success();
    }


    /**
     * 通过编号查询详情
     * @param code
     * @return
     */
    @Override
    public TLocation getByCode(String code) {
        TLocation tLocation = tLocationMapper.selectOne(Wrappers.<TLocation>lambdaQuery()
                .eq(TLocation::getCode, code)
                .eq(TLocation::getDelFlag, Constants.DEL_FLAG_NO).last(" limit 1"));
        return tLocation;
    }


}
