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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;


public class smoothing extends JFrame {

	JPanel contentPane;
	Rectangle captureRect;

	/**
	 * Create the frame.
	 */
	public smoothing(String imgPath) {
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JLabel lblPicture = new JLabel("");
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
		
		final BufferedImage final_img = img;   // have to make a final version 
		
		final BufferedImage screenCopy = new BufferedImage( // a copy version of the image
				final_img.getWidth(),
				final_img.getHeight(),
				final_img.getType());
		
		
		lblPicture.setIcon(new ImageIcon(screenCopy));
		
		JScrollPane screenScroll = new JScrollPane(lblPicture);   // Scroll Pane stores the image label
		screenScroll.setBounds(6,6,661,476);
		
		contentPane.add(screenScroll);
		
		JLabel lblSelection = new JLabel("Drag a rectangle to be smoothed");
		lblSelection.setBounds(29, 498, 592, 50);
		contentPane.add(lblSelection);
		

		
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

}
