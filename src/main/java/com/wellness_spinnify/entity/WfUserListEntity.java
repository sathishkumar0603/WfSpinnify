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
@Table(name = "WF_USERLIST")
public class WfUserListEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;
	@Column(name = "IS_WINNERS")
	private boolean isWinners;
}
