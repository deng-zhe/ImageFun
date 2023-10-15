import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import static java.lang.Math.abs;

public class ImagePanel extends JPanel {
    //display and select image
    public BufferedImage image;
    //input image
    public BufferedImage selectMap=null;
    //select image
    public double zoom=1;
    //display zoom factor
    public int Width;
    //image width
    public int Height;
    //image height
    public int StartX;
    //draw start x
    public int StartY;
    //draw start y
    public boolean selchg;
    //true when some areas are be selected
    public int[][] pixmap;
    //mark selected pixel

    public int[][] prevmap;
    //store the selected pixel previous click
    public ImagePanel() {
        // mouse listener to detect scroll wheel events
        addMouseWheelListener(e -> {
            //zoom the display image when scroll the mouse wheel
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    zoomOut();
                } else {
                    zoomIn();
                }
            } else if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    zoomOut();
                } else {
                    zoomIn();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(e.isShiftDown()) {
                    // while shift down act as select
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //left button click for add select area
                        int x = e.getX();
                        int y = e.getY();
                        if (selectMap == null && image != null) {
                            int width = image.getWidth();
                            int height = image.getHeight();
                            selectMap = new BufferedImage(width, height, image.getType());
                            selectMap = image;
                        }
                        int ofstx = x - StartX;
                        int ofsty = y - StartY;
                        int locationX = (int) (ofstx / zoom);
                        int locationY = (int) (ofsty / zoom);
                        //selectMap.setRGB(locationX, locationY, new Color(255, 255, 255).getRGB());
                        //selchg=true;
                        copyAry(pixmap,prevmap);
                        selectMap = pickObject(locationX, locationY, 60);
                        selchg = true;
                        repaint();
                    }
                    if (SwingUtilities.isRightMouseButton(e)) {
                        //right button to undo previous select (only once)
                        copyAry(prevmap,pixmap);
                        selectMap=RevByMap();
                        repaint();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            //draw image
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            int pnlWidth = getWidth();
            int pnlHeight = getHeight();
            double imgRadio;
            double pnlRadio;
            if (imgHeight > 0 && pnlHeight > 0 && imgWidth > 0 && pnlWidth >0 && zoom==1)
            {
                imgRadio = (double) imgWidth/imgHeight;
                pnlRadio = (double) pnlWidth/pnlHeight;
                if (imgRadio > pnlRadio)
                {
                    zoom=(double) pnlWidth/imgWidth;
                } else {
                    zoom=(double) pnlHeight/imgHeight;
                }
            }
            int DispWidth=(int) (zoom*imgWidth);
            int DispHeight=(int) (zoom*imgHeight);
            StartY = (pnlHeight-DispHeight)/2;
            StartX = (pnlWidth-DispWidth)/2;
            if (selchg)
            //if some area select draw the selected image
            {
                g.drawImage(selectMap.getScaledInstance(DispWidth,DispHeight,Image.SCALE_FAST),StartX,StartY,null);
            } else {
                g.drawImage(image.getScaledInstance(DispWidth, DispHeight, Image.SCALE_SMOOTH), StartX, StartY, null);
            }
        }
    }
    public void zoomIn() {
        zoom = zoom/0.9;
        repaint();
    }

    public void zoomOut() {
        zoom = zoom*0.9;
        repaint();
    }
    public void chgImage(BufferedImage Inimage){
        //load or reset image
        image=Inimage;
        Width = image.getWidth();
        Height= image.getHeight();
        pixmap= new int[Width][Height];
        prevmap= new int[Width][Height];
        selchg=false;
    }
    public BufferedImage pickObject(int x, int y,int diff)
        //detect object within a color range value,and save the result in an array
    {
        Color color = new Color(image.getRGB(x, y));
        int[][] borderAry = new int[Width][Height];
        for (int i =0 ;i<Width;i++)
        {
            for(int j=0;j<Height;j++)
            {
                Color color1 = new Color(image.getRGB(i,j));
                if ( abs(color.getRed()-color1.getRed()) < diff && abs(color.getGreen()-color1.getGreen()) < diff &&  abs(color.getBlue()-color1.getBlue()) <diff )
                {
                    borderAry[i][j]=1;
                }
            }
        }
        Spiral sp = new Spiral(borderAry,x,y);
        //borderAry=sp.run();
        borderAry=sp.Infect();
        selMerge(borderAry);
        return RevByMap();
    }
    private BufferedImage RevByMap()
    {
        //reverse the select pixel
        BufferedImage result=new BufferedImage(Width,Height,image.getType());
        for (int i =0 ;i<Width;i++)
        {
            for(int j=0;j<Height;j++)
            {
                if (pixmap[i][j]==2)
                {
                    Color cl = new Color(image.getRGB(i,j));
                    result.setRGB(i,j,pixelReverse(cl));
                }
                else
                {
                    result.setRGB(i,j,image.getRGB(i,j));
                }
            }
        }
        return result;
    }
    private int enLight(Color cl,int light)
            //make a pixel more light by add a value
    {
        int R=cl.getRed()+light;
        int G=cl.getGreen()+light;
        int B=cl.getBlue()+light;
        if(R>255) R=255;
        if(G>255) G=255;
        if(B>255) B=255;
        return new Color(R,G,B).getRGB();
    }
    private int pixelReverse(Color cl)
            //reverse one pixel
    {
        int R=255-cl.getRed();
        int G=255-cl.getGreen();
        int B=255-cl.getBlue();
        return new Color(R,G,B).getRGB();
    }
    private void selMerge(int[][] ary)
            //merge new select map  to the selected one.
    {
        for(int i=0;i<Width;i++)
        {
            for(int j=0;j<Height;j++)
            {
                if (ary[i][j]==2)
                {
                    pixmap[i][j]=2;
                }
            }
        }
    }
    private void copyAry(int[][] a,int[][] b)
            //copy array
    {
        for(int i=0;i<Width;i++)
        {
            for(int j=0;j<Height;j++)
            {
                b[i][j]=a[i][j];
            }
        }
    }
}