package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.Adiacenza;
import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	Graph<String, DefaultWeightedEdge> grafo;
	int pesoMin;
	List<String> listaRicorsione;
	List<Adiacenza> archiMax;
	
	public String doCreaGrafo(String offense, int year) {
		EventsDao dao =  new EventsDao();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<String> vertici = new ArrayList<>(dao.getVertici(offense, year));
		Graphs.addAllVertices(grafo, vertici);
		List<Adiacenza> archi = new ArrayList<>(dao.getArchi(offense, year));
		Collections.sort(archi);
		for(Adiacenza a : archi) {
			Graphs.addEdge(grafo, a.getId1(), a.getId2(), a.getPeso());
		}
		String result = "";
		if(this.grafo==null) {
			result ="Grafo non creato";
			return result;
		}
		result = "Grafo creato con :\n# "+this.grafo.vertexSet().size()+" VERTICI\n# "+this.grafo.edgeSet().size()+" ARCHI\n";
		int nmax = 0;
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			int p = (int) grafo.getEdgeWeight(e);
			if(p>nmax) {
				nmax=p;
			}
		}
		archiMax = new ArrayList<>();
		for(Adiacenza a: archi) {
			if(a.getPeso()==nmax) {
				archiMax.add(a);
			}
		}
		result+=archiMax;
		return result;
	}
	
	public String run(Adiacenza a) {
		pesoMin=1000;
		listaRicorsione = new ArrayList<>();
		int nVertici = grafo.vertexSet().size()-1;
		String verticePartenza = a.getId1();
		String verticeArrivo = a.getId2();
		List<String> parziale = new ArrayList<>();
		parziale.add(verticePartenza);
		cerca(nVertici,parziale,0,0,verticeArrivo);
		String result="\n\nRicorsione avvenuta!!!\nPercorso ottenuto di peso #"+pesoMin+" passando per : \n";
		for(String s: listaRicorsione) {
			result+=s+"\n";
		}
		return result;
	}
	
	public void cerca(int nVertici, List<String> parziale, int passo, int pesoParziale, String verticeArrivo) {
		if(parziale.get(passo).equals(verticeArrivo)) {
			if(nVertici==passo) {
				if(pesoParziale<pesoMin) {
					pesoMin=pesoParziale;
					listaRicorsione =new ArrayList<>(parziale);
					return;
				}
			}
			return;
		}else {
			String ultimo = parziale.get(passo);
			List<String> vicini =new ArrayList<>(Graphs.neighborListOf(grafo, ultimo));
			for(String v: vicini) {
				if(!parziale.contains(v)) {
					int peso = (int) grafo.getEdgeWeight(grafo.getEdge(ultimo, v));
					parziale.add(v);
					cerca(nVertici,parziale,passo+1,pesoParziale+peso,verticeArrivo);
					parziale.remove(passo+1);
				}
			}
		}
	}
	
	public List<String> listAllOffense(){
		EventsDao dao =  new EventsDao();
		return dao.listAllOffense();
	}
	public List<Integer> listAllYears(){
		EventsDao dao =  new EventsDao();
		return dao.listAllYears();
	}
	
	public List<DefaultWeightedEdge> getArchi(){
		List<DefaultWeightedEdge> archi = new ArrayList<>();
		return archi;
	}

	public List<Adiacenza> getArchiMax() {
		return archiMax;
	}
	
}
