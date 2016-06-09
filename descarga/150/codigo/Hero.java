class Hero extends Sprite {

	private int state;
	private boolean animacion;

	public void setState(int state) {
		this.state=state;
	}

	public int getState(int state) {
		return state;
	}
	
	public void doAnimation() {
		if (getFrame() >= 3) {
			switch (getFrame()) {
				case 3:
					selFrame(4);
					animacion=true;
					break;
				case 4:
					if (animacion) {
						selFrame(5);
					} else {
						selFrame(3);
					}
					break;
				case 5:
					selFrame(4);
					animacion=false;
					break;
			}
		}
	}
	
	public Hero(int nFrames) {
		super(nFrames);
	}
}