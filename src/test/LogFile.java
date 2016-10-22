package test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class LogFile {
	private static LogFile instance=null;
	private FileWriter fw;
	private HashMap<String, FileWriter> mapLog;
	public static final String LOG_EJECUCION="log_ejecucion.txt";
	public static final String LOG_LOCALIDADES_CREADAS="log_localidades_creadas.txt";
	public static final String LOG_CLIENTES_CREADOS="log_clientes_creados.txt";
	public static final String LOG_ORDENES_CREADAS="log_ordenes_creadas.txt";
	public static final String LOG_REMITOS_CREADAS="log_remitos_creados.txt";
	public static final String LOG_ERROR_LECTURA_PDF="error_leer_pdf.txt";
	public static final String LOG_ERROR_INSERCION_REMITO="Error_insercion_remito.txt";
	public static final String SEPARADOR="************************************";
	private LogFile() throws IOException{
		mapLog=new HashMap<String,FileWriter>();        
	}
	
	
	private synchronized static LogFile getInstance() throws IOException{
		if(instance==null)
			instance=new LogFile();
		return instance;
	}
	
	private FileWriter recovery(String nombreLog) throws IOException{
		if(mapLog.containsKey(nombreLog))
			return mapLog.get(nombreLog);
        FileWriter fw= new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio(Propiedades.getInstance().getPath_pdf(),nombreLog),true);
		mapLog.put(nombreLog, fw);
		return fw;
	}
	
	private void escribirEnLogPrivado(String nombreLog,String linea) throws IOException{
		this.recovery(nombreLog).write(linea+"\n");
	}
	
	public static void escribirEnLog(String nombreLog, String linea) throws IOException{
		LogFile.getInstance().escribirEnLogPrivado(nombreLog,linea);
	}
	
	private void cerrarYGuardarLogsPrivado() throws IOException{
		for(FileWriter fw: this.mapLog.values())
			fw.close();
	}
	public static void cerrarYGuardarLogs() throws IOException{
		LogFile.getInstance().cerrarYGuardarLogsPrivado();
	}
	
}
