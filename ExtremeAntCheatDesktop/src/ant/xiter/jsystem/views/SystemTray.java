package ant.xiter.jsystem.views;


import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Khamis
 * 
 */

public class SystemTray {
    
    public SystemTray(){
        createSystemTray();
    }
    
    public static void main(String[] args) {
    	new SystemTray();
    }


    protected  Image createImage(String path) {
    	try{
    		Image image = ImageIO.read(new File(path)); 		
    		return image;
    	}catch(Exception e){
    	   e.printStackTrace();
    	}
		return null;
    }

    private void createSystemTray() {
        // Make sure you check for tray icon support first
        if (!java.awt.SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        
        // Create popup menu for tray icon info
        final PopupMenu popup = new PopupMenu();
        
        // Fetch the tray image icon
        Image img = createImage("Resources/imagens/antxiter2.png");
        ImageIcon icon = new ImageIcon(img);
        
        // Create the tray icon object
        final TrayIcon trayIcon = new TrayIcon(icon.getImage());
        
        // Create the system tray for tray the icon
        final java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
 
        MenuItem aboutItem = new MenuItem("Tray Info");
        popup.add(aboutItem);
        MenuItem exitItem = new MenuItem("Exit");
        popup.add(exitItem);
         
        // Set the popup menu to the tray icon
        trayIcon.setPopupMenu(popup);
        
        // Catch possible exceptions when adding icon to the tray
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Cannot add the TrayIcon!");
            
        }
          
            trayIcon.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                      JOptionPane.showMessageDialog(null,
                        "This is the tray icon's info");
            }
        });
            
               aboutItem.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                      JOptionPane.showMessageDialog(null,
                       "This dialog is all about system tray program");
            }
        });
               
               exitItem.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                      tray.remove(trayIcon);
                       System.exit(0);
            }
        });
               
    }
}

