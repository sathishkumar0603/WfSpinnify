package com.wellness_spinnify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wellness_spinnify.entity.WfCampaignEntity;

@Repository
public interface WfCampaignRepository extends JpaRepository<WfCampaignEntity, Integer> {

}
