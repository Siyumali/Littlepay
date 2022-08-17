package com.engineering.littlepay.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
	public static final String TAP_TYPE_ON = "ON";
	public static final String TAP_TYPE_OFF = "OFF";
	
	private String id;
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private Date dateTimeUTC;
	private String tapType;
	private String stopId;
	private String companyId;
	private String busId;
	private String pan;
}
