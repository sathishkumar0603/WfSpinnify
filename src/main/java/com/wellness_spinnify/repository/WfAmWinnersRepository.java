package com.wellness_spinnify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wellness_spinnify.entity.WfAmWinnersEntity;

public interface WfAmWinnersRepository extends JpaRepository<WfAmWinnersEntity, Integer> {

	List<WfAmWinnersEntity> findByCampaignId(int maxId);

}
