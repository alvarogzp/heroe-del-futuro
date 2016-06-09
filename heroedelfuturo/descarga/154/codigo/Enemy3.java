import java.util.*;

class Enemy3 extends Sprite {

	private int type,state,deltaX,deltaY,movimiento;
	private boolean animacion;

	public void setState(int state) {
		this.state=state;
	}

	public int getState(int state) {
		return state;
	}

	public void setType(int type) {
		this.type=type;
	}

	public int getType() {
		return type;
	}

	public void doMovement() {
		Random random = new java.util.Random();
		
		// Incrementar el número de ciclos que lleva en la pantalla
		movimiento++;
		
		// Los enemigos de tipo 2 cambiaran su trayectoria al alcanzar una posición determinada (pos. 50)
		if (type > 1 && type < 4 && getY() > 50 && state != 2) {
			// Paso al estado 2 (movimiento diagonal)
			state = 2;
			if ((Math.abs(random.nextInt()) % 2) + 1 == 1) {
				deltaX=2; 
			} else {
				deltaX=-2; 
			}
		}
		
		// La animación del enemigo 4
		int cambio = 4;
		
		if (type >= 4 && movimiento%cambio == 0) {
			switch (type) {
				case 4:
					type=5;
					animacion=true;
					break;
				case 5:
					if (animacion) {
						type=6;
					} else {
						type=4;
					}
					break;
				case 6:
					if (animacion) {
						type=7;
					} else {
						type=5;
					}
					break;
				case 7:
					if (animacion) {
						type=8;
					} else {
						type=6;
					}
					break;
				case 8:
					if (animacion) {
						type=9;
					} else {
						type=7;
					}
					break;
				case 9:
					type=8;
					animacion=false;
					break;
			}
		}

				
		// movemos la nave
		setX(getX()+deltaX);
		setY(getY()+deltaY);
	}

	public void init(int xhero) {
		
		// Ciclos que lleva en la pantalla
		movimiento=0;
		
		deltaY=3;
		deltaX=0;

		if (type == 1 || type > 3) {
			if (xhero > getX()) {
				deltaX=2;
			} else {
				deltaX=-2;
			}
		}
	}

	// Sobrecarga del método draw de la clase Sprite
	public void draw (javax.microedition.lcdui.Graphics g) {
		selFrame(type);
		// llamamos al método 'draw' de la clase padre (Sprite)
		super.draw(g);
	}

	public Enemy3(int nFrames) {
		super(nFrames);
	}
}