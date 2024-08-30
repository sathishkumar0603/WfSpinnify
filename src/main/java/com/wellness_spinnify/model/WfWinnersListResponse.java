package com.wellness_spinnify.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wellness_spinnify.entity.WfUserListEntity;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WfWinnersListResponse {
	private boolean status;
	private String message;
	private List<WfUserListEntity> winners;
}
