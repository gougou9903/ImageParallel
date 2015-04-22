import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class smoothing extends JFrame implements ActionListener{

	JPanel contentPane;
	Rectangle captureRect;
	JButton btnProcess;
	final BufferedImage final_img;
	final JLabel lblPicture;
	
	/**
	 * Create the frame.
	 */
	public smoothing(String imgPath) {
		setBounds(100, 100, 1200, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblPicture = new JLabel("");
		lblPicture.setOpaque(true);
//		lblPicture.setBounds(6, 6, 661, 476);
		
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
		    e.printStackTrace();
		}
//		Image dimg = img.getScaledInstance(lblPicture.getWidth(), lblPicture.getHeight(), Image.SCALE_SMOOTH);
//		ImageIcon imageIcon = new ImageIcon(dimg);
//		lblPicture.setIcon(imageIcon);
		
		final_img = img;   // have to make a final version 
		
		final BufferedImage screenCopy = new BufferedImage( // a copy version of the image
				final_img.getWidth(),
				final_img.getHeight(),
				final_img.getType());
		
		
		lblPicture.setIcon(new ImageIcon(screenCopy));
		
		JScrollPane screenScroll = new JScrollPane(lblPicture);   // Scroll Pane stores the image label
		screenScroll.setBounds(6,6,1188,640);
		
		contentPane.add(screenScroll);
		
		JLabel lblSelection = new JLabel("Drag a rectangle to be smoothed"); // information label
		lblSelection.setBounds(23, 658, 592, 50);
		contentPane.add(lblSelection);
		
		btnProcess = new JButton("Process");  // process button
		btnProcess.addActionListener(this);
		btnProcess.setBounds(1077, 670, 117, 29);
		contentPane.add(btnProcess);
		
		

		
		repaint(final_img,screenCopy);
		lblPicture.repaint();
		
		lblPicture.addMouseMotionListener(new MouseMotionAdapter() {
			Point start = new Point();
			
			@Override
            public void mouseMoved(MouseEvent me) {
				start = me.getPoint();
				repaint(final_img,screenCopy);
				lblSelection.setText("Start Point: " + start);
				
				
				lblPicture.repaint();
			}
			
			@Override
            public void mouseDragged(MouseEvent me) {
				Point end = me.getPoint();
				captureRect = new Rectangle(start,
                        new Dimension(end.x-start.x, end.y-start.y));
				repaint(final_img, screenCopy);
				lblPicture.repaint();
				lblSelection.setText("Rectangle: " + captureRect);
			}
			
		});
		
		
		
	}
	
	
	
	public void repaint(BufferedImage orig, BufferedImage copy){
		Graphics2D g = copy.createGraphics();
        g.drawImage(orig,0,0, null);
        if (captureRect!=null) {
            g.setColor(Color.RED);
            g.draw(captureRect);
            g.setColor(new Color(255,255,255,150));
            g.fill(captureRect);
        }
        g.dispose();
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedImage smoothingPartSource;
		BufferedImage smoothingPartTarget;
		BufferedImage smoothingTarget = final_img;
		BufferedImage enlargeTarget;
		
		if(e.getSource() == btnProcess){
			long startTime = System.nanoTime();
			try{
				long start = System.currentTimeMillis();
				
				/*Gaussian Blur*/
				smoothingPartSource = final_img.getSubimage(captureRect.x, captureRect.y, captureRect.width, captureRect.height);
				GaussianBlur GB = new GaussianBlur();
				smoothingPartTarget = GB.gaussianBlur(smoothingPartSource, 1.4);
				
				int w = smoothingPartTarget.getWidth();
				int h = smoothingPartTarget.getHeight();
				
				/*put partTarget back to Target*/
				for(int i=0 ; i < w; i++){
					for(int j=0 ; j < h; j++){
						smoothingTarget.setRGB(captureRect.x + i, captureRect.y + j, smoothingPartTarget.getRGB(i, j));
					}
				}
				
				/*enlarge this smoothed image*/
				float ratio = 2f;
				Enlarge E = new Enlarge(smoothingTarget,ratio);
				enlargeTarget = E.enlarge();
				
			lblPicture.setIcon(new ImageIcon(enlargeTarget));
			
			long time = System.currentTimeMillis() - start;
			System.out.println("Smoothing and enlarging time: "+time+ " ms");
			
			}catch(RasterFormatException d){
				JOptionPane.showMessageDialog(null, "please select an area first");
			} 
			
//			catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
			long totalTime = (System.nanoTime() - startTime)/1000000;
			System.out.println("\nSmoothing and Enlarging: " + totalTime + "ms");
			
			}
		
	}



}
