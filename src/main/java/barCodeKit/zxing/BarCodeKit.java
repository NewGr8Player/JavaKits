package barCodeKit.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <p>条形码生成工具类</p>
 * 
 * @author NewGr8Player
 * @version 0.1 (2017/11/22)
 */
public class BarCodeKit {
    /**
     * 编码
     *
     * @param contents 内容
     * @param width    宽度
     * @param height   高度
     */
    public static BufferedImage encode(String contents, int width, int height, BarcodeFormat barcodeFormat) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    barcodeFormat, width, height, null);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage(0, 0, 0);
        }

    }


    public static void main(String[] args) {
        String imgPath = "D:\\testQrCode\\test.png";
        String contents = "12345678901";
        /*  62 : 19 */

        System.out.println("length:" + contents.length());

        int width = 105, height = 50;
        File file = new File(imgPath);
        BufferedImage bufferedImage = BarCodeKit.encode(contents, width, height, BarcodeFormat.CODE_128);
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("==");
    }
}
