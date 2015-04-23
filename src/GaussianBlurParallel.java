import java.awt.Color;
import java.awt.image.BufferedImage;


public class GaussianBlurParallel {
		private final static int THREADS = 8;
		Thread[] thread = new Thread[THREADS];
		int height;
		int width;
		int n;
		BufferedImage bufferImage;
		BufferedImage tempImage;
		BufferedImage filteredImage;
		double[] window;

    public BufferedImage gaussianBlur(BufferedImage image,double sigma) throws InterruptedException {
    	
    	this.bufferImage = image;
        this.height = image.getHeight(null);
        this.width = image.getWidth(null);

        tempImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        filteredImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        //--->>
        n = (int) (6 * sigma + 1);

        window = new double[n];
        double s2 = 2 * sigma * sigma;

        window[(n - 1) / 2] = 1;
        for (int i = 0; i < (n - 1) / 2; i++) {
            window[i] = Math.exp((double) (-i * i) / (double) s2);
            window[n - i - 1] = window[i];
        }

        //--->>
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                double sum = 0;
//                double[] colorRgbArray = new double[]{0, 0, 0};
//                for (int k = 0; k < window.length; k++) {
//                    int l = i + k - (n - 1) / 2;
//                    if (l >= 0 && l < width) {
//                        Color imageColor = new Color(image.getRGB(l, j));
//                        colorRgbArray[0] = colorRgbArray[0] + imageColor.getRed() * window[k];
//                        colorRgbArray[1] = colorRgbArray[1] + imageColor.getGreen() * window[k];
//                        colorRgbArray[2] = colorRgbArray[2] + imageColor.getBlue() * window[k];
//                        sum += window[k];
//                    }
//                }
//                for (int t = 0; t < 3; t++) {
//                    colorRgbArray[t] = colorRgbArray[t] / sum;
//                }
//                Color tmpColor = new Color((int) colorRgbArray[0], (int) colorRgbArray[1], (int) colorRgbArray[2]);
//                tempImage.setRGB(i, j, tmpColor.getRGB());
//            }
//        }
        
        
        
        
        
        thread[0] = new Thread1();
        thread[1] = new Thread2();
        thread[2] = new Thread3();
        thread[3] = new Thread4();
        thread[4] = new Thread5();
        thread[5] = new Thread6();
        thread[6] = new Thread7();
        thread[7] = new Thread8();
        
        
        thread[0].start();
        thread[1].start();
        thread[2].start();
        thread[3].start();
        
        thread[0].join();
        thread[1].join();
        thread[2].join();
        thread[3].join();
        
        
        
        
        
        
        //--->>
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                double sum2 = 0;
//                double[] colorRgbArray2 = new double[]{0, 0, 0};
//                for (int k = 0; k < window.length; k++) {
//                    int l = j + k - (n - 1) / 2;
//                    if (l >= 0 && l < height) {
//                        Color imageColor = new Color(tempImage.getRGB(i, l));
//                        colorRgbArray2[0] = colorRgbArray2[0] + imageColor.getRed() * window[k];
//                        colorRgbArray2[1] = colorRgbArray2[1] + imageColor.getGreen() * window[k];
//                        colorRgbArray2[2] = colorRgbArray2[2] + imageColor.getBlue() * window[k];
//                        sum2 += window[k];
//                    }
//                }
//                for (int t = 0; t < 3; t++) {
//                    colorRgbArray2[t] = colorRgbArray2[t] / sum2;
//                }
//                Color tmpColor = new Color((int) colorRgbArray2[0], (int) colorRgbArray2[1], (int) colorRgbArray2[2]);
//                filteredImage.setRGB(i, j, tmpColor.getRGB());
//            }
//        }
        
        thread[4].start();
        thread[5].start();
        thread[6].start();
        thread[7].start();
        
        thread[4].join();
        thread[5].join();
        thread[6].join();
        thread[7].join();
        

        return filteredImage;
    }
    
    class Thread1 extends Thread{
    	
