package com.engineering.littlepay.impl;

import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import com.engineering.littlepay.util.Constants;

import com.engineering.littlepay.dto.EventDto;
import com.engineering.littlepay.dto.RouteDto;
import com.engineering.littlepay.dto.TripDto;

public class TripFunctions {
	private static final DecimalFormat df = new DecimalFormat("0.00");

	public static TripDto buildTrip(EventDto startEvent, EventDto endEvent) {
		if (startEvent == null) {
			throw new IllegalArgumentException("startEvent not given! Cannot build trip!");
		}
		TripDto trip = new TripDto(startEvent);
		if (endEvent != null) {
			trip.setFinished(endEvent.getDateTimeUTC());
			trip.setToStopId(endEvent.getStopId());
			trip.setDurationSecs(TripFunctions.calculateDuration(trip));
		}
		trip.setChargeAmount(TripFunctions.constructDollarChargeAmount(trip));
		trip.setStatus(TripFunctions.calculatStatus(trip));
		return trip;
	}

	public static String constructDollarChargeAmount(TripDto trip) {
		Double amount = calculateChargeAmount(trip);
		return "$" + df.format(amount / 100);
	}

	public static Double calculateChargeAmount(TripDto trip) {
		return calculateChargeAmount(trip.getFromStopId(), trip.getToStopId());
	}

	private static Double calculateChargeAmount(String fromStopId, String toStopId) {
		if (StringUtils.isBlank(fromStopId)) {
			throw new IllegalArgumentException("fromStopId not given! Cannot calculate trip charge!");
		}
		if (StringUtils.isBlank(toStopId)) {
			// toStopId is blank/not given, so we find the most expensive route feature the
			// fromStopId
			RouteDto expensiveRoute = Constants.AVAILABLE_ROUTES.stream().filter(route -> {
				return fromStopId.equalsIgnoreCase(route.getFromStopId())
						|| fromStopId.equalsIgnoreCase(route.getToStopId());
			}).sorted((r1, r2) -> {
				return r2.getCharge().compareTo(r1.getCharge());
			}).findFirst().orElse(null);
			return expensiveRoute == null ? 0 : expensiveRoute.getCharge();
		} else if (fromStopId.equalsIgnoreCase(toStopId)) {
			// start and end locations are the same, so trip has been cancelled, return 0
			return 0d;
		} else {
			RouteDto expensiveRoute = Constants.AVAILABLE_ROUTES.stream().filter(route -> {
				return ((fromStopId.equalsIgnoreCase(route.getFromStopId())
						&& toStopId.equalsIgnoreCase(route.getToStopId()))
						|| (toStopId.equalsIgnoreCase(route.getFromStopId())
								&& fromStopId.equalsIgnoreCase(route.getToStopId())));
			}).sorted((r1, r2) -> {
				return r2.getCharge().compareTo(r1.getCharge());
			}).findFirst().orElse(null);
			return expensiveRoute == null ? 0 : expensiveRoute.getCharge();
		}
	}

	public static long calculateDuration(TripDto trip) {
		return calculateDuration(trip.getStarted(), trip.getFinished());
	}

	private static long calculateDuration(Date started, Date finished) {
		if (started == null || finished == null) {
			throw new IllegalArgumentException(
					"Started time and/or finished time not given! Cannot calculate trip duration!");
		}
		if (finished.getTime() < started.getTime()) {
			throw new IllegalArgumentException(
					"Finished time occurs before the start time! Cannot calculate trip duration!");
		}
		// Date.getTime gives us milliseconds since epoch
		// we can subtract the start from the finish to get the difference in
		// milliseconds, then divide by 1000 to get seconds
		return (finished.getTime() - started.getTime()) / 1000;
	}

	public static String calculatStatus(TripDto trip) {
		return calculatStatus(trip.getFromStopId(), trip.getToStopId());
	}

	private static String calculatStatus(String fromStopId, String toStopId) {
		if (StringUtils.isBlank(toStopId)) {
			return TripDto.STATUS_INCOMPLETE;
		}
		if (StringUtils.equalsIgnoreCase(fromStopId, toStopId)) {
			return TripDto.STATUS_CANCELLED;
		}
		return TripDto.STATUS_COMPLETED;
	}
}
