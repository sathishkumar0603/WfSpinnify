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
@Table(name = "WF_AM_WINNERS_ENTITY")
public class WfAmWinnersEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "AM_WINNERS_NAME")
	private String amWinnersname;
	@Column(name = "AM_WINNERS_ID")
	private String amWinnersId;
	@Column(name = "AM_WINNERS_CATEGORY")
	private String amWinnersCategory;
	@Column(name = "CAMPAIGN_ID")
	private int campaignId;
	@Column(name = "CAMPAIGN_NAME")
	private String campaignName;
	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;
}
