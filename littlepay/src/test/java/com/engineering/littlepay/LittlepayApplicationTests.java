package com.engineering.littlepay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.commons.lang3.StringUtils;

import com.engineering.littlepay.dto.EventDto;
import com.engineering.littlepay.dto.RouteDto;
import com.engineering.littlepay.dto.TripDto;
import com.engineering.littlepay.impl.EventFunctions;
import com.engineering.littlepay.impl.TripFunctions;
import com.engineering.littlepay.util.Constants;

@SpringBootTest
class LittlepayApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testRouteCharges() {
		// validly built trips with from and (optional) to stops
		assert (testValidRouteCharge(RouteDto.STOP_1, null, "$7.30"));
		assert (testValidRouteCharge(RouteDto.STOP_1, RouteDto.STOP_1, "$0.00"));
		assert (testValidRouteCharge(RouteDto.STOP_1, RouteDto.STOP_2, "$3.25"));
		assert (testValidRouteCharge(RouteDto.STOP_1, RouteDto.STOP_3, "$7.30"));
		assert (testValidRouteCharge(RouteDto.STOP_2, null, "$5.50"));
		assert (testValidRouteCharge(RouteDto.STOP_2, RouteDto.STOP_1, "$3.25"));
		assert (testValidRouteCharge(RouteDto.STOP_2, RouteDto.STOP_2, "$0.00"));
		assert (testValidRouteCharge(RouteDto.STOP_2, RouteDto.STOP_3, "$5.50"));
		assert (testValidRouteCharge(RouteDto.STOP_3, null, "$7.30"));
		assert (testValidRouteCharge(RouteDto.STOP_3, RouteDto.STOP_1, "$7.30"));
		assert (testValidRouteCharge(RouteDto.STOP_3, RouteDto.STOP_2, "$5.50"));
		assert (testValidRouteCharge(RouteDto.STOP_3, RouteDto.STOP_3, "$0.00"));
		// invalidly built trips, with null from route
		try {
			testInvalidRouteCharge(null, RouteDto.STOP_1);
			// if the construction goes off without a hitch, then something is wrong
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}
		try {
			testInvalidRouteCharge(null, RouteDto.STOP_2);
			// if the construction goes off without a hitch, then something is wrong
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}
		try {
			testInvalidRouteCharge(null, RouteDto.STOP_3);
			// if the construction goes off without a hitch, then something is wrong
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}
		try {
			testInvalidRouteCharge(null, null);
			// if the construction goes off without a hitch, then something is wrong
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}

	}

	private boolean testValidRouteCharge(String fromStop, String toStop, String correctCharge) {
		TripDto trip = new TripDto();
		trip.setFromStopId(fromStop);
		trip.setToStopId(toStop);
		return correctCharge.equalsIgnoreCase(TripFunctions.constructDollarChargeAmount(trip));
	}

	private void testInvalidRouteCharge(String fromStop, String toStop) throws IllegalArgumentException {
		TripDto trip = new TripDto();
		trip.setFromStopId(fromStop);
		trip.setToStopId(toStop);
		TripFunctions.constructDollarChargeAmount(trip);
	}

	@Test
	public void testDurationCalculation() {
		Date startDate;
		Date finishDate;
		// validly built trips with start and finished times
		Calendar startCalendar = Calendar.getInstance();
		startDate = startCalendar.getTime();
		Calendar finishCalendar = Calendar.getInstance();
		// initialise the start calendar to an identical time to the start calendar
		finishCalendar.setTime(startDate);

		finishCalendar.add(Calendar.SECOND, 10);
		finishDate = finishCalendar.getTime();
		// finish calendar is 10s into the future
		assert (testValidDuration(startDate, finishDate, 10));

		// add another minute to the end calendar
		finishCalendar.add(Calendar.MINUTE, 1);
		finishDate = finishCalendar.getTime();
		// finish calendar is now 70s into the future
		assert (testValidDuration(startDate, finishDate, 70));

		// add another hour to the end calendar
		finishCalendar.add(Calendar.HOUR, 1);
		finishDate = finishCalendar.getTime();
		// finish calendar is now 3670s into the future
		assert (testValidDuration(startDate, finishDate, 3670));

		// now add 420 minutes to the end calendar
		finishCalendar.add(Calendar.MINUTE, 420);
		finishDate = finishCalendar.getTime();
		// finish calendar is now 28870s into the future
		assert (testValidDuration(startDate, finishDate, 28870));

		// now add 20 days to the end calendar
		finishCalendar.add(Calendar.DAY_OF_MONTH, 20);
		finishDate = finishCalendar.getTime();
		// finish calendar is now 1756870s into the future
		assert (testValidDuration(startDate, finishDate, 1756870));

		// invalidly built trips, with missing start and/or finish times
		try {
			// if the calculation goes off without a hitch, then something is wrong
			testInvalidDuration(Calendar.getInstance().getTime(), null);
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}
		try {
			// if the calculation goes off without a hitch, then something is wrong
			testInvalidDuration(null, Calendar.getInstance().getTime());
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}
		try {
			// if the calculation goes off without a hitch, then something is wrong
			testInvalidDuration(null, null);
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}

		// invalidly built trips, with a finish time before the start time
		try {
			Calendar cal = Calendar.getInstance();
			startDate = cal.getTime();
			cal.add(Calendar.MINUTE, -20);
			finishDate = cal.getTime();
			// if the calculation goes off without a hitch, then something is wrong
			testInvalidDuration(startDate, finishDate);
			assert (false);
		} catch (IllegalArgumentException e) {
			// we're expecting an IllegalArgumentException here
			assert (true);
		}
	}

	private boolean testValidDuration(Date startTime, Date finishTime, long correctDuration) {
		TripDto trip = new TripDto();
		trip.setStarted(startTime);
		trip.setFinished(finishTime);
		return correctDuration == TripFunctions.calculateDuration(trip);
	}

	private void testInvalidDuration(Date startTime, Date finishTime) throws IllegalArgumentException {
		TripDto trip = new TripDto();
		trip.setStarted(startTime);
		trip.setFinished(finishTime);
		TripFunctions.calculateDuration(trip);
	}

	@Test
	public void testStatusCalculation() {
		Calendar calendar = Calendar.getInstance();
		Date startDate = calendar.getTime();
		calendar.add(Calendar.MINUTE, 2);
		Date finishDate = calendar.getTime();

		// start with an incomplete trip
		EventDto event1 = new EventDto();
		event1.setId("1");
		event1.setDateTimeUTC(startDate);
		event1.setTapType(EventDto.TAP_TYPE_ON);
		event1.setStopId(RouteDto.STOP_1);
		event1.setCompanyId(Constants.COMPANY_ID);
		event1.setBusId(Constants.BUS_ID);
		event1.setPan(Constants.PAN_AMEX);
		TripDto trip1 = TripFunctions.buildTrip(event1, null);
		// trip should be incomplete, and being from Stop 1, $7.30
		assert (TripDto.STATUS_INCOMPLETE.equalsIgnoreCase(trip1.getStatus())
				&& StringUtils.equalsAnyIgnoreCase(trip1.getChargeAmount(), "$7.30"));

		// now a cancelled trip (tap on and off from same location)
		EventDto event2 = new EventDto();
		event2.setId("2");
		event2.setDateTimeUTC(finishDate);
		event2.setTapType(EventDto.TAP_TYPE_OFF);
		event2.setStopId(RouteDto.STOP_1);
		event2.setCompanyId(Constants.COMPANY_ID);
		event2.setBusId(Constants.BUS_ID);
		event2.setPan(Constants.PAN_AMEX);
		TripDto trip2 = TripFunctions.buildTrip(event1, event2);
		// trip should be incomplete and $0.00
		assert (TripDto.STATUS_CANCELLED.equalsIgnoreCase(trip2.getStatus())
				&& StringUtils.equalsAnyIgnoreCase(trip2.getChargeAmount(), "$0.00"));

		// now a complete trip (tap on and off from different location)
		EventDto event3 = new EventDto();
		event3.setId("3");
		event3.setDateTimeUTC(finishDate);
		event3.setTapType(EventDto.TAP_TYPE_OFF);
		event3.setStopId(RouteDto.STOP_2);
		event3.setCompanyId(Constants.COMPANY_ID);
		event3.setBusId(Constants.BUS_ID);
		event3.setPan(Constants.PAN_AMEX);
		TripDto trip3 = TripFunctions.buildTrip(event1, event3);
		// trip should be complete, and being from stop 1 to stop 2, $3.25
		assert (TripDto.STATUS_COMPLETED.equalsIgnoreCase(trip3.getStatus())
				&& StringUtils.equalsAnyIgnoreCase(trip3.getChargeAmount(), "$3.25"));

		// now try something that should fail
		try {
			// if building the trip goes off without a hitch, then something is wrong
			TripDto trip4 = TripFunctions.buildTrip(null, event3);
			assert (trip4 == null);
		} catch (IllegalArgumentException e) {
			assert (true);
		}
	}

	@Test
	public void testOutputListLength() {
		List<EventDto> eventList = buildSampleEventList();
		// the number of trips we expect from the output is equal to the number of tap
		// on events in our event set
		long expectedNumberOfTrips = eventList.stream().filter(event -> {
			return EventDto.TAP_TYPE_ON.equalsIgnoreCase(event.getTapType());
		}).count();
		try {
			List<TripDto> tripList = EventFunctions.processEventData(eventList);
			// at this point, with the above tests that specifically test the hard
			// calculations
			// we can assume that if the list that comes back here exists and is the right
			// size, then the data is correct
			assert (tripList != null && tripList.size() == expectedNumberOfTrips);
		} catch (Exception e) {
			assert (false);
		}
	}

	private List<EventDto> buildSampleEventList() {
		List<EventDto> eventList = new ArrayList<EventDto>();
		Calendar daCal = Calendar.getInstance();
		// sequential events in a pair with a duration of 1 minute, from stop1 to stop 2
		{
			EventDto event = new EventDto();
			event.setId("1");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_ON);
			event.setStopId(RouteDto.STOP_1);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_AMEX);
			eventList.add(event);
		}
		{
			daCal.add(Calendar.MINUTE, 1);
			EventDto event = new EventDto();
			event.setId("2");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_OFF);
			event.setStopId(RouteDto.STOP_2);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_AMEX);
			eventList.add(event);
		}
		// now a pair of non-sequential events
		// pair 1 (MASTERCARD_1) will be from Stop2 to Stop3 in 2 minutes
		{
			EventDto event = new EventDto();
			event.setId("3");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_ON);
			event.setStopId(RouteDto.STOP_2);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_MASTERCARD_1);
			eventList.add(event);
		}
		// pair 2 (MASTERCARD_2) will be from Stop1 to Stop3 in 3 minutes
		{
			EventDto event = new EventDto();
			event.setId("4");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_ON);
			event.setStopId(RouteDto.STOP_1);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_MASTERCARD_2);
			eventList.add(event);
		}
		{
			daCal.add(Calendar.MINUTE, 2);
			EventDto event = new EventDto();
			event.setId("5");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_OFF);
			event.setStopId(RouteDto.STOP_3);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_MASTERCARD_1);
			eventList.add(event);
		}
		{
			daCal.add(Calendar.MINUTE, 1);
			EventDto event = new EventDto();
			event.setId("6");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_OFF);
			event.setStopId(RouteDto.STOP_3);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_MASTERCARD_2);
			eventList.add(event);
		}
		// now an event that doen't have a TAP OFF event
		{
			EventDto event = new EventDto();
			event.setId("7");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_ON);
			event.setStopId(RouteDto.STOP_1);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_DINERS_1);
			eventList.add(event);
		}
		// now a TAP OFF event that doen't have a TAP ON event (this event will end up
		// nowhere)
		{
			EventDto event = new EventDto();
			event.setId("8");
			event.setDateTimeUTC(daCal.getTime());
			event.setTapType(EventDto.TAP_TYPE_OFF);
			event.setStopId(RouteDto.STOP_2);
			event.setCompanyId(Constants.COMPANY_ID);
			event.setBusId(Constants.BUS_ID);
			event.setPan(Constants.PAN_DINERS_2);
			eventList.add(event);
		}
		return eventList;
	}

}
