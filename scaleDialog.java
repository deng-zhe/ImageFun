import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class scaleDialog extends JDialog {
    //define a dialog to get scale parameter
    public float scaleX = 0;
    public float scaleY = 0;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private final JLabel factorXLabel = new JLabel("Pixels  At  X:");
    private final  JLabel factorYLabel = new JLabel("Pixels  At  Y:");
    private final JTextField factorYField = new JTextField(13);
    private final JTextField factorXField = new JTextField(13);
    private final JToggleButton lockXYToggle = new JToggleButton("Free XY Scale");
    private final JToggleButton byPixelToggle = new JToggleButton("By  Pixels");

    public scaleDialog(Frame parent,int imgWidth,int imgHeight) {
        super(parent, "Scale Setting", true);
        minX = imgWidth/10;
        maxX = imgWidth*5;
        minY = imgHeight/10;
        maxY= imgHeight*5;
        factorYField.setText(String.valueOf(imgHeight));
        factorYField.addFocusListener( new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String txtValue= factorYField.getText();
                int intValue=0;
                try {
                    intValue= Integer.parseInt(txtValue);
                } catch (NumberFormatException ex) {
                    factorYField.setText(String.valueOf(imgHeight));
                }
                if (intValue < minY || intValue > maxY) {
                    if(byPixelToggle.isSelected()) {
                        factorYField.setText(String.valueOf(imgHeight));
                    } else {
                        factorYField.setText("100");
                    }
                }
            }
        });
        factorXField.setText(String.valueOf(imgWidth));
        factorXField.addFocusListener( new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String txtValue = factorXField.getText();
                int intValue = 0;
                try {
                    intValue = Integer.parseInt(txtValue);
                } catch (NumberFormatException ex) {
                    factorXField.setText(String.valueOf(imgWidth));
                }
                if (intValue < minX || intValue > maxX) {
                    if(byPixelToggle.isSelected()) {
                        factorXField.setText(String.valueOf(imgWidth));
                    } else {
                        factorXField.setText("100");
                    }
                }
                txtValue = factorXField.getText();
                if (lockXYToggle.isSelected()) {
                    if (byPixelToggle.isSelected()) {
                        int newHeight =  Integer.parseInt(txtValue) * imgHeight / imgWidth;
                        factorYField.setText(String.valueOf(newHeight));
                    } else {
                        factorYField.setText(String.valueOf(txtValue));
                    }
                }
            }
        });

        lockXYToggle.addActionListener(e -> {
            factorYField.setEnabled(!lockXYToggle.isSelected());
            if (lockXYToggle.isSelected()) {
                lockXYToggle.setText("Lock XY Scale");
            } else {
                lockXYToggle.setText("Free XY Scale");
            }
        });
        lockXYToggle.setSelected(false);
        byPixelToggle.setSelected(true);
        byPixelToggle.addActionListener(e -> {
            if(byPixelToggle.isSelected()) {
                factorXLabel.setText("Pixels  At  X:");
                factorYLabel.setText("Pixels  At  Y:");
                minX = imgWidth/10;
                maxX = imgWidth*5;
                minY = imgHeight/10;
                maxY= imgHeight*5;
                factorXField.setText(String.valueOf(imgWidth));
                factorYField.setText(String.valueOf(imgHeight));
                byPixelToggle.setText("By Pixels");
            } else {
                factorXLabel.setText("Percent  At  X%:");
                factorYLabel.setText("Percent  At  Y%:");
                minX = 10;
                maxX = 500;
                minY = 10;
                maxY=500;
                factorXField.setText("100");
                factorYField.setText("100");
                byPixelToggle.setText("By Percent");
            }
        });
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        // 设置对话框的布局和大小
        setLayout(new FlowLayout());
        setSize(250, 150);
        setLocationRelativeTo(parent);
        // 添加按钮的事件监听器
        okButton.addActionListener(e -> {
            // 获取文本框中的参数值
            if (byPixelToggle.isSelected()) {
                int pixelX = Integer.parseInt(factorXField.getText());
                int PixelY = Integer.parseInt(factorYField.getText());
                scaleX =(float) pixelX/imgWidth;
                scaleY =(float) PixelY/imgHeight;
            } else {
                int percentX = Integer.parseInt(factorXField.getText());
                int percentY = Integer.parseInt(factorYField.getText());
                scaleX =(float) percentX/100;
                scaleY =(float) percentY/100;
            }
            if (lockXYToggle.isSelected()) {
                {
                    scaleY = scaleX;
                }
            }
            dispose();
        });
        cancelButton.addActionListener(e -> {
            scaleX=1;
            scaleY=1;
            dispose();
        });
        // 将文本框和按钮添加到对话框中
        setLayout(new FlowLayout());
        add(lockXYToggle);
        add(byPixelToggle);
        add(factorXLabel);
        add(factorXField);
        add(factorYLabel);
        add(factorYField);
        add(okButton);
        add(cancelButton);
    }
}