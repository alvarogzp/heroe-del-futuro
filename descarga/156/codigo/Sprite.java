package code;

import javax.microedition.lcdui.*;

class Sprite {

	private int posx,posy,frame,nframes;
	private boolean active;
	private Image[] sprites;

	// constructor. 'nframes' es el número de frames del Sprite.
	public Sprite(int nframes) {
		// El Sprite no está activo por defecto.
		active=false;
		frame=1;
		this.nframes=nframes;
		sprites=new Image[nframes+1];
	}

	public void setX(int x) {
		posx=x;
	}

	public void setY(int y) {
		posy=y;
	}

	int getX() {
		return posx;
	}

	int getY() {
		return posy;
	}

	int getW() {
		return sprites[frame].getWidth();
	}

	int getH() {
		return sprites[frame].getHeight();
	}

	int getFrame() {
		return frame;
	}

	public void on() {
		active=true;
	}

	public void off() {
		active=false;
	}

	public boolean isActive() {
		return active;
	}

	public void selFrame(int frameno) {
		frame=frameno;
	}

	public int frames() {
		return nframes;
	}

	// Carga un archivo tipo .PNG y lo añade al sprite en
	// el frame indicado por 'frameno'
	public void addFrame(int frameno, String path) {
		try {
			sprites[frameno]=Image.createImage(path);
		} catch (Exception e) {
			System.err.println("Can`t load the image " + path + ": " + e.toString());
		}
	}

	boolean collide(Sprite sp) {
		int w1,h1,w2,h2,x1,y1,x2,y2;
		int xo1,xf1,yo1,yf1,xo2,xf2,yo2,yf2;

		w1=getW(); // ancho del sprite1
		h1=getH(); // altura del sprite1
		w2=sp.getW(); // ancho del sprite2
		h2=sp.getH(); // alto del sprite2
		x1=getX(); // pos. X del sprite1
		y1=getY(); // pos. Y del sprite1
		x2=sp.getX(); // pos. X del sprite2
		y2=sp.getY(); // pos. Y del sprite2


		// Coordenadas que delimitan a los sprites
		xo1=x1-w1/2; // Coordenada más cercana al eje OX del sprite1
		xf1=x1+w1/2; // Coordenada más lejana al eje OX del sprite1
		yo1=y1-h1/2; // Coordenada más cercana al eje OY del sprite1
		yf1=y1+h1/2; // Coordenada más lejana al eje OY del sprite1
		xo2=x2-w2/2; // Coordenada más cercana al eje OX del sprite2
		xf2=x2+w2/2; // Coordenada más lejana al eje OX del sprite2
		yo2=y2-h2/2; // Coordenada más cercana al eje OY del sprite2
		yf2=y2+h2/2; // Coordenada más lejana al eje OY del sprite2


		// Ahora comprobamos si colisionan, descartando los casos en los que no podrían colisionar
		if ((xo1 > xf2) || (xf1 < xo2) || (yo1 > yf2) || (yf1 < yo2)) {
			return false;
		} else {
			return true;
		}
	}

	// Dibujamos el Sprite
	public void draw(Graphics g) {
		g.drawImage(sprites[frame],posx,posy,Graphics.HCENTER|Graphics.VCENTER);
	}
}