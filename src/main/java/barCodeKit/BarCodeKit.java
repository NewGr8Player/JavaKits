package barCodeKit;

import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


/**
 * <p>条形码工具类</p>
 * <p>工具类中所有长度计量单位均为毫米(mm)，请使用工具类<b>UnitConv</b>进行转换</p>
 * <p>参考:org.krysalis.barcode4j.tools.UnitConv</p>
 *
 * @author NewGr8Player
 * @version 0.1 (2017/11/21)
 */
public class BarCodeKit {

    public static final String BARCODE_IMAGE_TYPE_PNG = "image/png";

    public static final String BARCODE_TYPE_INTERLEAVED2OF5 = "Interleaved2Of5";
    public static final String BARCODE_TYPE_CODE128 = "Code128Bean";
    public static final String BARCODE_TYPE_CODE39 = "Code39Bean";

    //public static AbstractBarcodeBean bean = new Interleaved2Of5Bean(); /* 不识别 */
    //public static AbstractBarcodeBean bean = new Code39Bean();/* 数据承载量有限 */
    public static AbstractBarcodeBean bean = new Code128Bean();

    public static int dpi = 300;/* 精细度（Dot Per Inch）,即每英寸打印点或线的数量 */
    public static double modelWidth = UnitConv.in2mm(2.5f / dpi);

    /**
     * <p>初始化默认值</p>
     */
    private static void init() {
        bean.doQuietZone(true);/* 两侧是否包含空白区域 */
        bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);/* 数据位置 */
        bean.setBarHeight(4d);
    }

    /**
     * <p>生成条形码（根据默认值）</p>
     *
     * @param msg          条形码上承载的信息
     * @param outputStream 输出流
     */
    public static void generator(String msg, OutputStream outputStream) {
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(outputStream, BarCodeKit.BARCODE_IMAGE_TYPE_PNG, dpi,
                BufferedImage.TYPE_BYTE_BINARY, false, 0);
        bean.generateBarcode(canvas, msg);
        try {
            canvas.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>生成条形码</p>
     * <p>根据参数内容生成二维码，保留默认值参数传入<b>null</b></p>
     * <p>进制转换请使用使用<b>UnitConv</b>工具类</p>
     *
     * @param msg          条形码上承载的信息
     * @param outputStream 输出流
     * @param barHeigth    线条高度
     * @param modelWidth   标准模式的宽度(建议根据dpi计算,单位：mm)
     * @param modelHeigth  标准模式的高度(单位：mm)
     * @param quietZone    两侧空白的宽度(<b>null</b>为不留白)
     * @param fontSize     字体大小
     * @param type         生成条形码的类型
     */
    public static void generator(String msg, OutputStream outputStream, Double barHeigth, Double modelWidth, Double modelHeigth, Double quietZone, Double fontSize, String type) {
        //TODO Java1.7:此处可以使用Switch替代当前if
        type = (type == null ? "" : type);
        /* 判断传入类型 */
        if (BARCODE_TYPE_CODE128.equals(type)) {
            bean = new Code128Bean();
        } else if (BARCODE_TYPE_CODE39.equals(type)) {
            bean = new Code39Bean();
        } else if (BARCODE_TYPE_INTERLEAVED2OF5.equals(type)) {
            bean = new Interleaved2Of5Bean();
        } else {
            bean = new Code128Bean();/* 默认使用Code128码 */
        }
        init();
        if (null != barHeigth) {
            bean.setBarHeight(barHeigth);
        }
        if (null != modelWidth) {
            bean.setModuleWidth(modelWidth);
        }
        if (null != modelHeigth) {
            bean.setHeight(modelHeigth);
        }
        if (null != quietZone) {
            bean.doQuietZone(true);
            bean.setQuietZone(quietZone);
        }
        if (null != fontSize) {
            bean.setFontSize(fontSize);
        }
        generator(msg, outputStream);
    }

    /**
     * <p>生成条形码</p>
     * <p>根据参数内容生成二维码，保留默认值参数传入<b>null</b></p>
     * <p>进制转换请使用使用<b>UnitConv</b>工具类</p>
     *
     * @param msg          条形码上承载的信息
     * @param outputStream 输出流
     * @param barHeigth    线条高度
     * @param modelWidth   标准模式的宽度(建议根据dpi计算,单位：mm)
     * @param modelHeigth  标准模式的高度(单位：mm)
     * @param quietZone    两侧空白的宽度(<b>null</b>为不留白)
     * @param fontSize     字体大小
     * @param type         生成条形码的类型
     * @return 包含如满足输入规格的与数据的BufferedImage对象
     */
    public static BufferedImage generatorBufferedImage(String msg, OutputStream outputStream, Double barHeigth, Double modelWidth, Double modelHeigth, Double quietZone, Double fontSize, String type) {
        File file = new File("temp.png");
        BufferedImage bufferedImage = null;
        try {
            outputStream = new FileOutputStream(file);
            generator(msg, outputStream, barHeigth, modelWidth, modelHeigth, quietZone, fontSize, type);
            bufferedImage = ImageIO.read(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    /**
     * <p>重置图片尺寸</p>
     *
     * @param bufferedImage BufferedImage对象
     * @param width         宽
     * @param height        高
     * @return 满足输入规格的BufferedImage对象
     */
    private static BufferedImage resize(BufferedImage bufferedImage, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(bufferedImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    public static void main(String[] args) {
        final String content = "201522151234567";/* 11 ~ 15位纯数字 */
        System.out.println("...:" + content.length());
        init();
        OutputStream outputStream;
        try {
            File file = new File("d:\\" + dpi + ".png");
            outputStream = new FileOutputStream(file);
            BufferedImage bufferedImage = generatorBufferedImage(content, outputStream, null, modelWidth, null, null, null, null);
            bufferedImage = resize(bufferedImage, 115, 46);
            ImageIO.write(bufferedImage, "png", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}