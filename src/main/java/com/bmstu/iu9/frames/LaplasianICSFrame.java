package com.bmstu.iu9.frames;

import com.bmstu.iu9.dip.LaplasianICS;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.ImageWindow;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;

public class LaplasianICSFrame {

    private JFrame frame;
    private ImageWindow imageWindow;

    public void build() {
        frame = new JFrame("Image contour selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font("Verdana", Font.PLAIN, 14);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont( font );
        fileMenu.add( openItem );

        openItem.addActionListener( actionPerformed -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter( new ImageFilter() );
            fileChooser.setAcceptAllFileFilterUsed( false );

            if ( fileChooser.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION ) {
                imageWindow = new ImageWindow("", 0);
                if ( fileChooser.getSelectedFile().exists() ) {
                    Mat laplasianICSresult = LaplasianICS.run( fileChooser.getSelectedFile().getAbsolutePath() );
                    imageWindow.setMat( laplasianICSresult );
                    imageWindow.setFrameLabelVisible(frame,
                            new JLabel( new ImageIcon( HighGui.toBufferedImage( laplasianICSresult ) ) ) );
                } else {
                    JOptionPane.showMessageDialog( frame,
                            "Error opening image",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE );
                }
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setFont( font );
        fileMenu.add( saveItem );
        saveItem.addActionListener( e -> {
            if ( imageWindow != null ) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter( new ImageFilter() );
                fileChooser.setAcceptAllFileFilterUsed( false );

                if ( fileChooser.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION ) {
                    Imgcodecs.imwrite(fileChooser.getSelectedFile().getAbsolutePath(), imageWindow.img);
                }
            } else {
                JOptionPane.showMessageDialog( frame,
                                        "Nothing to save",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE );
            }
        } );

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.setFont( font );
        fileMenu.add( closeItem );
        closeItem.addActionListener( e -> {
            imageWindow = null;
            frame.repaint();
        } );

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener( e -> System.exit(0));

        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);

        frame.setSize(560, 200);
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }

    public static void main(String[] args) {
        LaplasianICSFrame laplasianICSFrame = new LaplasianICSFrame();
        laplasianICSFrame.build();
    }
}

