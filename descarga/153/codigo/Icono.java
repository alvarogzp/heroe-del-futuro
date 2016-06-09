class Icono extends Sprite {
	
	private int getHeight,getWidth;
	
	public Icono(int nFrames,int getHeight,int getWidth) {
		super(nFrames);
		this.getHeight = getHeight;
		this.getWidth = getWidth;
	}
	
	public void proceso(int random1,int random2,int random3) {
		// Creamos icono
		crearIcono(random1,random2,random3);
		
		// Movemos icono
		moverIcono();
		
		// Borramos icono
		borrarIcono();
	}
	
	void crearIcono(int random1,int random2,int random3) {
		if (!super.isActive()) {
			// Aproximadamente cada 500 ciclos se creará un icono
			if (random1%500 == 1) {
				super.on();
				// Posición aleatoria en la pantalla
				super.setX((random2%(getWidth-getW())) + (1+getW()/2));
				super.setY(0);
				// Icono aleatorio
				super.selFrame((random3%(frames())) + 1);
			}
		}
	}
	
	void moverIcono() {
		if (isActive()) {
			setY(getY()+2);
		}
	}
	
	void borrarIcono() {
		if (isActive() && (getY()-getH()/2) > getHeight) {
			off();
		}
	}
}