import java.util.*;

class Icono extends Sprite {
	
	private int getHeight,getWidth;
	
	public Icono(int nFrames,int getHeight,int getWidth) {
		super(nFrames);
		this.getHeight = getHeight;
		this.getWidth = getWidth;
	}
	
	public void proceso(int cicle) {
		// Creamos icono
		crearIcono(cicle);
		
		// Movemos icono
		moverIcono();
		
		// Borramos icono
		borrarIcono();
	}
	
	void crearIcono(int cicle) {
		if (!super.isActive()) {
			
			// Crear número aleatorio para crear o no un nuevo icono
			Random random = new java.util.Random();
			
			// Aproximadamente cada 100 ciclos se creará un icono
			if (Math.abs(random.nextInt())%100 == 1 && cicle%5 == 0) {
				// Crear número aleatorio para elegir la posición del icono en la pantalla
			    Random random2 = new java.util.Random();
				super.on();
				super.setX((Math.abs(random2.nextInt()) % (getWidth-getW())) + (1+getW()/2));
				super.setY(0);
				// Crear número aleatorio para elegir el tipo de icono
			    Random random3 = new java.util.Random(cicle);
				super.selFrame((Math.abs(random3.nextInt()) % (frames())) + 1);
			}
		}
	}
	
	void moverIcono() {
		if (super.isActive()) {
			super.setY(super.getY()+2);
		}
	}
	
	void borrarIcono() {
		if (super.isActive() && (super.getY()-super.getH()/2) > getHeight) {
			super.off();
		}
	}
}