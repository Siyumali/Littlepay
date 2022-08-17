package com.engineering.littlepay.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.engineering.littlepay.dto.EventDto;
import com.engineering.littlepay.dto.TripDto;

public class EventFunctions {
	public static List<TripDto> processEventData(List<EventDto> eventList) {
		
    	//the array list that we are going to start the trips in once they have been calculated
    	List<TripDto> tripData = new ArrayList<TripDto>();
    	
    	Map<String, EventDto> tapOnEvents = new HashMap<String, EventDto>();
    	
    	//process the tap on events, in DateTime order
    	eventList.stream().sorted((e1, e2) -> {
    		return e1.getDateTimeUTC().compareTo(e2.getDateTimeUTC());
    	}).forEach(event -> {
    		if(EventDto.TAP_TYPE_ON.equalsIgnoreCase(event.getTapType())) {
    			
    			//TAP ON event
    			if(tapOnEvents.containsKey(event.getPan())) {
    			
    				//map contains a TAP ON record for this PAN already, so build an INCOMPLETE trip for that TAP ON event
    				tripData.add(TripFunctions.buildTrip(event, null));
    				tapOnEvents.remove(event.getPan());
    			}
    			
    			//put the event in the map as a TAP ON event waiting for a corresponding TAP OFF event
    			tapOnEvents.put(event.getPan(), event);
    			
    		} else if(EventDto.TAP_TYPE_OFF.equalsIgnoreCase(event.getTapType())) {
    			
    			//TAP OFF event
    			if(tapOnEvents.containsKey(event.getPan())) {
    			
    				//map contains a TAP ON event for this PAN, so build a complete trip
    				tripData.add(TripFunctions.buildTrip(tapOnEvents.get(event.getPan()), event));
    				tapOnEvents.remove(event.getPan());
    				
    			} else {
    				//a tap off event with no corresponding tap on event???
    			}
    		} else {
    			//unknown type of tap event, just ignore it for now
    		}
    	});
    	
    	//at this point, anything left in the processing map is a TAP ON event without a corresponding TAP OFF event
    	tapOnEvents.values().forEach(orphanedTapOnEvent -> {
    		tripData.add(TripFunctions.buildTrip(orphanedTapOnEvent, null));
    	});
    	
    	return tripData;
    }
}
