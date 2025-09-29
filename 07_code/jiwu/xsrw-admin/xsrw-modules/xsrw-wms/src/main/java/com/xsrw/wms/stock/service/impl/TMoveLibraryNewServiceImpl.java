package com.xsrw.wms.stock.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.TMoveLibraryDetail;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.vo.MoveLibraryDetailVo;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import com.xsrw.wms.stock.mapper.TMoveLibraryNewMapper;
import com.xsrw.wms.stock.service.ITMoveLibraryDetailService;
import com.xsrw.wms.stock.service.ITMoveLibraryNewService;
import com.xsrw.wms.stock.service.ITMoveLibraryService;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import com.xsrw.wms.web.util.WcsMoveUtil;
import com.xsrw.wms.web.util.WcsReportUtil;

@Service
public class TMoveLibraryNewServiceImpl extends ServiceImpl<TMoveLibraryNewMapper , TMoveLibrary> implements ITMoveLibraryNewService{

    @Autowired
    private TMoveLibraryNewMapper tMoveLibraryNewMapper;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    ITLocationService locationService;

    @Autowired
    private ITTaskWcsService taskWcsService;

    @Autowired
    private WcsReportUtil wcsReportUtil;

    @Autowired
    private WcsMoveUtil wcsMoveUtil;

    @Autowired
    private ITMoveLibraryDetailService itMoveLibraryDetailService;

    @Autowired
    private ITMoveLibraryService itMoveLibraryService;

    @Override
    public List<MoveLibraryVo> selectMoveLibraryList(MoveLibraryVo tMoveLibrary) {
        return tMoveLibraryNewMapper.selectMoveLibraryList(tMoveLibrary);
    }

    public List<TLocationVO> selectTLocationEmptyList(TLocationDTO tLocationDTO){
        return tMoveLibraryNewMapper.selectTLocationEmptyList(tLocationDTO);
    }


    public List<MoveLibraryDetailVo> selectMoveLibraryDetails(TMoveLibrary tMoveLibrary){
        return tMoveLibraryNewMapper.selectMoveLibraryDetails(tMoveLibrary);
    }

