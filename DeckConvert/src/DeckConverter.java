import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;

public class DeckConverter {

	public static void main(String[] args) throws IOException {

		String[] entries = { "EnergyRatFromCHP", "EnergyRatToDHW", "PumpFR", "PumpFRnom", "Pumpsignal", "CHPTdelta",
				"EnergyRateToDSH", "Pload1", "PDEGSmin", "PDEGSmax", "PDEGS", "PDEGSwlot", "wlacznik", "wlacznikfinal",
				"wlacznik1", "CHP_Pel", "EnElBought", "Tin_to_HX3", "FR_to_HX3", "Tset_to_SH", "Q_HX3", "Q_sh_corr",
				"SH_HX3_prod", "SH_bought", "ThotNet", "msource", "kWhFromSubstToDHW", "kWhDryCooler", "Demand_FR",
				"Tcold", "Tload", "T_hotNet", "TankTempHot", "TankFR_Hot", "Aterm", "Bterm", "PTCHP", "EnergyRateToDHW",
				"PthCHP", "PthCHPnom", "PelCHP", "PelCHPnom", "PumpOutTemp", "PumpOutFR", "CHPTret",
				"Demand_on_DHW_Pth", "TankCHPTin", "TankCHPTout", "TankEnergyRateToDHW", "PumpSignal", "PumpFlowrate",
				"TankPortIn", "TankPortOut", "HXout", "HXEnergyRateToDHW", "DemandFR", "mload", "EnergyFromSubstToDHW",
				"DryCoolerRate", "Tout_from_HX3", "FR_from_HX3", "HX3_HTrate", "HTC", "SH_Demand", "mload_teepiece",
				"T_HX3in", "DHWDemand", "HX1", "HX2", "TeePiece", "DivOut1Tank", "DivOut2TeePiec", "FanSpeed", "PumpFR",
				"PumpSignal", "Tcold_fromNet", "T_to_DHW", "Treturn_from_DryCooler_to_CHP", "Tsource", "HX1out_toTank",
				"HX1out_to_Diverter", "Tout_from_Diverter_to_HX2", "HX3_Tout_source", "HX2_Tout_source",
				"TeePiece_Tout", "LMTD", "dT1", "dT2", "PelCHP", "PthCHP", "SH_rate", "EnergyRatToDHW", "kWhDryCooler",
				"kWhFromSubstToDHW", "PVPower", "Pel_demand", "DHW_demand", "Q_HX3_from_sourceSide", "Q_HX3_toLoad",
				"SH_demand", "GasConsumption", "SubstRes", "FuelRes", "ElPowerRes", "TotalEFFRes", "SH_HX3_rate_kW",
				"DemandDHW_Pth_re", "SH_Demand_re", "Pel_demand_re", "Power.dat", "Bills.dat", "Bills2.dat", "FR.dat",
				"Temperatures.dat" };

		ArrayList<String> newList = new ArrayList<>();
		ArrayList<String> list = new ArrayList<>();
		Collections.addAll(list, entries);

		try {
			int counter = 1;
			int count = 0;

			Path inputPath = Paths
					.get("P:\\Projekty\\Rennovates\\WP 02 RTD SIMULATIONS\\TRNSYS\\_Baseline\\Building_imported1.dck");
			Path inputPathUTF8 = Paths
					.get("P:\\Projekty\\Rennovates\\WP 02 RTD SIMULATIONS\\TRNSYS\\_Baseline\\DeckFileIn.dck");
			ByteBuffer bb = ByteBuffer.wrap(Files.readAllBytes(inputPath));
			CharBuffer cb = Charset.forName("windows-1252").decode(bb);
			bb = Charset.forName("UTF-8").encode(cb);
			Files.write(inputPathUTF8, bb.array());
			List<String> file = Files.readAllLines(inputPathUTF8);

			for (String line : file) {
				if (line.contains("* Model \"SH_HX3-")) { // * Model "SH_HX3-2" (Type 91)
					counter++;
				}

				for (String parameter : list) {
					if (line.contains(parameter)) {
						count++;
						String replaced = line.replaceAll("\\b" + parameter + "\\b", parameter + "_" + counter);
						line = replaced;
					}
				}
				newList.add(line);
			}
			System.out.println(
					"Liczba znalezionych modu³ów: " + counter + ", liczba zmian nazw parametrów: " + count + ".");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Files.write(Paths.get("P:\\Projekty\\Rennovates\\WP 02 RTD SIMULATIONS\\TRNSYS\\_Baseline\\DeckFileOut.dck"),
				newList);
	}
}
