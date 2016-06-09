import java.util.*;

class Enemy extends Sprite {

    private int type,state,deltaX,deltaY,movimiento,getHeight,getWidth;
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
        
        // Los enemigos de tipo 2 cambiaran su trayectoria
        // al alcanzar una posición determinada (pos. 50)
        if (type == 2 && getY() > 50 && state != 2) {
            // paso al estado 2 (movimiento diagonal)
            state = 2;
            if ((Math.abs(random.nextInt()) % 2) + 1 == 1) {
                deltaX=2; 
            } else {
                deltaX=-2; 
            }
        }
        
        // La animación del enemigo 3 del segundo nivel
        int cambio = 7;
        
        if (type >= 3 && movimiento%cambio == 0) {
	        switch (type) {
		        case 3:
			        type=4;
			        animacion=true;
			        break;
		        case 4:
			        if (animacion) {
				        type=5;
			        } else {
				        type=3;
			        }
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
			        type=6;
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

        if (type == 1 || type == 3) {
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

    public Enemy(int nFrames, int getHeight, int getWidth) {
        super(nFrames);
        this.getHeight = getHeight;
		this.getWidth = getWidth;
    }
}