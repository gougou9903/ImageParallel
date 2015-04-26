import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
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
	float ratio;
	
	/**
	 * Create the frame.
	 */
	public smoothing(String imgPath, float ratio) {
		this.ratio = ratio;
		
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
		BufferedImage innerEnlargeTarget;
		BufferedImage outerEnlargeTarget;
		BufferedImage enlargeTarget;
		
		if(e.getSource() == btnProcess){    // process button
			long startTime = System.nanoTime();
			try{
				/*dig out the smoothing part, enlarging the rest using 4 threads*/
				BufferedImage[] subImage_out = new BufferedImage[4];        //divide this ring into 4 parts
		        BufferedImage[] subOutImage_out = new BufferedImage[4];     //store enlarged image
		        Thread[] subThreads = new Thread[4];
		        
		        int imgWidth = final_img.getWidth();
		        int imgHeight = final_img.getHeight();
		        subImage_out[0] = final_img.getSubimage(0, 0, captureRect.x, imgHeight);
		        subImage_out[1] = final_img.getSubimage(captureRect.x, 0, captureRect.width, captureRect.y);
		        subImage_out[2] = final_img.getSubimage(captureRect.x+captureRect.width, 0, imgWidth-captureRect.x-captureRect.width, imgHeight);
		        subImage_out[3] = final_img.getSubimage(captureRect.x,captureRect.y+captureRect.height,captureRect.width,imgHeight-captureRect.y-captureRect.height);
				
		        for(int i=0; i<4 ;i++){
		        	subOutImage_out[i] = new BufferedImage((int)(subImage_out[i].getWidth() * ratio)-1, (int)(subImage_out[i].getHeight() * ratio)-1,BufferedImage.TYPE_INT_RGB );
                    subThreads[i] = new Thread(new EnlargeParallel(subImage_out[i], subOutImage_out[i], ratio));
                    subThreads[i].start();
		        }
		        
		        /*Gaussian Blur*/
				smoothingPartSource = final_img.getSubimage(captureRect.x, captureRect.y, captureRect.width, captureRect.height);
				GaussianBlur GB = new GaussianBlur();
				smoothingPartTarget = GB.gaussianBlur(smoothingPartSource, 0);
				
				/*Enlarge Blurring part*/
				Enlarge E = new Enlarge(smoothingPartTarget,ratio);
				innerEnlargeTarget = E.enlarge();
		        
		        for (int i = 0; i < 4; i++) {  
		            try {
		                subThreads[i].join();
		            } catch (Exception ex) {
		            }
		        }
		        
		        
		        /*repaint 4 parts*/
		        outerEnlargeTarget = new BufferedImage((int)(imgWidth*ratio)-1, (int)(imgHeight*ratio)-1,BufferedImage.TYPE_INT_RGB);
		        Graphics g = outerEnlargeTarget.getGraphics();
		        g.drawImage(subOutImage_out[0], 0, 0, null);
		        g.drawImage(subOutImage_out[1], subOutImage_out[0].getWidth(), 0, null);
		        g.drawImage(subOutImage_out[2], subOutImage_out[0].getWidth()+subOutImage_out[1].getWidth(), 0, null);
		        g.drawImage(subOutImage_out[3], subOutImage_out[0].getWidth(), outerEnlargeTarget.getHeight()-subOutImage_out[3].getHeight(), null);
		        g.drawImage(innerEnlargeTarget, subOutImage_out[0].getWidth(),subOutImage_out[1].getHeight()+1,null);
		        g.dispose();
		        
		        
				
				
//				int w = smoothingPartTarget.getWidth();
//				int h = smoothingPartTarget.getHeight();
				
//				/*put partTarget back to Target*/
//				for(int i=0 ; i < w; i++){
//					for(int j=0 ; j < h; j++){
//						smoothingTarget.setRGB(captureRect.x + i, captureRect.y + j, smoothingPartTarget.getRGB(i, j));
//					}
//				}
				
				/*enlarge this smoothed image*/
				
//				Enlarge E = new Enlarge(smoothingTarget,ratio);
//				enlargeTarget = E.enlarge();
				
			lblPicture.setIcon(new ImageIcon(outerEnlargeTarget));
			
			
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
