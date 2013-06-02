import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.color.ColorSpace;


/**
 * Creating first instance of OCR ticket scanner
 *
 * @author god
 */
public class ImageDemo
{
    public static void main(String[] args) throws Exception
    {
        new ImageDemo("/Users/omarxf/Downloads/im1.jpg");
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }


    public ImageDemo(final String filename) throws Exception
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame editorFrame = new JFrame("Image Demo");

                editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


                BufferedImage image = null;

                try
                {
                    image = ImageIO.read(new File(filename));

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.exit(1);
                }

                /////////Adaptive Thresholding

                System.out.println(image.getColorModel());

                ///////Filter method, not used because of image quality
               /* ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                ColorConvertOp op = new ColorConvertOp(cs, null);
                image = op.filter(image, null);
                System.out.println(image.getColorModel());*/
                ///////Filter method, not used because of image quality

                ////////Binarizing Image using graphics
                BufferedImage gimage = new BufferedImage(image.getWidth(), image.getHeight(),
                        BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = gimage.getGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                ////////Binarizing Image using graphics

                System.out.println(gimage.getColorModel());

                ///Creating integral image array
                int intgrimage[][] = new int[gimage.getWidth()][gimage.getHeight()];
                ///Creating integral

                ///Creating output image using deepcopy
                BufferedImage outimage= deepCopy(gimage);
                ///Creating output image using deepcopy


                ///////////////Adaptive threshold
                /////Integral Image Calculation
                for(int i =0; i<gimage.getWidth();i++){
                    int sum =0 ;
                    for(int j =0; j<gimage.getHeight(); j++){
                        ///Calculating running sum
                        sum+=(gimage.getRaster().getSample(i,j,0));
                        //Border Check
                        if(0==i){
                            intgrimage[i][j]=sum;
                        }
                        else{
                            //Appending running sum
                            intgrimage[i][j]=intgrimage[i-1][j]+sum;
                        }
                    }
                }

                int s=35;   ////Window Size
                int t = 15;
                for(int i =0; i<gimage.getWidth();i++){
                    for(int j =0; j<gimage.getHeight(); j++){
                        ////Window definition and border checking set to truncate window
                        int x1 = i-(s/2);
                        if(x1<1)
                            x1=1;
                        int x2 = i+(s/2);
                        if(x2>gimage.getWidth()-1)
                            x2=gimage.getWidth()-1;
                        int y1 = j-(s/2);
                        if(y1<1)
                            y1=1;
                        int y2 = j+(s/2);
                        if(y2>gimage.getHeight()-1)
                            y2=gimage.getHeight()-1;
                        ////Window definition and border checking set to truncate window
                        int count = (x2-x1)*(y2-y1);
                        int sum = intgrimage[x2][y2]-intgrimage[x2][y1-1]-intgrimage[x1-1][y2]+intgrimage[x1-1][y1-1];

                        ////Actual pixel definition
                        if ((gimage.getRaster().getSample(i,j,0)*count)<=(sum*(100-t)/100)){
                            outimage.getRaster().setSample(i,j,0,0);
                        }
                        else{
                            outimage.getRaster().setSample(i,j,0,255);
                        }
                        ////Actual pixel definition
                    }
                }
                try {
                    // retrieve image
                    File outputfile = new File("/Users/omarxf/Downloads/imout.gif");
                    ImageIO.write(outimage, "gif", outputfile);
                } catch (IOException e) {

                }
                /////Integral Image Calculation
                ////////Adaptive threshold
               ///////Clean resulting image: remove isolated black pixels in window size
                for(int i =0; i<outimage.getWidth();i++){
                    for(int j =0; j<outimage.getHeight(); j++){


                        ///Defining limits based on windows and border checking
                        int tempi=i-1;
                        if (tempi<0){
                            tempi=0;
                        }
                        int tempj=j-1;
                        if (tempj<0){
                            tempj=0;
                        }
                        int tempif=i+2;
                        if (tempif>outimage.getWidth()){
                            tempif=outimage.getWidth();
                        }
                        int tempjf=j+2;
                        if (tempjf>outimage.getHeight()){
                            tempjf=outimage.getHeight();
                        }
                        boolean isolated=true;
                        for (;tempi<tempif;tempi++){
                            for (int m=tempj;m<tempjf;m++){
                                if (!(tempi==i&&m==j)){
                                    if(outimage.getRaster().getSample(tempi,m,0)==0){
                                         isolated=false;
                                    }
                                }

                            }
                        }
                        if (isolated){
                            outimage.getRaster().setSample(i,j,0,255);
                        }
                    }
                }

               ///////Clean resulting image: remove isolated black pixels
                ///////Fill operation, looking for isolated white pixels
                for(int i =0; i<outimage.getWidth();i++){
                    for(int j =0; j<outimage.getHeight(); j++){


                        ///Defining limits based on windows and border checking
                        int tempi=i-1;
                        if (tempi<0){
                            tempi=0;
                        }
                        int tempj=j-1;
                        if (tempj<0){
                            tempj=0;
                        }
                        int tempif=i+2;
                        if (tempif>outimage.getWidth()){
                            tempif=outimage.getWidth();
                        }
                        int tempjf=j+2;
                        if (tempjf>outimage.getHeight()){
                            tempjf=outimage.getHeight();
                        }
                        boolean isolated=true;
                        for (;tempi<tempif;tempi++){
                            for (int m=tempj;m<tempjf;m++){
                                if (!(tempi==i&&m==j)){
                                    if(outimage.getRaster().getSample(tempi,m,0)==255){
                                        isolated=false;
                                    }
                                }

                            }
                        }
                        if (isolated){
                            outimage.getRaster().setSample(i,j,0,0);
                        }
                    }
                }

                ///////Fill operation, looking for isolated white pixels



                ImageIcon imageIcon = new ImageIcon(outimage);
                JLabel jLabel = new JLabel();
                jLabel.setIcon(imageIcon);
                editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

                editorFrame.pack();
                editorFrame.setLocationRelativeTo(null);
                editorFrame.setVisible(true);

                try {
                    // retrieve image
                    File outputfile = new File("/Users/omarxf/Downloads/imout2.gif");
                    ImageIO.write(outimage, "gif", outputfile);
                } catch (IOException e) {

                }




            }
        });
    }
}
