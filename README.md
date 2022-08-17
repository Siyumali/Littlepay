
# Littlepay - Engineering Team Project - Siyumali Athukorala

This is a sample project for Littlepay built using Springboot framework to provide a standalone application to read and write to a .csv file. The application is developed based on Java 11 and maven.

### Technology and Libraries

The following technologies and libraries were used to create the application:

- Java 11
- Spring Boot
- Spring web
- Maven
- jackson-dataformat-csv
- jackson-databind
- lombok
- JUnit

## Assumptions
1. An incomplete data will have no tap data
2. Sorting the tap data from date
3. Log formatting is out of scope of this project
4. Input and Output file paths are already fed into the system(inside \src\main\resources)

## How to build and Run
## Requirements
	JAVA- 11 Maven- 3.8.6
1.	Jar file can be found at littlePay-Assignment\littlepay\target\ folder
2.  cd in to the target folder
2.	run command
	
		java -jar .\littlepay-0.0.1-SNAPSHOT.jar

## Example Output
Enter input csv file path
C:\Users\sathukor\littlePay-Assignment\littlepay\dataFiles\inputFile\taps.csv
Enter output csv file path
C:\Users\sathukor\littlePay-Assignment\littlepay\dataFiles\outputFile\output.csv
Reading tap events from: C:\Users\sathukor\littlePay-Assignment\littlepay\dataFiles\inputFile\taps.csv
Writing trip summary to: C:\Users\sathukor\littlePay-Assignment\littlepay\dataFiles\outputFile\output.csv
Trip data successfully written to: C:\Users\sathukor\littlePay-Assignment\littlepay\dataFiles\outputFile\output.csv
