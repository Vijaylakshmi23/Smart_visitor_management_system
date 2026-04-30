package com.example.visitor.util;

import java.io.File;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

    public static void generateQRCode(String text, String filePath) throws Exception {

        QRCodeWriter writer = new QRCodeWriter();
        var bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        Path path = new File(filePath).toPath();

        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                if (bitMatrix.get(x, y)) {
                    // simple logic (can improve later)
                }
            }
        }
    }
}
