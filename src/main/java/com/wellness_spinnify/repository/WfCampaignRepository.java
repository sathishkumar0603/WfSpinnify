package com.wellness_spinnify.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wellness_spinnify.entity.WfCampaignEntity;

@Repository
public interface WfCampaignRepository extends JpaRepository<WfCampaignEntity, Integer> {

	@Query("SELECT MAX(c.id) FROM WfCampaignEntity c")
	Optional<Integer> findMaxId();

	WfCampaignEntity findById(int id);

}
