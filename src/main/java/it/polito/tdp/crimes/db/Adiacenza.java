package it.polito.tdp.crimes.db;

public class Adiacenza implements Comparable<Adiacenza>{
	String id1;
	String id2;
	Integer peso;
	public Adiacenza(String id1, String id2, Integer peso) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso = peso;
	}
	public String getId1() {
		return id1;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public String getId2() {
		return id2;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return id1 + " -- " + id2 + " | " + peso + "\n";
	}
	@Override
	public int compareTo(Adiacenza other) {
		return this.getPeso().compareTo(other.getPeso());
	}
	

}
