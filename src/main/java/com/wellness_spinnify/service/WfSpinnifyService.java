package com.wellness_spinnify.service;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.wellness_spinnify.entity.WfCampaignEntity;
import com.wellness_spinnify.entity.WfUserListEntity;
import com.wellness_spinnify.entity.WfWinnersEntity;
import com.wellness_spinnify.helper.WfSpinnifyHelper;
import com.wellness_spinnify.model.StoreRequest;
import com.wellness_spinnify.model.WfCampaignRequest;
import com.wellness_spinnify.model.WfGetAllUserListResponse;
import com.wellness_spinnify.model.WfUserListResponse;
import com.wellness_spinnify.model.WfWinnersListResponse;
import com.wellness_spinnify.repository.WfCampaignRepository;
import com.wellness_spinnify.repository.WfSpinnifyRepository;
import com.wellness_spinnify.repository.WfWinnersRepository;

import jakarta.transaction.Transactional;

@Service
public class WfSpinnifyService {

	WfSpinnifyHelper helper;

	WfSpinnifyRepository repository;

	WfCampaignRepository campaignRepository;

	WfWinnersRepository winnersRepository;

	@Autowired
	public WfSpinnifyService(WfSpinnifyHelper helper, WfSpinnifyRepository repository,
			WfCampaignRepository campaignRepository, WfWinnersRepository winnersRepository) {
		this.helper = helper;
		this.repository = repository;
		this.campaignRepository = campaignRepository;
		this.winnersRepository = winnersRepository;
	}

	public Timestamp dateAndTime() {
		LocalDateTime dateTimes = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return Timestamp.valueOf(dateTimes.format(myFormatObj));
	}

	public WfUserListResponse extraxtData(MultipartFile csvFile, int noOfSpins, int noOfWinners, String campaignName) {
		WfUserListResponse listResponse = new WfUserListResponse();
		Timestamp dateTime = dateAndTime();
		try (CSVReader reader = new CSVReader(new InputStreamReader(csvFile.getInputStream()))) {
			String[] line = reader.readNext();
			while ((line = reader.readNext()) != null) {
				WfUserListEntity userListEntity = helper.convertToEntity(line, dateTime);
				repository.save(userListEntity);
			}
			WfCampaignEntity campaignEntity = helper.convertToCampaignEntity(noOfSpins, noOfWinners, campaignName,
					dateTime);
			campaignRepository.save(campaignEntity);
			listResponse.setStatus(true);
			listResponse.setMessage("FILE UPLOADED SUCCESSFULLY");
		} catch (Exception e) {
			e.printStackTrace();
			listResponse.setStatus(false);
			listResponse.setMessage("File upload failed: " + e.getMessage());
		}
		return listResponse;
	}

	public WfWinnersListResponse getWinners() {
		WfWinnersListResponse listResponse = new WfWinnersListResponse();
		try {
			List<WfUserListEntity> elegibleUsers = repository.findByIsWinnersFalse();
			Collections.shuffle(elegibleUsers);
			List<WfUserListEntity> selectedWinners = elegibleUsers.stream().limit(5).toList();
			if (!selectedWinners.isEmpty()) {
				for (WfUserListEntity wfUserListEntity : selectedWinners) {
					helper.updateTheWinners(wfUserListEntity);
					repository.save(wfUserListEntity);
				}
				listResponse.setStatus(true);
				listResponse.setMessage("WINNERS LIST GOT SUCCESSFULLY");
				listResponse.setWinners(selectedWinners);
			} else {
				listResponse.setStatus(false);
				listResponse.setMessage("WINNERS LIST IS NOT AVAILABLE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listResponse;
	}

	public WfUserListResponse saveCampaign(WfCampaignRequest campaignRequest) {
		WfUserListResponse listResponse = new WfUserListResponse();
		try {
			WfCampaignEntity campaignEntity = helper.convertToCampaign(campaignRequest);
			campaignRepository.save(campaignEntity);
			listResponse.setStatus(true);
			listResponse.setMessage("WINNERS CAMPAIGN SAVED SUCCESSFULLY");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listResponse;
	}

	public WfUserListResponse downloadCsv() {
		WfUserListResponse listResponse = new WfUserListResponse();
		try {

			String base64String = helper.convertToCsvFile();
			if (!base64String.isEmpty()) {
				listResponse.setStatus(true);
				listResponse.setMessage("Download Successful");
				listResponse.setData(base64String);
			} else {
				listResponse.setStatus(false);
				listResponse.setMessage("Download Failed");
				listResponse.setData(base64String);
			}

		} catch (Exception e) {
			e.printStackTrace();
			listResponse.setStatus(false);
			listResponse.setMessage("Download Failed: " + e.getMessage());
		}
		return listResponse;
	}

	public List<WfGetAllUserListResponse> getAll() {
		List<WfGetAllUserListResponse> allUserListResponses = new ArrayList<>();
		try {
			Timestamp latestUploadTime = repository.findLatestUpdatedTime();
			List<WfUserListEntity> entities = repository.findByUpdatedTime(latestUploadTime);
			if (!entities.isEmpty()) {
				for (WfUserListEntity wfUserListEntity : entities) {
					WfGetAllUserListResponse allUserListResponse = helper.convertToGetAllListResponse(wfUserListEntity);
					allUserListResponses.add(allUserListResponse);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allUserListResponses;
	}

	@Transactional
	public WfUserListResponse winnners(Map<String, List<StoreRequest>> wfWinnersRequest) {
		WfUserListResponse listResponse = new WfUserListResponse();
		try {
			List<WfWinnersEntity> allWinners = new ArrayList<>();
			Timestamp dateTime = dateAndTime();
			Optional<Integer> maxIdOptional = campaignRepository.findMaxId();
			int maxId = 0;
			if (maxIdOptional.isPresent()) {
				maxId = maxIdOptional.get();
			}
			WfCampaignEntity campaignEntity = campaignRepository.findById(maxId);
			for (Map.Entry<String, List<StoreRequest>> entry : wfWinnersRequest.entrySet()) {
				String winnerKey = entry.getKey();
				List<WfWinnersEntity> winnersEntities = helper.convertToWinnersList(winnerKey, entry.getValue(),
						dateTime, campaignEntity);
				allWinners.addAll(winnersEntities);
				winnersRepository.saveAll(allWinners);
			}
			if (campaignEntity.getSpinStartsAt() == null) {
				campaignEntity.setSpinStartsAt(dateTime);
				campaignRepository.save(campaignEntity);
			}
			listResponse.setStatus(true);
			listResponse.setMessage("Winners saved successfully");
		} catch (Exception e) {
			e.printStackTrace();
			listResponse.setStatus(false);
			listResponse.setMessage("Failed to save winners.");
		}
		return listResponse;
	}

}
