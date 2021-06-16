package PembelianBuku;

public class Pembeli {
	String id;
	String nama;
	String judul;
	int jumlah;
	long harga;
	
	public Pembeli(String id, String nama, String judul, int jumlah, long harga) {
		super();
		this.id = id;
		this.nama = nama;
		this.judul = judul;
		this.jumlah = jumlah;
		this.harga = harga;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJudul() {
		return judul;
	}

	public void setJudul(String judul) {
		this.judul = judul;
	}

	public int getJumlah() {
		return jumlah;
	}

	public void setJumlah(int jumlah) {
		this.jumlah = jumlah;
	}

	public long getHarga() {
		return harga;
	}

	public void setHarga(long harga) {
		this.harga = harga;
	}

}
