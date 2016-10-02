package test;

import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFImageWriter;
import org.apache.pdfbox.util.PDFTextStripperByArea;
public class LeerPdf {
	public static int aciertos=0;
	public static int fallos=0;
	public static final int NO_EXISTE_EN_BD=-1;
    public static void main(String[] args) throws IOException {
        LeerPdf leerPDF =new LeerPdf();
        Properties props=null;
            try{
        	ArrayList<Remito> remitos= leerPDF.lecturaPDF();
   			System.out.println("Total de registros:" +remitos.size());
   			LogFile.escribirEnLog(LogFile.LOG_EJECUCION, "Total de registros:" +remitos.size());
   			for(Remito remito: remitos){
   				leerPDF.insertarRemito(remito);
   			}
   			System.out.println("Aciertos "+aciertos+" Fallos "+fallos);
   			LogFile.cerrarYGuardarLogs();
            }
            catch(Exception e){
       			LogFile.cerrarYGuardarLogs();
            }
    }
    
    private static Connection conn;
    public void insertarRemito(Remito remito) throws IOException{
        try { 
     	   Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        conn=DriverManager.getConnection("jdbc:ucanaccess://"+Propiedades.getInstance().getPath_bd_access());         
 	    String nombreCliente;
 	    if(remito.getAgente().length()>=1)
 	    	nombreCliente=remito.getAgente();
 	    else if(remito.getCliente().length()>=1)
 	    	nombreCliente=remito.getCliente();
 	    else{
 	    	escribirErrorRemito(remito,"No se encuentra nombre de angente ni nombre de cliente");
 	    	return;
 	    } 	
 	    	remito.setLocalidadId(this.getIdLocalidadBD(remito.getLocalidad()));
 	    	remito.setClienteId(getCliente(nombreCliente,remito));
 	    	remito.setOrdenId(createOrden(remito));
 	    	createRemito(remito);
 	    
        }
        catch (Exception e) {
        	escribirErrorRemito(remito, "Error al insertar en el access: "+e.getMessage());
     	   e.printStackTrace();
        }
    }
    
    private long getIdLocalidadBD(String localidad) throws SQLException{
    	String sql = "Select * from Localidad where Localidad_Nombre='"+localidad+"'"; 
 	    Statement st = conn.createStatement(); 
	       ResultSet rs = st.executeQuery(sql);
	       int id=NO_EXISTE_EN_BD;
	       while(rs.next()){
	    	   id=rs.getInt("Localidad_Id");
	       }
	   return id==NO_EXISTE_EN_BD?createLocalidadBD(localidad):id;
    }
    

    
    private long getCliente(String nombreCliente, Remito remito) throws SQLException{
    	String sql = "Select * from Clientes where Cliente_Nombre='"+nombreCliente+"' and Cliente_Direccion='"+remito.getDireccion()+"' and Cliente_Localidad_Id='"+remito.getLocalidadId()+"'"	;
	 	    Statement st = conn.createStatement(); 
	        ResultSet rs = st.executeQuery(sql);
	        int id=NO_EXISTE_EN_BD;
		    while(rs.next()){
		    	id=rs.getInt("Cliente_Id");
		    }
		    return id==NO_EXISTE_EN_BD?createCliente(nombreCliente, remito):id;
    }
    
    private long createCliente(String nombreCliente, Remito remito) throws SQLException{
    	System.out.println("Creando cliente");
	    String sql = "insert into Clientes (Cliente_Nombre,Cliente_Direccion,Cliente_Localidad_Id) VALUES('"+nombreCliente+"','"+remito.getDireccion()+"','"+remito.getLocalidadId()+"')";
 	    Statement st = conn.createStatement(); 
 	    int rs = st.executeUpdate(sql);
        if (rs == 0) {
            throw new SQLException("Creacion de Cliente Fallida.");
        }        ResultSet generatedKeys = st.getGeneratedKeys();
        long idCliente;
            if (generatedKeys.next()) {
            	idCliente=generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creacion de Cliente Fallida");
            }
            return idCliente;
    }
    
