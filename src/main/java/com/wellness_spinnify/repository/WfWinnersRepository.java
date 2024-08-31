package com.wellness_spinnify.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wellness_spinnify.entity.WfWinnersEntity;

public interface WfWinnersRepository extends JpaRepository<WfWinnersEntity, Integer> {
	List<WfWinnersEntity> findByUpdatedTime(Timestamp uploadTime);

	@Query("SELECT MAX(p.updatedTime) FROM WfWinnersEntity p")
	Timestamp findLatestUpdatedTime();
}
