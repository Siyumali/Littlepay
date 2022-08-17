package com.engineering.littlepay.util;

import java.util.HashSet;
import java.util.Set;

import com.engineering.littlepay.dto.RouteDto;

public class Constants {	
	// Initialize the available routes with their cost
		public static final Set<RouteDto> AVAILABLE_ROUTES;
		static {
			Set<RouteDto> rtSet = new HashSet<RouteDto>();
			rtSet.add(new RouteDto(RouteDto.STOP_1, RouteDto.STOP_2, 325d));
			rtSet.add(new RouteDto(RouteDto.STOP_2, RouteDto.STOP_3, 550d));
			rtSet.add(new RouteDto(RouteDto.STOP_1, RouteDto.STOP_3, 730d));
			AVAILABLE_ROUTES = rtSet;
		}
		//these are only used for the test cases really
		public static final String COMPANY_ID = "Company1";
		public static final String BUS_ID = "Bus37";
		
		//sample PANS for test cases
		public static final String PAN_AMEX = "34343434343434";
		public static final String PAN_DINERS_1 = "36700102000000";
		public static final String PAN_DINERS_2 = "36148900647913";
		public static final String PAN_MASTERCARD_1 = "5555555555554444";
		public static final String PAN_MASTERCARD_2 = "5454545454545454";
		public static final String PAN_VISA_1 = "4444333322221111";
		public static final String PAN_VISA_2 = "4911830000000";
		public static final String PAN_VISA_3 = "4917610000000000";
}
