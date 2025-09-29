package com.xsrw.wms.base.service.impl;

import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.common.enums.TrayTypeEnum;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.domain.dto.ExcelMaterialErpDTO;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.ExcelMaterialVO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.base.mapper.*;
import com.xsrw.wms.base.service.ITCodeConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.service.ITMaterialService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 物料Service业务层处理
 *
 * @author wxr
 * @date 2023-05-05
 */
@Service
public class TMaterialServiceImpl extends ServiceImpl<TMaterialMapper, TMaterial> implements ITMaterialService {
    @Autowired
    private TMaterialMapper tMaterialMapper;
    @Autowired
    private ITCodeConfigService codeConfigService;
    @Autowired
    private TUnitMapper tUnitMapper;
    @Autowired
    private TCategoryMapper tCategoryMapper;
    @Autowired
    private TContactsUnitMapper tContactsUnitMapper;
    @Autowired
    private TBatchAttrMapper tBatchAttrMapper;


    /**
     * 查询物料列表
     *
     * @param tMaterial 物料
     * @return 物料
     */
    @Override
    public List<TMaterialVO> selectTMaterialList(TMaterial tMaterial) {
        return tMaterialMapper.selectTMaterialList(tMaterial);
    }

    /**
     * 查询物料
     *
     * @param id 物料主键
     * @return 物料
     */
    @Override
    public TMaterial selectTMaterialById(Long id) {
        return tMaterialMapper.selectById(id);
    }

    /**
     * 新增物料
     *
     * @param tMaterial 物料
     * @return 结果
     */
    @Override
    public AjaxResult insertTMaterial(TMaterial tMaterial) {

        // 校验物料编码是否已存在
        TMaterial material = tMaterialMapper.selectOne(new QueryWrapper<TMaterial>().eq("code", tMaterial.getCode()));
        if (material != null){
            return AjaxResult.error("物料编码已存在");
        }

        tMaterial.setInspectionMethod("1");
        if (tMaterial.getExpirationDate() != null && tMaterial.getExpirationDate() == 0){
            tMaterial.setExpirationDate(null);
        }
        if (tMaterial.getExpirationFlag() == null){
            tMaterial.setExpirationFlag("0");
        }
        tMaterial.setRoughWeight(null);
        tMaterialMapper.insert(tMaterial);
        return AjaxResult.success();
    }

    /**
     * 修改物料
     *
     * @param tMaterial 物料
     * @return 结果
     */
    @Override
    public int updateTMaterial(TMaterial tMaterial) {
        return tMaterialMapper.updateById(tMaterial);
    }


