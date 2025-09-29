package com.xsrw.wms.stock.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.vo.MoveLibraryDetailVo;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;

public interface ITMoveLibraryNewService extends IService<TMoveLibrary> {

    public List<MoveLibraryVo> selectMoveLibraryList(MoveLibraryVo tMoveLibrary);

    public List<TLocationVO>selectTLocationEmptyList(TLocationDTO tLocationDTO);

    public List<MoveLibraryDetailVo> selectMoveLibraryDetails(TMoveLibrary tMoveLibrary) ;

    AjaxResult moveAdd(TMoveLibrary tMoveLibrary);

    public void selectReissueMove(TTaskWcsVO taskWcsVO);
}
