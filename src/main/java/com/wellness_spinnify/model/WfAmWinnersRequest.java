package com.wellness_spinnify.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WfAmWinnersRequest {
	private String areaManagerId;
	private String areaManagerName;
	private String campaignName;
}
