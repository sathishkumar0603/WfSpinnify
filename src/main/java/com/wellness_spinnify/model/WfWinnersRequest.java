package com.wellness_spinnify.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class WfWinnersRequest {

	private Map<String, List<StoreRequest>> winners;

}
