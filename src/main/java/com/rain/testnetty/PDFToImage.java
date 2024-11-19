package com.rain.testnetty;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFToImage {

    public static void main(String[] args) {
        String pdfFilePath = "/Users/Rain/Desktop/卡号.pdf"; // PDF文件路径
        String outputImagePath = "/Users/Rain/Desktop/卡号1.png"; // 输出图片路径

        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            if (!document.isEncrypted()) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                // 获取第一页的图片
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

                // 保存图片
                ImageIO.write(bufferedImage, "PNG", new File(outputImagePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
