package com.wellness_spinnify.service;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.wellness_spinnify.entity.WfAmEntity;
import com.wellness_spinnify.entity.WfAmWinnersEntity;
import com.wellness_spinnify.entity.WfCampaignEntity;
import com.wellness_spinnify.helper.WfAmHelper;
import com.wellness_spinnify.model.WfGetAllAmListResponse;
import com.wellness_spinnify.model.WfUserListResponse;
import com.wellness_spinnify.model.WfWinnerAmRequest;
import com.wellness_spinnify.repository.WfAmListRepository;
import com.wellness_spinnify.repository.WfAmRepository;
import com.wellness_spinnify.repository.WfAmWinnersRepository;
import com.wellness_spinnify.repository.WfCampaignRepository;

@Service
public class WfSpinnifyAmService {

	WfAmHelper amHelper;
	WfAmRepository wfAmRepository;
	WfCampaignRepository campaignRepository;
	WfAmListRepository amListRepository;
	WfAmWinnersRepository wfAmWinnersRepository;

	@Autowired
	public WfSpinnifyAmService(WfAmHelper amHelper, WfAmRepository wfAmRepository,
			WfCampaignRepository campaignRepository, WfAmListRepository amListRepository,
			WfAmWinnersRepository wfAmWinnersRepository) {
		this.amHelper = amHelper;
		this.wfAmRepository = wfAmRepository;
		this.campaignRepository = campaignRepository;
		this.amListRepository = amListRepository;
		this.wfAmWinnersRepository = wfAmWinnersRepository;
	}

	public Timestamp dateAndTime() {
		LocalDateTime dateTimes = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return Timestamp.valueOf(dateTimes.format(myFormatObj));
	}

	public WfUserListResponse extraxtAreaData(MultipartFile csvFile, int noOfSpins, int noOfWinners,
			String campaignName) {
		WfUserListResponse listResponse = new WfUserListResponse();
		Timestamp dateTime = dateAndTime();
		try (CSVReader reader = new CSVReader(new InputStreamReader(csvFile.getInputStream()))) {
			String[] line = reader.readNext();
			while ((line = reader.readNext()) != null) {
				WfAmEntity userListEntity = amHelper.convertToAmEntity(line, dateTime);
				wfAmRepository.save(userListEntity);
			}
			WfCampaignEntity campaignEntity = amHelper.convertToCampaignEntity(noOfSpins, noOfWinners, campaignName,
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

	public List<WfGetAllAmListResponse> getAllAmList() {
		List<WfGetAllAmListResponse> allAmListResponses = new ArrayList<>();
		try {
			Timestamp latestUploadTime = wfAmRepository.findLatestUpdatedTime();
			List<WfAmEntity> entities = wfAmRepository.findByUpdatedTime(latestUploadTime);
			if (!entities.isEmpty()) {
				for (WfAmEntity wfUserListEntity : entities) {
					WfGetAllAmListResponse allUserListResponse = amHelper
							.convertToGetAllAmListResponse(wfUserListEntity);
					allAmListResponses.add(allUserListResponse);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allAmListResponses;
	}

	public WfUserListResponse saveAmWinnners(Map<String, List<WfWinnerAmRequest>> wfWinnersAmRequest) {
		WfUserListResponse listResponse = new WfUserListResponse();
		try {
			List<WfAmWinnersEntity> allWinners = new ArrayList<>();
			Timestamp dateTime = dateAndTime();
			Optional<Integer> maxIdOptional = campaignRepository.findMaxId();
			int maxId = 0;
			if (maxIdOptional.isPresent()) {
				maxId = maxIdOptional.get();
			}
			WfCampaignEntity campaignEntity = campaignRepository.findById(maxId);
			for (Map.Entry<String, List<WfWinnerAmRequest>> entry : wfWinnersAmRequest.entrySet()) {
				String winnerKey = entry.getKey();
				List<WfAmWinnersEntity> winnersEntities = amHelper.convertToAmWinnersList(winnerKey, entry.getValue(),
						dateTime, campaignEntity);
				allWinners.addAll(winnersEntities);
				wfAmWinnersRepository.saveAll(allWinners);
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

	public WfUserListResponse downloadAmCsv() {
		WfUserListResponse listResponse = new WfUserListResponse();
		try {

			String base64String = amHelper.convertToAmCsvFile();
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

}
