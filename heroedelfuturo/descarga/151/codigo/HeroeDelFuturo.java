import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class HeroeDelFuturo extends MIDlet implements CommandListener {

	private Command exitCommand, playCommand;
	private Display display;
	private Level1 screen;
	private Level2 screen2;
	private Level3 screen3;
	private boolean jugando,nivel2Tocado;

	public HeroeDelFuturo() {
		display=Display.getDisplay(this);
		playCommand = new Command("Jugar",Command.OK,1);
		exitCommand = new Command("Salir",Command.EXIT,2);

		screen=new Level1();
		screen2=new Level2();
		screen3=new Level3();
		nivel2Tocado = false;

		screen.addCommand(playCommand);
		screen.addCommand(exitCommand);
		screen.setCommandListener(this);
	}

	public void startApp() throws MIDletStateChangeException {
		display.setCurrent(screen);
		// Parpadeamiento del título
		screen.cicloTitulo();
		controlNiveles();
	}

	public void pauseApp() {
		if (screen.isPlaying()) {
			screen.pausar();
		} else {
			if (screen2.isPlaying()) {
				screen2.pausar();
			} else {
				if (screen3.isPlaying()) {
					screen3.pausar();
				}
			}
		}
	}

	public void destroyApp(boolean unconditional) {
		jugando = false;
	}

	public void commandAction(Command c, Displayable s) {
		if (c == exitCommand) {
			if (screen.isPlaying()) {
				screen.quitGame();
			} else {
				if (screen2.isPlaying()) {
					screen2.quitGame();
				} else {
					if (screen3.isPlaying()) {
						screen3.quitGame();
					} else {
						destroyApp(false);
						notifyDestroyed();
					}
				}
			}
		}

		if (c == playCommand) {
			if (!screen.isPlaying() && !screen2.isPlaying() && !screen3.isPlaying()) {
				// Play!!!
				if (nivel2Tocado) {
					nivel1();
				} else {
					new Thread(screen).start();
				}
			}
		}
	}
	
	void nivel1() {
		screen.addCommand(playCommand);
		screen.addCommand(exitCommand);
		screen.setCommandListener(this);
		display.setCurrent(screen);
		new Thread(screen).start();
	}
	
	void nivel2() {
		nivel2Tocado = true;
		screen2.addCommand(playCommand);
		screen2.addCommand(exitCommand);
		screen2.setCommandListener(this);
		display.setCurrent(screen2);
		screen2.run(screen.vidas(),screen.puntos(),screen.tiempoJugado());
	}
	
	void nivel3() {
		screen3.addCommand(playCommand);
		screen3.addCommand(exitCommand);
		screen3.setCommandListener(this);
		display.setCurrent(screen3);
		screen3.run(screen2.vidas(),screen2.puntos(),screen2.tiempoJugado(),screen2.balasSuperDisparo());
	}
	
	void controlNiveles() {
		jugando = true;
		
		while (jugando) {
			if (screen.nivelGanado()) {
				nivel2();
			}
			
			if (screen2.nivelGanado()) {
				nivel3();
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("controlNiveles Exception: " + e.toString());
			}
		}
	}
}