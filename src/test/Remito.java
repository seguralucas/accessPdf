package test;

public class Remito {
	private String agente;
	private String cliente;
	private String localidad;
	private String direccion;
	private String nEnvio;
	private String bulto;
	
	
	public String getAgente() {
		return agente;
	}
	public void setAgente(String agente) {
		this.agente = agente;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getnEnvio() {
		return nEnvio;
	}
	public void setnEnvio(String nEnvio) {
		this.nEnvio = nEnvio;
	}
	public String getBulto() {
		return bulto;
	}
	public void setBulto(String bulto) {
		this.bulto = bulto;
	}
	
	@Override
	public String toString() {
		return this.agente+"-"+this.cliente+"-"+this.localidad+"-"+this.direccion+"-"+this.nEnvio+"-"+this.bulto;
	}
	
	
}
