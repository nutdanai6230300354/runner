import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Runner extends JPanel {
  public static void main(String[] args) {
    JFrame window = new JFrame("Runner");
    Runner content = new Runner();
    window.setContentPane(content);
    window.setSize(800, 400);
    window.setLocation(100, 100);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setVisible(true);
  }

  private Timer timer;
  private int width,height,highPoint,boxMax;
  private Player player;
  private Box box[] = new Box[3];
  private Point point;
  private Ground ground1,ground2;
  private String gameOver;
  private boolean checkGame;
  private Icon BG,BG2;
  private Random random = new Random();
  public Runner() {
    highPoint=0;
    gameOver="";
    boxMax=width;
    BG = new ImageIcon("C:/Users/NUTNUT/Desktop/Pro/666.png");
    BG2 =new ImageIcon("C:/Users/NUTNUT/Desktop/Pro/777.png");

    ActionListener action = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (player != null) {
          player.updateForNewFrame();
          for(int i=0;i<3;i++){
            box[i].updateForNewFrame();
          }
          point.updateForNewFrame();
          ground1.updateForNewFrame();
          ground2.updateForNewFrame();
        }
        repaint();
      }
    };
    timer = new Timer( 30, action );

    addMouseListener( new MouseAdapter() {
      public void mousePressed(MouseEvent evt) {
        requestFocus();
          if(!checkGame){
            player = new Player();
            for(int i=0;i<3;i++){
              box[i]=new Box();
            }
            point = new Point();
            ground1 = new Ground(0,BG);
            ground2 = new Ground(width,BG2);
            checkGame=true;
            gameOver="";
            timer.start();
          }
    }
    } );



    addKeyListener( new KeyAdapter() {
      public void keyPressed(KeyEvent evt) {
        int code = evt.getKeyCode();
        if (code == KeyEvent.VK_UP) {
          if(player.slide){
            player.centerY = height-125;
            player.slide = false;
          }
            player.jump = true;
        }else if (code == KeyEvent.VK_DOWN) {
          if(!player.jump){
            player.slide = true;
          }
        }
      }
      public void keyReleased(KeyEvent evt) {
        int code = evt.getKeyCode();
        if (code == KeyEvent.VK_DOWN) {
          if(!player.jump){
            player.centerY = height-125;
            player.slide = false;
          }
        }
      }
    } );

  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (player == null) {
      width = getWidth();
      height = getHeight();
      player = new Player();
      for(int i=0;i<3;i++){
        box[i]=new Box();
      }
      point = new Point();
      ground1 = new Ground(0,BG);
      ground2 = new Ground(width,BG2);
      checkGame = false;
    }
  
    ground1.draw(g);
    ground2.draw(g);
    player.draw(g);
    for(int i=0;i<3;i++){
      box[i].draw(g);
    }
    point.draw(g);
    if(!checkGame){
      g.drawString("CLICK TO ACTIVATE", 20, 30);
      g.setFont(new Font("Calibri",Font.BOLD,60));
      g.drawString(gameOver,width/2-150,height/2-40);
    }
  }

  private class Player {
    int centerX,centerY,centerYJump,vPlay;
    boolean jump,slide;
    Icon playerIcon,playerJumpIcon,playerSlideIcon;
    Player() {
      centerX = 100;
      centerY = height-125;
      jump=false;
      slide=false;
      centerYJump=centerY;
      vPlay=-8;
      playerIcon = new ImageIcon("C:/Users/NUTNUT/Desktop/Pro/123456.gif");
      playerJumpIcon = new ImageIcon("C:/Users/NUTNUT/Desktop/Pro/555.gif");
      playerSlideIcon = new ImageIcon("C:/Users/NUTNUT/Desktop/Pro/like555.gif");
    }
    void updateForNewFrame() {
      if(jump){
        if(centerY<centerYJump-100){
          vPlay=8;
        }
        centerY+=vPlay;
        if(centerY==centerYJump){
          jump=false;
          vPlay=-8;
        }
      }else if(slide){
        centerY=height-100;
      }
    }
    void draw(Graphics g) {
      if(jump){
        playerJumpIcon.paintIcon(null,g,centerX-50,centerY-52);
      }
      else if(slide){
        playerSlideIcon.paintIcon(null,g,centerX-50,centerY-52);
      }else{
        playerIcon.paintIcon(null,g,centerX-50,centerY-52);
      }
    }
  }
  private class Box {
    int centerX, centerY,randomIcon;
    Icon[] itemIcon= new Icon[2];
    Icon icon;
    Box() {
      centerX = boxMax+(int)(250+100*Math.random());
      boxMax = centerX;
      itemIcon[0] = new ImageIcon("C:/Users/NUTNUT/Desktop/Pro/888.png");
      itemIcon[1] = new ImageIcon("C:/Users/NUTNUT/Desktop/Pro/456.gif");
      randomIcon=random.nextInt(2);
      if(randomIcon==0){
        centerY=height-100;
        icon=itemIcon[randomIcon];
      }
      else{
        centerY=height-180;
        icon=itemIcon[randomIcon];
      }

    }
    void updateForNewFrame() {
      centerX-=10;
      if(centerX<-30){
        boxMax=width;
        for(int i=0;i<3;i++){
          if(box[i].centerX>boxMax)
            boxMax=box[i].centerX;
        }
        centerX = boxMax+(int)(250+100*Math.random());
        randomIcon=random.nextInt(2);
        if(randomIcon==0){
          centerY=height-100;
          icon=itemIcon[randomIcon];
        }
        else{
          centerY=height-180;
          icon=itemIcon[randomIcon];
        }
      }
      if (Math.abs(player.centerX - centerX) <= 40 &&
          Math.abs(player.centerY - centerY) <=78) {
            if(point.point>highPoint){
              highPoint = (int)point.point;
            }
            checkGame=false;
            gameOver="GAME OVER";
            boxMax=width;
            timer.stop();
      }

    }
    void draw(Graphics g) {
      icon.paintIcon(null,g,centerX-30,centerY-30);
    }
  }
  private class Point {
    int centerX, centerY;
    double point;
    Font font=new Font("Calibri",Font.BOLD,20);
    Point() {
      centerX = width-70;
      centerY = 50;
      point=0;
    }
    void updateForNewFrame() {
      point+=0.3;
    }
    void draw(Graphics g) {
      g.setColor(Color.BLACK);
      g.setFont(font);
      g.drawString(Integer.toString((int)point),centerX , centerY);
      g.drawString("HighPoint "+Integer.toString(highPoint),20,50);
    }
  }
  private class Ground {
    Icon groundIcon;
    int centerX, centerY;
    Ground(int x,Icon bg) {
      centerX = x;
      centerY = 0;
      groundIcon = bg;
    }
    void updateForNewFrame() {
      centerX-=10 ;
      if(centerX<-width){
        centerX = width;
      }
    }
    void draw(Graphics g) {
      groundIcon.paintIcon(null,g,centerX,centerY);

    }
  }

}
