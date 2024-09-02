package com.wellness_spinnify.service;

import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.wellness_spinnify.entity.WfAmEntity;
import com.wellness_spinnify.entity.WfCampaignEntity;
import com.wellness_spinnify.entity.WfUserListEntity;
import com.wellness_spinnify.helper.WfAmHelper;
import com.wellness_spinnify.model.WfUserListResponse;
import com.wellness_spinnify.repository.WfAmRepository;
import com.wellness_spinnify.repository.WfCampaignRepository;

@Service
public class WfSpinnifyAmService {

	@Autowired
	WfAmHelper amHelper;

	@Autowired
	WfAmRepository wfAmRepository;

	@Autowired
	WfCampaignRepository campaignRepository;

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

}