    public AjaxResult moveAdd(TMoveLibrary tMoveLibrary){
        //判断是否有未完成的wcs任务
       if (tMoveLibraryNewMapper.selecttTaskWcs() != 0){
            return AjaxResult.error("WCS有其他任务未完成");
        }
        //获取当前载具所有物料信息
        List<TStock> list = tMoveLibraryNewMapper.selectTStock(tMoveLibrary);
        //根据前端传值查询载具信息
        TTray tTray = tMoveLibraryNewMapper.selecttTray(tMoveLibrary);
        //判断载具状态
        if (tTray.getStatus().equals("0")){
            return AjaxResult.error("载具状态不正确");
        }
        //根据查询的载具信息查询库位信息
        TLocation tLocation = tMoveLibraryNewMapper.selecttLocation(tTray.getLocationId());
        //判断库位状态
        if (!tLocation.getGoodsAllocationStatus().equals("2")){
            return AjaxResult.error("载具货位状态不正确");
        }
        //根据前端传参查询出库库位、入库库位信息
        TLocation intLocation = tMoveLibraryNewMapper.selecttLocation(tMoveLibrary.getLocationInId());
        TLocation outtLocation = tMoveLibraryNewMapper.selecttLocation(tMoveLibrary.getLocationOutId());
        if (!Objects.equals(tLocation.getId(), outtLocation.getId())){
            return AjaxResult.error("载具库位和出库库位不相符");
        }
        if (!outtLocation.getGoodsAllocationStatus().equals("2")){
            return AjaxResult.error("出库载具货位状态不正确");
        }else if (!intLocation.getGoodsAllocationStatus().equals("1")){
            return AjaxResult.error("入库载具货位状态不正确");
        }
        //处理移库表数据
        TMoveLibrary tmoveLibrary = new TMoveLibrary();
        tmoveLibrary.setCode(codeConfigService.getCode(CodeEnum.MKNYW.getCodeName()));
        tmoveLibrary.setLocationOutId(tMoveLibrary.getLocationOutId());
        tmoveLibrary.setLocationInId(tMoveLibrary.getLocationInId());
        tmoveLibrary.setTrayId(tMoveLibrary.getTrayId());
        itMoveLibraryService.save(tmoveLibrary);
        //处理移库详情表数据
        List<TMoveLibraryDetail> tMoveLibraryDetailList = new ArrayList<>();
        for(TStock tStock :  list){
            TMoveLibraryDetail tMoveLibraryDetail = new TMoveLibraryDetail();
            tMoveLibraryDetail.setMoveLibraryCode(tmoveLibrary.getCode());
            tMoveLibraryDetail.setStockId(tStock.getId());
            tMoveLibraryDetail.setMaterialId(tStock.getMaterialId());
            tMoveLibraryDetail.setCount(tStock.getCount());
            tMoveLibraryDetail.setBatchCode(tStock.getBatchCode());
            tMoveLibraryDetailList.add(tMoveLibraryDetail);
        }
        itMoveLibraryDetailService.saveBatch(tMoveLibraryDetailList);
        //处理t_task_wcs表数据
        TTaskWcs taskWcs = new TTaskWcs();
        taskWcs.setTaskType(Constants.TASK_TYPE_MOVE);
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
        taskWcs.setTrayId(tTray.getId());
        taskWcs.setTrayCode(tTray.getCode());
        taskWcs.setLocationId(intLocation.getId());
        taskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
        taskWcs.setMainTaskNo(taskWcs.getTaskNo());
        taskWcs.setPurposePosition(intLocation.getCode().toString());
        taskWcs.setStartPosition(outtLocation.getCode().toString());
        taskWcsService.save(taskWcs);
        //更新移库表wcs_id
        tmoveLibrary.setWcsId(taskWcs.getId());
        itMoveLibraryService.updateById(tmoveLibrary);
        //移库下发判断条件
        if (outtLocation.getExtentionType() == 1){
            if (intLocation.getExtentionType() == 1){
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            } else if (intLocation.getExtentionType() == 2 && intLocation.getExtentionFristId()==null) {
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            }else {
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                WcsOrderDTO wcsOrderDTOInAMP = sendWcsInAMP(taskWcs);
                wcsOrderDTOInAMP.setMainTaskNo(wcsOrderDTODTP.getMainTaskNo());
                wcsOrderDTOInAMP.setMainSort(wcsOrderDTODTP.getMainSort());
                wcsOrderDTODTP.setMoveTaskNo(wcsOrderDTOInAMP.getMoveTaskNo());
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTOInAMP);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            }
        } else if (outtLocation.getExtentionType() == 2 && outtLocation.getExtentionFristId() == null) {
            if (intLocation.getExtentionType() == 1){
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            } else if (intLocation.getExtentionType() == 2 && intLocation.getExtentionFristId()==null) {
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            }else {
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                WcsOrderDTO wcsOrderDTOInAMP = sendWcsInAMP(taskWcs);
                wcsOrderDTOInAMP.setMainTaskNo(wcsOrderDTODTP.getMainTaskNo());
                wcsOrderDTOInAMP.setMainSort(wcsOrderDTODTP.getMainSort());
                wcsOrderDTODTP.setMoveTaskNo(wcsOrderDTOInAMP.getMoveTaskNo());
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTOInAMP);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            }
        } else if (outtLocation.getExtentionType() == 2 && outtLocation.getExtentionFristId() != null) {
            if (intLocation.getExtentionType() == 1){
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                WcsOrderDTO wcsOrderDTOOutAMP = sendWcsOutAMP(taskWcs);
                wcsOrderDTOOutAMP.setMainTaskNo(wcsOrderDTODTP.getMainTaskNo());
                wcsOrderDTOOutAMP.setMainSort(wcsOrderDTODTP.getMainSort());
                wcsOrderDTODTP.setMoveTaskNo(wcsOrderDTOOutAMP.getMoveTaskNo());
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTOOutAMP);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            } else if (intLocation.getExtentionType() == 2 && intLocation.getExtentionFristId()==null) {
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                WcsOrderDTO wcsOrderDTOOutAMP = sendWcsOutAMP(taskWcs);
                wcsOrderDTOOutAMP.setMainTaskNo(wcsOrderDTODTP.getMainTaskNo());
                wcsOrderDTOOutAMP.setMainSort(wcsOrderDTODTP.getMainSort());
                wcsOrderDTODTP.setMoveTaskNo(wcsOrderDTOOutAMP.getMoveTaskNo());
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTOOutAMP);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            }else {
                WcsOrderDTO wcsOrderDTODTP = sendWcsDTP(taskWcs);
                WcsOrderDTO wcsOrderDTOOutAMP = sendWcsOutAMP(taskWcs);
                WcsOrderDTO wcsOrderDTOInAMP = sendWcsInAMP(taskWcs);
                wcsOrderDTOOutAMP.setMainTaskNo(wcsOrderDTODTP.getMainTaskNo());
                wcsOrderDTOInAMP.setMainTaskNo(wcsOrderDTODTP.getMainTaskNo());
                wcsOrderDTOOutAMP.setMainSort(wcsOrderDTODTP.getMainSort());
                wcsOrderDTOInAMP.setMainSort(wcsOrderDTODTP.getMainSort());
                wcsOrderDTOInAMP.setMoveTaskNo(wcsOrderDTOOutAMP.getMoveTaskNo());
                wcsOrderDTODTP.setMoveTaskNo(wcsOrderDTOInAMP.getMoveTaskNo());
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTOOutAMP);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTOInAMP);
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTODTP);
            }
        }
        return AjaxResult.success("操作成功");
    }


    /*直接移库参数*/
    private WcsOrderDTO sendWcsDTP(TTaskWcs taskWcs) {
        TLocation tLocationOut = locationService.getByCode(taskWcs.getStartPosition());
        TLocation tLocationIn = locationService.getByCode(taskWcs.getPurposePosition());
        String startStation = "";
        String endStation = "";
        if (Constants.LOCATION_FLOOR_FIRST.equals(tLocationOut.getFloorType())) {
            //起始库位
            startStation = tLocationOut.getLocationPlies() + "-" + tLocationOut.getPalletNodeId();
            //目标库位
            endStation = tLocationIn.getLocationPlies() + "-" + tLocationIn.getPalletNodeId();
            //身位
            Integer mainSort = tLocationOut.getExtentionType();
            WcsOrderDTO wcsOrderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(),
                    startStation, endStation, tLocationOut.getCode(), taskWcs.getTrayCode());
            wcsOrderDTO.setMainSort(mainSort);
            wcsOrderDTO.setMainTaskNo(taskWcs.getMainTaskNo());
            wcsOrderDTO.setProductDesc("move");
            tLocationOut.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(tLocationOut);
            tLocationIn.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
            locationService.updateById(tLocationIn);
            return wcsOrderDTO;
        }
        return null;
    }

    /*起始库位算法移库参数*/
    private WcsOrderDTO sendWcsOutAMP(TTaskWcs taskWcs) {
        TLocation tLocationOut = locationService.getByCode(taskWcs.getStartPosition());
        TLocation tLocationIn = locationService.getByCode(taskWcs.getPurposePosition());
        if (Constants.LOCATION_FLOOR_FIRST.equals(tLocationOut.getFloorType())){
            WcsOrderDTO wcsOrderDTO = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), tLocationOut.getCode());
            wcsOrderDTO.setProductDesc("move");
            return wcsOrderDTO;
        }
        return null;
    }

    /*目标库位算法移库参数*/
    private WcsOrderDTO sendWcsInAMP(TTaskWcs taskWcs) {
        TLocation tLocationOut = locationService.getByCode(taskWcs.getStartPosition());
        TLocation tLocationIn = locationService.getByCode(taskWcs.getPurposePosition());
        if (Constants.LOCATION_FLOOR_FIRST.equals(tLocationOut.getFloorType())){
            WcsOrderDTO wcsOrderDTO = wcsMoveUtil.dealDoubleExtension(null,
                    tLocationIn.getPalletNum(), tLocationIn.getCode());
            wcsOrderDTO.setProductDesc("move");
            return wcsOrderDTO;
        }
        return null;
    }


    /*补发移库任务*/
    public void selectReissueMove(TTaskWcsVO taskWcsVO){
        /*根据一身位查询二身位库位信息*/
        TLocation tLocationTwo = tMoveLibraryNewMapper.selecttLocationTwo(taskWcsVO);
        //根据二身位信息查询是否有出库任务
        TTaskWcsVO tTaskWcsVOOut = tMoveLibraryNewMapper.selectTaskWcsOut(tLocationTwo);
        /*根据二身位信息查询二身位是否有移库任务*/
        TTaskWcsVO  tTaskWcsVOMove = tMoveLibraryNewMapper.selectTaskWcsMove(tTaskWcsVOOut);
        if (tTaskWcsVOOut != null && tTaskWcsVOMove == null){
            System.out.println("补发移库任务-----"+taskWcsVO);
            WcsOrderDTO wcsOrderDTO = wcsMoveUtil.dealDoubleExtension(null,
                    tLocationTwo.getPalletNum(),tLocationTwo.getCode());
            wcsOrderDTO.setMainTaskNo(taskWcsVO.getMainTaskNo());
            wcsOrderDTO.setProductDesc("ReissueMove");
            wcsReportUtil.sendWcsMoveReport(wcsOrderDTO);
        }
    }























    /*无库位推荐算法直接移库*/
    /*一身位无货或在一身位*/
    private void sendWcsNoAlgorithm(TTaskWcs taskWcs) {
        TLocation tLocationOut = locationService.getById(taskWcs.getPurposePosition());
        TLocation tLocationIn = locationService.getById(taskWcs.getStartPosition());
        String startStation = "";
        String endStation = "";
        if (Constants.LOCATION_FLOOR_FIRST.equals(tLocationOut.getFloorType())){
            startStation = tLocationOut.getLocationPlies() + "-" + tLocationOut.getPalletNodeId();
            endStation = tLocationIn.getLocationPlies() + "-" + tLocationIn.getPalletNodeId();
            Integer mainSort = tLocationOut.getExtentionType();
            WcsOrderDTO wcsOrderDTOOut = new WcsOrderDTO(taskWcs.getId(),taskWcs.getTaskNo(),
                    startStation,endStation,tLocationOut.getCode(),taskWcs.getTrayCode());
            wcsOrderDTOOut.setMainSort(mainSort);
            wcsOrderDTOOut.setMainTaskNo(taskWcs.getMainTaskNo());
            tLocationOut.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(tLocationOut);
            tLocationIn.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
            locationService.updateById(tLocationOut);
            wcsReportUtil.sendWcsMoveReport(wcsOrderDTOOut);
        }
    }

    /*有推荐库位算法移库*/
    /*起始一身位有货，目标一身位无货*/
    private void sendWcsOneStock(TTaskWcs taskWcs) {
        TLocation tLocationOutTwo = locationService.getById(taskWcs.getPurposePosition());
        TLocation tLocationIn = locationService.getById(taskWcs.getStartPosition());
        String startStation = "";
        String endStation = "";
        //判断一楼
        if (Constants.LOCATION_FLOOR_FIRST.equals(tLocationOutTwo.getFloorType())){
            startStation = tLocationOutTwo.getLocationPlies() + "-" + tLocationOutTwo.getPalletNodeId();
            endStation = tLocationIn.getLocationPlies() + "-" + tLocationIn.getPalletNodeId();
            Integer mainSort = tLocationOutTwo.getExtentionType();
            //组装出库库位参数
            WcsOrderDTO wcsOrderDTOOutTwo = new WcsOrderDTO(taskWcs.getId(),taskWcs.getTaskNo(),
                    startStation,endStation,tLocationOutTwo.getCode(),taskWcs.getTrayCode());
            wcsOrderDTOOutTwo.setMainSort(mainSort);
            wcsOrderDTOOutTwo.setMainTaskNo(taskWcs.getMainTaskNo());
            tLocationOutTwo.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(tLocationOutTwo);
            tLocationIn.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
            locationService.updateById(tLocationOutTwo);
            //获取出库一身位移库参数
            WcsOrderDTO wcsOrderDTOIn = wcsMoveUtil.dealDoubleExtension(null,
                    taskWcs.getTaskNo(), tLocationOutTwo.getCode());
            if (wcsOrderDTOIn != null) {
                wcsOrderDTOIn.setMainTaskNo(wcsOrderDTOOutTwo.getMainTaskNo());
                wcsOrderDTOIn.setMainSort(mainSort);
                wcsOrderDTOOutTwo.setMoveTaskNo(wcsOrderDTOIn.getMoveTaskNo());
                wcsReportUtil.sendWcsMoveReport(wcsOrderDTOIn);
            }
            wcsReportUtil.sendWcsMoveReport(wcsOrderDTOOutTwo);
        }
    }
}
