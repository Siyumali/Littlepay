package com.engineering.littlepay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RouteDto {
	public static final String STOP_1 = "Stop1";
	public static final String STOP_2 = "Stop2";
	public static final String STOP_3 = "Stop3";
	
	private String fromStopId;
	private String toStopId;
	private Double charge; 
	
	public RouteDto(String fromStopId, String toStopId, double charge) {
		this.fromStopId = fromStopId;
		this.toStopId = toStopId;
		this.charge = charge;
	}
}
