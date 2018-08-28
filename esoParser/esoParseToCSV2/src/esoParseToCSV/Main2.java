package esoParseToCSV;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main2 {

	public static final String[] zone = { "Z01", "Z02", "Z03", "Z04" };
	public static final String resultPath = "C:/Users/jwiniarski.MOSTWAR1/Desktop/Java/esoParseToCSV2/resources/";
	public static final String parameter = "Heating:EnergyTransfer";
	public static final String[] esoFiles = {
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S1_bud1.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S1_bud2.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S1_bud3.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S2_bud1.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S2_bud2.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S2_bud3.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S3_bud1.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S3_bud2.tri_dat/Energyplus/Project_dem.res/Project_dem.eso",
			"C:/Users/jwiniarski.MOSTWAR1/Desktop/Rennovates/WP 02 RTD SIMULATIONS/CYPEtherm Eplus Results/RNV_6_S3_bud3.tri_dat/Energyplus/Project_dem.res/Project_dem.eso", };

	public static void main(String[] args) {

		for (int i = 0; i < esoFiles.length; i++) { 													//Iterate through all files
			String inputFileName = esoFiles[i];															//String with path of current inputfile
			Path spaceHeatDemandSumFilePath = Paths.get(resultPath + "resultsSpaceSum_" + i + ".eso");	//
			
			try {
				for (int j = 0; j <= zone.length; j++) {												// For every input file, iterate also all zones
					Files.write(spaceHeatDemandSumFilePath,
							generateSumOfResultFiles(generateResultFiles(inputFileName, zone[j])));
				}
			} catch (IOException e) {
				System.out.println("Can't write a file.");
				e.printStackTrace();
			}
		}
	}
/**
 * Method takes 
 * @param inputFileName and
 * @param zone
 * and generates the List of Lists, structure that contains all data records wanted for all spaces respectively. At the end
 * @return 
 * @throws FileNotFoundException
 * @throws IOException
 */
	private static List<List<String>> generateResultFiles(String inputFileName, String zone)
			throws FileNotFoundException, IOException {

		int cnt = 0;
		String line;
		String[] inputDataRowWithZoneToken;
		List<String> listOfDataForSpace = new ArrayList<String>(); 		//Container for one row of results
		List<List<String>> listTable = new ArrayList<List<String>>(); 	// Container for gathering ArrayLists listOfDataForSpace  
//		List<String> resList = new LinkedList<>();

		List<String> spaceList = Files.lines(Paths.get(esoFiles[0]))
				.filter(fline -> fline.contains(parameter)
						&& fline.contains(zone))
				.collect(Collectors.toList()); 							// Find all names of spaces, for which program will gather data, not necessary to get results

		Files.write(Paths.get(resultPath + "fspaces"), spaceList);

//		for (String space : spaceList) {
//			resList.add(Files.lines(Paths.get(esoFiles[0]))
//					.filter(a -> a.contains(space))
//					.toString());
//		}
//		Files.write(Paths.get(resultPath + "resList"), resList);

		for (String space : spaceList) {
			System.out.println("Start to gathering data for space");	//For each space name, search the text file and store occurrences in the List.

			BufferedReader br = new BufferedReader(new FileReader(inputFileName));
			System.out.println("Reading input file for space data collection");
			while ((line = br.readLine()) != null) {
				if (line.contains(space + ",") && !line.contains(parameter)) {
					// System.out.println("Found data for space");
					inputDataRowWithZoneToken = line.split(",");
					listOfDataForSpace.add(inputDataRowWithZoneToken[1]);
				}
			}
			br.close();
			System.out.println("Ended gathering data for space, size: " + listOfDataForSpace.size());
			listTable.add(listOfDataForSpace);							// List with data for given space name is completed, so store it in List<List<String>>
			listOfDataForSpace = null;									// Clear List with fata for given space name before loading the next space name. 
			cnt++;					
		}

		System.out.println("Input file processing ended. Spaces searched: " + cnt);
		System.out.println("Created list of data for list of spaces, size: " + listTable.size());
		return listTable;		
	}

	/**
	 * Method takes 
	 * @param listTable
	 * in order to generate sum for each inner List and returns
	 * @return the List of sums.
	 */
	
	private static List<String> generateSumOfResultFiles(List<List<String>> listTable) {

		List<String> resultList = new ArrayList<>();			// Initialize the list which will be returned as a result
		double sum = 0;

		for (int i = 0; i < listTable.get(0).size(); i++) {		// Iterate through List of Lists, the size of first list is the size of all
			for (List<String> ss : listTable) {					// Collect and sum up elements of the same position (i) in the Lists.  
				try {
					sum += Double.parseDouble(ss.get(i) + "");
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Error occured at line no " + i);
				}
			}
			sum = Math.round(sum);
			resultList.add(sum + "");						// Store the sum of all elements of the same position and store it in another, results list.
			System.out.println("Sum for line " + i + "equals: " + sum);
			sum = 0;
		}
		System.out.println("Sum of result files generated");
		return resultList;
	}

}
