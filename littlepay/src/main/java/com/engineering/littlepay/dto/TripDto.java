package com.engineering.littlepay.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TripDto {
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_INCOMPLETE = "INCOMPLETE";
	public static final String STATUS_CANCELLED = "CANCELLED";
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	@JsonProperty(value="Started")
	private Date started;
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	@JsonProperty(value="Finished")
	private Date finished;
	
	@JsonProperty(value="DurationSecs")
	private long durationSecs;
	
	@JsonProperty(value="FromStopId")
	private String fromStopId;
	
	@JsonProperty(value="ToStopId")
	private String toStopId;
	
	@JsonProperty(value="ChargeAmount")
	private String chargeAmount;
	
	@JsonProperty(value="CompanyId")
	private String companyId;
	
	@JsonProperty(value="BusID")
	private String busId;
	
	@JsonProperty(value="PAN")
	private String pan;
	
	@JsonProperty(value="Status")
	private String status;
	
	public TripDto(EventDto fromEvent) {
		this.started = fromEvent.getDateTimeUTC();
		this.fromStopId = fromEvent.getStopId();
		this.companyId = fromEvent.getCompanyId();
		this.busId = fromEvent.getBusId();
		this.pan = fromEvent.getPan();
		this.status = STATUS_INCOMPLETE; //initialize to INCOMPLETE
	}
}
