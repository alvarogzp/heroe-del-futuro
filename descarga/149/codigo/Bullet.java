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
		// si owner = 1, 5 o 6 el disparo es nuestro
		// si no, es del enemigo
		if (owner == 1 || owner == 5 || owner == 6) {
			if (owner == 5) {
				// Reducimos la velocidad del bazooka
				setY(getY()-4);
			} else {
				setY(getY()-6);
			}
		} else {
			if (owner == 7) {
				setX(getX()-6);
			} else {
				if (owner == 8) {
					setX(getX()+6);
				} else {
					if (owner == 9) {
						setX(getX()-4);
						setY(getY()+4);
					} else {
						if (owner == 10) {
							setX(getX()+4);
							setY(getY()+4);
						} else {
							setY(getY()+6);
						}
					}
				}
			}
		}
	}

	// Sobrecarga del método draw de la clase Sprite
	public void draw (javax.microedition.lcdui.Graphics g) {
		selFrame(owner);
		// llamamos al método 'draw' de la clase padre (Sprite)
		super.draw(g);
	}
}