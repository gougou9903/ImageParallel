import java.awt.EventQueue;
import java.awt.ItemSelectable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;

import java.awt.Color;
import java.io.File;

import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;


public class UI extends JFrame implements ActionListener, ItemListener {

	private JFrame frame;
	File[] sf;
	JButton btnES = new JButton("Enlarge-Smoothing");
	JButton btnChooseFile = new JButton("Choose File");
	private final JLabel lblChosenFile = new JLabel("Chosen File:");
	private final JLabel lblChosenFileContent = new JLabel("");
	private final JLabel lblProcessingMethod = new JLabel("Processing Method: ");
	private final JComboBox comboBoxPM = new JComboBox();
	private final JLabel lblEnlargeRatio = new JLabel("Enlarge Ratio: ");
	private JTextField textFieldER;

	String method = "Smoothing";
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 630, 461);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		btnES.addActionListener(this);
		btnES.setBounds(458, 404, 166, 29);
		frame.getContentPane().add(btnES);
		btnChooseFile.setForeground(Color.BLACK);
		btnChooseFile.setBackground(Color.RED);
		
		btnChooseFile.addActionListener(this);
		btnChooseFile.setBounds(6, 404, 117, 29);
		frame.getContentPane().add(btnChooseFile);
		lblChosenFile.setBounds(6, 187, 85, 50);
		
		frame.getContentPane().add(lblChosenFile);
		lblChosenFileContent.setOpaque(true);
		lblChosenFileContent.setVerticalAlignment(SwingConstants.TOP);
		lblChosenFileContent.setBackground(Color.ORANGE);
		lblChosenFileContent.setBounds(107, 187, 504, 100);
		
		frame.getContentPane().add(lblChosenFileContent);
		lblProcessingMethod.setBounds(6, 27, 127, 50);
		
		frame.getContentPane().add(lblProcessingMethod);
		comboBoxPM.setBounds(139, 27, 133, 50);
		
		frame.getContentPane().add(comboBoxPM);
		comboBoxPM.addItem("Smoothing");
		comboBoxPM.addItemListener(this);
		lblEnlargeRatio.setBounds(309, 27, 90, 50);
		
		frame.getContentPane().add(lblEnlargeRatio);
		
		textFieldER = new JTextField();
		textFieldER.setBounds(400, 38, 68, 28);
		frame.getContentPane().add(textFieldER);
		textFieldER.setColumns(10);
		textFieldER.setText("1");
		textFieldER.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnChooseFile){
			JFileChooser chooser = new JFileChooser();
    		chooser.setMultiSelectionEnabled(true);
    		int option = chooser.showOpenDialog(this);
    		sf = chooser.getSelectedFiles();
    		String filelist = "nothing";
            if (sf.length > 0) filelist = "<html>" + sf[0].getName();
            for(int i = 1; i< sf.length; i++){
            	if((i+1)%6 == 0){
            		filelist += "<br>" + sf[i].getName();
            	}else{
            	filelist += ", " + sf[i].getName();
            	}
            }
            lblChosenFileContent.setText(filelist);
		}else if(e.getSource() == btnES){
			if(sf.length == 1){
			smoothing smoothing = new smoothing(sf[0].getPath());
			System.out.println(sf[0].getPath());
			smoothing.setVisible(true);
			}else
				JOptionPane.showMessageDialog(null, "please use one image");
		}
	}
	
	public void itemStateChanged(ItemEvent i)
    {
		if(i.getSource() == comboBoxPM){
			int state = i.getStateChange();
			if(state == ItemEvent.SELECTED){
				ItemSelectable is = i.getItemSelectable();
		    	Object select[] = is.getSelectedObjects();
		    	this.method = (String)select[0];
		    	System.out.println(method);
			}
		}
    }
}


