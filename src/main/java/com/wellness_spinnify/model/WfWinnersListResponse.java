package com.wellness_spinnify.model;

import java.util.List;

import com.wellness_spinnify.entity.WfUserListEntity;

import lombok.Data;

@Data
public class WfWinnersListResponse {
	private boolean status;
	private String message;
	private List<WfUserListEntity> winners;
}
