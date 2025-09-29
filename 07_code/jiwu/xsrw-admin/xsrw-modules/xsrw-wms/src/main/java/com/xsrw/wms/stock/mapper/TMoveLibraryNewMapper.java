package com.xsrw.wms.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.vo.MoveLibraryDetailVo;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库内移位新Mapper接口
 *
 */
public interface TMoveLibraryNewMapper extends BaseMapper<TMoveLibrary> {

    public List<MoveLibraryVo> selectMoveLibraryList(@Param("tml")MoveLibraryVo tMoveLibrary);

    public List<TLocationVO> selectTLocationEmptyList(@Param("tld")TLocationDTO tLocationDTO);

    public List<MoveLibraryDetailVo> selectMoveLibraryDetails(@Param("smld")TMoveLibrary tMoveLibrary);

    /*获取载具上物料信息*/
    public List<TStock> selectTStock(@Param("sts")TMoveLibrary  tMoveLibrary);

    /*获取载具状态*/
    public TTray selecttTray (@Param("stt")TMoveLibrary tMoveLibrary);

    /*获取载具库位状态*/
    public TLocation selecttLocation(@Param("locationId") Long locationId);

    /*获取未完成的wcs任务列表*/
    public Long selecttTaskWcs();

    /*根据一身位查询二身位库位信息*/
    public TLocation selecttLocationTwo(@Param("twv")TTaskWcsVO tTaskWcsVO);
    /*根据二身位库位信息查询是否有移库任务*/
    public TTaskWcsVO selectTaskWcsOut(@Param("tl")TLocation tLocation);
    /*根据二身位库位信息查询二身位出库任务是否有移库任务*/
    public TTaskWcsVO selectTaskWcsMove(@Param("ttwv")TTaskWcsVO tTaskWcsVO);
}
