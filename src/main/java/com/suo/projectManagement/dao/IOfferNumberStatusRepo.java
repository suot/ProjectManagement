package com.suo.projectManagement.dao;

import com.suo.projectManagement.po.OfferNumberStatus;


public interface IOfferNumberStatusRepo {
    int add(OfferNumberStatus offerNumberStatus);
    int update(OfferNumberStatus offerNumberStatus);
    OfferNumberStatus find(int projectID, String stratificationFactor, int organizationID);
    OfferNumberStatus find(int projectID,String stratificationFactor );
    OfferNumberStatus findByGroupNumber(int projectID,String stratificationFactor,int groupNumber );
}
