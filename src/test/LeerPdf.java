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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFImageWriter;
import org.apache.pdfbox.util.PDFTextStripperByArea;
public class LeerPdf {
    public static void main(String[] args) {
        LeerPdf leerPDF =new LeerPdf();
        //leerPDF.lecturaPDF();
    //	leerPDF.leerBD();
        Properties props=null;
            try {
                 props = leerPDF.loadProperties();
			} catch (IOException e) {
				e.printStackTrace();
			}
            //leerPDF.leerBD();
            leerPDF.lecturaPDF();
          //  System.out.println(props.getProperty("path"));
          //  System.out.println(props.getProperty("pathDB"));
    }
    
    private Properties loadProperties() throws IOException{
        Properties props = new Properties();
		props.load(new FileReader("configuracion.properties"));
		return props;
    }
    
    public void leerBD(){
        try { 
     	   Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
         Connection conn=DriverManager.getConnection("jdbc:ucanaccess://H:/Users/GAS/Desktop/TR_Logistica&Distribución_v1.6_Beatriz.mdb");         
 	    System.out.println("OK");
 	    Statement st = conn.createStatement(); 
	//       String insert = "insert into Clientes(Cliente_Id,Cliente_Nombre,Cliente_Localidad_Id) values(9999,'asdasd',156)"; 
	//       st.executeUpdate(insert);
 	       String sql = "Select * from Clientes"; 
 	       ResultSet rs = st.executeQuery(sql);
 	       int i=0;
 	       File borrar= new File("H:/Users/GAS/Desktop/borrar.txt");
 	       FileWriter fw = new FileWriter(borrar);
        while(rs.next()){
     	   i++;
     	   System.out.println(i);
     	   fw.write("\n"+rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
     	   }
        fw.close();
        }
        catch (Exception e) {
     	   e.printStackTrace();
        }
    }
    
    public void lecturaPDF(){
        String ln = System.getProperty("line.separator"); 
        File dir = new File("H:/Users/GAS/Desktop/pdf");//CREO UN OBJETO CON TODOS LOS ARCHIVOS QUE CONTIENE LA CARPETA QUE CONTIENE LOS PDFS.
        String[] ficheros = dir.list();//ARREGLO QUE ALMACENARÁ TODOS LOS NOMBRES DE LOS ARCHIVOS QUE ESTAN DENTRO DEL OBJETO.
        
        if (ficheros == null)//EXCEPCION
              System.out.println("No hay archivos en la carpeta especificada");
        else { 
          for (int x=0;x<ficheros.length;x++){//RECORREMOS EL ARREGLO CON LOS NOMBRES DE ARCHIVO
            String ruta=new String();//VARIABLE QUE DETERMINARA LA RUTA DEL ARCHIVO A LEER.
            ruta=("H:/Users/GAS/Desktop/pdf/"+ficheros[x]); //SE ALMACENA LA RUTA DEL ARCHIVO A LEER. 
            
              try {
            	  PDDocument pd = PDDocument.load(ruta); //CARGAR EL PDF
                  List l = pd.getDocumentCatalog().getAllPages();//NUMERO LAS PAGINAS DEL ARCHIVO
                  Object[] obj = l.toArray();//METO EN UN OBJETO LA LISTA DE PAGINAS PARA MANIPULARLA
                                    
                  
                  ArrayList<Remito> list= new ArrayList<Remito>();
                  	for(int n=0;n<obj.length;n++){
                  		PDPage page = (PDPage) obj[n];//PAGE ES LA PAGINA 1 DE LA QUE CONSTA EL ARCHIVO
                        PageFormat pageFormat = pd.getPageFormat(0);//PROPIEDADES DE LA PAGINA (FORMATO)
                        Double d1 = new Double(pageFormat.getHeight());//ALTO
                        Double d2 = new Double(pageFormat.getWidth());//ANCHO
                        int width = d1.intValue();//ANCHO
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
	                        	  list.add(r);
                              }
                    	  }
                  	}
                  		for(Remito r:list)
                    		  System.out.println(r);
                  		System.out.println("Total de registros:" +list.size());

                  /*for(int i=1;i<agentesArray.length;i++){ //El primero lo ignoramos porque es el titulo
                	  Remito r= new Remito();
                	  r.setAgente(agentesArray[i]);
                	  r.setCliente(clientesArray[i]);
                	  r.setLocalidad(localidadesArray[i]);
                	  r.setDireccion(direccionesArray[i]);
                	  r.setnEnvio(nEnviosArray[i]);
                	  r.setBulto(bultosArray[i]);
                	  System.out.println(r);
                  }*/
                                                                       
                  pd.close();//CERRAMOS OBJETO ACROBAT
              } catch (IOException e) {
                  if(e.toString()!=null){
                    File archivo=new File("dañado_"+ficheros[x]+".txt");//SEPARA LOS DAÑADOS
                  }
                  System.out.println("Archivo dañado "+ficheros[x]);// INDICA EN CONSOLA CUALES SON LOS DAÑADOS
                  e.printStackTrace();
              }//CATCH
          }//FOR
        }//ELSE
    }//LECTURAPDF()
}//CLASS