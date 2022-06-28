package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO dao = new ImdbDAO();
	private Graph<Director, DefaultWeightedEdge> grafo;
	public int totAttoriCondivisi;
	
	public Model() {
		this.totAttoriCondivisi = 0;
	}
	
	private List<Director> listaMigliore;

	public String creaGrafo(int anno) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//creo vertici
		Graphs.addAllVertices(this.grafo, dao.listAllDirectors(anno));
		
		//creo archi
		List<Adiacenza> adiacenza = new ArrayList<>();
		adiacenza = dao.getArchi(anno);
		
		for (Adiacenza a: adiacenza) {
			if(a.getPeso()>0) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
			}
		}
		return "Grafo creato con: " +this.grafo.vertexSet().size()+ " vertici e " +this.grafo.edgeSet().size()+ " archi.";
	}
	
	public Set<Director> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public List<Vicini> getVicini(Director d){
		List<Director> vicini= new ArrayList<>();
		vicini = Graphs.neighborListOf(this.grafo, d);
		List<Vicini> a = new ArrayList<>();
		int peso;
		for(Director v: vicini) {
			peso = (int) this.grafo.getEdgeWeight(this.grafo.getEdge(d, v));
			a.add(new Vicini(v, peso));
		}
		return a;
	}
	
	public List<Director> calcolaPercorsoMigliore(Director d, int c) {
		//recupero la componenete connessa di c
			this.listaMigliore = new ArrayList<>();
			List<Director> parziale = new ArrayList<>();
			
			parziale.add(d);
			
			cerca(parziale, c);	
			return listaMigliore;

	}

	private void cerca(List<Director> parziale, int c) {
		
		if(parziale.size()>listaMigliore.size()) {
			listaMigliore = new ArrayList<Director>(parziale);
			this.totAttoriCondivisi = sommaPesi(this.listaMigliore);
			
		}
		
		List<Director> vicini = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
		
		for(Director d : vicini) {
			if(!parziale.contains(d) && (sommaPesi(parziale)+this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1),d)))<=c) {
				parziale.add(d) ;
				cerca(parziale, c) ;
				parziale.remove(parziale.size()-1) ;
			}
			}
		}
		

	private int sommaPesi(List<Director> parziale) {
		int peso = 0;
		for(int i=1; i<parziale.size(); i++) {
			double p = this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i))) ;
			peso += p ;
		}
		return peso ;
	}
}
