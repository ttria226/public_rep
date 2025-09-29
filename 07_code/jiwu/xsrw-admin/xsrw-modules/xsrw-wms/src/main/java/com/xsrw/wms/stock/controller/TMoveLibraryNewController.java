package com.xsrw.wms.stock.controller;

import com.alibaba.fastjson.JSONObject;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.dto.TTrayDTO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.vo.MoveLibraryDetailVo;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import com.xsrw.wms.stock.service.ITMoveLibraryNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/moveLibraryNew")
public class TMoveLibraryNewController extends BaseController {

    @Autowired
    private ITMoveLibraryNewService itMoveLibraryNewService;
    @Autowired
    private ITTrayService itTrayService;

    @Autowired
    private ITLocationService itLocationService;


    //查询载具状态
    @GetMapping("/tray")
    public AjaxResult tray(TTrayDTO tTray){
        TTray tray = itTrayService.selectTTrayByCode(tTray.getCode());
        if (tray == null){
            return AjaxResult.error("未查询到数据");
        }else {
            return AjaxResult.success(tray);
        }
    }

    //查询库位状态
    @GetMapping("/location")
    public AjaxResult location(Long id){
        return AjaxResult.success(itLocationService.selectTLocationById(id));
    }

    //查询同层空库位状态
    @GetMapping("/locationEmpty")
    public TableDataInfo locationEmpty(TLocationDTO tLocation){
        List<TLocationVO> list = itMoveLibraryNewService.selectTLocationEmptyList(tLocation);
        return getDataTable(list);
    }


    //查询移库任务列表
    @GetMapping("/list")
    public TableDataInfo list(MoveLibraryVo tMoveLibrary){
        startPage();
       List<MoveLibraryVo> list = itMoveLibraryNewService.selectMoveLibraryList(tMoveLibrary);
        return getDataTable(list);
    }

    @GetMapping("/details")
    //查询移库任务详情
    public TableDataInfo details(TMoveLibrary tMoveLibrary){
        startPage();
        List<MoveLibraryDetailVo> list = itMoveLibraryNewService.selectMoveLibraryDetails(tMoveLibrary);
        return getDataTable(list);
    }

  //  @RequiresPermissions("stock:moveLibraryNew:move")
    @Log(title = "库内移位" ,businessType = BusinessType.INSERT)
    @PostMapping("/move")
    public AjaxResult move(@RequestBody TMoveLibrary tMoveLibrary ){
       if (tMoveLibrary.getTrayId() == null || tMoveLibrary.getTrayId() == 0L){
            return error("载具ID为空");
       }else if (tMoveLibrary.getLocationInId() == null || tMoveLibrary.getLocationInId() == 0L){
           return  error("转入库位ID为空");
       }else if (tMoveLibrary.getLocationOutId() == null || tMoveLibrary.getLocationOutId() == 0L){
           return error("转出库位ID为空");
       }else if (Objects.equals(tMoveLibrary.getLocationOutId(), tMoveLibrary.getLocationInId())){
           return error("转入库位不能和转出库位相同");
       }
        return itMoveLibraryNewService.moveAdd(tMoveLibrary);
    }

}
