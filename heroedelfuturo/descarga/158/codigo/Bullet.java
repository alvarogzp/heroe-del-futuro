package code;

class Bullet extends Sprite {
	private int owner;

	public Bullet(int nFrames) {
		super(nFrames);
	}

	public void setOwner(int owner) {
		this.owner=owner;
	}

	public int getOwner() {
		return owner;
	}

	public void doMovement() {
		// 1 = disparo del héroe, 2 = disparo del enemigo 1, 3 = disparo del monstruo, 4 = disparo del enemigo 2, 5 = superdisparo, 6 = dobledisparo, el resto = disparos del monstruo del nivel 3.
		switch (owner) {
			case 1:
				setY(getY()-6);
				break;
			case 2:
				setY(getY()+6);
				break;
			case 3:
				setY(getY()+6);
				break;
			case 4:
				setY(getY()+6);
				break;
			case 5:
				setY(getY()-4);
				break;
			case 6:
				setY(getY()-6);
				break;
			case 7:
				setX(getX()-6);
				break;
			case 8:
				setX(getX()+6);
				break;
			case 9:
				setX(getX()-4);
				setY(getY()+4);
				break;
			case 10:
				setX(getX()+4);
				setY(getY()+4);
				break;
		}
	}

	// Sobrecarga del método draw de la clase Sprite
	public void draw (javax.microedition.lcdui.Graphics g) {
		selFrame(owner);
		// llamamos al método 'draw' de la clase padre (Sprite)
		super.draw(g);
	}
}