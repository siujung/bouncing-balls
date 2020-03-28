package hw4;

import java.awt.*;
import java.awt.event.*;
public class s20140310hw4 extends Frame implements ActionListener
{  	private BallCanvas canvas; 
	
	public s20140310hw4()
   	{  	canvas = new BallCanvas();
      	add("Center", canvas);
      	Panel p = new Panel();
		Button s = new Button("Start");
		Button c = new Button("Close");
		p.add(s);	p.add(c);
      	s.addActionListener(this);
		c.addActionListener(this);
      	add("South", p);
   	}
	public void actionPerformed(ActionEvent evt)
   	{  	if (evt.getActionCommand() == "Start")
      	{  	
   			canvas.makeBall();
      	}
      	else if (evt.getActionCommand() == "Close")
         	System.exit(0);
   	}
	class WindowDestroyer extends WindowAdapter
	{
	    public void windowClosing(WindowEvent e) 
	    {
	        System.exit(0);
	    }
	}
	public static void main(String[] args)
   	{  	Frame f = new s20140310hw4();
      	f.setSize(400, 300);
      	//WindowDestroyer listener = new WindowDestroyer();  
      		f.addWindowListener(
      				new WindowAdapter() {
      					 public void windowClosing(WindowEvent e)
      					 {
      						 System.exit(0);
      					 }
      				}
      			);
      	f.setVisible(true);  
   }
	
	class BallCanvas extends Canvas {
		private int dx, dy;
		private int ballNum=0;
		private Ball[] b=new Ball[1500];
		private Color[] ballColor= {Color.BLACK, Color.BLUE, Color.GREEN, Color.GRAY, Color.RED};
		
		public void makeBall() {
			for(int i=0; i<5;i++) {
				dx=(int)(Math.cos((double)i/5*Math.PI*2)*5)+(int)(5*Math.random()-2);
				dy=(int)(Math.sin((double)i/5*Math.PI*2)*5)-(int)(5*Math.random()-2);
				b[ballNum]=new Ball(canvas, canvas.getWidth()/2, canvas.getHeight()/2, dx, dy, 20, ballColor[i]);
				b[ballNum].start();
				ballNum++;
			}
		}
	
		public void checkCollision(Ball ball) {
			Graphics g = this.getGraphics();
			int newBallNum=ballNum;
			int i;
			for(i=0; i<ballNum; i++) {
				if(b[i].colliding(ball) && (b[i]!=ball) && ball.running && b[i].running && !ball.collideFlag && !b[i].collideFlag) {
					b[i].collideFlag=true;
					ball.collideFlag=true;
					g.clearRect(b[i].getX(), b[i].getY(), b[i].getSize(), b[i].getSize());
					g.clearRect(ball.getX(), ball.getY(), ball.getSize(), ball.getSize());
					b[i].collided();
					ball.collided();
					if(b[i].running) {
						b[newBallNum]=new Ball(canvas, b[i].getX()+b[i].getSize(), b[i].getY()+b[i].getSize(), -b[i].getDY(), b[i].getDX(), (int)b[i].getRadius()*2, b[i].getColor());
						b[newBallNum].start();
						newBallNum++;
					}
					if(ball.running) {
						b[newBallNum]=new Ball(canvas, ball.getX()+ball.getSize(), ball.getY()+ball.getSize(), -ball.getDY(), ball.getDX(), (int)ball.getRadius()*2, ball.getColor());
						b[newBallNum].start();
						newBallNum++;
					}	
					ballNum=newBallNum;
					return;
				}
			}
		}
	}
	
	class Ball extends Thread
	{  	private int x = 0;
		private int y = 0;
		private int dx = 0;
		private int dy = 0; 
		private int xsize;
		private int ysize;
		public boolean running=true;
		public boolean collideFlag=true;
		private BallCanvas box;
		private Color ballColor;
		public Ball(BallCanvas c, int startX, int startY, int startDX, int startDY, int startSize, Color startColor) 
		{	
			box = c;
			x=startX;
			y=startY;
			dx=startDX;
			dy=startDY;
			xsize=startSize;
			ysize=startSize;
			ballColor=startColor;
		}
		public boolean colliding(Ball ball)
		{
			double distance;
			double diffX=this.getCenterX()-ball.getCenterX();
			double diffY=this.getCenterY()-ball.getCenterY();
			diffX=diffX*diffX;
			diffY=diffY*diffY;
			distance=Math.sqrt(diffY+diffX);
			if(distance<this.getRadius()+ball.getRadius()) {
				return true;
			}
			return false;
		}
		public void collided() {
			Graphics g = box.getGraphics();
			g.clearRect(x, y, xsize, ysize);
			if(this.xsize<=5) 
				{this.running=false;
				return;	}
			x=x-xsize;
			y=y-ysize;
			this.dx=-(this.dx);
			//this.dy=-(this.dy);
			this.xsize=(int)this.xsize/2;
			this.ysize=(int)this.ysize/2;
			//collideFlag=true;
		}
		public Color getColor() {
			return this.ballColor;
		}
		public double getCenterX() {
			double centerX=x+xsize/2;
			return centerX;
		}
		public double getCenterY() {
			double centerY=y+ysize/2;
			return centerY;
		}
		public double getRadius() {
			double radius=xsize/2;
			return radius;
		}
		public int getSize() {
			return xsize;
		}
		public int getDX() {
			return dx;
		}
		public int getDY() {
			return dy;
		}

		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public void draw()
		{  	
			Graphics g = box.getGraphics();
			g.clearRect(x, y, xsize, ysize);
			g.setColor(ballColor);
  			g.fillOval(x, y, xsize, ysize);
  			g.dispose();
		}
		public void move()
		{  
			Graphics g = box.getGraphics();
			//g.setXORMode(box.getBackground());
			//g.fillOval(x, y, xsize, ysize);
			g.clearRect(x, y, xsize, ysize);
			x += dx;	y += dy;
			Dimension d = box.getSize();
			if (x <= 0) { x = 0; dx = -dx; }
			if (x + xsize >= d.width) { x = d.width - xsize; dx = -dx; }
			if (y <= 0) { y = 0; dy = -dy; }
			if (y + ysize >= d.height) { y = d.height - ysize; dy = -dy; }
			g.setColor(ballColor);
			g.fillOval(x, y, ysize, ysize);
			g.dispose();
		}	
		public void run()
		{  	
			draw();
			
			while(running) {
				
				if(collideFlag) {
					for(int i=1; running && i<=25; i++) {
						move();
						try { 
							Thread.sleep(15); } 
						catch(InterruptedException e) {}
					}
					collideFlag=false;
				}
				else {
					move();
					try {
						Thread.sleep(15);
						canvas.checkCollision(this);}
					catch(InterruptedException e) {}
				}
			}
			if(!running) {
				Graphics g = box.getGraphics();
				g.clearRect(x, y, xsize*2, ysize*2);
				g.dispose();
			}			
		}
	}
}