package com.wellness_spinnify.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WfWinnersDownloadRequest {

	private String winnersId;
	private String winnersName;
	private String winnersCategory;

}
