package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Arrays;

public class Sum {

	public static void main(String[] args) throws IOException {

		// csv file containing data
		String strDir = "C:\\Users\\jwiniarski.MOSTWAR1\\Desktop\\Rennovates\\WP 02 RTD SIMULATIONS\\TRNSYS\\TrnsysScenariosResults\\";
		String strFile = "\\Power.dat_";
		String line = "";
		String[] values;
		double[] singleFileSum = new double[18];
		double[] allFilesSum = new double[18];
		double[][] allScenarios = new double[8][18];
		double electricityBalance;
		double elExport = 0.0;
		double elImport = 0.0;

		for (int k = 0; k <= 7; k++) {

			for (int i = 1; i <= 9; i++) {

				BufferedReader br = new BufferedReader(new FileReader(strDir + k + strFile + i));
				// read comma separated file line by line
				line = br.readLine();// skip header
				line = br.readLine();// skip units

				// reinitialize local sum[] to 0.0 before reading the next file
				Arrays.fill(singleFileSum, 0.0);
				// read file numeric data and sum each column to sum[]

				while ((line = br.readLine()) != null) {
					values = line.split("	  ");
					electricityBalance = Double.parseDouble(values[8]) - Double.parseDouble(values[1])
							- Double.parseDouble(values[7]);
					// System.out.println(electricityBalance);
					if (electricityBalance >= 0) {
						elExport = electricityBalance;
						values[16] = elExport + "";
						// System.out.println(elExport);
					} else {
						elImport = electricityBalance;
						values[17] = elImport + "";
					}
					for (int j = 0; j < values.length; j++) {
						singleFileSum[j] += Double.parseDouble(values[j]);
					}

				}
				// add sum for each column to global sums[]
				for (int j = 0; j < allFilesSum.length; j++) {
					allFilesSum[j] += singleFileSum[j];
				}
				for (int j = 0; j < allFilesSum.length; j++) {
					allScenarios[k][j] = allFilesSum[j];
				}
			}
			System.out.println("Scenario: " + k + " ");
			for (double d : allFilesSum) {
				System.out.println(d);
			}

			// reinitialize global sum[] to 0.0 before reading the next scenario
			Arrays.fill(allFilesSum, 0.0);
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < allScenarios.length; i++)// for each row
		{
			for (int j = 0; j < allScenarios.length; j++)// for each column
			{
				builder.append(allScenarios[i][j] + "");// append to the output
														// string
				if (j < allScenarios.length - 1)// if this is not the last row
												// element
					builder.append("	  ");// then add comma (if you don't like
										// commas you can use spaces)
			}
			builder.append("\n");// append new line at the end of the row
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(strDir + "results"));
		writer.write(builder.toString());// save the string representation of
											// the allScenarios
		writer.close();

	}
}