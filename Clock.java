
package clock;

import        java.awt.*                    ;
import        java.awt.Graphics2D           ;
import        java.io.IOException           ;
import        java.net.Socket               ;
import        java.net.UnknownHostException ;
import        java.util.Scanner             ;
import        javax.swing.*                 ;
import static java.lang.Thread.sleep        ;

/**
 *
 * @author kb2784
 * 
 */

public class Clock {

    static int nHours;
    static int nMinutes;
    static int nSeconds;
    
    static void displayAnalogClock ()    {
        
        JFrame jf = new JFrame("Clock");
        jf.setSize(600,600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ClockPanel clockPanel = new ClockPanel();
        jf.add(clockPanel);
        jf.setVisible(true);
        
    }
    
    static int  getHours   (String time) {
        
        String hourStr = "";
        char hourChar  = time.charAt(0);
        
        hourStr  += hourChar;
        hourChar  = time.charAt(1);
        hourStr  += hourChar;
        nHours    = Integer.valueOf(hourStr);
        
        return nHours;
        
    }
    
    static int  getMinutes (String time) {
        
        String minuteStr = "";
        char minuteChar  = time.charAt(3);
        
        minuteStr  += minuteChar;
        minuteChar  = time.charAt(4);
        minuteStr  += minuteChar;
        nMinutes    = Integer.valueOf(minuteStr);
        
        return nMinutes;
        
    }
    
    static int  getSeconds (String time) {
        
        String secondsStr = "";
        char secondsChar  = time.charAt(6);
        
        secondsStr  += secondsChar;
        secondsChar  = time.charAt(7);
        secondsStr  += secondsChar;
        nSeconds     = Integer.valueOf(secondsStr);
        
        return nSeconds;
        
    }
    
    public static void main(String[] args) {
        
        displayAnalogClock();
                
        while (true) {    
            
            try {       
                
                try {
                    
                    Socket sock = new Socket("time-a-g.nist.gov", 13); 
                    
                    if (sock.isConnected()){
      
                        Scanner sin = new Scanner(sock.getInputStream());

                        sin.nextLine();
                        sin.nextInt();
                        sin.next();
                        
                        String time = sin.next();

                        nHours = getHours(time);
                        ClockPanel.clockHours = nHours;

                        nMinutes = getMinutes(time);
                        ClockPanel.clockMinutes = nMinutes;

                        nSeconds = getSeconds(time);
                        ClockPanel.clockSeconds = nSeconds;
                        
                    }
                    
                    else {
                        
                        System.out.println("socket can't connect");
                        
                    }
                    
                    sleep(60000);
                    
                }
                catch (UnknownHostException e) { System.out.println("Unknown Host: " + e.toString());}
                catch (IOException e)          { System.out.println("IO Error: " + e.toString());    }
                
            } 
            
            catch (InterruptedException e) { System.out.println("Interrupted Exception: " + e.toString()); } 
        }   
    } 
}


class ClockPanel extends JPanel{
    
    static int clockSeconds;
    static float clockMinutes;
    static float clockHours;

    protected void createClockFace  (Graphics graphic, int height,  int width   ) {
        
        graphic.setColor(Color.BLACK);
        graphic.fillOval(width/2 - 150, height/2 - 150, 300, 300);
        graphic.setColor(Color.WHITE);
        graphic.fillOval(width/2 - 140, height/2 - 140, 280, 280);
        graphic.setColor(Color.BLACK);
        graphic.fillOval(width/2 - 14, height/2 - 5, 28, 28);
        graphic.setFont(new Font("Georgia", Font.BOLD, 30)); 
        graphic.drawString("12", width/2 - 17, height/2 - 110);
        
    }
    
    protected void drawSecondsHands (Graphics graphic, int centerX, int centerY ) {
        
        double angle = Math.toRadians((15 - clockSeconds) * 6); 
        int x = (int) (Math.cos(angle) * 120); 
        int y = (int) (Math.sin(angle) * 120);
        
        Graphics2D g2d = (Graphics2D)graphic;
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawLine(centerX, centerY, centerX + x, centerY - y);
        
    }
    
    protected void drawMinutesHands (Graphics graphic, int centerX, int centerY ) {
        
        double angle = Math.toRadians((15 - clockMinutes) * 6); 
        int x = (int) (Math.cos(angle) * 100); 
        int y = (int) (Math.sin(angle) * 100);
        
        Graphics2D g2d = (Graphics2D)graphic;
        g2d.setColor(Color.blue);
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawLine(centerX, centerY, centerX + x, centerY - y);
        
    }
    
    protected void drawHoursHands   (Graphics graphic, int centerX, int centerY ) {
        
        double angle = Math.toRadians((15 - (clockHours * 5)) * 6); 
        int x = (int) (Math.cos(angle) * 70); 
        int y = (int) (Math.sin(angle) * 70);
        
        Graphics2D g2d = (Graphics2D)graphic;
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(4f));
        g2d.drawLine(centerX, centerY, centerX + x, centerY - y);
        
    } 
    
    @Override
    protected void paintComponent(Graphics graphic) {
        
        System.out.println("Hr: "      + clockHours   );
        System.out.println("Min: "     + clockMinutes );
        System.out.println("Seconds: " + clockSeconds );
        
        System.out.println();
        
        super.paintComponent(graphic);
  
        int height = this.getHeight();
        int width  = this.getWidth();
        
        createClockFace (graphic, height, width);
        
        int centerX = getWidth()/2;
        int centerY = getWidth()/2;
       
        
        drawSecondsHands (graphic,centerX,centerY);
        
        drawMinutesHands (graphic,centerX,centerY);
        
        drawHoursHands (graphic,centerX,centerY);
        
        try {
            this.clockSeconds += 1;
            sleep(1000);
            repaint();
        } 
        
        catch (InterruptedException e) { System.out.println("Interrupted Exception: " + e.toString()); }

    }

}