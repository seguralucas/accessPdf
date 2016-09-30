package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class DirectorioManager {
	
	
	public static File getDirectorioFechaYHoraInicio(String pathInicial,String nombreFichero) throws IOException{
		File file = new File(pathInicial+"/"+AlmacenadorFechaYHora.getFechaYHoraInicio());
		if(!file.exists())
			Files.createDirectories(Paths.get(pathInicial+"/"+AlmacenadorFechaYHora.getFechaYHoraInicio()));
		return new File(pathInicial+"/"+AlmacenadorFechaYHora.getFechaYHoraInicio()+"/"+nombreFichero);
	}
	
	public static File getDirectorioFechaYHoraInicio(String nombreFichero) throws IOException{
		return getDirectorioFechaYHoraInicio("",nombreFichero);
	}

}
