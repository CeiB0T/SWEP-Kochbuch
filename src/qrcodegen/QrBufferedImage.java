package qrcodegen;

import Rezeptteile.Rezeptkopf;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class QrBufferedImage {

    public static java.awt.image.BufferedImage toImage(QrCode qr, int scale, int border, int lightColor, int darkColor) {
        //Objects.requireNonNull(qr);
        if (scale <= 0 || border < 0)
            throw new IllegalArgumentException("Value out of range");
        if (border > Integer.MAX_VALUE / 2 || qr.size + border * 2L > Integer.MAX_VALUE / scale)
            throw new IllegalArgumentException("Scale or border too large");

        java.awt.image.BufferedImage result = new java.awt.image.BufferedImage((qr.size + border * 2) * scale, (qr.size + border * 2) * scale, java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                boolean color = qr.getModule(x / scale - border, y / scale - border);
                result.setRGB(x, y, color ? darkColor : lightColor);
            }
        }
        return result;
    }

    public static BufferedImage qrGenerieren(Rezeptkopf rezeptkopf){
        try {
            QrCode.Ecc errCorLvl = QrCode.Ecc.LOW;
            if (rezeptkopf.getrKoRezeptzutat().size() > 0) {
                QrCode qr = QrCode.encodeText(rezeptkopf.zutatenToString(), errCorLvl);
                BufferedImage img = QrBufferedImage.toImage(qr, 14, 2, 0xffffff, 0x000000);
                return img;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
