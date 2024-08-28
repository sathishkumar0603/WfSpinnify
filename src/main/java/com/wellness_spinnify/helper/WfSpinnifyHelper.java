package com.wellness_spinnify.helper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wellness_spinnify.entity.WfUserListEntity;
import com.wellness_spinnify.entity.WfWinnersEntity;
import com.wellness_spinnify.model.WfWinnersListResponse;
import com.wellness_spinnify.repository.WfWinnersRepository;

@Component
public class WfSpinnifyHelper {

	@Autowired
	WfWinnersRepository wfWinnersRepository;

	public WfUserListEntity convertToEntity(String[] line) {
		WfUserListEntity entity = new WfUserListEntity();
		entity.setUserId(line[0]);
		entity.setName(line[1]);
		return entity;
	}

	public WfWinnersListResponse convertToWinnersResponse() {
		return new WfWinnersListResponse();
	}

	public void updateTheWinners(WfUserListEntity wfUserListEntity) {
		wfUserListEntity.setWinners(true);
		WfWinnersEntity winnersEntity = new WfWinnersEntity();
		winnersEntity.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
		winnersEntity.setWinnersId(wfUserListEntity.getUserId());
		winnersEntity.setWinnersname(wfUserListEntity.getName());
		wfWinnersRepository.save(winnersEntity);
	}

}
