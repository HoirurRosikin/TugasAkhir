package PembelianBuku;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RecordPembeli {
	
	// Arraylist untuk menyimpan data pembeli, agar bisa diolah lagi
	ArrayList<Pembeli> pembeli = new ArrayList<Pembeli>();

	private JFrame frmRecordTokoBuku;
	private JTextField txtId;
	private JTextField txtNama;
	private JTextField txtJudul;
	private JTextField txtJumlah;
	private JTextField txtHarga;
	private JTable table;
	private JTextField txtCari;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecordPembeli window = new RecordPembeli();
					window.frmRecordTokoBuku.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// FUNGSI UNTUK MENCARI ID PEMBELI SAAT DESKRIPSI
	public class searchFunction{
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		public ResultSet find(String s){
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost/buku", "root", "");
				ps = con.prepareStatement("select * from belibuku where idbuku= ?");
				ps.setString(1, s);
				rs = ps.executeQuery();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			return rs;
		}
	}
	
	// FUNGSI UNTUK MENAMPILKAN DATA KE TABEL
	public void tampilData() {
		try {
			String query = "select * from belibuku";
			Connection con = (Connection)JDBC.koneksi();
			PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery(query);
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
	        model.setRowCount(0);
	        
	        String [] data = new String [5];
	        
	        while(rs.next()) {
	        	 data[0] = rs.getString("idbuku");
		         data[1] = rs.getString("nama");
		         data[2] = rs.getString("judul");
		         data[3] = rs.getString("jumlah");
		         data[4] = rs.getString("harga");
		         
	             model.addRow(data); 
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Gagal Insert Data\n"+e);
		}
	}
	
	// FUNGSI UNTUK MENGOSONGKAN FORM
	public void kosongForm() {
		txtId.setText("");
		txtNama.setText("");
		txtJudul.setText("");
		txtJumlah.setText("");
		txtHarga.setText("");
	}

	/**
	 * Create the application.
	 */
	public RecordPembeli() {
		initialize();
		tampilData();
		kosongForm();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRecordTokoBuku = new JFrame();
		frmRecordTokoBuku.setResizable(false);
		frmRecordTokoBuku.setIconImage(Toolkit.getDefaultToolkit().getImage(RecordPembeli.class.getResource("/foto/logobuku.jpg")));
		frmRecordTokoBuku.setTitle("Record Toko Buku");
		frmRecordTokoBuku.setBounds(100, 100, 700, 500);
		frmRecordTokoBuku.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRecordTokoBuku.getContentPane().setLayout(null);
		
		JPanel panelDeskripsi = new JPanel();
		panelDeskripsi.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Deskripsi Data Pembeli", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDeskripsi.setOpaque(false);
		panelDeskripsi.setBounds(410, 11, 264, 247);
		frmRecordTokoBuku.getContentPane().add(panelDeskripsi);
		panelDeskripsi.setLayout(null);
		
		txtCari = new JTextField();
		txtCari.setColumns(10);
		txtCari.setBounds(10, 52, 145, 23);
		panelDeskripsi.add(txtCari);
		
		// KODINGAN UNTUK CARI ID PEMBELI DAN TAMPILKAN KE TEXT AREA
		JButton cari = new JButton("CARI");
		cari.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						searchFunction sf = new searchFunction();
						ResultSet rs = null;
						
						rs = sf.find(txtCari.getText());
						
						try {
							if (rs.next()) {
								pembeli.add(new Pembeli(rs.getString("idbuku"), rs.getString("nama"),rs.getString("judul"),rs.getInt("jumlah"), rs.getLong("harga")));
								textArea.setText("ID Pembeli\t: "+rs.getString("idbuku")+"\nNama Pembeli\t: "+rs.getString("nama")+"\nJudul Buku\t: "+rs.getString("judul")+"\nJumlah Buku\t: "+String.valueOf(rs.getString("jumlah"))+"\nHarga Buku\t:"+String.valueOf(rs.getString("harga")));
							} else {
								JOptionPane.showMessageDialog(cari, "Tidak ada data dengan ID tersebut");
								txtCari.setText("");
								textArea.setText("");
							}
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
							txtCari.setText("");
							textArea.setText("");
						  }	
					}
				});
				
		cari.setFocusable(false);
		cari.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					cari.setForeground(new Color(255,255,255));
					cari.setBackground(new Color(75,0,130));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					cari.setForeground(new Color(0, 0, 0));
					cari.setBackground(new Color(255,255,255));
				}
				@Override
				public void mousePressed(MouseEvent e) {
					cari.setForeground(new Color(255,255,255));
					cari.setBackground(new Color(75,0,130));
				}
		});
		cari.setBackground(Color.WHITE);
		cari.setBounds(165, 52, 89, 23);
		panelDeskripsi.add(cari);
		
		JLabel lblMasukanIdPembeli = new JLabel("Masukan ID Pembeli");
		lblMasukanIdPembeli.setHorizontalAlignment(SwingConstants.CENTER);
		lblMasukanIdPembeli.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblMasukanIdPembeli.setBounds(10, 24, 142, 23);
		panelDeskripsi.add(lblMasukanIdPembeli);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 86, 244, 116);
		panelDeskripsi.add(textArea);
		
		JButton btnBersihkan = new JButton("BERSIHKAN");
		btnBersihkan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtCari.setText("");
				textArea.setText("");
			}
		});
		btnBersihkan.setFocusable(false);
		btnBersihkan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnBersihkan.setForeground(new Color(255,255,255));
				btnBersihkan.setBackground(new Color(75,0,130));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnBersihkan.setForeground(new Color(0, 0, 0));
				btnBersihkan.setBackground(new Color(255,255,255));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				btnBersihkan.setForeground(new Color(255,255,255));
				btnBersihkan.setBackground(new Color(75,0,130));
			}
	});
		btnBersihkan.setBackground(Color.WHITE);
		btnBersihkan.setBounds(145, 213, 109, 23);
		panelDeskripsi.add(btnBersihkan);
		
		JPanel panelForm = new JPanel();
		panelForm.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 2, true), "Record Data Baru", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelForm.setOpaque(false);
		panelForm.setBounds(10, 11, 390, 247);
		frmRecordTokoBuku.getContentPane().add(panelForm);
		panelForm.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("ID Pembeli");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setBounds(61, 41, 81, 21);
		panelForm.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Nama Pembeli");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1.setBounds(61, 73, 106, 21);
		panelForm.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Judul Buku");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_1.setBounds(61, 105, 81, 21);
		panelForm.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Jumlah Buku");
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_1_1.setBounds(61, 137, 106, 21);
		panelForm.add(lblNewLabel_1_1_1_1);
		
		JLabel lblNewLabel_1_1_1_2 = new JLabel("Harga Buku");
		lblNewLabel_1_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1_1_1_2.setBounds(61, 169, 106, 21);
		panelForm.add(lblNewLabel_1_1_1_2);
		
		txtId = new JTextField();
		txtId.setBounds(202, 43, 125, 20);
		panelForm.add(txtId);
		txtId.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.setColumns(10);
		txtNama.setBounds(202, 75, 125, 20);
		panelForm.add(txtNama);
		
		txtJudul = new JTextField();
		txtJudul.setColumns(10);
		txtJudul.setBounds(202, 107, 125, 20);
		panelForm.add(txtJudul);
		
		txtJumlah = new JTextField();
		txtJumlah.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char karakter = e.getKeyChar();
				if (!(((karakter >= '0') && (karakter <= '9') || (karakter == KeyEvent.VK_BACK_SPACE) || (karakter == KeyEvent.VK_DELETE)))) {
				    e.consume();
				}
			}	
		});
		txtJumlah.setColumns(10);
		txtJumlah.setBounds(202, 139, 125, 20);
		panelForm.add(txtJumlah);
		
		txtHarga = new JTextField();
		txtHarga.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char karakter = e.getKeyChar();
				if (!(((karakter >= '0') && (karakter <= '9') || (karakter == KeyEvent.VK_BACK_SPACE) || (karakter == KeyEvent.VK_DELETE)))) {
				    e.consume();
				}
			}	
		});
		txtHarga.setColumns(10);
		txtHarga.setBounds(202, 171, 125, 20);
		panelForm.add(txtHarga);
		
		JButton simpan = new JButton("SIMPAN");
		
		// FUNGSI UNTUK MEMASUKKAN DATA BARU
		simpan.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						String sql = "insert into belibuku values('"+txtId.getText()+"','"+txtNama.getText()+"','"+txtJudul.getText()+"','"+Integer.parseInt(txtJumlah.getText())+"','"+Long.parseLong(txtHarga.getText())+"')";
						Connection con = (Connection)JDBC.koneksi();
						PreparedStatement pst = con.prepareStatement(sql);
						if (txtId.getText().equals("")) {
							JOptionPane.showMessageDialog(simpan, "ID Pembeli tidak boleh kosong!");
						} else if (txtNama.getText().equals("")) {
							JOptionPane.showMessageDialog(simpan, "Nama Pembeli tidak boleh kosong!");
						} else if (txtJudul.getText().equals("")) {
							JOptionPane.showMessageDialog(simpan, "Judul Buku tidak boleh kosong!");
						} else if (txtHarga.getText().equals("")) {
							JOptionPane.showMessageDialog(simpan, "Harga Buku tidak boleh kosong!");
						} else if (txtJumlah.getText().equals("")) {
							JOptionPane.showMessageDialog(simpan, "Jumlah Buku tidak boleh kosong!");
						} else {
							pst.execute();
							JOptionPane.showMessageDialog(simpan, "Data Barang Elektronik Tersimpan");
							tampilData();
							kosongForm();
							txtId.setEditable(true);
						} 
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(simpan, "Gagal simpan data", "KESALAHAN INPUT!!", 0);
					}
			}
		});		simpan.setFocusable(false);
		simpan.setBackground(Color.WHITE);
		simpan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				simpan.setForeground(new Color(255,255,255));
				simpan.setBackground(new Color(75,0,130));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				simpan.setForeground(new Color(0, 0, 0));
				simpan.setBackground(new Color(255,255,255));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				simpan.setForeground(new Color(255,255,255));
				simpan.setBackground(new Color(75,0,130));
			}
		});
		simpan.setBounds(53, 213, 89, 23);
		panelForm.add(simpan);
		
		JButton btnHapus = new JButton("HAPUS");
		
		// FUNGSI UNTUK MENGHAPUS DATA
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(btnHapus, "Apakah anda yakin ingin menghapus data?", "Warning!!!.", JOptionPane.YES_NO_OPTION);
				
				try {
					String query = "delete from belibuku where idbuku='"+txtId.getText()+"'";
					Connection conn = (Connection)JDBC.koneksi();
					PreparedStatement st = conn.prepareStatement(query);
					
					if (confirm==0) {
						st.executeUpdate();
						JOptionPane.showMessageDialog(btnHapus, "Berhasil menghapus data...");
						tampilData();
						kosongForm();
						txtId.setEditable(true);
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(btnHapus, "Gagal Hapus");
					System.out.println(e2);
				}
			}
		});
		btnHapus.setFocusable(false);
		btnHapus.setBackground(Color.WHITE);
		btnHapus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHapus.setForeground(new Color(255,255,255));
				btnHapus.setBackground(new Color(75,0,130));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnHapus.setForeground(new Color(0, 0, 0));
				btnHapus.setBackground(new Color(255,255,255));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				btnHapus.setForeground(new Color(255,255,255));
				btnHapus.setBackground(new Color(75,0,130));
			}
		});
		btnHapus.setBounds(249, 213, 89, 23);
		panelForm.add(btnHapus);
		
		JButton edit = new JButton("EDIT");
		
		// FUNGSI UNTUK MENGEDIT DATA
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(edit, "Yakin ingin mengubah data pembeli?", "Confirm", JOptionPane.YES_NO_OPTION);
				try {
					String query = "update belibuku set nama='"+txtNama.getText()+"',judul='"+txtJudul.getText()+"',jumlah='"+Integer.parseInt(txtJumlah.getText())+"',harga='"+Long.parseLong(txtHarga.getText())+"' where idbuku='"+txtId.getText()+"'";
					Connection conn = (Connection)JDBC.koneksi();
					PreparedStatement st = conn.prepareStatement(query);
					if (txtId.getText().equals("")) {
						JOptionPane.showMessageDialog(edit, "ID Pembeli tidak boleh kosong!");
					} else if (txtNama.getText().equals("")) {
						JOptionPane.showMessageDialog(edit, "Nama Pembeli tidak boleh kosong!");
					} else if (txtJudul.getText().equals("")) {
						JOptionPane.showMessageDialog(edit, "Judul Buku tidak boleh kosong!");
					} else if (txtHarga.getText().equals("")) {
						JOptionPane.showMessageDialog(edit, "Harga Buku tidak boleh kosong!");
					} else if (txtJumlah.getText().equals("")) {
						JOptionPane.showMessageDialog(edit, "Jumlah Buku tidak boleh kosong!");
					} else if (confirm==0) {
                        st.executeUpdate();
                        JOptionPane.showMessageDialog(edit, "Update data barang berhasil");
                        tampilData();
                        kosongForm();
                        txtId.setEditable(true);
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(edit, "Gagal update data");
					System.out.println(e2);
				}
			}
		});
		edit.setFocusable(false);
		edit.setBackground(Color.WHITE);
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				edit.setForeground(new Color(255,255,255));
				edit.setBackground(new Color(75,0,130));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				edit.setForeground(new Color(0, 0, 0));
				edit.setBackground(new Color(255,255,255));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				edit.setForeground(new Color(255,255,255));
				edit.setBackground(new Color(75,0,130));
			}
		});
		edit.setBounds(152, 213, 89, 23);
		panelForm.add(edit);
		
		JPanel panelTabel = new JPanel();
		panelTabel.setBorder(new LineBorder(Color.BLACK, 2));
		panelTabel.setOpaque(false);
		panelTabel.setBounds(10, 269, 664, 181);
		frmRecordTokoBuku.getContentPane().add(panelTabel);
		panelTabel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Tabel Record Data");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel.setBounds(249, 11, 142, 23);
		panelTabel.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 40, 644, 130);
		panelTabel.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID Pembeli", "Nama Pembeli", "Judul Buku", "Jumlah Buku", "Harga Buku"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6583557841625223379L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Integer.class, Long.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, true, true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(table);
		
		// FUNGSI UNTUK KETIKA KLIK BARI TABEL, DATANYA AKAN DI KIRIM KE FORM UNTUK DIEDIT ATAU HAPUS
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				boolean set = false;
				txtId.setEditable(set);
				
				int row = table.rowAtPoint(e.getPoint());
				
				String id = table.getValueAt(row, 0).toString();
				String nama = table.getValueAt(row, 1).toString();
				String judul = table.getValueAt(row, 2).toString();
				String jumlah = table.getValueAt(row, 3).toString();
				String harga = table.getValueAt(row, 4).toString();
				
				
				txtId.setText(String.valueOf(id));
				txtNama.setText(String.valueOf(nama));
				txtJudul.setText(String.valueOf(judul));
				txtJumlah.setText(String.valueOf(jumlah));
				txtHarga.setText(String.valueOf(harga));
			}
		});
		
		JLabel background = new JLabel("");
		background.setHorizontalAlignment(SwingConstants.TRAILING);
		background.setIcon(new ImageIcon(RecordPembeli.class.getResource("/foto/latar.jpg")));
		background.setBounds(0, 0, 684, 461);
		frmRecordTokoBuku.getContentPane().add(background);
	}
}
