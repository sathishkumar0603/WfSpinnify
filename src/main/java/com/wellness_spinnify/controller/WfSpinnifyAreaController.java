package com.wellness_spinnify.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wellness_spinnify.model.WfGetAllAmListResponse;
import com.wellness_spinnify.model.WfGetAllUserListResponse;
import com.wellness_spinnify.model.WfUserListResponse;
import com.wellness_spinnify.service.WfSpinnifyAmService;

@RestController
@RequestMapping("/areaSpinnify")
public class WfSpinnifyAreaController {

	@Autowired
	WfSpinnifyAmService spinnifyAmService;

	@PostMapping("/areaUpload")
	public ResponseEntity<WfUserListResponse> uploadFile(@RequestParam("csvFile") MultipartFile csvFile,
			@RequestParam("noOfSpins") int noOfSpins, @RequestParam("noOfWinners") int noOfWinners,
			@RequestParam("campaignName") String campaignName) {
		WfUserListResponse listResponse = null;
		try {
			listResponse = spinnifyAmService.extraxtAreaData(csvFile, noOfSpins, noOfWinners, campaignName);
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
	
	@GetMapping("/getAllList")
	public ResponseEntity<List<WfGetAllAmListResponse>> getAllList() {
		List<WfGetAllAmListResponse> allAmListResponses = null;
		try {
			allAmListResponses = spinnifyAmService.getAllAmList();
			if (!allAmListResponses.isEmpty()) {
				return new ResponseEntity<>(allAmListResponses, HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