    	public void run() {
        for (int i = 0; i < width/2; i++) {
          for (int j = 0; j < height/2; j++) {
        	  double sum = 0;
              double[] colorRgbArray = new double[]{0, 0, 0};
              
              for (int k = 0; k < window.length; k++) {
                  int l = i + k - (n - 1) / 2;
                  if (l >= 0 && l < width) {
                      Color imageColor = new Color(bufferImage.getRGB(l, j));
                      colorRgbArray[0] = colorRgbArray[0] + imageColor.getRed() * window[k];
                      colorRgbArray[1] = colorRgbArray[1] + imageColor.getGreen() * window[k];
                      colorRgbArray[2] = colorRgbArray[2] + imageColor.getBlue() * window[k];
                      sum += window[k];
                  }
              }
              for (int t = 0; t < 3; t++) {
                  colorRgbArray[t] = colorRgbArray[t] / sum;
              }
              Color tmpColor = new Color((int) colorRgbArray[0], (int) colorRgbArray[1], (int) colorRgbArray[2]);
              tempImage.setRGB(i, j, tmpColor.getRGB());
          }
      }
        System.out.println("Thread1");
    	}
    }
    
    
    class Thread2 extends Thread{
    	public void run() {
    		for (int i = width/2; i < width; i++) {
                for (int j = 0; j < height/2; j++) {
                	double sum = 0;
                    double[] colorRgbArray = new double[]{0, 0, 0};
                    
                    for (int k = 0; k < window.length; k++) {
                        int l = i + k - (n - 1) / 2;
                        if (l >= 0 && l < width) {
                            Color imageColor = new Color(bufferImage.getRGB(l, j));
                            colorRgbArray[0] = colorRgbArray[0] + imageColor.getRed() * window[k];
                            colorRgbArray[1] = colorRgbArray[1] + imageColor.getGreen() * window[k];
                            colorRgbArray[2] = colorRgbArray[2] + imageColor.getBlue() * window[k];
                            sum += window[k];
                        }
                    }
                    for (int t = 0; t < 3; t++) {
                        colorRgbArray[t] = colorRgbArray[t] / sum;
                    }
                    Color tmpColor = new Color((int) colorRgbArray[0], (int) colorRgbArray[1], (int) colorRgbArray[2]);
                    tempImage.setRGB(i, j, tmpColor.getRGB());
                }
            }
    		System.out.println("Thread2");
    	}
    }
    
    class Thread3 extends Thread{
    	public void run(){
    		for (int i = 0; i < width/2; i++) {
                for (int j = height/2; j < height; j++) {
                	double sum = 0;
                    double[] colorRgbArray = new double[]{0, 0, 0};
                    
                    for (int k = 0; k < window.length; k++) {
                        int l = i + k - (n - 1) / 2;
                        if (l >= 0 && l < width) {
                            Color imageColor = new Color(bufferImage.getRGB(l, j));
                            colorRgbArray[0] = colorRgbArray[0] + imageColor.getRed() * window[k];
                            colorRgbArray[1] = colorRgbArray[1] + imageColor.getGreen() * window[k];
                            colorRgbArray[2] = colorRgbArray[2] + imageColor.getBlue() * window[k];
                            sum += window[k];
                        }
                    }
                    for (int t = 0; t < 3; t++) {
                        colorRgbArray[t] = colorRgbArray[t] / sum;
                    }
                    Color tmpColor = new Color((int) colorRgbArray[0], (int) colorRgbArray[1], (int) colorRgbArray[2]);
                    tempImage.setRGB(i, j, tmpColor.getRGB());
                }
            }
    		System.out.println("Thread3");
    	}
    }
    
    class Thread4 extends Thread{
    	public void run(){
    		for (int i = width/2; i < width; i++) {
                for (int j = height/2; j < height; j++) {
                	double sum = 0;
                    double[] colorRgbArray = new double[]{0, 0, 0};
                    
                    for (int k = 0; k < window.length; k++) {
                        int l = i + k - (n - 1) / 2;
                        if (l >= 0 && l < width) {
                            Color imageColor = new Color(bufferImage.getRGB(l, j));
                            colorRgbArray[0] = colorRgbArray[0] + imageColor.getRed() * window[k];
                            colorRgbArray[1] = colorRgbArray[1] + imageColor.getGreen() * window[k];
                            colorRgbArray[2] = colorRgbArray[2] + imageColor.getBlue() * window[k];
                            sum += window[k];
                        }
                    }
                    for (int t = 0; t < 3; t++) {
                        colorRgbArray[t] = colorRgbArray[t] / sum;
                    }
                    Color tmpColor = new Color((int) colorRgbArray[0], (int) colorRgbArray[1], (int) colorRgbArray[2]);
                    tempImage.setRGB(i, j, tmpColor.getRGB());
                }
            }
    		System.out.println("Thread4");
    	}
    }
    
