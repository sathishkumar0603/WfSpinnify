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
@Table(name = "WF_AM_ENTITY")
public class WfAmEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	@Column(name = "AREA_MANAGER_ID")
	private String amId;
	@Column(name = "AREA_MANAGER_NAME")
	private String amName;
	@Column(name = "IMAGE_URL")
	private String imageUrl;
	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;
}
