package com.wellness_spinnify.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.wellness_spinnify.customexception.FileDeletionException;
import com.wellness_spinnify.entity.WfAmEntity;
import com.wellness_spinnify.entity.WfAmWinnersEntity;
import com.wellness_spinnify.entity.WfCampaignEntity;
import com.wellness_spinnify.model.WfAmWinnersRequest;
import com.wellness_spinnify.model.WfGetAllAmListResponse;
import com.wellness_spinnify.model.WfWinnerAmRequest;
import com.wellness_spinnify.repository.WfAmWinnersRepository;
import com.wellness_spinnify.repository.WfCampaignRepository;

@Component
public class WfAmHelper {

	WfAmWinnersRepository amWinnersRepository;
	WfCampaignRepository campaignRepository;

	@Autowired
	public WfAmHelper(WfAmWinnersRepository amWinnersRepository, WfCampaignRepository campaignRepository) {
		this.amWinnersRepository = amWinnersRepository;
		this.campaignRepository = campaignRepository;
	}

	public WfAmEntity convertToAmEntity(String[] line, Timestamp dateTime) {
		WfAmEntity entity = new WfAmEntity();
		entity.setAmId(line[0]);
		entity.setAmName(line[1]);
		entity.setImageUrl(line[2]);
		entity.setUpdatedTime(dateTime);
		return entity;
	}

	public WfCampaignEntity convertToCampaignEntity(int noOfSpins, int noOfWinners, String campaignName,
			Timestamp dateTime) {
		WfCampaignEntity campaignEntity = new WfCampaignEntity();
		campaignEntity.setCampaignName(campaignName);
		campaignEntity.setCeatedAt(dateTime);
		campaignEntity.setNoOfSpins(noOfSpins);
		campaignEntity.setNoOfWinners(noOfWinners);
		return campaignEntity;
	}

	public WfGetAllAmListResponse convertToGetAllAmListResponse(WfAmEntity wfUserListEntity) {
		WfGetAllAmListResponse allAmListResponse = new WfGetAllAmListResponse();
		allAmListResponse.setAmId(wfUserListEntity.getAmId());
		allAmListResponse.setAmName(wfUserListEntity.getAmName());
		allAmListResponse.setImage(wfUserListEntity.getImageUrl());
		return allAmListResponse;
	}

	public List<WfAmWinnersEntity> convertToAmWinnersList(String winnerKey, List<WfWinnerAmRequest> value,
			Timestamp dateTime, WfCampaignEntity campaignEntity) {
		List<WfAmWinnersEntity> entities = new ArrayList<>();
		for (WfWinnerAmRequest wfWinnerAmRequest : value) {
			WfAmWinnersEntity entity = new WfAmWinnersEntity();
			entity.setAmWinnersCategory(winnerKey);
			entity.setAmWinnersId(wfWinnerAmRequest.getAmId());
			entity.setAmWinnersname(wfWinnerAmRequest.getAmName());
			entity.setUpdatedTime(dateTime);
			entity.setCampaignId(campaignEntity.getId());
			entity.setCampaignName(campaignEntity.getCampaignName());
			entities.add(entity);
		}
		return entities;
	}

	public String convertToAmCsvFile() throws Exception {
		// Path to save CSV file
		Path path = Paths.get("C:\\Users\\satish.kumar\\Downloads\\customers-100.csv");
		File csvFile = path.toFile();
		Optional<Integer> maxIdOptional = campaignRepository.findMaxId();
		int maxId = 0;
		if (maxIdOptional.isPresent()) {
			maxId = maxIdOptional.get();
		}
		List<WfAmWinnersEntity> wfAmWinnersEntity = amWinnersRepository.findByCampaignId(maxId);

		List<WfAmWinnersRequest> csvData = wfAmWinnersEntity.stream()
				.map(entity -> new WfAmWinnersRequest(entity.getAmWinnersId(), entity.getAmWinnersname(),
						entity.getCampaignName()))
				.toList();
		if (Files.exists(csvFile.toPath())) {
			try {
				Files.delete(csvFile.toPath());
			} catch (IOException e) {
				throw new FileDeletionException("FAILED TO DELETE THE FILE: " + csvFile.getPath(), e);
			}
		}

		// Create CSV Mapper and Schema
		CsvMapper csvMapper = new CsvMapper();
		CsvSchema csvSchema = CsvSchema.builder().addColumn("areaManagerId").addColumn("areaManagerName")
				.addColumn("campaignName").setUseHeader(true).build();

		// Write data to CSV
		csvMapper.writerFor(List.class).with(csvSchema).writeValue(csvFile, csvData);

		byte[] fileContent = Files.readAllBytes(csvFile.toPath());

		// Encode the byte array to a Base64 string
		return Base64.getEncoder().encodeToString(fileContent);
	}
}
