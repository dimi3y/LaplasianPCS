package com.bmstu.iu9.frames;

import com.bmstu.iu9.dip.LaplasianICS;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.ImageWindow;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LaplasianICSFrame {

    private JFrame frame;
    private BufferedImage image;

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
                if ( fileChooser.getSelectedFile().exists() ) {
                    image = null;
                    frame.getContentPane().removeAll();
                    Mat laplasianICSresult = LaplasianICS.run( fileChooser.getSelectedFile().getAbsolutePath() );
                    JScrollPane scrollPane = new JScrollPane(new ImagePanel( (image = (BufferedImage) HighGui.toBufferedImage( laplasianICSresult ) ) ));
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    scrollPane.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                    JPanel contentPane = new JPanel(null);
                    contentPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
                    contentPane.add(scrollPane);
                    frame.setContentPane(contentPane);
                    frame.pack();
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
            if ( image != null ) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter( new ImageFilter() );
                fileChooser.setAcceptAllFileFilterUsed( false );

                if ( fileChooser.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION ) {
                    try {
                        ImageIO.write(image, ImageFilter.getExtension(fileChooser.getSelectedFile()), fileChooser.getSelectedFile());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog( frame,
                            "Invalid file name",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE );
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
            image = null;
            frame.getContentPane().removeAll();
            frame.repaint();
        } );

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener( e -> System.exit(0));

        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);

        frame.setSize(1700, 1000);
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }

    public static void main(String[] args) {
        LaplasianICSFrame laplasianICSFrame = new LaplasianICSFrame();
        laplasianICSFrame.build();
    }
}

