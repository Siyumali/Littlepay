package com.engineering.littlepay.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import com.engineering.littlepay.dto.EventDto;
import com.engineering.littlepay.dto.TripDto;

public class FileFunctions {
	public static List<EventDto> readInputCsv(String fileName) throws IOException {
    	File csvInputFile = new File(fileName);
    	
        if(!csvInputFile.canRead())
        	csvInputFile.setReadable(true);
    	
    	return readInputCsv(csvInputFile);
    }
    
    private static List<EventDto> readInputCsv(File inputFile) throws IOException {
    	CsvMapper csvMapper = CsvMapper.builder()
			.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
			.enable(CsvParser.Feature.TRIM_SPACES)
			.build();
    	CsvSchema schema = CsvSchema.emptySchema().withHeader(); 
    	 
    	ObjectReader oReader = csvMapper.readerFor(EventDto.class).with(schema);
    	try (Reader reader = new FileReader(inputFile)) {
    	    MappingIterator<EventDto> mi = oReader.readValues(reader);
    	    return mi.readAll();
    	}
    }
    
    public static void writeOutputCsv(String fileName, List<TripDto> tripList) throws IOException {
    	File csvOutputFile = new File(fileName);
    	if(!csvOutputFile.isFile()) {
    		csvOutputFile.createNewFile();
    	}
    	writeOutputCsv(csvOutputFile, tripList);
    }
    
    private static void writeOutputCsv(File outputFile, List<TripDto> tripList) throws IOException {
    	
        if(!outputFile.canWrite())
        	outputFile.setWritable(true);
    	
    	CsvMapper csvMapper = CsvMapper.builder()
			.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
			.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
			.build();

    	CsvSchema schema = CsvSchema.builder()
			.setUseHeader(true)
    		.addColumn("Started")
    		.addColumn("Finished")
    		.addColumn("DurationSecs")
    		.addColumn("FromStopId")
    		.addColumn("ToStopId")
    		.addColumn("ChargeAmount")
    		.addColumn("CompanyId")
    		.addColumn("BusID")
    		.addColumn("PAN")
    		.addColumn("Status")
		.build();
    	
    	ObjectWriter oWriter = csvMapper.writerFor(TripDto.class).with(schema);
    	
    	oWriter.writeValues(outputFile).writeAll(tripList);
    }
}
