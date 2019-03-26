package com.bmstu.iu9.dip;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class LaplasianICS {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Mat run(String filename) {

        Mat src_gray = new Mat();
        Mat abs_dst = new Mat();
        Mat dst = new Mat();
        Mat src;
        int kernel_size = 3;
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;

        src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);

        // Reduce noise by blurring with a Gaussian filter ( kernel size = 3 )
        Imgproc.GaussianBlur( src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT );

        // Convert the image to grayscale
        Imgproc.cvtColor( src, src_gray, Imgproc.COLOR_RGB2GRAY );

        /// Apply Laplace function
        Imgproc.Laplacian( src_gray, dst, ddepth, kernel_size, scale, delta, Core.BORDER_DEFAULT );

        // converting back to CV_8U
        Core.convertScaleAbs( dst, abs_dst );

        return abs_dst;
    }
}