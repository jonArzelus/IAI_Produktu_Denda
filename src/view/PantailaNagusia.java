package view;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Panel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Image;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.phidget22.PhidgetException;
import com.phidget22.RFID;
import com.phidget22.RFIDTagEvent;
import com.phidget22.RFIDTagListener;
import com.phidget22.RFIDTagLostEvent;
import com.phidget22.RFIDTagLostListener;

import model.Eroslea;
import model.Produktua;
import model.Saltzailea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.SystemColor;

public class PantailaNagusia {

	private JFrame frmPhidgetDenda;
	private JPasswordField pwdAdminPwd;
	private JTextField txtAdminErab;
	private JTable tblInbentarioa;
	private JTextField txtInbIzena;
	private JTextField txtInbKopurua;
	private JTextField txtInbBalioa;
	private JTextField txtInbId;

	// bestelako konponenteak
	JLabel lblAdminIzena;
	JLabel lblAdminArgazkia;
	JLabel lblAdminMota;
	JLabel lblAdminSarrera;
	JLabel lblAdminInfo;
	JLabel lblAdminEr1;
	JLabel lblAdminEr2;
	JLabel lblAdminId;
	JLabel lblInbInfo;
	JLabel lblDendaKargaInfo;
	JLabel lblDendaTxartela;
	JLabel lblDendaDirukop;
	JLabel lblEroslearenTxartelarenInformazioa;
	JLabel lblEskuragarria;
	JLabel lblTxartelaid;

	JButton btnAdminAmaitu;
	JButton btnAdminHasi;
	JButton btnInbEzabatu;
	JButton btnInbBalioa;
	JButton btnInbGehitu;
	JButton btnDendaKargatu;

	// panelak
	JTabbedPane tabbedPane;
	JPanel panelAdmin;
	JPanel panelDenda;
	JPanel panelInbentarioa;

	// dendako itemak, erabiltzaileak...
	private HashMap<String, Saltzailea> saltzaileZerrenda = new HashMap<String, Saltzailea>();
	private HashMap<String, Eroslea> erosleZerrenda = new HashMap<String, Eroslea>();
	private HashMap<String, Produktua> produktuZerrenda = new HashMap<String, Produktua>();
	private HashMap<String, Produktua> erosketaZerrenda = new HashMap<String, Produktua>();
	DefaultTableModel dtmAdmin, dtmDenda;
	String headerAdmin[] = new String[] { "ID", "Izena", "Kopurua", "Prezioa" };
	String headerDenda[] = new String[] { "ID", "Izena", "Kopurua", "Prezioa" };

	private boolean ordaintzeko = false;

	// saioaren datuak
	private Object saioSaltzailea;

	// txartel irakurtzailea
	RFID ch;
	private JTable tblDendaErosketa;
	private JLabel lblTotala;
	private JTextField txtDendaTotala;
	private JLabel lblDendaInfo;
	private JTextField txtDendaDirua;

