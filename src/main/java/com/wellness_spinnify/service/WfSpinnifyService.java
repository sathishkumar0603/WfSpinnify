package com.wellness_spinnify.service;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.wellness_spinnify.entity.WfUserListEntity;
import com.wellness_spinnify.helper.WfSpinnifyHelper;
import com.wellness_spinnify.model.WfCampaignRequest;
import com.wellness_spinnify.model.WfUserListResponse;
import com.wellness_spinnify.model.WfWinnersListResponse;
import com.wellness_spinnify.repository.WfSpinnifyRepository;

@Service
public class WfSpinnifyService {

	WfSpinnifyHelper helper;

	WfSpinnifyRepository repository;

	@Autowired
	public WfSpinnifyService(WfSpinnifyHelper helper, WfSpinnifyRepository repository) {
		this.helper = helper;
		this.repository = repository;
	}

	public WfUserListResponse extraxtData(MultipartFile csvFile) {
		WfUserListResponse listResponse = new WfUserListResponse();
		try {
			try (CSVReader reader = new CSVReader(new InputStreamReader(csvFile.getInputStream()))) {
				String[] line = reader.readNext();
				while ((line = reader.readNext()) != null) {
					WfUserListEntity userListEntity = helper.convertToEntity(line);
					repository.save(userListEntity);
				}
				listResponse.setStatus(true);
				listResponse.setMessage("FILE UPLOADED SUCCESSFULLY");
			} catch (Exception e) {
				e.printStackTrace();
				listResponse.setStatus(false);
				listResponse.setMessage("File upload failed: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listResponse;
	}

	public WfWinnersListResponse getWinners() {
		WfWinnersListResponse listResponse = new WfWinnersListResponse();
		try {
			List<WfUserListEntity> elegibleUsers = repository.findByIsWinnersFalse();
			Collections.shuffle(elegibleUsers);
			List<WfUserListEntity> selectedWinners = elegibleUsers.stream().limit(5).collect(Collectors.toList());
			for (WfUserListEntity wfUserListEntity : selectedWinners) {
				helper.updateTheWinners(wfUserListEntity);
				repository.save(wfUserListEntity);
			}
			listResponse.setStatus(true);
			listResponse.setMessage("WINNERS LIST GOT SUCCESSFULLY");
			listResponse.setWinners(selectedWinners);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listResponse;
	}

	public WfUserListResponse saveCampaign(WfCampaignRequest campaignRequest) {
		WfUserListResponse listResponse = new WfUserListResponse();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listResponse;
	}

}
