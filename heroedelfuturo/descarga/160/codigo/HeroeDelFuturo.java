package code;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class HeroeDelFuturo extends MIDlet implements CommandListener,Runnable {

	private Command exitCommand,playCommand,atrasCommand,controlesCommand;
	private Display display;
	private Level1 screen;
	private Level2 screen2;
	private Level3 screen3;
	private Form controles;
	private boolean jugando,nivel2Tocado;

	public HeroeDelFuturo() {
		display=Display.getDisplay(this);
		playCommand=new Command("Jugar",Command.SCREEN,1);
		controlesCommand=new Command("Ayuda",Command.SCREEN,4);
		exitCommand=new Command("Salir",Command.SCREEN,5);
		atrasCommand=new Command("Atrás",Command.BACK,1);

		screen=new Level1();
		screen.addCommand(playCommand);
		screen.addCommand(controlesCommand);
		screen.addCommand(exitCommand);
		screen.setCommandListener(this);

		screen2=new Level2();
		screen2.addCommand(playCommand);
		screen2.addCommand(controlesCommand);
		screen2.addCommand(exitCommand);
		screen2.setCommandListener(this);

		screen3=new Level3();
		screen3.addCommand(playCommand);
		screen3.addCommand(controlesCommand);
		screen3.addCommand(exitCommand);
		screen3.setCommandListener(this);

		nivel2Tocado = false;

		// La pantalla de los controles
		controles=new Form("Controles");
		StringItem texto=new StringItem("Nave","\n--Izquierda: Tecla izquierda o 4\n--Derecha: Tecla derecha o 6\n--Arriba: Tecla arriba o 2\n--Abajo: Tecla abajo o 8\n--Disparar: Tecla fuego o 5\n--Cambiar de arma: Tecla GAME C (* o 7)\n");
		StringItem texto2=new StringItem("Otros controles","--Menú: Tecla de navegación izquierda o derecha");
		controles.append(texto);
		controles.append(texto2);
		controles.addCommand(atrasCommand);
		controles.setCommandListener(this);
	}

	public void startApp() throws MIDletStateChangeException {
		display.setCurrent(screen);
		// Parpadeamiento del título
		screen.cicloTitulo();
		new Thread (this).start();
	}

	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {
		jugando = false;
	}

	public void commandAction(Command c, Displayable s) {
		if (c == exitCommand) {
			if (screen.isPlaying()) {
				screen.quitGame();
			} else if (screen2.isPlaying()) {
				screen2.quitGame();
			} else if (screen3.isPlaying()) {
				screen3.quitGame();
			} else {
				destroyApp(false);
				notifyDestroyed();
			}
		}

		if (c == playCommand) {
			if (!screen.isPlaying() && !screen2.isPlaying() && !screen3.isPlaying()) {
				// Play!!!
				if (nivel2Tocado) {
					nivel1();
				} else {
					nivel2Tocado=true;
					new Thread(screen).start();
				}
			}
		}

		if (c == atrasCommand) {
			if (screen3.isPlaying()) {
				display.setCurrent(screen3);
			} else if (screen2.isPlaying()) {
				display.setCurrent(screen2);
			} else {
				display.setCurrent(screen);
			}
		}

		if (c == controlesCommand) {
			display.setCurrent(controles);
		}
	}

	void nivel1() {
		display.setCurrent(screen);
		new Thread(screen).start();
	}

	void nivel2() {
		display.setCurrent(screen2);
		screen2.run(screen.vidas(),screen.puntos(),screen.tiempoJugado());
	}

	void nivel3() {
		display.setCurrent(screen3);
		screen3.run(screen2.vidas(),screen2.puntos(),screen2.tiempoJugado(),screen2.balasSuperDisparo());
	}

	public void run() {
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