	public PantailaNagusia() {
		initialize();
		saltzaileZerrenda.put("Giltzak1",
				new Saltzailea("Giltzak1", "Jon Arzelus Rodriguez", "Saltzaile Arrunta", "1234", "Saltzaile1.png"));
		saltzaileZerrenda.put("Giltzak2",
				new Saltzailea("Giltzak2", "Julen Diez Martin", "Saltzaile Arrunta", "1234", "Saltzaile2.png"));
		// erosle zerrenda
		erosleZerrenda.put("Erosle1", new Eroslea("Erosle1", "Iker", "iker123", 100.35));
		erosleZerrenda.put("Erosle2", new Eroslea("Erosle2", "Marta", "marta123", 142.14));
		// produktu zerrenda
		produktuZerrenda.put("0a00ec5ce2", new Produktua("0a00ec5ce2", "Botoi Beltza", 5, 10.0));
		produktuZerrenda.put("4d004a650f", new Produktua("4d004a650f", "Baldosa Berdea", 5, 10.0));
		produktuZerrenda.put("4d004b17c3", new Produktua("4d004b17c3", "Txakurrarentzako Kolgantea", 5, 10.0));
		produktuZerrenda.put("0f000a7398", new Produktua("0f000a7398", "Diska Zuria - Txikia", 5, 10.0));
		produktuZerrenda.put("4300d02f4a", new Produktua("4300d02f4a", "Iltze Beltza", 5, 10.0));
		produktuZerrenda.put("0f0001b130", new Produktua("0f0001b130", "Diska Zuria - Handia", 5, 10.0));
		produktuZerrenda.put("4800eef486", new Produktua("4800eef486", "Txorien Kontrolerako Aparatua", 5, 10.0));
		produktuZerrenda.put("3f0060b6b6", new Produktua("3f0060b6b6", "Roska Tapoi Beltza", 5, 10.0));
		saioSaltzailea = null;
		try {
			ch = new RFID();
			ch.addTagListener(new RFIDTagListener() {
				public void onTag(RFIDTagEvent e) {
					if (tabbedPane.getSelectedIndex() == 0) { // administrazio
																// panela
						if (saioSaltzailea == null) {
							System.out.println("Tag read: " + e.getTag());
							txtAdminErab.setText(e.getTag());
							pwdAdminPwd.setEnabled(true);
							btnAdminHasi.setEnabled(true);
							lblAdminInfo.setText("Giltza detektatuta, mesedez, sartu pasahitza saioa hasteko");
						}
					} else if (tabbedPane.getSelectedIndex() == 1) {
						if (ordaintzeko) {
							if (erosleZerrenda.containsKey(e.getTag())) {
								Eroslea eros = erosleZerrenda.get(e.getTag());
								String pwd = JOptionPane.showInputDialog("Sartu txartelaren pasahitza");
								if (pwd.equalsIgnoreCase(eros.getPasahitza())) {
									if (eros.getDirua() >= Double.parseDouble(txtDendaTotala.getText())) {
										eros.setDirua(eros.getDirua() - Double.parseDouble(txtDendaTotala.getText()));
										erosleZerrenda.put(e.getTag(), eros);
										JOptionPane.showMessageDialog(tabbedPane, "Ordainduta");
										for (String erosle : erosketaZerrenda.keySet())
											erosketaZerrenda.remove(erosle);
										tabbedPane.setSelectedIndex(-1);
										tabbedPane.setSelectedIndex(1);
										ordaintzeko = false;
										txtDendaTotala.setText("0");
										lblDendaDirukop.setText(String.valueOf(((Eroslea)saioSaltzailea).getDirua())+"€");
									} else {
										JOptionPane.showMessageDialog(tabbedPane, "Txartelak ez dauka hainbeste diru");
									}
								} else {
									JOptionPane.showMessageDialog(tabbedPane, "Pasahitza ez da zuzena");
								}
							} else {
								lblDendaInfo.setText("Txartel hau ez da baliagarria, txartel berri bat sartu");
							}
							lblDendaInfo.setText("");
						} else {
							lblDendaInfo.setText("");
							if (produktuZerrenda.containsKey(e.getTag())) {
								Produktua temp = produktuZerrenda.get(e.getTag());
								if (!erosketaZerrenda.containsKey(e.getTag())) {
									if (temp.getKopurua() > 0) {
										Produktua prod = new Produktua(e.getTag(), temp.getIzena(), 1,
												temp.getPrezioa());
										erosketaZerrenda.put(e.getTag(), prod);
										tabbedPane.setSelectedIndex(-1);
										tabbedPane.setSelectedIndex(1);
										txtDendaTotala.setText(""
												+ (Double.parseDouble(txtDendaTotala.getText()) + temp.getPrezioa()));
										lblDendaInfo.setText("Produktua gehituta");
									} else {
										lblDendaInfo.setText("Ez daukagu produktu honen stock-a");
									}
								} else {
									int zenbat = erosketaZerrenda.get(e.getTag()).getKopurua();
									if (temp.getKopurua() > zenbat) {
										Produktua prod = erosketaZerrenda.get(e.getTag());
										prod.setKopurua(prod.getKopurua() + 1);
										erosketaZerrenda.put(e.getTag(), prod);
										tabbedPane.setSelectedIndex(-1);
										tabbedPane.setSelectedIndex(1);
										txtDendaTotala.setText(""
												+ (Double.parseDouble(txtDendaTotala.getText()) + temp.getPrezioa()));
										lblDendaInfo.setText("Produktua gehituta");
									} else {
										lblDendaInfo.setText("Ez dago produktu honen stock gehiago");
									}
								}
							} else {
								lblDendaInfo.setText("Produktu hori ez da gure dendako produktu bat");
							}
						}
					} else if (tabbedPane.getSelectedIndex() == 2) { // inbentario
																		// panela
						if (produktuZerrenda.get(e.getTag()) == null) {
							lblInbInfo.setText("ID: " + e.getTag()
									+ " detektatuta, inbentarioan gehitzeko osatu datuak eta sakatu \"Produktua Gehitu\" botoia");
							tblInbentarioa.clearSelection();
							txtInbIzena.setText("");
							txtInbKopurua.setText("");
							txtInbBalioa.setText("");
							txtInbId.setText(e.getTag());
							btnInbEzabatu.setEnabled(false);
							btnInbBalioa.setEnabled(false);
							btnInbGehitu.setEnabled(true);
						} else {
							lblInbInfo.setText("ID: " + e.getTag() + " detektatuta, inbentarioan gehituta dagoeneko ("
									+ produktuZerrenda.get(e.getTag()).getIzena() + ")");
							int i = 0;
							while (!tblInbentarioa.getValueAt(i, 0).toString().equals(e.getTag())) {
								i++;
								System.out.println(tblInbentarioa.getValueAt(i, 0));
							}
							tblInbentarioa.changeSelection(i, 0, false, false);
							btnInbEzabatu.setEnabled(true);
							btnInbBalioa.setEnabled(true);
							btnInbGehitu.setEnabled(false);
						}
					}

				}
			});
			ch.addTagLostListener(new RFIDTagLostListener() {
				public void onTagLost(RFIDTagLostEvent e) {
					if (tabbedPane.getSelectedIndex() == 0) { // administrazio
																// panela
						if (saioSaltzailea == null) {
							System.out.println("Tag lost: " + e.getTag());
							txtAdminErab.setText("");
							pwdAdminPwd.setText("");
							lblAdminEr1.setText("");
							lblAdminEr2.setText("");
							pwdAdminPwd.setEnabled(false);
							btnAdminHasi.setEnabled(false);
							lblAdminInfo.setText(
									"Giltza detekzioa galduta. Mesedez, hurbildu identifikazio giltza saioa hasteko");
						}
					} else if (tabbedPane.getSelectedIndex() == 2) { // inbentario
																		// panela
						if (produktuZerrenda.get(e.getTag()) == null) {
							lblInbInfo.setText("ID: " + e.getTag()
									+ " detektoretik aldendua. Inbentarioan gehitzeko osatu datuak eta sakatu \"Produktua Gehitu\" botoia");
						} else {
							lblInbInfo.setText("Produktu berriak gehitzeko hurbildu produktua detektorera");
						}
					}
				}
			});

			ch.open();

		} catch (PhidgetException ex) {
			ex.printStackTrace();
		}

		// saioa hasteko
		Random r = new Random();
		if (r.nextBoolean())
			txtAdminErab.setText("Giltzak1");
		else
			txtAdminErab.setText("Giltzak2");
		pwdAdminPwd.setText("1234");
		btnAdminHasi.setEnabled(true);
		btnAdminHasi.doClick();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPhidgetDenda = new JFrame();
		frmPhidgetDenda.setResizable(false);
		frmPhidgetDenda.setTitle("Phidget Denda");
		frmPhidgetDenda.setBounds(100, 100, 675, 385);
		frmPhidgetDenda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					if (tabbedPane.getSelectedIndex() == 2) {
						for (String k : produktuZerrenda.keySet()) {
							Produktua temp = produktuZerrenda.get(k);
							dtmAdmin.addRow(new Object[] { temp.getId(), temp.getIzena(), temp.getKopurua(),
									temp.getPrezioa() });
						}
					} else {
						while (dtmAdmin.getRowCount() > 0)
							dtmAdmin.removeRow(0);
						lblInbInfo.setText("Produktu berriak gehitzeko hurbildu produktua detektorera");
						txtInbId.setText("");
						txtInbIzena.setText("");
						txtInbKopurua.setText("");
						txtInbBalioa.setText("");
						btnInbBalioa.setEnabled(false);
						btnInbEzabatu.setEnabled(false);
					}
					if (tabbedPane.getSelectedIndex() == 1) {
						for (String k : erosketaZerrenda.keySet()) {
							Produktua temp= erosketaZerrenda.get(k);
							dtmDenda.addRow(new Object[] { temp.getId(), temp.getIzena(), temp.getKopurua(),
									temp.getPrezioa() });
						}
					} else {
						while (dtmDenda.getRowCount() > 0)
							dtmDenda.removeRow(0);
					}
				} catch (Exception ex) {// taula hasieratzean errorea ekiditeko
					// ex.printStackTrace();
				}
			}
		});
		tabbedPane.setEnabled(false);
		frmPhidgetDenda.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		panelAdmin = new JPanel();
		tabbedPane.addTab("Administrazioa", null, panelAdmin, null);
		panelAdmin.setLayout(null);

		btnAdminAmaitu = new JButton("Saioa Amaitu");
		btnAdminAmaitu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saioaAmaitu();
			}
		});
		btnAdminAmaitu.setEnabled(false);
		btnAdminAmaitu.setBounds(28, 191, 234, 64);
		panelAdmin.add(btnAdminAmaitu);

		lblAdminIzena = new JLabel("");
		lblAdminIzena.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminIzena.setBounds(359, 11, 267, 14);
		panelAdmin.add(lblAdminIzena);

		lblAdminArgazkia = new JLabel("");
		lblAdminArgazkia.setVerticalAlignment(SwingConstants.TOP);
		lblAdminArgazkia.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminArgazkia.setBounds(359, 36, 267, 169);
		panelAdmin.add(lblAdminArgazkia);

		JLabel lblErabiltzaileMota = new JLabel("Erabiltzaile Mota:");
		lblErabiltzaileMota.setBounds(359, 216, 127, 14);
		panelAdmin.add(lblErabiltzaileMota);

		JLabel lblNewLabel = new JLabel("Azken Sarrera:");
		lblNewLabel.setBounds(359, 266, 127, 14);
		panelAdmin.add(lblNewLabel);

		lblAdminMota = new JLabel("");
		lblAdminMota.setBounds(496, 216, 158, 14);
		panelAdmin.add(lblAdminMota);

		lblAdminSarrera = new JLabel("");
		lblAdminSarrera.setBounds(496, 266, 158, 14);
		panelAdmin.add(lblAdminSarrera);

		JLabel lblPasahitza = new JLabel("Pasahitza:");
		lblPasahitza.setBounds(28, 91, 84, 14);
		panelAdmin.add(lblPasahitza);

		pwdAdminPwd = new JPasswordField();
		pwdAdminPwd.setEnabled(false);
		pwdAdminPwd.setBounds(120, 88, 142, 20);
		panelAdmin.add(pwdAdminPwd);

		btnAdminHasi = new JButton("Saioa Hasi");
		btnAdminHasi.setEnabled(false);
		btnAdminHasi.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				//Saltzailea temp;
				if (saltzaileZerrenda.get(txtAdminErab.getText()) != null) {
					if (saltzaileZerrenda.get(txtAdminErab.getText().toString()).getPasahitza()
							.equals(pwdAdminPwd.getText().toString())) {
						saioaHasi();
					} else {
						lblAdminEr1.setText("");
						lblAdminEr2.setText("Pasahitza okerra, saiatu berriro");
					}
				} else if(erosleZerrenda.get(txtAdminErab.getText())!=null) {
					if (erosleZerrenda.get(txtAdminErab.getText().toString()).getPasahitza().equals(pwdAdminPwd.getText().toString())) {
						saioaHasiErosle();
					} else {
						lblAdminEr1.setText("");
						lblAdminEr2.setText("Pasahitza okerra, saiatu berriro");
					}
				} else {
					lblAdminEr2.setText("");
					lblAdminEr1.setText("Erabiltzailea ez da aurkitu");
				}
			}
		});
		btnAdminHasi.setBounds(28, 138, 234, 23);
		panelAdmin.add(btnAdminHasi);

		JLabel lblErabiltzailea = new JLabel("Erabiltzailea:");
		lblErabiltzailea.setBounds(26, 36, 84, 19);
		panelAdmin.add(lblErabiltzailea);

		txtAdminErab = new JTextField();
		txtAdminErab.setEditable(false);
		txtAdminErab.setBounds(120, 36, 142, 20);
		panelAdmin.add(txtAdminErab);
		txtAdminErab.setColumns(10);

		lblAdminEr1 = new JLabel("");
		lblAdminEr1.setForeground(Color.RED);
		lblAdminEr1.setBounds(28, 66, 234, 14);
		panelAdmin.add(lblAdminEr1);

		lblAdminEr2 = new JLabel("");
		lblAdminEr2.setForeground(Color.RED);
		lblAdminEr2.setBounds(28, 113, 234, 14);
		panelAdmin.add(lblAdminEr2);

		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(359, 241, 111, 14);
		panelAdmin.add(lblId);

		lblAdminId = new JLabel("");
		lblAdminId.setBounds(496, 241, 158, 14);
		panelAdmin.add(lblAdminId);

		lblAdminInfo = new JLabel("Mesedez, hurbildu identifikazio giltza saioa hasteko");
		lblAdminInfo.setForeground(Color.BLUE);
		lblAdminInfo.setBounds(10, 303, 644, 14);
		panelAdmin.add(lblAdminInfo);

		panelDenda = new JPanel();
		tabbedPane.addTab("Denda", null, panelDenda, null);
		panelDenda.setLayout(null);

		JLabel lblDendaArgazkia = new JLabel("");
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/view/makina.png"));
		lblDendaArgazkia.setIcon(new ImageIcon(ii.getImage().getScaledInstance(267, 169, Image.SCALE_SMOOTH)));
		lblDendaArgazkia.setVerticalAlignment(SwingConstants.TOP);
		lblDendaArgazkia.setHorizontalAlignment(SwingConstants.CENTER);
		lblDendaArgazkia.setBounds(25, 26, 267, 169);
		panelDenda.add(lblDendaArgazkia);

		dtmDenda = new DefaultTableModel(0, 0);
		dtmDenda.setColumnIdentifiers(headerDenda);
				
		tblDendaErosketa = new JTable();
		tblDendaErosketa.setModel(dtmDenda);
		tblDendaErosketa.setBounds(302, 26, 352, 169);
		panelDenda.add(tblDendaErosketa);
		
		lblTotala = new JLabel("Totala");
		lblTotala.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTotala.setBounds(489, 225, 47, 14);
		panelDenda.add(lblTotala);
		
		txtDendaTotala = new JTextField();
		txtDendaTotala.setEnabled(false);
		txtDendaTotala.setText("0");
		txtDendaTotala.setBounds(546, 224, 86, 20);
		panelDenda.add(txtDendaTotala);
		txtDendaTotala.setColumns(10);
		
		JLabel label = new JLabel("\u20AC");
		label.setBounds(642, 227, 12, 14);
		panelDenda.add(label);
		
		lblDendaInfo = new JLabel("");
		lblDendaInfo.setForeground(Color.RED);
		lblDendaInfo.setEnabled(false);
		lblDendaInfo.setBounds(24, 303, 366, 14);
		panelDenda.add(lblDendaInfo);
		
		JButton btnOrdaindu = new JButton("Ordaindu");
		btnOrdaindu.addActionListener(new ActionListener() {

	public void actionPerformed(ActionEvent arg0) {
				if(Double.parseDouble(txtDendaTotala.getText())>0){
					lblDendaInfo.setText("Pasatu zure txartela ordaintzeko");
					ordaintzeko = true;
				}
				
			}
		});
		btnOrdaindu.setBounds(546, 269, 89, 23);
		panelDenda.add(btnOrdaindu);
		
		JButton btnDendaEzabatuProduktua = new JButton("Ezabatu Produktua");
		btnDendaEzabatuProduktua.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String prodIz = tblDendaErosketa.getModel().getValueAt(tblDendaErosketa.getSelectedRow(), 0).toString();
					Produktua prod = erosketaZerrenda.get(prodIz);
					txtDendaTotala.setText("" + (Double.parseDouble(txtDendaTotala.getText()) - (prod.getPrezioa() * prod.getKopurua())));
					erosketaZerrenda.remove(prodIz);
					tabbedPane.setSelectedIndex(-1);
					tabbedPane.setSelectedIndex(1);
					lblDendaInfo.setText("Produktua modu egokian ezabatu da");
				} catch (Exception ex) {
					lblDendaInfo.setText(
							"Ezin izan da produktua ezabatu. Mesedez, kontaktatu administrariarekin eta eman 0xheh211221h errore kodea ;)");
				}
			}
		});
		btnDendaEzabatuProduktua.setBounds(302, 223, 177, 23);
		panelDenda.add(btnDendaEzabatuProduktua);
		
		lblEroslearenTxartelarenInformazioa = new JLabel("Eroslearen txartelaren informazioa");
		lblEroslearenTxartelarenInformazioa.setBounds(25, 200, 267, 14);
		panelDenda.add(lblEroslearenTxartelarenInformazioa);
		
		lblTxartelaid = new JLabel("Txartela (ID):");
		lblTxartelaid.setBounds(25, 227, 89, 14);
		panelDenda.add(lblTxartelaid);
		
		lblDendaTxartela = new JLabel("id");
		lblDendaTxartela.setBounds(113, 227, 86, 14);
		panelDenda.add(lblDendaTxartela);
		
		lblEskuragarria = new JLabel("Eskuragarria:");
		lblEskuragarria.setBounds(25, 252, 89, 14);
		panelDenda.add(lblEskuragarria);
		
		lblDendaDirukop = new JLabel("x.x\u20AC");
		lblDendaDirukop.setBounds(113, 252, 83, 14);
		panelDenda.add(lblDendaDirukop);
		
		btnDendaKargatu = new JButton("Sartu");
		btnDendaKargatu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					((Eroslea)saioSaltzailea).setDirua(((Eroslea)saioSaltzailea).getDirua()+Double.parseDouble(txtDendaDirua.getText()));
					erosleZerrenda.put(((Eroslea)saioSaltzailea).getId(),(Eroslea)saioSaltzailea);
					lblDendaDirukop.setText(String.valueOf(((Eroslea)saioSaltzailea).getDirua())+"€");
					lblDendaKargaInfo.setText("Adierazitako diru kopurua ("+txtDendaDirua.getText()+"€) kargatu da");
					txtDendaDirua.setText("");
				} catch(Exception e) {
					lblDendaKargaInfo.setText("Adierazitako kopurua ez da baliozkoa, saiatu berriro");
				}
			}
		});
		btnDendaKargatu.setBounds(206, 248, 86, 23);
		panelDenda.add(btnDendaKargatu);
		
		txtDendaDirua = new JTextField();
		txtDendaDirua.setBounds(206, 224, 86, 20);
		panelDenda.add(txtDendaDirua);
		txtDendaDirua.setColumns(10);
		
		lblDendaKargaInfo = new JLabel("info: sartu botoiak adierazitako dirua kargatuko du txartelean");
		lblDendaKargaInfo.setForeground(new Color(255, 20, 147));
		lblDendaKargaInfo.setBounds(25, 278, 511, 14);
		panelDenda.add(lblDendaKargaInfo);

		panelInbentarioa = new JPanel();
		tabbedPane.addTab("Inbentarioa", null, panelInbentarioa, null);
		panelInbentarioa.setLayout(null);

		JLabel lblDendako = new JLabel("Dendako Inbentarioa");
		lblDendako.setHorizontalAlignment(SwingConstants.CENTER);
		lblDendako.setBounds(10, 11, 455, 14);
		panelInbentarioa.add(lblDendako);

		tblInbentarioa = new JTable();
		tblInbentarioa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = tblInbentarioa.getSelectedRow();
				if (row != -1) {
					txtInbId.setText(tblInbentarioa.getModel().getValueAt(row, 0).toString());
					txtInbIzena.setText(tblInbentarioa.getModel().getValueAt(row, 1).toString());
					txtInbKopurua.setText(tblInbentarioa.getModel().getValueAt(row, 2).toString());
					txtInbBalioa.setText(tblInbentarioa.getModel().getValueAt(row, 3).toString());
					btnInbBalioa.setEnabled(true);
					btnInbEzabatu.setEnabled(true);
					btnInbGehitu.setEnabled(false);
				}
			}
		});
		tblInbentarioa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblInbentarioa.setShowGrid(false);
		tblInbentarioa.setShowHorizontalLines(false);

		/*
		 * tblInbentarioa.setModel(new DefaultTableModel( new Object[][] {
		 * {null, null, null, null}, {null, null, null, null}, {null, null,
		 * null, null}, {null, null, null, null}, {null, null, null, null},
		 * {null, null, null, null}, {null, null, null, null}, {null, null,
		 * null, null}, {null, null, null, null}, {null, null, null, null},
		 * {null, null, null, null}, {null, null, null, null}, }, new String[] {
		 * "ID", "Izena", "Kopurua", "Balioa" } ) { Class[] columnTypes = new
		 * Class[] { String.class, String.class, Integer.class, Double.class };
		 * public Class getColumnClass(int columnIndex) { return
		 * columnTypes[columnIndex]; } });
		 */
		// taularen ediziorako
		dtmAdmin = new DefaultTableModel(0, 0);
		dtmAdmin.setColumnIdentifiers(headerAdmin);
		tblInbentarioa.setModel(dtmAdmin);
		tblInbentarioa.setBounds(20, 54, 444, 212);
		tblInbentarioa.setDefaultEditor(Object.class, null);
		panelInbentarioa.add(tblInbentarioa);

		JLabel lblNewLabel_1 = new JLabel("ID");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(20, 36, 111, 14);
		panelInbentarioa.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Izena");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(130, 36, 111, 14);
		panelInbentarioa.add(lblNewLabel_2);

		JLabel lblBalioa = new JLabel("Balioa");
		lblBalioa.setHorizontalAlignment(SwingConstants.CENTER);
		lblBalioa.setBounds(354, 36, 111, 14);
		panelInbentarioa.add(lblBalioa);

		JLabel lblKopurua = new JLabel("Kopurua");
		lblKopurua.setHorizontalAlignment(SwingConstants.CENTER);
		lblKopurua.setBounds(244, 36, 111, 14);
		panelInbentarioa.add(lblKopurua);

		btnInbEzabatu = new JButton("Ezabatu Produktua");
		btnInbEzabatu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					produktuZerrenda.remove(
							tblInbentarioa.getModel().getValueAt(tblInbentarioa.getSelectedRow(), 0).toString());
					tabbedPane.setSelectedIndex(-1);
					tabbedPane.setSelectedIndex(2);
					lblInbInfo.setText("Produktua modu egokian ezabatu da");
				} catch (Exception ex) {
					lblInbInfo.setText(
							"Ezin izan da produktua ezabatu. Mesedez, kontaktatu administrariarekin eta eman 0xheh211221h errore kodea ;)");
				}
			}
		});
		btnInbEzabatu.setEnabled(false);
		btnInbEzabatu.setBounds(474, 209, 180, 23);
		panelInbentarioa.add(btnInbEzabatu);

		txtInbIzena = new JTextField();
		txtInbIzena.setToolTipText("Izena");
		txtInbIzena.setBounds(474, 82, 180, 20);
		panelInbentarioa.add(txtInbIzena);
		txtInbIzena.setColumns(10);

		txtInbKopurua = new JTextField();
		txtInbKopurua.setToolTipText("Kopurua");
		txtInbKopurua.setBounds(474, 113, 180, 20);
		panelInbentarioa.add(txtInbKopurua);
		txtInbKopurua.setColumns(10);

		txtInbBalioa = new JTextField();
		txtInbBalioa.setToolTipText("Balioa");
		txtInbBalioa.setBounds(474, 144, 180, 20);
		panelInbentarioa.add(txtInbBalioa);
		txtInbBalioa.setColumns(10);

		btnInbBalioa = new JButton("Balioa(k) Aldatu");
		btnInbBalioa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					produktuZerrenda.replace(txtInbId.getText(), produktuZerrenda.get(txtInbId.getText()),
							new Produktua(txtInbId.getText(), txtInbIzena.getText(),
									Integer.parseInt(txtInbKopurua.getText()),
									Double.parseDouble(txtInbBalioa.getText())));
					tabbedPane.setSelectedIndex(-1);
					tabbedPane.setSelectedIndex(2);
					lblInbInfo.setText("Produktua modu egokian eguneratu da");
				} catch (Exception ex) {
					lblInbInfo.setText(
							"Sartutako balioak ez dira egokiak eta ezin izan da produktua eguneratu. Mesedez, saiatu berriro");
				}
			}
		});
		btnInbBalioa.setEnabled(false);
		btnInbBalioa.setBounds(474, 175, 180, 23);
		panelInbentarioa.add(btnInbBalioa);

		JLabel lblNewLabel_3 = new JLabel("Konfigurazioa");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(475, 36, 179, 14);
		panelInbentarioa.add(lblNewLabel_3);

		lblInbInfo = new JLabel("Produktu berriak gehitzeko hurbildu produktua detektorera");
		lblInbInfo.setForeground(Color.BLUE);
		lblInbInfo.setBounds(10, 303, 644, 14);
		panelInbentarioa.add(lblInbInfo);

		txtInbId = new JTextField();
		txtInbId.setToolTipText("ID");
		txtInbId.setEditable(false);
		txtInbId.setColumns(10);
		txtInbId.setBounds(474, 54, 180, 20);
		panelInbentarioa.add(txtInbId);

		btnInbGehitu = new JButton("Produktua Gehitu");
		btnInbGehitu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					produktuZerrenda.put(txtInbId.getText(), new Produktua(txtInbId.getText(), txtInbIzena.getText(),
							Integer.parseInt(txtInbKopurua.getText()), Double.parseDouble(txtInbBalioa.getText())));
					tabbedPane.setSelectedIndex(-1);
					tabbedPane.setSelectedIndex(2);
					lblInbInfo.setText("Produktua modu egokian gehitu da inbentariora");
				} catch (Exception ex) {
					lblInbInfo.setText(
							"Sartutako balioak ez dira egokiak eta ezin izan da produktua inbentarioan gehitu. Mesedez, saiatu berriro");
				}
			}
		});
		btnInbGehitu.setEnabled(false);
		btnInbGehitu.setBounds(474, 243, 180, 23);
		panelInbentarioa.add(btnInbGehitu);
	}

	public void setVisible(boolean b) {
		frmPhidgetDenda.setVisible(b);
	}

	/*
	 * public void erakutsiLogin() {
	 * 
	 * }
	 */

	public void saioaHasi() { //saltzailearen logina
		saioSaltzailea = saltzaileZerrenda.get(txtAdminErab.getText());
		// fondoa
		ImageIcon ii = new ImageIcon(this.getClass().getResource(((Saltzailea) saioSaltzailea).getIrudia()));
		lblAdminArgazkia.setIcon(ii);
		lblAdminId.setText(((Saltzailea) saioSaltzailea).getId());
		lblAdminIzena.setText(((Saltzailea) saioSaltzailea).getIzena());
		lblAdminMota.setText(((Saltzailea) saioSaltzailea).getMota());
		if (((Saltzailea) saioSaltzailea).getAzkenSarrera() != null) {
			lblAdminSarrera.setText(((Saltzailea) saioSaltzailea).getAzkenSarrera().toString());
		} else {
			lblAdminSarrera.setText(LocalDateTime.now().toString());
		}
		btnAdminHasi.setEnabled(false);
		txtAdminErab.setEnabled(false);
		pwdAdminPwd.setEnabled(false);
		lblAdminInfo.setText("Saioa hasita. Ez ahaztu saioa amaitzen joan aurretikan");
		lblAdminEr1.setText("");
		lblAdminEr2.setText("");
		btnAdminAmaitu.setEnabled(true);
		tabbedPane.setEnabled(true);
		// panelDenda.setEnabled(true);
		// panelInbentarioa.setEnabled(true);
		
		//eroslearen asuntuak ezkutatu
		lblDendaDirukop.setVisible(false);
		lblDendaKargaInfo.setVisible(false);
		lblDendaTxartela.setVisible(false);
		lblEskuragarria.setVisible(false);
		lblTxartelaid.setVisible(false);
		txtDendaDirua.setVisible(false);
		btnDendaKargatu.setVisible(false);
		lblEroslearenTxartelarenInformazioa.setVisible(false);
		
	}
	
	public void saioaHasiErosle() {
		saioSaltzailea = erosleZerrenda.get(txtAdminErab.getText());
		// fondoa
		ImageIcon ii = new ImageIcon();
		lblAdminArgazkia.setIcon(ii);
		lblAdminId.setText(((Eroslea) saioSaltzailea).getId());
		lblAdminIzena.setText(((Eroslea) saioSaltzailea).getIzena());
		lblAdminMota.setText("Erosle arrunta"); //erosle premiun egiteko aukera?
		lblAdminSarrera.setText(LocalDateTime.now().toString());
		btnAdminHasi.setEnabled(false);
		txtAdminErab.setEnabled(false);
		pwdAdminPwd.setEnabled(false);
		lblAdminInfo.setText("Saioa hasita EROSLE bezala. Ez ahaztu saioa amaitzen joan aurretikan");
		lblAdminEr1.setText("");
		lblAdminEr2.setText("");
		btnAdminAmaitu.setEnabled(true);
		tabbedPane.setEnabled(true);
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, true);
		tabbedPane.setEnabledAt(2, false);
		
		//eroslearen asuntuak erakutsi
				lblDendaDirukop.setVisible(true);
				lblDendaDirukop.setText(String.valueOf(((Eroslea)saioSaltzailea).getDirua())+"€");
				lblDendaKargaInfo.setVisible(true);
				lblDendaKargaInfo.setText("info: sartu botoiak adierazitako dirua kargatuko du txartelean");
				lblDendaTxartela.setVisible(true);
				lblDendaTxartela.setText(((Eroslea)saioSaltzailea).getId());
				lblEskuragarria.setVisible(true);
				lblTxartelaid.setVisible(true);
				txtDendaDirua.setVisible(true);
				txtDendaDirua.setText("");
				btnDendaKargatu.setVisible(true);
				lblEroslearenTxartelarenInformazioa.setVisible(true);
	}

	public void saioaAmaitu() {
		saioSaltzailea = null;
		lblAdminArgazkia.setIcon(null);
		lblAdminId.setText("");
		lblAdminIzena.setText("");
		lblAdminMota.setText("");
		lblAdminSarrera.setText("");
		pwdAdminPwd.setText("");
		txtAdminErab.setText("");
		// btnAdminHasi.setEnabled(true);
		txtAdminErab.setEnabled(true);
		// pwdAdminPwd.setEnabled(true);
		lblAdminInfo.setText("Mesedez, hurbildu identifikazio giltza saioa hasteko");
		lblAdminEr1.setText("");
		lblAdminEr2.setText("");
		btnAdminAmaitu.setEnabled(false);
		tabbedPane.setEnabled(false);
		// panelDenda.setEnabled(false);
		// panelInbentarioa.setEnabled(false);
	}
}
