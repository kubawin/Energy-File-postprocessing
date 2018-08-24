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
	public static String resultPath = "C:/Users/jwiniarski.MOSTWAR1/Desktop/Java/esoParseToCSV2/resources/";
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

		for (int i = 0; i < esoFiles.length; i++) {
			String inputFileName = esoFiles[i];
			Path spaceHeatDemandSumFilePath = Paths.get(resultPath + "resultsSpaceSum_" + i + ".eso");
			try {
				for (int j = 0; j <= zone.length; j++) {
					Files.write(spaceHeatDemandSumFilePath,
							generateSumOfResultFiles(generateResultFiles(inputFileName, zone[j])));
				}
			} catch (IOException e) {
				System.out.println("Nie uda³o siê zapisac pliku.");
				e.printStackTrace();
			}
		}
	}

	private static List<List<String>> generateResultFiles(String inputFileName, String zone)
			throws FileNotFoundException, IOException {

		int cnt = 0;
		String line;
		String[] inputDataRowWithZoneToken;
		List<String> listOfDataForSpace = new ArrayList<String>(); 		//Container for one row of results
		List<List<String>> listTable = new ArrayList<List<String>>(); 	// Container for gathering ArrayLists listOfDataForSpace  
//		List<String> resList = new LinkedList<>();

		List<String> spaceList = Files.lines(Paths.get(esoFiles[0]))
				.filter(fline -> fline.contains("Heating:EnergyTransfer")
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
			System.out.println("Start to gathering data for space");

			BufferedReader br = new BufferedReader(new FileReader(inputFileName));
			System.out.println("Reading input file for space data collection");
			while ((line = br.readLine()) != null) {
				if (line.contains(space + ",") && !line.contains("Heating:EnergyTransfer")) {
					// System.out.println("Found data for space");
					inputDataRowWithZoneToken = line.split(",");
					listOfDataForSpace.add(inputDataRowWithZoneToken[1]);
				}
			}
			br.close();
			System.out.println("Ended gathering data for space, size: " + listOfDataForSpace.size());
			listTable.add(listOfDataForSpace);
			listOfDataForSpace = null;
			cnt++;
		}

		System.out.println("Input file processing ended. Spaces searched: " + cnt);
		System.out.println("Created list of data for list of spaces, size: " + listTable.size());
		return listTable;
	}

	private static List<String> generateSumOfResultFiles(List<List<String>> listTable) {

		List<String> table = new LinkedList<>();
		double sum = 0;

		for (int i = 0; i < listTable.get(0).size(); i++) {
			for (List<String> ss : listTable) {
				try {
					sum += Double.parseDouble(ss.get(i) + "");
				} catch (IndexOutOfBoundsException e) {
					System.out.println("B³¹d przy linii nr " + i);
				}
			}
			sum = Math.round(sum);
			table.add(sum + "");
			System.out.println("Sum for line " + i + "equals: " + sum);
			sum = 0;
		}
		System.out.println("Sum of result files generated");
		return table;
	}

}
