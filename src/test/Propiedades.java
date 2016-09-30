package test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Propiedades {
	private String path_pdf;
	private String path_bd_access;
	private static Propiedades instance=null;
	private Propiedades(){
        Properties props = new Properties();
		try {
			props.load(new FileReader("configuracion.properties"));
			this.setPath_pdf(props.getProperty("ficheroPDF"));
			this.setPath_bd_access(props.getProperty("pathDBAccess"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized static Propiedades getInstance(){
		if(instance==null)
			instance=new Propiedades();
		return instance;
	}


	public String getPath_pdf() {
		return path_pdf;
	}


	public void setPath_pdf(String path_pdf) {
		this.path_pdf = path_pdf;
	}


	public String getPath_bd_access() {
		return path_bd_access;
	}


	public void setPath_bd_access(String path_bd_access) {
		this.path_bd_access = path_bd_access;
	}
	
	
	
}
