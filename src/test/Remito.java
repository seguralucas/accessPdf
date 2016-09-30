package test;

public class Remito {
	
	private String agente;
	private String cliente;
	private long   clienteId;
	private String localidad;
	private long localidadId;
	private long ordenId;
	private String direccion;
	private String nEnvio;
	private String bulto;
	private String pdf;
	private String pagina;
	private String numeroRegistro;
	
	
	
	
	
	public long getOrdenId() {
		return ordenId;
	}
	public void setOrdenId(long ordenId) {
		this.ordenId = ordenId;
	}
	public long getClienteId() {
		return clienteId;
	}
	public void setClienteId(long clienteId) {
		this.clienteId = clienteId;
	}
	public long getLocalidadId() {
		return localidadId;
	}
	public void setLocalidadId(long localidadId) {
		this.localidadId = localidadId;
	}
	public String getPdf() {
		return pdf;
	}
	public void setPdf(String pdf) {
		this.pdf = pdf;
	}
	public String getPagina() {
		return pagina;
	}
	public void setPagina(String pagina) {
		this.pagina = pagina;
	}
	public String getNumeroRegistro() {
		return numeroRegistro;
	}
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}
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
