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
import java.util.HashMap;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
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

public class PantailaNagusia {

	private JFrame frmPhidgetDenda;
	private JPasswordField pwdAdminPwd;
	private JTextField txtAdminErab;
	private JTable tblInbentarioa;
	private JTextField txtInbIzena;
	private JTextField txtInbKopurua;
	private JTextField txtInbBalioa;
	private JTextField txtInbId;
	
	//bestelako konponenteak
	JLabel lblAdminIzena;
	JLabel lblAdminArgazkia;
	JLabel lblAdminMota;
	JLabel lblAdminSarrera;
	JLabel lblAdminInfo;
	JLabel lblAdminEr1;
	JLabel lblAdminEr2;
	JLabel lblAdminId;
	JLabel lblInbInfo;
	
	JButton btnAdminAmaitu;
	JButton btnAdminHasi;
	JButton btnInbEzabatu;
	JButton btnInbBalioa;
	JButton btnInbGehitu;
	
	//panelak
	JTabbedPane tabbedPane;
	JPanel panelAdmin;
	JPanel panelDenda;
	JPanel panelInbentarioa;
	
	//dendako itemak, erabiltzaileak...
	private HashMap<String, Saltzailea> saltzaileZerrenda = new HashMap<String, Saltzailea>();
	private HashMap<String, Eroslea> erosleZerrenda = new HashMap<String, Eroslea>();
	private HashMap<String, Produktua> produktuZerrenda = new HashMap<String, Produktua>();
	DefaultTableModel dtm;
	String header[] = new String[] {"ID", "Izena", "Kopurua", "Prezioa"};
	
	//saioaren datuak
	private Saltzailea saioSaltzailea;
	
	//txartel irakurtzailea
	RFID ch;

