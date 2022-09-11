package it.polito.tdp.itunes.model;

public class AlbumBilancio implements Comparable<AlbumBilancio>{
	private Album album;
	private Double bilancio;
	
	public AlbumBilancio(Album album, Double bilancio) {
		this.album = album;
		this.bilancio = bilancio;
	}
	
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public Double getBilancio() {
		return bilancio;
	}
	public void setBilancio(Double bilancio) {
		this.bilancio = bilancio;
	}

	@Override
	public int compareTo(AlbumBilancio o) {
		return (o.bilancio.compareTo(bilancio));
	}

	@Override
	public String toString() {
		return album + ", bilancio=" + String.format("%.2f", bilancio);
	}
	
	
}
