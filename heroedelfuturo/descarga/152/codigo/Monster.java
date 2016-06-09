class Monster extends Sprite {

	private int deltaX,deltaY;

	public void setDeltaX(int deltaX) {
		this.deltaX=deltaX;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public void setDeltaY(int deltaY) {
		this.deltaY=deltaY;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public void init() {
		deltaY=1;
		deltaX=0;
	}

	public Monster(int nFrames) {
		super(nFrames);
	}
}