	public PantailaNagusia() {
		initialize();
		saltzaileZerrenda.put("Giltzak1", new Saltzailea("Giltzak1", "Jon Arzelus Rodriguez", "Saltzaile Arrunta", "1234", "Saltzaile1.png"));
		saltzaileZerrenda.put("Giltzak2", new Saltzailea("Giltzak2", "Julen Diez Martin", "Saltzaile Arrunta", "1234", "Saltzaile2.png"));
		//produktu zerrenda
		produktuZerrenda.put("0a00ec5ce2", new Produktua("0a00ec5ce2","Botoi Beltza",10.0));
		produktuZerrenda.put("4d004a650f", new Produktua("4d004a650f","Baldosa Berdea",10.0));
		produktuZerrenda.put("4d004b17c3", new Produktua("4d004b17c3","Txakurrarentzako Kolgantea",10.0));
		produktuZerrenda.put("0f000a7398", new Produktua("0f000a7398","Diska Zuria - Txikia",10.0));
		produktuZerrenda.put("4300d02f4a", new Produktua("4300d02f4a","Iltze Beltza",10.0));
		produktuZerrenda.put("0f0001b130", new Produktua("0f0001b130","Diska Zuria - Handia",10.0));
		produktuZerrenda.put("4800eef486", new Produktua("4800eef486","Txorien Kontrolerako Aparatua",10.0));
		produktuZerrenda.put("3f0060b6b6", new Produktua("3f0060b6b6","Roska Tapoi Beltza",10.0));
		saioSaltzailea=null;
		try {
			ch = new RFID();
			ch.addTagListener(new RFIDTagListener() {
				public void onTag(RFIDTagEvent e) {
					if(tabbedPane.getSelectedIndex()==0) { //administrazio panela
						if(saioSaltzailea==null) {
							System.out.println("Tag read: " + e.getTag());
							txtAdminErab.setText(e.getTag());
							pwdAdminPwd.setEnabled(true);
							btnAdminHasi.setEnabled(true);
							lblAdminInfo.setText("Giltza detektatuta, mesedez, sartu pasahitza saioa hasteko");
						}
					} else if(tabbedPane.getSelectedIndex()==2) { //inbentario panela
						if(produktuZerrenda.get(e.getTag())==null) {
							lblInbInfo.setText("ID: "+e.getTag()+" detektatuta, inbentarioan gehitzeko osatu datuak eta sakatu \"Produktua Gehitu\" botoia");
							tblInbentarioa.clearSelection();
							txtInbIzena.setText("");
							txtInbKopurua.setText("");
							txtInbBalioa.setText("");
							txtInbId.setText(e.getTag());
							btnInbEzabatu.setEnabled(false);
							btnInbBalioa.setEnabled(false);
							btnInbGehitu.setEnabled(true);
						} else {
							lblInbInfo.setText("ID: "+e.getTag()+" detektatuta, inbentarioan gehituta dagoeneko ("+produktuZerrenda.get(e.getTag()).getIzena()+")");
							int i = 0;
							while(!tblInbentarioa.getValueAt(i, 0).toString().equals(e.getTag())) {
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
					if(tabbedPane.getSelectedIndex()==0) { //administrazio panela
						if(saioSaltzailea==null) {
							System.out.println("Tag lost: " + e.getTag());
							txtAdminErab.setText("");
							pwdAdminPwd.setText("");
							lblAdminEr1.setText("");
							lblAdminEr2.setText("");
							pwdAdminPwd.setEnabled(false);
							btnAdminHasi.setEnabled(false);
							lblAdminInfo.setText("Giltza detekzioa galduta. Mesedez, urbildu identifikazio giltza saioa hasteko");
						}
					} else if(tabbedPane.getSelectedIndex()==2) { //inbentario panela
						if(produktuZerrenda.get(e.getTag())==null) {
							lblInbInfo.setText("ID: "+e.getTag()+" detektoretik aldendua. Inbentarioan gehitzeko osatu datuak eta sakatu \"Produktua Gehitu\" botoia");
						} else {
							lblInbInfo.setText("Produktu berriak gehitzeko hurbildu produktua detektorera");
						}
					}
				}
	        });
			
			ch.open();
			
		} catch(PhidgetException ex) {
			ex.printStackTrace();
		}
		
		//saioa hasteko
		Random r = new Random();
		if(r.nextBoolean())
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
					if(tabbedPane.getSelectedIndex()==2) {
						for(String k: produktuZerrenda.keySet()) {
							Produktua temp = produktuZerrenda.get(k);
							dtm.addRow(new Object[] {temp.getId(),temp.getIzena(),temp.getKopurua(),temp.getPrezioa()});
						}
					} else {
						while(dtm.getRowCount()>0)
							dtm.removeRow(0);
						lblInbInfo.setText("Produktu berriak gehitzeko hurbildu produktua detektorera");
						txtInbId.setText("");
						txtInbIzena.setText("");
						txtInbKopurua.setText("");
						txtInbBalioa.setText("");
						btnInbBalioa.setEnabled(false);
						btnInbEzabatu.setEnabled(false);
					}
				} catch(Exception ex) {//taula hasieratzean errorea ekiditeko
					//ex.printStackTrace();
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
				Saltzailea temp;
				if(saltzaileZerrenda.get(txtAdminErab.getText())!=null) {
					if(saltzaileZerrenda.get(txtAdminErab.getText().toString()).getPasahitza().equals(pwdAdminPwd.getText().toString())) {
						saioaHasi();
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
		
		lblAdminInfo = new JLabel("Mesedez, urbildu identifikazio giltza saioa hasteko");
		lblAdminInfo.setForeground(Color.BLUE);
		lblAdminInfo.setBounds(10, 303, 644, 14);
		panelAdmin.add(lblAdminInfo);
		
		panelDenda = new JPanel();
		tabbedPane.addTab("Denda", null, panelDenda, null);
		panelDenda.setLayout(null);
		
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
				if(row!=-1) {
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

		/*tblInbentarioa.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"ID", "Izena", "Kopurua", "Balioa"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});*/
		//taularen ediziorako
		dtm = new DefaultTableModel(0,0);
		dtm.setColumnIdentifiers(header);
		tblInbentarioa.setModel(dtm);
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
					produktuZerrenda.remove(tblInbentarioa.getModel().getValueAt(tblInbentarioa.getSelectedRow(), 0).toString());
					tabbedPane.setSelectedIndex(-1);
					tabbedPane.setSelectedIndex(2);
					lblInbInfo.setText("Produktua modu egokian ezabatu da");
				} catch(Exception ex) {
					lblInbInfo.setText("Ezin izan da produktua ezabatu. Mesedez, kontaktatu administrariarekin eta eman 0xheh211221h errore kodea ;)");
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
					produktuZerrenda.replace(txtInbId.getText(),produktuZerrenda.get(txtInbId.getText()), new Produktua(txtInbId.getText(),txtInbIzena.getText(),Integer.parseInt(txtInbKopurua.getText()),Double.parseDouble(txtInbBalioa.getText())));
					tabbedPane.setSelectedIndex(-1);
					tabbedPane.setSelectedIndex(2);
					lblInbInfo.setText("Produktua modu egokian eguneratu da");
				} catch(Exception ex) {
					lblInbInfo.setText("Sartutako balioak ez dira egokiak eta ezin izan da produktua eguneratu. Mesedez, saiatu berriro");
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
					produktuZerrenda.put(txtInbId.getText(),new Produktua(txtInbId.getText(),txtInbIzena.getText(),Integer.parseInt(txtInbKopurua.getText()),Double.parseDouble(txtInbBalioa.getText())));
					tabbedPane.setSelectedIndex(-1);
					tabbedPane.setSelectedIndex(2);
					lblInbInfo.setText("Produktua modu egokian gehitu da inbentariora");
				} catch(Exception ex) {
					lblInbInfo.setText("Sartutako balioak ez dira egokiak eta ezin izan da produktua inbentarioan gehitu. Mesedez, saiatu berriro");
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
	
	public void erakutsiLogin() {
		
	}
	
	public void saioaHasi() {
		saioSaltzailea = saltzaileZerrenda.get(txtAdminErab.getText());
		ImageIcon ii = new ImageIcon(this.getClass().getResource(saioSaltzailea.getIrudia())); //pistaren irudia label batean jarri, fondo modura
		lblAdminArgazkia.setIcon(ii);
		lblAdminId.setText(saioSaltzailea.getId());
		lblAdminIzena.setText(saioSaltzailea.getIzena());
		lblAdminMota.setText(saioSaltzailea.getMota());
		if(saioSaltzailea.getAzkenSarrera()!=null) {
			lblAdminSarrera.setText(saioSaltzailea.getAzkenSarrera().toString());
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
		//panelDenda.setEnabled(true);
		//panelInbentarioa.setEnabled(true);
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
		//btnAdminHasi.setEnabled(true);
		txtAdminErab.setEnabled(true);
		//pwdAdminPwd.setEnabled(true);
		lblAdminInfo.setText("Mesedez, urbildu identifikazio giltza saioa hasteko");
		lblAdminEr1.setText("");
		lblAdminEr2.setText("");
		btnAdminAmaitu.setEnabled(false);
		tabbedPane.setEnabled(false);
		//panelDenda.setEnabled(false);
		//panelInbentarioa.setEnabled(false);
	}
}