    class Thread5 extends Thread{
    	public void run(){
    		for (int i = 0; i < width/2; i++) {
                for (int j = 0; j < height/2; j++) {
                    double sum2 = 0;
                    double[] colorRgbArray2 = new double[]{0, 0, 0};
                    for (int k = 0; k < window.length; k++) {
                        int l = j + k - (n - 1) / 2;
                        if (l >= 0 && l < height) {
                            Color imageColor = new Color(tempImage.getRGB(i, l));
                            colorRgbArray2[0] = colorRgbArray2[0] + imageColor.getRed() * window[k];
                            colorRgbArray2[1] = colorRgbArray2[1] + imageColor.getGreen() * window[k];
                            colorRgbArray2[2] = colorRgbArray2[2] + imageColor.getBlue() * window[k];
                            sum2 += window[k];
                        }
                    }
                    for (int t = 0; t < 3; t++) {
                        colorRgbArray2[t] = colorRgbArray2[t] / sum2;
                    }
                    Color tmpColor = new Color((int) colorRgbArray2[0], (int) colorRgbArray2[1], (int) colorRgbArray2[2]);
                    filteredImage.setRGB(i, j, tmpColor.getRGB());
                }
            }
    		System.out.println("Thread5");
    	}
    }
    
    class Thread6 extends Thread{
    	public void run(){
    		for (int i = width/2; i < width; i++) {
                for (int j = 0; j < height/2; j++) {
                    double sum2 = 0;
                    double[] colorRgbArray2 = new double[]{0, 0, 0};
                    for (int k = 0; k < window.length; k++) {
                        int l = j + k - (n - 1) / 2;
                        if (l >= 0 && l < height) {
                            Color imageColor = new Color(tempImage.getRGB(i, l));
                            colorRgbArray2[0] = colorRgbArray2[0] + imageColor.getRed() * window[k];
                            colorRgbArray2[1] = colorRgbArray2[1] + imageColor.getGreen() * window[k];
                            colorRgbArray2[2] = colorRgbArray2[2] + imageColor.getBlue() * window[k];
                            sum2 += window[k];
                        }
                    }
                    for (int t = 0; t < 3; t++) {
                        colorRgbArray2[t] = colorRgbArray2[t] / sum2;
                    }
                    Color tmpColor = new Color((int) colorRgbArray2[0], (int) colorRgbArray2[1], (int) colorRgbArray2[2]);
                    filteredImage.setRGB(i, j, tmpColor.getRGB());
                }
            }
    		System.out.println("Thread6");
    	}
    }
    
    class Thread7 extends Thread{
    	public void run(){
    		for (int i = 0; i < width/2; i++) {
                for (int j = height/2; j < height; j++) {
                    double sum2 = 0;
                    double[] colorRgbArray2 = new double[]{0, 0, 0};
                    for (int k = 0; k < window.length; k++) {
                        int l = j + k - (n - 1) / 2;
                        if (l >= 0 && l < height) {
                            Color imageColor = new Color(tempImage.getRGB(i, l));
                            colorRgbArray2[0] = colorRgbArray2[0] + imageColor.getRed() * window[k];
                            colorRgbArray2[1] = colorRgbArray2[1] + imageColor.getGreen() * window[k];
                            colorRgbArray2[2] = colorRgbArray2[2] + imageColor.getBlue() * window[k];
                            sum2 += window[k];
                        }
                    }
                    for (int t = 0; t < 3; t++) {
                        colorRgbArray2[t] = colorRgbArray2[t] / sum2;
                    }
                    Color tmpColor = new Color((int) colorRgbArray2[0], (int) colorRgbArray2[1], (int) colorRgbArray2[2]);
                    filteredImage.setRGB(i, j, tmpColor.getRGB());
                }
            }
    		System.out.println("Thread7");
    	}
    }
    
    class Thread8 extends Thread{
    	public void run(){
    		for (int i = width/2; i < width; i++) {
                for (int j = height/2; j < height; j++) {
                    double sum2 = 0;
                    double[] colorRgbArray2 = new double[]{0, 0, 0};
                    for (int k = 0; k < window.length; k++) {
                        int l = j + k - (n - 1) / 2;
                        if (l >= 0 && l < height) {
                            Color imageColor = new Color(tempImage.getRGB(i, l));
                            colorRgbArray2[0] = colorRgbArray2[0] + imageColor.getRed() * window[k];
                            colorRgbArray2[1] = colorRgbArray2[1] + imageColor.getGreen() * window[k];
                            colorRgbArray2[2] = colorRgbArray2[2] + imageColor.getBlue() * window[k];
                            sum2 += window[k];
                        }
                    }
                    for (int t = 0; t < 3; t++) {
                        colorRgbArray2[t] = colorRgbArray2[t] / sum2;
                    }
                    Color tmpColor = new Color((int) colorRgbArray2[0], (int) colorRgbArray2[1], (int) colorRgbArray2[2]);
                    filteredImage.setRGB(i, j, tmpColor.getRGB());
                }
            }
    		System.out.println("Thread8");
    	}
    }
    
}