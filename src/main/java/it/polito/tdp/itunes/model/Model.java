package it.polito.tdp.itunes.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private Map<Integer, Album> AlbumIDMap;
	private Map<Integer, Track> TrackIDMap;
	private Map<Integer, Playlist> PlaylistIDMap;
	private ItunesDAO dao;
	private Graph<Album, DefaultEdge> grafo;

	public Model() {
		this.dao = new ItunesDAO();

	}

	public void BuildGrafo(int durata) {
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		int durataMillis = durata*60*1000;

		// Creazione vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(durataMillis));
		for (Album nodo : this.grafo.vertexSet()) {
			for (Album nodo2 : this.grafo.vertexSet()) {
				if (nodo != nodo2) {
					Set<Integer> intersezione = new HashSet<>(this.dao.playlistAlbum(nodo));
					Set<Integer> playlist2 = new HashSet<>(this.dao.playlistAlbum(nodo2));
					intersezione.retainAll(playlist2);
					if (intersezione.size()>0) {
						this.grafo.addEdge(nodo, nodo2);
					}
				}
			}
		}


		}
		
	
	public boolean isCreated() {
		return (this.grafo.vertexSet().size()>0);
	}
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	public int numEdges() {
		return this.grafo.edgeSet().size();
	}
	public Set<Album> componenteConnessa(Album a) {
		ConnectivityInspector<Album, DefaultEdge> connectivity = new ConnectivityInspector<>(grafo);
		Set<Album> componenteConnessa = connectivity.connectedSetOf(a);
		return componenteConnessa;
	}
	
	public List<Album>getVertici(){
		List<Album> result = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(result);
		return result;
	}
}
