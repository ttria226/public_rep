package com.xsrw.wms.base.domain.vo;

import com.xsrw.wms.base.domain.TPutAwayRule;
import com.xsrw.wms.base.domain.TPutAwayRuleDetail;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/6 13:55
 */
public class TPutAwayRuleVO extends TPutAwayRule {

    /**
     * 上架策略明细信息
     */
    private List<TPutAwayRuleDetail> putAwayRuleDetailList;

    public List<TPutAwayRuleDetail> getPutAwayRuleDetailList() {
        return putAwayRuleDetailList;
    }

    public void setPutAwayRuleDetailList(List<TPutAwayRuleDetail> putAwayRuleDetailList) {
        this.putAwayRuleDetailList = putAwayRuleDetailList;
    }
}
