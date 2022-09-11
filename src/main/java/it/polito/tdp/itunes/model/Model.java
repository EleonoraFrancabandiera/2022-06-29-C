package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Album, DefaultWeightedEdge> grafo;
	private Map<Integer, Album> idMap;
	
	private List<Album> listaMigliore;
	
	public Model() {
		dao= new ItunesDAO();
		idMap = new HashMap<>();
		
		this.dao.getAllAlbums(idMap);
	}
	
	public void creaGrafo(double prezzo){
		//creo il grafo
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici--> identity map
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(prezzo, idMap));
		
		//aggiungo gli archi
		for(Album a1: this.grafo.vertexSet()) {
			for(Album a2 : this.grafo.vertexSet()) {
				if(a1.getAlbumId()<a2.getAlbumId()) {
					double peso = a1.getPrezzo()-a2.getPrezzo();
					if(peso>0) {//se il prezzo di a1> prezzo a2
						Graphs.addEdgeWithVertices(this.grafo, a1, a2, peso);
					}else if(peso<0) {
						Graphs.addEdgeWithVertices(this.grafo, a1, a2, (-1)*peso);
					}
				}
			}
		}
		
		System.out.println("Grafo creato!");
		System.out.println(String.format("# Vertici: %d", this.grafo.vertexSet().size()));
		System.out.println(String.format("# Archi: %d", this.grafo.edgeSet().size()));
		
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo==null)
			return false;
		return true;
	}
	
	public List<Album> getVertici() {
		List<Album> vertici = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}
	
	public List<AlbumBilancio> getAdiacenze(Album a1){
		List<Album> adiacenti = Graphs.neighborListOf(this.grafo, a1);
		List<AlbumBilancio> result = new ArrayList<>();
		
		for(Album adiacente: adiacenti) {
			AlbumBilancio ab = this.calcolaBilancio(adiacente);
			result.add(ab);
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	private AlbumBilancio calcolaBilancio(Album a) {
		List<Album> adiacenti = Graphs.neighborListOf(this.grafo, a);
		
		double sommaPesi=0;
		int numArchi=adiacenti.size();
		
		for(Album adiacente: adiacenti) {
			sommaPesi+=this.grafo.getEdgeWeight(this.grafo.getEdge(adiacente, a));
		}
		
		double bilancio= sommaPesi/numArchi;
		
		return new AlbumBilancio(a, bilancio);		
		
	}
	
	public List<Album> calcolaPercorso(Album a1, Album a2, double x) {
		List<Album> albumValidi = new ArrayList<Album>();
		ConnectivityInspector<Album, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		
		albumValidi.addAll(ci.connectedSetOf(a1));
		boolean trovato= false;
		for(Album a: albumValidi) {
			if(a.getAlbumId()==a2.getAlbumId()) {
				trovato=true;
				break;
			}
		}
		if(!trovato) {
			return null;
		}
		
		List<Album> parziale = new ArrayList<>();
		listaMigliore= new ArrayList<>();
		parziale.add(a1);
		
		cerca(parziale, albumValidi, a1, a2, x);
		
		return listaMigliore;
	}
	
	private void cerca(List<Album> parziale, List<Album> albumValidi, Album a1, Album a2, double x) {
		double bilancio1 = this.calcolaBilancio(a1).getBilancio();	
		int bilancioP=0;
		int bilancioMigliore=0;
		
		
		
		for(Album a: parziale) {
			double b = this.calcolaBilancio(a).getBilancio();
			if(b>bilancio1) {
				bilancioP++;
			}
		}
		
		for(Album a: listaMigliore) {
			double b = this.calcolaBilancio(a).getBilancio();
			if(b>bilancio1) {
				bilancioMigliore++;
			}
		}
		
		if(bilancioP>bilancioMigliore) {
			DefaultWeightedEdge ultimoArco = this.grafo.getEdge(parziale.get(parziale.size()-1), a2);
			if(ultimoArco!=null) {
				parziale.add(a2);
				listaMigliore = new ArrayList<>(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
		for(Album a: albumValidi) {
			if(!a.equals(a2)) {
				if(!parziale.contains(a)) {
					DefaultWeightedEdge arco = this.grafo.getEdge(parziale.get(parziale.size()-1), a);
					if(arco!=null) {
						double peso = this.grafo.getEdgeWeight(arco);
						if(peso>=x) {
							parziale.add(a);
							cerca(parziale, albumValidi, a1, a2, x);
							parziale.remove(parziale.size()-1);
						}
					}
				}
				
			}
		}
		
	}
	
}
