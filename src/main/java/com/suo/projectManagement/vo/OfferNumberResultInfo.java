package com.suo.projectManagement.vo;

import com.suo.projectManagement.po.InvestiGroup;
import com.suo.projectManagement.po.OfferNumberResult;

/**
 * Created by TianSuo on 2018-07-20.
 */
public class OfferNumberResultInfo {
    private OfferNumberResult offerNumberResult;
    private InvestiGroup investiGroup;

    public OfferNumberResultInfo(OfferNumberResult offerNumberResult, InvestiGroup investiGroup) {
        this.offerNumberResult = offerNumberResult;
        this.investiGroup = investiGroup;
    }

    public OfferNumberResult getOfferNumberResult() {
        return offerNumberResult;
    }

    public void setOfferNumberResult(OfferNumberResult offerNumberResult) {
        this.offerNumberResult = offerNumberResult;
    }

    public InvestiGroup getInvestiGroup() {
        return investiGroup;
    }

    public void setInvestiGroup(InvestiGroup investiGroup) {
        this.investiGroup = investiGroup;
    }


    @Override
    public String toString() {
        return "OfferNumberResultInfo{" +
                "offerNumberResult=" + offerNumberResult +
                ", investiGroup=" + investiGroup +
                '}';
    }
}
