package com.wellness_spinnify.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wellness_spinnify.entity.WfAmEntity;

public interface WfAmRepository extends JpaRepository<WfAmEntity, Integer> {

	List<WfAmEntity> findByUpdatedTime(Timestamp uploadTime);

	@Query("SELECT MAX(p.updatedTime) FROM WfAmEntity p")
	Timestamp findLatestUpdatedTime();

}
