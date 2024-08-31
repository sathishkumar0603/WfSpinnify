package com.wellness_spinnify.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wellness_spinnify.entity.WfUserListEntity;

@Repository
public interface WfSpinnifyRepository extends JpaRepository<WfUserListEntity, Integer> {

	List<WfUserListEntity> findByUpdatedTime(Timestamp uploadTime);

	@Query("SELECT MAX(p.updatedTime) FROM WfUserListEntity p")
	Timestamp findLatestUpdatedTime();

	List<WfUserListEntity> findByIsWinnersFalse();

}
