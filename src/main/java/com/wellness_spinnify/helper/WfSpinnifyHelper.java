package com.wellness_spinnify.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.wellness_spinnify.customexception.FileDeletionException;
import com.wellness_spinnify.entity.WfCampaignEntity;
import com.wellness_spinnify.entity.WfUserListEntity;
import com.wellness_spinnify.entity.WfWinnersEntity;
import com.wellness_spinnify.model.StoreRequest;
import com.wellness_spinnify.model.WfCampaignRequest;
import com.wellness_spinnify.model.WfGetAllUserListResponse;
import com.wellness_spinnify.model.WfWinnersDownloadRequest;
import com.wellness_spinnify.repository.WfCampaignRepository;
import com.wellness_spinnify.repository.WfWinnersRepository;

@Component
public class WfSpinnifyHelper {

	WfWinnersRepository wfWinnersRepository;
	WfCampaignRepository campaignRepository;

	public WfSpinnifyHelper(WfWinnersRepository wfWinnersRepository, WfCampaignRepository campaignRepository) {
		this.wfWinnersRepository = wfWinnersRepository;
		this.campaignRepository = campaignRepository;
	}

	public WfUserListEntity convertToEntity(String[] line, Timestamp dateTime) {
		WfUserListEntity entity = new WfUserListEntity();
		entity.setUserId(line[0]);
		entity.setName(line[1]);
		entity.setUpdatedTime(dateTime);
		return entity;
	}

	public void updateTheWinners(WfUserListEntity wfUserListEntity) {
		wfUserListEntity.setWinners(true);
		WfWinnersEntity winnersEntity = new WfWinnersEntity();
		winnersEntity.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
		winnersEntity.setWinnersId(wfUserListEntity.getUserId());
		winnersEntity.setWinnersname(wfUserListEntity.getName());
		wfWinnersRepository.save(winnersEntity);
	}

	public WfCampaignEntity convertToCampaign(WfCampaignRequest campaignRequest) {
		WfCampaignEntity campaignEntity = new WfCampaignEntity();
		campaignEntity.setNoOfSpins(campaignRequest.getNoOfSpins());
		campaignEntity.setNoOfWinners(campaignRequest.getNoOfWinners());
		campaignEntity.setCeatedAt(Timestamp.valueOf(LocalDateTime.now()));
		campaignEntity.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
		return campaignEntity;
	}

	public WfGetAllUserListResponse convertToGetAllListResponse(WfUserListEntity wfUserListEntity) {
		WfGetAllUserListResponse allUserListResponse = new WfGetAllUserListResponse();
		allUserListResponse.setStoreId(wfUserListEntity.getUserId());
		allUserListResponse.setStoreName(wfUserListEntity.getName());
		return allUserListResponse;
	}

	public List<WfWinnersEntity> convertToWinnersList(String winnerKey, List<StoreRequest> stores, Timestamp dateTime,
			WfCampaignEntity campaignEntity) {
		List<WfWinnersEntity> entities = new ArrayList<>();
		for (StoreRequest storeRequest : stores) {
			WfWinnersEntity entity = new WfWinnersEntity();
			entity.setWinnersCategory(winnerKey);
			entity.setWinnersId(storeRequest.getStoreId());
			entity.setWinnersname(storeRequest.getStoreName());
			entity.setUpdatedTime(dateTime);
			entity.setCampaignId(campaignEntity.getId());
			entity.setCampaignName(campaignEntity.getCampaignName());
			entities.add(entity);
		}
		return entities;
	}

	public WfCampaignEntity convertToCampaignEntity(int noOfSpins, int noOfWinners, String campaignName,
			Timestamp dateTime) {
		WfCampaignEntity campaignEntity = new WfCampaignEntity();
		campaignEntity.setCampaignName(campaignName);
		campaignEntity.setNoOfSpins(noOfSpins);
		campaignEntity.setNoOfWinners(noOfWinners);
		campaignEntity.setCeatedAt(dateTime);
		return campaignEntity;
	}

	public String convertToCsvFile() throws IOException {

		// Path to save CSV file
		Path path = Paths.get("C:\\Users\\satish.kumar\\Downloads\\customers-100.csv");
		File csvFile = path.toFile();
		Optional<Integer> maxIdOptional = campaignRepository.findMaxId();
		int maxId = 0;
		if (maxIdOptional.isPresent()) {
			maxId = maxIdOptional.get();
		}
		List<WfWinnersEntity> wfWinnersEntity = wfWinnersRepository.findByCampaignId(maxId);

		// Convert List<WfWinnersEntity> to List<WfWinnersCsvDto>
		List<WfWinnersDownloadRequest> csvData = wfWinnersEntity.stream()
				.map(entity -> new WfWinnersDownloadRequest(entity.getWinnersId(), entity.getWinnersname(),
						entity.getWinnersCategory(), entity.getCampaignName()))
				.toList();
		if (Files.exists(csvFile.toPath())) {
			try {
				Files.delete(csvFile.toPath());
			} catch (IOException e) {
				throw new FileDeletionException("Failed to delete the file: " + csvFile.getPath(), e);
			}
		}

		// Create CSV Mapper and Schema
		CsvMapper csvMapper = new CsvMapper();
		CsvSchema csvSchema = CsvSchema.builder().addColumn("storeId").addColumn("winnersName")
				.addColumn("winnersCategory").addColumn("campaignsName").setUseHeader(true).build();

		// Write data to CSV
		csvMapper.writerFor(List.class).with(csvSchema).writeValue(csvFile, csvData);

		byte[] fileContent = Files.readAllBytes(csvFile.toPath());

		// Encode the byte array to a Base64 string
		return Base64.getEncoder().encodeToString(fileContent);

	}

}
