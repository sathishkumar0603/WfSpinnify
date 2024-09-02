package com.wellness_spinnify.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WfWinnersDownloadRequest {

	private String storeId;
	private String winnersName;
	private String winnersCategory;
	private String campaignsName;

}
