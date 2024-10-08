package com.wellness_spinnify.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wellness_spinnify.model.StoreRequest;
import com.wellness_spinnify.model.WfCampaignRequest;
import com.wellness_spinnify.model.WfGetAllUserListResponse;
import com.wellness_spinnify.model.WfUserListResponse;
import com.wellness_spinnify.model.WfWinnersListResponse;
import com.wellness_spinnify.service.WfSpinnifyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/Spinnify")
@CrossOrigin
public class WfSinnifyStoreController {

	WfSpinnifyService spinnifyService;

	@Autowired
	public WfSinnifyStoreController(WfSpinnifyService spinnifyService) {
		this.spinnifyService = spinnifyService;
	}

	@PostMapping("/upload")
	public ResponseEntity<WfUserListResponse> uploadFile(@RequestParam("csvFile") MultipartFile csvFile,
			@RequestParam("noOfSpins") int noOfSpins, @RequestParam("noOfWinners") int noOfWinners,
			@RequestParam("campaignName") String campaignName) {
		WfUserListResponse listResponse = null;
		try {
			listResponse = spinnifyService.extraxtData(csvFile, noOfSpins, noOfWinners, campaignName);
			if (listResponse.isStatus()) {
				return new ResponseEntity<>(listResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(listResponse, HttpStatus.NOT_IMPLEMENTED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/winnersList")
	public ResponseEntity<WfWinnersListResponse> getWinnersList() {
		WfWinnersListResponse winnersListResponse = null;
		try {
			winnersListResponse = spinnifyService.getWinners();
			if (winnersListResponse != null) {
				return new ResponseEntity<>(winnersListResponse, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(winnersListResponse, HttpStatus.NOT_FOUND);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/storeCampaign")
	public ResponseEntity<WfUserListResponse> campaign(@RequestBody WfCampaignRequest campaignRequest) {
		WfUserListResponse listResponse = null;
		try {
			listResponse = spinnifyService.saveCampaign(campaignRequest);
			if (listResponse != null) {
				return new ResponseEntity<>(listResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(listResponse, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/downloadCsv")
	public ResponseEntity<WfUserListResponse> download() {
		WfUserListResponse listResponse = null;
		try {
			listResponse = spinnifyService.downloadCsv();
			if (listResponse.isStatus()) {
				return new ResponseEntity<>(listResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(listResponse, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getAllList")
	public ResponseEntity<List<WfGetAllUserListResponse>> getAllList() {
		List<WfGetAllUserListResponse> allUserListResponse = null;
		try {
			allUserListResponse = spinnifyService.getAll();
			if (!allUserListResponse.isEmpty()) {
				return new ResponseEntity<>(allUserListResponse, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/saveWinners")
	public ResponseEntity<WfUserListResponse> storeWinners(
			@RequestBody Map<String, List<StoreRequest>> wfWinnersRequest) {
		WfUserListResponse listResponse = null;
		try {
			listResponse = spinnifyService.winnners(wfWinnersRequest);
			if (listResponse.isStatus()) {
				return new ResponseEntity<>(listResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(listResponse, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