    /**
     * 批量删除物料
     *
     * @param ids 需要删除的物料主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialByIds(Long[] ids) {
        //todo wxr 或许是否要判断是否有库存
        return tMaterialMapper.deleteTMaterialByIds(ids);
    }

    /**
     * 删除物料信息
     *
     * @param id 物料主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialById(Long id) {
        return tMaterialMapper.deleteTMaterialById(id);
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
            InputStream inputStream = file.getInputStream();
            // 转换Excel数据
            ExcelUtil<ExcelMaterialVO> util = new ExcelUtil<>(ExcelMaterialVO.class);
            List<ExcelMaterialVO> materialVOS = util.importExcel(inputStream);
            List<ExcelMaterialVO> voList=new ArrayList<>();
            //物料名称
            Map<String,Object> map=new HashMap<>();
            //物料编号
            Map<String,Object> mapCode=new HashMap<>();
            if (materialVOS.size() >0) {

                //校验导入字段是否为空
                int notNullCount = 1;
                for (int i = 0; i < materialVOS.size(); i++) {
                    notNullCount = notNullCount + 1;
                    ExcelMaterialVO materialVO = materialVOS.get(i);
                    if (StringUtils.isEmpty(materialVO.getName())) {
                        throw new ServiceException("第：" + notNullCount + "条物料名称不可为空");
                    }
                    if(map.containsKey(materialVO.getName())){
                        throw new ServiceException("第：" + notNullCount + "条和第"+map.get(materialVO.getName())+"条物料名称重复");
                    }else{
                        map.put(materialVO.getName(),notNullCount);
                    }
                    if (!StringUtils.isEmpty(materialVO.getCode())) {
                        if(mapCode.containsKey(materialVO.getCode())){
                            throw new ServiceException("第：" + notNullCount + "条和第"+mapCode.get(materialVO.getCode())+"条物料编码重复");
                        }else{
                            mapCode.put(materialVO.getCode(),notNullCount);
                        }
                    }else{
                        materialVO.setCode(codeConfigService.getCode(CodeEnum.IWL.getCodeName()));
                    }
                    if (StringUtils.isEmpty(materialVO.getUnitName())) {
                        throw new ServiceException("第：" + notNullCount + "条单位名称不可为空");
                    }
                    if (StringUtils.isEmpty(materialVO.getCategoryName())) {
                        throw new ServiceException("第：" + notNullCount + "条物料类别不可为空");
                    }
                    if(materialVO.getStockMin()==null||materialVO.getStockMin()<=0){
                        throw new ServiceException("第：" + notNullCount + "条物料库存下限不可为空");
                    }
                    if(materialVO.getStockMax()==null||materialVO.getStockMax()<=0){
                        throw new ServiceException("第：" + notNullCount + "条物料库存上限不可为空");
                    }
                    if(materialVO.getStockMax()<materialVO.getStockMin()){
                        throw new ServiceException("第：" + notNullCount + "条物料库存下限不可以大于库存上限");
                    }
//                    if(StringUtils.isEmpty(materialVO.getBatchAttrName())){
//                        throw new ServiceException("第：" + notNullCount + "条批次属性不可以为空");
//                    }
//                    if(StringUtils.isEmpty(materialVO.getBatchFlag())){
//                        throw new ServiceException("第：" + notNullCount + "条是否启用批次不可以为空");
//                    }
                    if(StringUtils.isEmpty(materialVO.getContactsUnitName())){
                        throw new ServiceException("第：" + notNullCount + "条供应商不可以为空");
                    }
                    if(materialVO.getUnitPrice()==null||materialVO.getUnitPrice()<=0){
                        throw new ServiceException("第：" + notNullCount + "条单价不可以为空");
                    }
                    if(materialVO.getRoughWeight()==null||materialVO.getRoughWeight()<=0){
                        throw new ServiceException("第：" + notNullCount + "条重量不可以为空");
                    }
                    if(materialVO.getMaterialLength()==null||materialVO.getMaterialLength()<=0){
                        throw new ServiceException("第：" + notNullCount + "条长度不可以为空");
                    }
                    if(materialVO.getMaterialWidth()==null||materialVO.getMaterialWidth()<=0){
                        throw new ServiceException("第：" + notNullCount + "条宽度不可以为空");
                    }
                    if(materialVO.getMaterialHeight()==null||materialVO.getMaterialHeight()<=0){
                        throw new ServiceException("第：" + notNullCount + "条高度不可以为空");
                    }
                    QueryWrapper reservoirWrapper = new QueryWrapper();
                    reservoirWrapper.eq("name", materialVO.getName());
                    reservoirWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    Long size = tMaterialMapper.selectCount(reservoirWrapper);

                    if (1 <= size) {
                        throw new ServiceException("第:" + notNullCount + "条物料已存在,请检查物料信息");
                    }
                    //单位
                    TUnit tUnit=tUnitMapper.selectOne(Wrappers.lambdaQuery(TUnit.class)
                            .eq(TUnit::getName,materialVO.getUnitName())
                            .eq(TUnit::getDelFlag,"0")
                    );
                    if(tUnit==null){
                         tUnit=new TUnit();
                         tUnit.setName(materialVO.getUnitName());
                         tUnitMapper.insert(tUnit);
                         materialVO.setUnitId(tUnit.getId());
                    }else{
                        materialVO.setUnitId(tUnit.getId());
                    }
                    //物料类别
                    TCategory tCategory=tCategoryMapper.selectOne(Wrappers.lambdaQuery(TCategory.class)
                            .eq(TCategory::getName,materialVO.getCategoryName())
                            .eq(TCategory::getDelFlag,"0")
                    );
                    if(tCategory==null){
                        tCategory=new TCategory();
                        tCategory.setName(materialVO.getCategoryName());
                        tCategory.setDelFlag("0");
                        tCategoryMapper.insert(tCategory);
                        materialVO.setCategoryId(tCategory.getId());
                    }else{
                        materialVO.setCategoryId(tCategory.getId());
                    }
                    //供应商
                    TContactsUnit  tContactsUnit=tContactsUnitMapper.selectOne(Wrappers.lambdaQuery(TContactsUnit.class)
                            .eq(TContactsUnit::getName,materialVO.getContactsUnitName())
                            .eq(TContactsUnit::getDelFlag,"0")
                    );
                    if(tContactsUnit==null){
                        tContactsUnit=new TContactsUnit();
                        tContactsUnit.setName(materialVO.getContactsUnitName());
                        tContactsUnitMapper.insert(tContactsUnit);
                        materialVO.setContactsUnitId(tContactsUnit.getId());
                    }else{
                        materialVO.setContactsUnitId(tContactsUnit.getId());
                    }
                    //批次属性
                    TBatchAttr tBatchAttr=tBatchAttrMapper.selectOne(Wrappers.lambdaQuery(TBatchAttr.class)
                            .eq(TBatchAttr::getName,"制单日期")
                            .eq(TBatchAttr::getDelFlag,"0")
                    );
                    materialVO.setBatchAttrId(tBatchAttr.getId());
//                    if(materialVO.getBatchFlag().equals("是")){
//                        materialVO.setBatchFlag("1");
//                    }else{
//                        materialVO.setBatchFlag("0");
//                    }
                    materialVO.setBatchFlag("1");
                    //检验方式(1:免检 2抽检 3全检)
                    if(!StringUtils.isEmpty(materialVO.getInspectionMethod())){
                        if(materialVO.getInspectionMethod().equals("免检")){
                            materialVO.setInspectionMethod("1");
                        }else  if (materialVO.getInspectionMethod().equals("抽检")){
                            materialVO.setInspectionMethod("2");
                        }else if(materialVO.getInspectionMethod().equals("全检")){
                            materialVO.setInspectionMethod("3");
                        }
                    }
                    voList.add(materialVO);
                }

                //校验导入信息是否正确
                int count = 0;
                for (int i = 0; i < voList.size(); i++) {
                    count = count + 1;
                    ExcelMaterialVO materialVO = voList.get(i);
                    //导入
                    TMaterial tMaterial = new TMaterial();
                    BeanUtils.copyProperties(materialVO, tMaterial);
                    try {
                        tMaterialMapper.insert(tMaterial);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("第:" + count + "条数据出现错误请检查信息:" + materialVO);
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
     * erp物料信息导入
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public AjaxResult importDataErp(MultipartFile file) throws Exception {
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
            ExcelUtil<ExcelMaterialErpDTO> util = new ExcelUtil<>(ExcelMaterialErpDTO.class);
            List<ExcelMaterialErpDTO> materialVOS = util.importExcel(inputStream);
            List<ExcelMaterialErpDTO> voList=new ArrayList<>();
            //物料名称
            Map<String,Object> map=new HashMap<>();
            //物料编号
            Map<String,Object> mapCode=new HashMap<>();
            if (materialVOS.size() >0) {

                //校验导入字段是否为空
                int notNullCount = 1;
                for (int i = 0; i < materialVOS.size(); i++) {
                    notNullCount = notNullCount + 1;
                    ExcelMaterialErpDTO materialVO = materialVOS.get(i);
                    if (StringUtils.isEmpty(materialVO.getName())) {
                        throw new ServiceException("第：" + notNullCount + "条物料名称不可为空");
                    }
                    if(map.containsKey(materialVO.getName())){
                        throw new ServiceException("第：" + notNullCount + "条和第"+map.get(materialVO.getName())+"条物料名称重复");
                    }else{
                        map.put(materialVO.getName(),notNullCount);
                    }
//                    if (!StringUtils.isEmpty(materialVO.getCode())) {
//                        if(mapCode.containsKey(materialVO.getCode())){
//                            throw new ServiceException("第：" + notNullCount + "条和第"+mapCode.get(materialVO.getCode())+"条物料编码重复");
//                        }else{
//                            mapCode.put(materialVO.getCode(),notNullCount);
//                        }
//                    }else{
//                        materialVO.setCode(codeConfigService.getCode(CodeEnum.IWL.getCodeName()));
//                    }
                    if (StringUtils.isEmpty(materialVO.getUnitName())) {
                        throw new ServiceException("第：" + notNullCount + "条单位名称不可为空");
                    }
                    if (StringUtils.isEmpty(materialVO.getCategoryName())) {
                        throw new ServiceException("第：" + notNullCount + "条物料类别不可为空");
                    }

                    QueryWrapper reservoirWrapper = new QueryWrapper();
                    reservoirWrapper.eq("name", materialVO.getName());
                    reservoirWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
                    Long size = tMaterialMapper.selectCount(reservoirWrapper);

                    if (1 <= size) {
                        throw new ServiceException("第:" + notNullCount + "条物料已存在,请检查物料信息");
                    }
                    //单位
                    TUnit tUnit=tUnitMapper.selectOne(Wrappers.lambdaQuery(TUnit.class)
                            .eq(TUnit::getName,materialVO.getUnitName())
                            .eq(TUnit::getDelFlag,"0")
                    );
                    if(tUnit==null){
                        tUnit=new TUnit();
                        tUnit.setName(materialVO.getUnitName());
                        tUnitMapper.insert(tUnit);
                        materialVO.setUnitId(tUnit.getId());
                    }else{
                        materialVO.setUnitId(tUnit.getId());
                    }
                    //物料类别
                    TCategory tCategory=tCategoryMapper.selectOne(Wrappers.lambdaQuery(TCategory.class)
                            .eq(TCategory::getName,materialVO.getCategoryName())
                            .eq(TCategory::getDelFlag,"0")
                    );
                    if(tCategory==null){
                        tCategory=new TCategory();
                        tCategory.setId(materialVO.getCategoryId());
                        tCategory.setName(materialVO.getCategoryName());
                        tCategory.setDelFlag("0");
                        tCategoryMapper.insert(tCategory);
                        materialVO.setCategoryId(tCategory.getId());
                    }else{
                        materialVO.setCategoryId(tCategory.getId());
                    }
                    //供应商
//                    TContactsUnit  tContactsUnit=tContactsUnitMapper.selectOne(Wrappers.lambdaQuery(TContactsUnit.class)
//                            .eq(TContactsUnit::getName,materialVO.getContactsUnitId())
//                            .eq(TContactsUnit::getDelFlag,"0")
//                    );
//                    if(tContactsUnit==null){
//                        tContactsUnit=new TContactsUnit();
//                        tContactsUnit.setId(materialVO.getContactsUnitId());
////                        tContactsUnit.setName(materialVO.getContactsUnitName());
//                        tContactsUnitMapper.insert(tContactsUnit);
//                        materialVO.setContactsUnitId(tContactsUnit.getId());
//                    }else{
//                        materialVO.setContactsUnitId(tContactsUnit.getId());
//                    }
                    //批次属性
                    TBatchAttr tBatchAttr=tBatchAttrMapper.selectOne(Wrappers.lambdaQuery(TBatchAttr.class)
                            .eq(TBatchAttr::getName,"制单日期")
                            .eq(TBatchAttr::getDelFlag,"0")
                    );
                    materialVO.setBatchAttrId(tBatchAttr.getId());
//                    if(materialVO.getBatchFlag().equals("是")){
//                        materialVO.setBatchFlag("1");
//                    }else{
//                        materialVO.setBatchFlag("0");
//                    }

                    materialVO.setDescription(materialVO.getName());
                    int nameIndex = materialVO.getName().indexOf("\\");
                    if(nameIndex > 0){
                        materialVO.setSpecifications(materialVO.getDescription().substring(nameIndex+1));
                        materialVO.setName(materialVO.getDescription().substring(0,nameIndex));
                    }
                    //检验方式(1:免检 2抽检 3全检)
//                    if(!StringUtils.isEmpty(materialVO.getInspectionMethod())){
//                        if(materialVO.getInspectionMethod().equals("免检")){
//                            materialVO.setInspectionMethod("1");
//                        }else  if (materialVO.getInspectionMethod().equals("抽检")){
//                            materialVO.setInspectionMethod("2");
//                        }else if(materialVO.getInspectionMethod().equals("全检")){
//                            materialVO.setInspectionMethod("3");
//                        }
//                    }
                    voList.add(materialVO);
                }

                //校验导入信息是否正确
                int count = 0;
                for (int i = 0; i < voList.size(); i++) {
                    count = count + 1;
                    ExcelMaterialErpDTO materialVO = voList.get(i);
                    //导入
                    TMaterial tMaterial = new TMaterial();
                    BeanUtils.copyProperties(materialVO, tMaterial);
                    tMaterial.setBatchFlag("1");
                    tMaterial.setInspectionMethod("1");
                    tMaterial.setExpirationFlag("0");
                    tMaterial.setMaterialWidth(0L);
                    tMaterial.setMaterialHeight(0L);
                    tMaterial.setMaterialLength(0L);
                    try {
                        tMaterialMapper.insert(tMaterial);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new ServiceException("第:" + count + "条数据出现错误请检查信息:" + materialVO);
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

    public static void main(String[] args) {
        String str = "电磁阀\\055000.1-00/4990\\弓网故障快速自动降弓装置\\JG-61D";
        int nameIndex = str.indexOf("\\");
        System.out.println("=========="+nameIndex);
        System.out.println("=============1:"+str.substring(0,nameIndex));
        System.out.println("=============2:"+str.substring(nameIndex+1));
    }
    /**
     * 批量设置物料库存上限、库存下限
     * @param tMaterial
     * @return
     */
    @Override
    public int bacthStock(TMaterialDTO tMaterial) {

        String[] id = tMaterial.getIds().split(",");
        Object[] array = Arrays.asList(id).stream().map(e -> Long.valueOf(e)).toArray();


        TMaterial material = new TMaterial();
        material.setStockMax(tMaterial.getStockMax());
        material.setStockMin(tMaterial.getStockMin());
        return tMaterialMapper.update(material, new UpdateWrapper<TMaterial>().in("id", array));
    }

