package com.wellness_spinnify.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "WF_STORE_WINNERS_LIST")
public class WfWinnersEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "WINNERS_NAME")
	private String winnersname;
	@Column(name = "WINNERS_ID")
	private String winnersId;
	@Column(name = "WINNERS_CATEGORY")
	private String winnersCategory;
	@Column(name = "CAMPAIGN_ID")
	private int campaignId;
	@Column(name = "CAMPAIGN_NAME")
	private String campaignName;
	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;

}
