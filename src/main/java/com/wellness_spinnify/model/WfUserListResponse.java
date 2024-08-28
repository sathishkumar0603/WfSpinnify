package com.wellness_spinnify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class WfUserListResponse {
	private boolean status;
	private String message;
	private String data;
}
