package com.ia.model;

public class Parametros {

	private Integer numIndividuos;
	private Integer numMaxGeneraciones;
	private Integer numGenResumen;
	private Double probMutacion;
	public Parametros() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Parametros(Integer numIndividuos, Integer numMaxGeneraciones, Integer numGenResumen, Double probMutacion) {
		super();
		this.numIndividuos = numIndividuos;
		this.numMaxGeneraciones = numMaxGeneraciones;
		this.numGenResumen = numGenResumen;
		this.probMutacion = probMutacion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numGenResumen == null) ? 0 : numGenResumen.hashCode());
		result = prime * result + ((numIndividuos == null) ? 0 : numIndividuos.hashCode());
		result = prime * result + ((numMaxGeneraciones == null) ? 0 : numMaxGeneraciones.hashCode());
		result = prime * result + ((probMutacion == null) ? 0 : probMutacion.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parametros other = (Parametros) obj;
		if (numGenResumen == null) {
			if (other.numGenResumen != null)
				return false;
		} else if (!numGenResumen.equals(other.numGenResumen))
			return false;
		if (numIndividuos == null) {
			if (other.numIndividuos != null)
				return false;
		} else if (!numIndividuos.equals(other.numIndividuos))
			return false;
		if (numMaxGeneraciones == null) {
			if (other.numMaxGeneraciones != null)
				return false;
		} else if (!numMaxGeneraciones.equals(other.numMaxGeneraciones))
			return false;
		if (probMutacion == null) {
			if (other.probMutacion != null)
				return false;
		} else if (!probMutacion.equals(other.probMutacion))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Parametros [numIndividuos=" + numIndividuos + ", numMaxGeneraciones=" + numMaxGeneraciones
				+ ", numGenResumen=" + numGenResumen + ", probMutacion=" + probMutacion + "]";
	}
	public Integer getNumIndividuos() {
		return numIndividuos;
	}
	public void setNumIndividuos(Integer numIndividuos) {
		this.numIndividuos = numIndividuos;
	}
	public Integer getNumMaxGeneraciones() {
		return numMaxGeneraciones;
	}
	public void setNumMaxGeneraciones(Integer numMaxGeneraciones) {
		this.numMaxGeneraciones = numMaxGeneraciones;
	}
	public Integer getNumGenResumen() {
		return numGenResumen;
	}
	public void setNumGenResumen(Integer numGenResumen) {
		this.numGenResumen = numGenResumen;
	}
	public Double getProbMutacion() {
		return probMutacion;
	}
	public void setProbMutacion(Double probMutacion) {
		this.probMutacion = probMutacion;
	}
	
	
}