    private long createLocalidadBD(String localidad) throws SQLException{
    	String sql="INSERT INTO Localidad (Localidad_Nombre,Localidad_Tarifa_Id)  VALUES ('"+localidad+"',32);";
 	    Statement st = conn.createStatement(); 
 	    
 	    int rs = st.executeUpdate(sql);
        if (rs == 0) {
            throw new SQLException("Creacion de localidad Fallida.");
        }
        ResultSet generatedKeys = st.getGeneratedKeys();
        long idLocalidad;
            if (generatedKeys.next()) {
            	idLocalidad=generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creacion de localidad Fallida");
            }
            return idLocalidad;
    }
    
    private long createOrden(Remito remito) throws SQLException{
       System.out.println("Creando orden.");
 	   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
 	   Calendar cal = Calendar.getInstance();
 	   String fecha= dateFormat.format(cal.getTime()).toString();
	   String sql = "insert into Orden (Orden_Dest_Cliente_Id,Orden_Remi_Cliente_Id,Orden_Fecha) VALUES("+remito.getClienteId()+", 482 ,Date())";
 	   Statement st = conn.createStatement(); 
 	   int rs = st.executeUpdate(sql);
 	  if (rs == 0) {
          throw new SQLException("Creacion de Orden Fallida.");
      }
      ResultSet generatedKeys = st.getGeneratedKeys();
      long idOrden;
          if (generatedKeys.next()) {
        	  idOrden=generatedKeys.getLong(1);
          }
          else {
              throw new SQLException("Creacion de Orden Fallida");
          }
          return idOrden;
    	
    }
    
    private long createRemito(Remito remito) throws SQLException{
       System.out.println("Creando remito.");
 	   String sql = "insert into Remito (Remito_Orden_Id,Remito_RemitoStatus_id) VALUES("+remito.getOrdenId()+", 1)";
  	   Statement st = conn.createStatement(); 
  	   int rs = st.executeUpdate(sql);
  	  if (rs == 0) {
          throw new SQLException("Creacion de Remito Fallida.");
      }
      ResultSet generatedKeys = st.getGeneratedKeys();
      long idRemito;
          if (generatedKeys.next()) {
        	  idRemito=generatedKeys.getLong(1);
          }
          else {
              throw new SQLException("Creacion de Remito Fallida");
          }
          return idRemito;
    }
    
    
    private void escribirErrorRemito(Remito remito,String line) throws IOException{
    	LogFile.escribirEnLog(LogFile.LOG_ERROR_INSERCION_REMITO,"Error en el registro Nº:"+remito.getNumeroRegistro());
    	LogFile.escribirEnLog(LogFile.LOG_ERROR_INSERCION_REMITO,"Pagina:"+remito.getPagina());
    	LogFile.escribirEnLog(LogFile.LOG_ERROR_INSERCION_REMITO,"Fichero: "+remito.getPdf());
    	LogFile.escribirEnLog(LogFile.LOG_ERROR_INSERCION_REMITO,"Remito: "+remito);
    	LogFile.escribirEnLog(LogFile.LOG_ERROR_INSERCION_REMITO,"Descripcion: "+line);
    	LogFile.escribirEnLog(LogFile.LOG_ERROR_INSERCION_REMITO,LogFile.SEPARADOR);
    }
    
    
    
    
    
