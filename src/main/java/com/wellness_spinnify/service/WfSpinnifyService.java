package com.wellness_spinnify.service;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.opencsv.CSVReader;
import com.wellness_spinnify.entity.WfCampaignEntity;
import com.wellness_spinnify.entity.WfUserListEntity;
import com.wellness_spinnify.helper.WfSpinnifyHelper;
import com.wellness_spinnify.model.WfCampaignRequest;
import com.wellness_spinnify.model.WfGetAllUserListResponse;
import com.wellness_spinnify.model.WfUserListResponse;
import com.wellness_spinnify.model.WfWinnersDownloadRequest;
import com.wellness_spinnify.model.WfWinnersListResponse;
import com.wellness_spinnify.repository.WfCampaignRepository;
import com.wellness_spinnify.repository.WfSpinnifyRepository;

@Service
public class WfSpinnifyService {

	WfSpinnifyHelper helper;

	WfSpinnifyRepository repository;

	WfCampaignRepository campaignRepository;

	@Autowired
	public WfSpinnifyService(WfSpinnifyHelper helper, WfSpinnifyRepository repository,
			WfCampaignRepository campaignRepository) {
		this.helper = helper;
		this.repository = repository;
		this.campaignRepository = campaignRepository;
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

	public WfUserListResponse downloadCsv(List<WfWinnersDownloadRequest> winnersDownloadRequest) {
		WfUserListResponse listResponse = new WfUserListResponse();

		try {
			Path path = Paths.get("C:\\Users\\satish.kumar\\Downloads\\customers-100.csv");
			// path.toFile() to convert the Path to a File object
			File csvFile = path.toFile();
			CsvMapper csvMapper = new CsvMapper();
			CsvSchema csvSchema = csvMapper.schemaFor(WfWinnersDownloadRequest.class).withHeader();
			// Writing the list to the CSV file
			csvMapper.writerFor(List.class).with(csvSchema).writeValue(csvFile, winnersDownloadRequest);
			listResponse.setStatus(true);
			listResponse.setMessage("Download Successfull");
			listResponse.setData(path.toString());

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
			List<WfUserListEntity> entities = repository.findAll();
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

}
