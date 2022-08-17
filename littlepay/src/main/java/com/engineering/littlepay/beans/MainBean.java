package com.engineering.littlepay.beans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.engineering.littlepay.dto.EventDto;
import com.engineering.littlepay.dto.TripDto;
import com.engineering.littlepay.impl.EventFunctions;
import com.engineering.littlepay.impl.FileFunctions;

@Component
public class MainBean {
    public static void main(String[] args) {
    	
    	//command arguments to obtain input and output path directories
    	String inputFilePath = null;
    	String outputFilePath = null;
    	
    	if(args.length > 0) {
    		inputFilePath = args[0];
    	}
    	if(args.length > 1) {
    		outputFilePath = args[1];
    	}
    	
    	//prepare a buffered reader to potential read some user input
    	try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
	    	if(StringUtils.isBlank(inputFilePath)) {
	    		System.out.println("Enter input csv file path");
				inputFilePath = br.readLine();
	    	}
	    	if(StringUtils.isBlank(outputFilePath)) {
	    		System.out.println("Enter output csv file path");
	    		outputFilePath = br.readLine();
	    	}
    	} catch(IOException ioe) {
    		System.err.println(ioe);
    	}
    	
    	//if at this point either path is empty, then the user input is invalid
    	if(StringUtils.isBlank(inputFilePath)) {
    		System.err.println("Input file path not supplied!");
    		return;
    	}
    	if(StringUtils.isBlank(outputFilePath)) {
    		System.err.println("Output file path not supplied!");
    		return;
    	}

    	//so far so good, user has given us all of the input we need
    	System.out.println("Reading tap events from: " + inputFilePath);
    	System.out.println("Writing trip summary to: " + outputFilePath);
    	
    	
    	List<EventDto> eventList;
    	try {
    		//get a list of tap events from the input file
    		eventList = FileFunctions.readInputCsv(inputFilePath);
    	} catch(FileNotFoundException fnfe) {
    		System.err.println("Input csv file not found!");
    		System.err.println(fnfe);
    		return;
    	} catch(IOException ioe) {
    		System.err.println("Error parsing input csv file! Invalid format");
    		System.err.println(ioe);
    		return;
    	}
    	
    	//create a list of trips from the event data
    	
		List<TripDto> tripList = EventFunctions.processEventData(eventList);
		try {
			//write the output file
			FileFunctions.writeOutputCsv(outputFilePath, tripList);
			System.out.println("Trip data successfully written to: " + outputFilePath);
		} catch(IOException ioe) {
    		System.err.println("Error writing output csv file!");
    		System.err.println(ioe);
    	}
    }
}