    public ArrayList<Remito> lecturaPDF() throws IOException{
        File dir = new File(Propiedades.getInstance().getPath_pdf());//CREO UN OBJETO CON TODOS LOS ARCHIVOS QUE CONTIENE LA CARPETA QUE CONTIENE LOS PDFS.
        String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
        ArrayList<Remito> list= new ArrayList<Remito>();

        if (ficheros == null)//EXCEPCION
              System.out.println("No hay archivos en la carpeta especificada");
        else { 
          for (int x=0;x<ficheros.length;x++){ //RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
            String ruta=(Propiedades.getInstance().getPath_pdf()+"/"+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER. 
            if((new File(ruta).isDirectory())){ continue;}
              try {
            	  PDDocument pd = PDDocument.load(ruta); //CARGAR EL PDF
                  List l = pd.getDocumentCatalog().getAllPages();//NUMERO LAS PAGINAS DEL ARCHIVO
                  Object[] obj = l.toArray();//METO EN UN OBJETO LA LISTA DE PAGINAS PARA MANIPULARLA
                                    
                  	int lengthAnterior=0;
                  	for(int n=0;n<obj.length;n++){
                  		PDPage page = (PDPage) obj[n];//PAGE ES LA PAGINA 1 DE LA QUE CONSTA EL ARCHIVO
                        int eigth=1024;//ALTO
                        PDFTextStripperByArea stripper = new PDFTextStripperByArea();//COMPONENTE PARA ACCESO AL TEXTO
                    	  for(int i=100;i<eigth-140;i+=10){
                        	  Remito r= new Remito();
                              Rectangle agenteRectangle = new Rectangle(0, i, 125,10);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
                              Rectangle clienteRectangle = new Rectangle(125, i, 100,10);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
                              Rectangle localidadRectangle = new Rectangle(225, i, 65,10);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
                              Rectangle direccionRectangle = new Rectangle(290, i, 120,10);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
                              Rectangle nEnvioRectangle = new Rectangle(410, i, 50,10);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
                              Rectangle bultoRectangle = new Rectangle(460, i, 60,10);//DEFNIR AREA DONDE SE BUSCARA EL TEXTO
                              stripper.addRegion("areaAgente", agenteRectangle);//REGISTRAMOS LA REGION CON UN NOMBRE
                              stripper.addRegion("areaCliente", clienteRectangle);//REGISTRAMOS LA REGION CON UN NOMBRE
                              stripper.addRegion("areaLocalidad", localidadRectangle);//REGISTRAMOS LA REGION CON UN NOMBRE
                              stripper.addRegion("areaDireccion", direccionRectangle);//REGISTRAMOS LA REGION CON UN NOMBRE
                              stripper.addRegion("areaNEnvio", nEnvioRectangle);//REGISTRAMOS LA REGION CON UN NOMBRE
                              stripper.addRegion("areaBulto", bultoRectangle);//REGISTRAMOS LA REGION CON UN NOMBRE
                              stripper.extractRegions(page);//EXTRAE TEXTO DEL AREA
                              
                              String agentes=(stripper.getTextForRegion("areaAgente"));
                              String clientes=(stripper.getTextForRegion("areaCliente"));
                              String localidades=(stripper.getTextForRegion("areaLocalidad"));
                              String direcciones=(stripper.getTextForRegion("areaDireccion"));
                              String nEnvios=(stripper.getTextForRegion("areaNEnvio"));
                              String bultos=(stripper.getTextForRegion("areaBulto"));
                              if(nEnvios.length()>0 && !nEnvios.equalsIgnoreCase("TOTAL: ") ){
	                        	  r.setAgente(agentes);
	                        	  r.setCliente(clientes);
	                        	  r.setLocalidad(localidades);
	                        	  r.setDireccion(direcciones);
	                        	  r.setnEnvio(nEnvios);
	                        	  r.setBulto(bultos);
	                        	  r.setPagina(String.valueOf(n+1));
	                        	  r.setPdf(ficheros[x]);
	                        	  r.setNumeroRegistro(String.valueOf(list.size()-lengthAnterior+1));
	                        	  list.add(r);
                              }
                    	  }

                  	}
              	  int registrosFicheroProcesado= list.size()-lengthAnterior;
              	  lengthAnterior=list.size();
                  LogFile.escribirEnLog(LogFile.LOG_EJECUCION, "Se proceso el fichero: "+ficheros[x]);
                  LogFile.escribirEnLog(LogFile.LOG_EJECUCION, "Se registraron un total de "+registrosFicheroProcesado+ " registros en el fichero");
                  LogFile.escribirEnLog(LogFile.LOG_EJECUCION, LogFile.SEPARADOR);                                                                     
                  pd.close();//CERRAMOS OBJETO ACROBAT
                  
              } catch (Exception e) {
                  if(e.toString()!=null){
                	  LogFile.escribirEnLog(LogFile.LOG_ERROR_LECTURA_PDF, "Error al leer el fichero: "+ficheros[x]);
                  }
                  System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
                  e.printStackTrace();
              }//CATCH
          } //FOR
        }//ELSE
        return list;
    }//LECTURAPDF()
    
    
}//CLASS