    /**
     * 根据id获取编号
     * @param materialIds
     * @return
     */
    @Override
    public Map<Long, TMaterial> getCodeByIds(List<Long> materialIds) {
        Map<Long, TMaterial> resMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(materialIds)){
            QueryWrapper<TMaterial> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
            queryWrapper.in("id", materialIds);
            List<TMaterial> tMaterials = tMaterialMapper.selectList(queryWrapper);
            resMap = tMaterials.stream().collect(Collectors.toMap(TMaterial::getId, Function.identity()));
        }
        return resMap;
    }

    /**
     * 通过物料ids获取对应的物料重量
     * @param materialIds
     * @return
     */
    @Override
    public Map<Long, Double> getWeightByIds(List<Long> materialIds) {
        Map<Long, Double> resMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(materialIds)){
            QueryWrapper<TMaterial> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
            queryWrapper.in("id", materialIds);
            queryWrapper.isNotNull("rough_weight");
            List<TMaterial> tMaterials = tMaterialMapper.selectList(queryWrapper);
            resMap = tMaterials.stream().collect(Collectors.toMap(TMaterial::getId, TMaterial::getRoughWeight));
        }
        return resMap;
    }
    @Override
    public TMaterial getCodeById(String materialCode) {
        QueryWrapper<TMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("code", materialCode);
        return tMaterialMapper.selectOne(queryWrapper);
    }

    /**
     * 通过物料ids获取对应的推荐载具类型
     * @param materialIds
     * @return
     */
    @Override
    public AjaxResult getTrayTypeByMaterials(Long[] materialIds) {
        QueryWrapper<TMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.in("id", materialIds);
        List<TMaterial> tMaterials = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(tMaterials)) {
            Integer trayType = 0;
            for (TMaterial e : tMaterials) {
                Integer trayType1 = TrayTypeEnum.compareTrayType(e.getMaterialLength(), e.getMaterialWidth(), e.getMaterialHeight());
                if (trayType1 > trayType) {
                    trayType = trayType1;
                }
            }
            if (trayType > 0) {
                return AjaxResult.success(trayType);
            }
        }
        return AjaxResult.error("无可推荐载具，超出载具范围限制");
    }

    /**
     * 物料选择列表
     * @param tMaterial
     * @return
     */
    @Override
    public List<TMaterialVO> getMaterialSelectList(TMaterialDTO tMaterial) {
        return tMaterialMapper.getMaterialSelectList(tMaterial);
    }

}
