package ant.xiter.jsystem.views;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Mohammad Faisal
 * ermohammadfaisal.blogspot.com
 * facebook.com/m.faisal6621
 *
 */

public class HideToSystemTray{
    TrayIcon trayIcon;
    SystemTray tray;
    
    HideToSystemTray(final Frame frame){
        System.out.println("creating instance");
        try{
            System.out.println("setting look and feel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            System.out.println("Unable to set LookAndFeel");
        }
        //    System.out.println("system tray supported");
            //tray=SystemTray.

            Image image=Toolkit.getDefaultToolkit().getImage("/media/faisal/DukeImg/Duke256.png");
            ActionListener exitListener=new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting....");
                    System.exit(0);
                }
            };
            PopupMenu popup=new PopupMenu();
            MenuItem defaultItem=new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            defaultItem=new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	frame.setVisible(true);
                	frame.setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            trayIcon=new TrayIcon(image, "SystemTray Demo", popup);
            trayIcon.setImageAutoSize(true);
       
            frame.addWindowStateListener(new WindowStateListener() {
            @SuppressWarnings("static-access")
			public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==frame.ICONIFIED){
                    try {
//                        tray.add(trayIcon);
                    	frame.setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (Exception ex) {
                        System.out.println("unable to add to tray");
                    }
                }
        if(e.getNewState()==7){
                    try{
            //tray.add(trayIcon);
            frame.setVisible(false);
            System.out.println("added to SystemTray");
            }catch(Exception ex){
            System.out.println("unable to add to system tray");
        }
            }
        if(e.getNewState()==frame.MAXIMIZED_BOTH){
                    //tray.remove(trayIcon);
        	frame.setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if(e.getNewState()==frame.NORMAL){
                    //tray.remove(trayIcon);
                	frame.setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));

            frame.setVisible(true);
            frame.setSize(300, 200);
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
