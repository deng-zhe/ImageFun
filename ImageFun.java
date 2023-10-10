
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageFun extends JFrame {
    private static BufferedImage image;
    //input image
    private static final ImagePanel canvas = new ImagePanel();
    // image show and select class
    private String filePath="";
    //source image file path
    public static ImageHandler imgHandler;
    //image process class
    public ImageFun() {
        super("Image Fun");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        //container of buttons
        buttonPanel.setPreferredSize(new Dimension(1100, 40));
        buttonPanel.setLayout(new FlowLayout());
        //Create menu bar
        JMenuBar menuBar = CreateMenuBar();
        setJMenuBar(menuBar);
        // Create ToolBar
        JToolBar fileToolBar = CreateFileToolBar();
        buttonPanel.add(fileToolBar);
        JToolBar editToolBar = CreateEditToolBar();
        buttonPanel.add(editToolBar);
        JToolBar adjustToolBar = CreateAdjustToolBar();
        buttonPanel.add(adjustToolBar);
        JToolBar filterToolBar = CreateFilterToolBar();
        buttonPanel.add(filterToolBar);
        add(buttonPanel,BorderLayout.NORTH);
        add(canvas);

    }


    private JToolBar CreateFileToolBar() {
        //toolbar for file operating
        JToolBar fileToolBar = new JToolBar();

        JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> OpenImage());
        fileToolBar.add(openButton);

        JButton reloadButton = new JButton("Reset");
        reloadButton.addActionListener(e -> LoadImage());
        fileToolBar.add(reloadButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> SaveImage());
        fileToolBar.add(saveButton);

        JButton saveAsButton = new JButton("SaveAs");
        saveAsButton.addActionListener(e -> SaveImageAs());
        fileToolBar.add(saveAsButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        fileToolBar.add(exitButton);

       return fileToolBar;
    }
    private JToolBar CreateEditToolBar() {
        //toolbar for image trans
        JToolBar editToolBar = new JToolBar();

        JButton rotateButton = new JButton("Rotate90");
        rotateButton.addActionListener(e -> RotateImage(90));
        editToolBar.add(rotateButton);

        JButton mirrorXButton = new JButton("MirrorX");
        mirrorXButton.addActionListener(e -> MirrorImage('X'));
        editToolBar.add(mirrorXButton);

        JButton mirrorYButton = new JButton("MirrorY");
        mirrorYButton.addActionListener(e -> MirrorImage('Y'));
        editToolBar.add(mirrorYButton);

        JButton invertButton = new JButton("Reverse");
        invertButton.addActionListener(e -> InvertImage());
        editToolBar.add(invertButton);

        JButton ResizeButton = new JButton("Resize");
        ResizeButton.addActionListener(e -> ResizeImage());
        editToolBar.add(ResizeButton);
        return editToolBar;
    }

    private JToolBar CreateAdjustToolBar() {
        //toolbar for image adjust
        JToolBar adjustToolBar = new JToolBar();

        JButton sharpenButton = new JButton("Sharpen");
        sharpenButton.addActionListener(e -> SharpenImage());
        adjustToolBar.add(sharpenButton);

        JButton blurButton = new JButton("Blur");
        blurButton.addActionListener(e -> BlurImage());
        adjustToolBar.add(blurButton);

        JButton contrastButton = new JButton("Contrast");
        contrastButton.addActionListener(e -> ContrastImage());
        adjustToolBar.add(contrastButton);

        JButton brightnessButton = new JButton("Brightness");
        brightnessButton.addActionListener(e -> BrightImage());
        adjustToolBar.add(brightnessButton);

        JButton darknessButton = new JButton("Enhance");
        darknessButton.addActionListener(e -> DarkImage());
        adjustToolBar.add(darknessButton);

        return adjustToolBar;
    }
    private JToolBar CreateFilterToolBar() {
        //toolbar for image filter
        JToolBar filterToolBar = new JToolBar();

        JButton grayButton = new JButton("Gray");
        grayButton.addActionListener(e -> GrayImage());
        filterToolBar.add(grayButton);

        JButton SketchButton = new JButton("Sketch");
        SketchButton.addActionListener(e -> SketchImage());
        filterToolBar.add(SketchButton);

        JButton deNoiseButton = new JButton("DeNoise");
        deNoiseButton.addActionListener(e -> DeNoiseImage());
        filterToolBar.add(deNoiseButton);

        JButton pixelButton = new JButton("Pixel");
        pixelButton.addActionListener(e -> PixelImage());
        filterToolBar.add(pixelButton);


        return filterToolBar;
    }
    private JMenuBar CreateMenuBar() {
        //create menu bar and items
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Trans");
        JMenu adjustMenu = new JMenu("Adjust");
        JMenu filterMenu = new JMenu("Filter");

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> OpenImage());
        JMenuItem reloadItem = new JMenuItem("Reset");
        reloadItem.addActionListener(e -> LoadImage());
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> SaveImage());
        JMenuItem saveAsItem = new JMenuItem("SaveAs");
        saveAsItem.addActionListener(e -> SaveImageAs());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        JMenuItem mirrorXItem = new JMenuItem("MirrorX");
        mirrorXItem.addActionListener(e -> MirrorImage('X'));
        JMenuItem resizeItem = new JMenuItem("Resize");
        resizeItem.addActionListener(e -> ResizeImage());
        JMenuItem rotateItem = new JMenuItem("Rotate90");
        rotateItem.addActionListener(e -> RotateImage(90));
        JMenuItem mirrorYItem = new JMenuItem("MirrorY");
        mirrorYItem.addActionListener(e -> MirrorImage('Y'));
        JMenuItem invertItem = new JMenuItem("Revers");
        invertItem.addActionListener(e -> InvertImage());
        JMenuItem sharpenItem = new JMenuItem("Sharpen");
        sharpenItem.addActionListener(e -> SharpenImage());
        JMenuItem blurItem = new JMenuItem("Blur");
        blurItem.addActionListener(e -> BlurImage());
        JMenuItem contrastItem = new JMenuItem("Contrast");
        contrastItem.addActionListener(e -> ContrastImage());
        JMenuItem brightnessItem = new JMenuItem("Brightness");
        brightnessItem.addActionListener(e -> BrightImage());
        JMenuItem enhanceItem = new JMenuItem("Enhance");
        enhanceItem.addActionListener(e -> DarkImage());
        JMenuItem grayItem = new JMenuItem("Gray");
        grayItem.addActionListener(e -> GrayImage());
        JMenuItem SketchItem = new JMenuItem("Sketch");
        SketchItem.addActionListener(e -> SketchImage());

        JMenuItem deNoiseItem = new JMenuItem("DeNoise");
        deNoiseItem.addActionListener(e -> DeNoiseImage());

        // Add menu items to menus
        fileMenu.add(openItem);
        fileMenu.add(reloadItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        editMenu.add(rotateItem);
        editMenu.add(mirrorXItem);
        editMenu.add(mirrorYItem);
        editMenu.add(invertItem);
        editMenu.add(resizeItem);

        adjustMenu.add(sharpenItem);
        adjustMenu.add(blurItem);
        adjustMenu.add(contrastItem);
        adjustMenu.add(brightnessItem);

        filterMenu.add(SketchItem);
        filterMenu.add(grayItem);
        filterMenu.add(deNoiseItem);
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(adjustMenu);
        menuBar.add(filterMenu);
        return menuBar;
    }

    private void OpenImage() {
        //pick a file for input
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("jpg,png,bmp files", "jpg", "png","bmp"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath=selectedFile.getAbsolutePath();
            LoadImage();
        }
    }
    private void LoadImage() {
        //open image file and display it
            if(!filePath.isEmpty()) {
                try {
                    image = ImageIO.read(new File(filePath));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
                imgHandler = new ImageHandler(image);
                setTitle("Image Editor--" + filePath);
                canvas.chgImage(image);
                canvas.repaint();
            }
    }
    private void SaveImageAs(){
        //save image file as a new name
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as a file");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("jpg,png,bmp files", "jpg", "png","bmp"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String savePath = selectedFile.getAbsolutePath();
            if (!savePath.isEmpty()) {
                String fileFormat = savePath.substring(savePath.lastIndexOf('.')+1);
                try {
                    ImageIO.write(image, fileFormat, new File(savePath));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        }
    }
    private void SaveImage(){
        //save the image
        if (!filePath.isEmpty()) {
            String fileFormat = filePath.substring(filePath.lastIndexOf('.')+1);
            try {
                ImageIO.write(image, fileFormat, new File(filePath));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        }
    }
    private static void RotateImage(double angle) {
        //Rotate the image with an angle
        imgHandler.image=image;
        image=imgHandler.Rotate(angle);
        RePaint();
    }
    private static void MirrorImage(char axis) {
        // mirror image at an axis
        imgHandler.image=image;
        image=imgHandler.Mirror(axis);
        RePaint();
    }
    private static void InvertImage(){
        //reverse image color
        imgHandler.image=image;
        image=imgHandler.Reverse();
        RePaint();
    }
    private static void ResizeImage(){
        //change image size(pixels)
        imgHandler.image=image;
        image=imgHandler.Resize();
        RePaint();
    }
    private static void GrayImage(){
        //trans image to a gray value image
        imgHandler.image=image;
        image=imgHandler.Color2Gray();
        RePaint();
    }
    private static void RePaint() {
        //redraw image with a scale factor fit the panel size
        canvas.chgImage(image);
        canvas.zoom=1;
        canvas.repaint();
    }
    private static void SharpenImage() {
        //process the image with a sharpen filter
        imgHandler.image =image;
        image=imgHandler.Sharpen();
        RePaint();
    }
    private static void BlurImage() {
        //process the image with a blur filter
        imgHandler.image =image;
        image=imgHandler.Blur();
        RePaint();
    }
    private static void SketchImage() {
        //process the image to sketch style
        imgHandler.image =image;
        image=imgHandler.convertToSketchStyle();
        RePaint();
    }

    private static void DarkImage() {
        // make the image dark
        imgHandler.image =image;
        image=imgHandler.adjustDarkness(0.05f);
        RePaint();
    }

    private static void ContrastImage() {
        //adjust the contrast of image
        imgHandler.image =image;
        image=imgHandler.adjustContrast(0.05f);
        RePaint();
    }
    private static void DeNoiseImage() {
        //decrease the noise
        imgHandler.image =image;
        image=imgHandler.DeNoise2();
        RePaint();
    }
    private static void PixelImage() {
        //process image to pixel style or put a mask on select area
        String PixelCountStr = JOptionPane.showInputDialog(null, "Pixel Count", "Pixel Count", JOptionPane.QUESTION_MESSAGE);
        int PixelCount = Integer.parseInt(PixelCountStr);
        if (PixelCount < 2 ) {
            return;
        }
        imgHandler.image =image;
        if (canvas.selchg)
        {
            image=imgHandler.PartPixel(PixelCount, canvas.pixmap);
        }
        else
        {
            image=imgHandler.Pixel(PixelCount);
        }
        RePaint();
    }
    private static void BrightImage() {
        //make the image more bright
        imgHandler.image =image;
        image=imgHandler.adjustBrightness(0.05f );
        RePaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageFun().setVisible(true));
    }
    //main process

}