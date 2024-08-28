package com.wellness_spinnify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wellness_spinnify.entity.WfUserListEntity;

@Repository
public interface WfSpinnifyRepository extends JpaRepository<WfUserListEntity, Integer> {

	List<WfUserListEntity> findByIsWinnersFalse();

}
