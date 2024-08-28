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
@Table(name = "WF_CAMPAIGN_LIST")
public class WfCampaignEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "WINNERS_NAME")
	private String winnersName;
	@Column(name = "SPIN_TIMING")
	private Timestamp spinTiming;
	@Column(name = "NO_OF_WINNERS")
	private int noOfWinners;
	@Column(name = "NO_OF_SPINS")
	private int noOfSpins;
	@Column(name = "CREATED_AT")
	private Timestamp ceatedAt;
	@Column(name = "UPDATED_AT")
	private Timestamp updatedAt;

}
