package com.wellness_spinnify.helper;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.wellness_spinnify.entity.WfAmEntity;
import com.wellness_spinnify.entity.WfCampaignEntity;
import com.wellness_spinnify.entity.WfUserListEntity;
import com.wellness_spinnify.model.WfGetAllAmListResponse;

@Component
public class WfAmHelper {

	public WfAmEntity convertToAmEntity(String[] line, Timestamp dateTime) {
		WfAmEntity entity = new WfAmEntity();
		entity.setAmId(line[0]);
		entity.setAmName(line[1]);
		entity.setImageUrl(line[2]);
		entity.setUpdatedTime(dateTime);
		return entity;
	}

	public WfCampaignEntity convertToCampaignEntity(int noOfSpins, int noOfWinners, String campaignName,
			Timestamp dateTime) {
		WfCampaignEntity campaignEntity = new WfCampaignEntity();
		campaignEntity.setCampaignName(campaignName);
		campaignEntity.setCeatedAt(dateTime);
		campaignEntity.setNoOfSpins(noOfSpins);
		campaignEntity.setNoOfWinners(noOfWinners);
		return campaignEntity;
	}

	public WfGetAllAmListResponse convertToGetAllAmListResponse(WfAmEntity wfUserListEntity) {
		WfGetAllAmListResponse allAmListResponse = new WfGetAllAmListResponse();
		allAmListResponse.setAmId(wfUserListEntity.getAmId());
		allAmListResponse.setAmName(wfUserListEntity.getAmName());
		allAmListResponse.setImage(wfUserListEntity.getImageUrl());
		return allAmListResponse;
	}

}
