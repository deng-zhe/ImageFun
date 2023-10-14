import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;

import static java.lang.Math.abs;

public class ImageHandler {
    //image process class
    public BufferedImage image;
    //source image

    public ImageHandler(BufferedImage inputImage) {
        image = inputImage;
    }
    public BufferedImage Rotate(double angle) {
        //rotate image with an angle
        if (image == null) return null;
        // 计算旋转后的图像尺寸
        double sin = abs(Math.sin(Math.toRadians(angle)));
        double cos = abs(Math.cos(Math.toRadians(angle)));
        int newWidth = (int) (image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) (image.getWidth() * sin + image.getHeight() * cos);

        // 创建一个新的BufferedImage对象来存储旋转后的图像
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, image.getType());

        // 使用AffineTransform类进行旋转操作
        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - image.getWidth()) / 2.0, (newHeight - image.getHeight()) / 2.0);
        at.rotate(Math.toRadians(angle), image.getWidth() / 2.0, image.getHeight() / 2.0);
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotatedImage;
    }

    public BufferedImage Mirror(char axis) {
        // mirror image at axis x/y
        if (image == null) return null;
        BufferedImage mirroredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        // 使用AffineTransform类进行镜像操作
        Graphics2D g2d = mirroredImage.createGraphics();
        AffineTransform at = new AffineTransform();
        if (axis == 'X') {
            at.scale(-1, 1); // mirror by follow X axis
            at.translate(-image.getWidth(), 0); //move follow Y axis
        } else if (axis == 'Y') {
            at.scale(1, -1);
            at.translate(0, -image.getHeight()); // 沿y轴平移
        } else {
            return image;
        }
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return mirroredImage;
    }

    public BufferedImage Reverse() {
        //reverse image
        if (image == null) return null;
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage reversedImage = new BufferedImage(width, height, image.getType());
        // Reverse the image by swapping RGB values
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                rgb = ~rgb;
                reversedImage.setRGB(x, y, rgb);
            }
        }
        return reversedImage;
    }

    public BufferedImage Resize() {
        //change image size(pixel) with user input scale factor
        if (image == null) return null;
        float ScaleFactorX;
        float ScaleFactorY;
        scaleDialog dlg = new scaleDialog(null, image.getWidth(), image.getHeight());
        dlg.setVisible(true);
        ScaleFactorX = dlg.scaleX;
        ScaleFactorY = dlg.scaleY;
        if (ScaleFactorX != 1 || ScaleFactorY != 1) {
            int newWidth = (int) (image.getWidth() * ScaleFactorX);
            int newHeight = (int) (image.getHeight() * ScaleFactorY);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType());
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
            g2d.dispose();
            return resizedImage;
        }
        return image;
    }

    public BufferedImage Color2Gray() {
        //tran color image to gray style
        if (image == null) return null;
        int width = image.getWidth();
        int height = image.getHeight();
        // 修改像素值
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                int avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                int gray = new Color(avg, avg, avg).getRGB();
                image.setRGB(x, y, gray);
            }
        }
        return image;
    }

    public BufferedImage Sharpen() {
        //make image sharpen
        if (image == null) return null;
        float[] matrix = {
                0f, -1f, 0f,
                -1f, 5f, -1f,
                0f, -1f, 0f
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    public BufferedImage Blur() {
        //make image blur
        float[] matrix = {
                0.05f, 0.15f, 0.05f,
                0.15f, 0.3f, 0.15f,
                0.05f, 0.15f, 0.05f
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    public BufferedImage adjustContrast(float contrast) {
        // adjust image contrast
        float[] matrix = {
                contrast, contrast, 0f,
                0f, 1f, 0f,
                -contrast, 0f, contrast
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    public BufferedImage adjustBrightness(float brightness) {
        // adjust image brightness
        float[] matrix = {
                brightness, 0f, 0f,
                0f, brightness, 0f,
                0f, 0f, 1f
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    public BufferedImage adjustDarkness(float darkness) {
        //make image more dark
        image = Reverse();
        float[] matrix = {
                darkness, 0f, 0f,
                0f, darkness, 0f,
                0f, 0f, 1f
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        image = op.filter(image, null);
        return Reverse();
    }

    public BufferedImage convertToSketchStyle() {
        //process image as sketch style
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage diffX = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage diffY = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                Color colorN = new Color(image.getRGB(x, y));
                Color colorN1 = new Color(image.getRGB(x, y + 1));
                int diffR = abs(colorN.getRed() - colorN1.getRed());
                int diffG = abs(colorN.getGreen() - colorN1.getGreen());
                int diffB = abs(colorN.getBlue() - colorN1.getBlue());
                Color sketchColorRGB = new Color(diffR, diffG, diffB);
                diffY.setRGB(x, y, sketchColorRGB.getRGB());
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                Color colorN = new Color(image.getRGB(x, y));
                Color colorN1 = new Color(image.getRGB(x + 1, y));
                int diffR = abs(colorN.getRed() - colorN1.getRed());
                int diffG = abs(colorN.getGreen() - colorN1.getGreen());
                int diffB = abs(colorN.getBlue() - colorN1.getBlue());
                Color sketchColorRGB = new Color(diffR, diffG, diffB);
                diffX.setRGB(x, y, sketchColorRGB.getRGB());
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color1 = new Color(diffY.getRGB(x, y));
                Color color2 = new Color(diffX.getRGB(x, y));
                int R = (color1.getRed() + color2.getRed()) / 2;
                int G = (color1.getGreen() + color2.getGreen()) / 2;
                int B = (color1.getBlue() + color2.getBlue()) / 2;
                Color sketchColorRGB = new Color(R, G, B);
                outputImage.setRGB(x, y, sketchColorRGB.getRGB());
            }
        }
        image = outputImage;
        image = widen();
        image = Color2Gray();
        return Reverse();
    }

    public BufferedImage widen() {
        //widen the light lines
        float[] matrix = {
                1f, 1f, 1f,
                1f, 2f, 1f,
                1f, 1f, 1f
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    public BufferedImage DeNoise2() {
        //decrease noise
        float[] matrix = {
                0.04f, 0.04f, 0.04f, 0.04f, 0.04f,
                0.04f, 0.04f, 0.04f, 0.04f, 0.04f,
                0.04f, 0.04f, 0.04f, 0.04f, 0.04f,
                0.04f, 0.04f, 0.04f, 0.04f, 0.04f,
                0.04f, 0.04f, 0.04f, 0.04f, 0.04f,
        };
        Kernel kernel = new Kernel(5, 5, matrix);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    public BufferedImage DeNoise() {
        //decrease noise with another algorithm
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int windowSize = 4;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixelValues = new int[windowSize * windowSize];
                int index = 0;
                // 获取窗口内的像素值
                for (int j = -windowSize / 2; j <= windowSize / 2; j++) {
                    for (int i = -windowSize / 2; i <= windowSize / 2; i++) {
                        int offsetX = x + i;
                        int offsetY = y + j;

                        if (offsetX >= 0 && offsetX < width && offsetY >= 0 && offsetY < height) {
                            Color color = new Color(image.getRGB(offsetX, offsetY));
                            int grayValue = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                            pixelValues[index++] = grayValue;
                        }
                    }
                }

                // 对像素值进行排序
                Arrays.sort(pixelValues);

                // 取中值作为当前像素点的值
                int median = pixelValues[pixelValues.length / 2];
                result.setRGB(x, y, new Color(median, median, median).getRGB());
            }
        }
        return result;
    }

    public BufferedImage Pixel(int pixelCount) {
        //trans image to a Pixel style
        int width = image.getWidth();
        int height = image.getHeight();
        int BlockCountX = width / pixelCount;
        int BlockCountY = height / pixelCount;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if((width % pixelCount) != 0)  BlockCountX++;
        if((height % pixelCount) != 0)  BlockCountY++;
        for (int y = 0; y < BlockCountY; y++) {
            for (int x = 0; x < BlockCountX; x++) {
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                int cnt=0;
                for (int j = y * pixelCount; j < (y + 1) * pixelCount && j < height; j++) {
                    for (int i = x * pixelCount; i < (x + 1) * pixelCount && i< width; i++) {
                        Color color = new Color(image.getRGB(i, j));
                        sumR += color.getRed();
                        sumG += color.getGreen();
                        sumB += color.getBlue();
                        cnt++;
                    }
                }
                sumR = sumR / cnt;
                sumG = sumG / cnt;
                sumB = sumB / cnt;
                for (int j = y * pixelCount; j < (y + 1) * pixelCount && j < height; j++) {
                    for (int i = x * pixelCount; i < (x + 1) * pixelCount && i< width; i++) {
                        result.setRGB(i, j, new Color(sumR, sumG, sumB).getRGB());
                    }
                }
            }
        }
        return result;
    }
    public BufferedImage PartPixel(int pixelCount, int[][] pixelmap) {
        //trans selected area to pixel style
        int width = image.getWidth();
        int height = image.getHeight();
        int BlockCountX = width / pixelCount;
        int BlockCountY = height / pixelCount;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if((width % pixelCount) != 0)  BlockCountX++;
        if((height % pixelCount) != 0)  BlockCountY++;
        for (int y = 0; y < BlockCountY; y++) {
            for (int x = 0; x < BlockCountX; x++) {
                int sumR = 0;
                int sumG = 0;
                int sumB = 0;
                int cnt=0;
                boolean IsSelected=false;
                for (int j = y * pixelCount; j < (y + 1) * pixelCount && j < height; j++) {
                    for (int i = x * pixelCount; i < (x + 1) * pixelCount && i< width; i++) {
                        Color color = new Color(image.getRGB(i, j));
                        sumR += color.getRed();
                        sumG += color.getGreen();
                        sumB += color.getBlue();
                        cnt++;
                        if (pixelmap[i][j] ==2) IsSelected=true;
                    }
                }
                sumR = sumR / cnt;
                sumG = sumG / cnt;
                sumB = sumB / cnt;
                for (int j = y * pixelCount; j < (y + 1) * pixelCount && j < height; j++) {
                    for (int i = x * pixelCount; i < (x + 1) * pixelCount && i< width; i++) {
                        if (IsSelected) {
                            result.setRGB(i, j, new Color(sumR, sumG, sumB).getRGB());
                        }
                        else
                        {
                            result.setRGB(i,j,image.getRGB(i,j));
                        }
                    }
                }
            }
        }
        return result;
    }
}
