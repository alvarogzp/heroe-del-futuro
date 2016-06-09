package code;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class HeroeDelFuturo extends MIDlet implements CommandListener,ItemStateListener {

	private Command exitCommand,playCommand,volumenCommand,modoCommand,atrasCommand,valeCommand,controlesCommand;
	private Display display;
	private Level1 screen;
	private Level2 screen2;
	private Level3 screen3;
	private Form volumen,controles;
	private List modo;
	private Gauge volumenMusica,volumenEfectos;
	private boolean jugando,nivel2Tocado;

	public HeroeDelFuturo() {
		display=Display.getDisplay(this);
		playCommand=new Command("Jugar",Command.SCREEN,1);
		modoCommand=new Command("Modo de juego",Command.SCREEN,2);
		volumenCommand=new Command("Volumen",Command.SCREEN,3);
		controlesCommand=new Command("Ayuda",Command.SCREEN,4);
		exitCommand=new Command("Salir",Command.SCREEN,5);
		atrasCommand=new Command("Atrás",Command.BACK,1);
		valeCommand=new Command("Aceptar",Command.OK,1);

		screen=new Level1();
		screen.addCommand(playCommand);
		screen.addCommand(volumenCommand);
		screen.addCommand(modoCommand);
		screen.addCommand(controlesCommand);
		screen.addCommand(exitCommand);
		screen.setCommandListener(this);

		screen2=new Level2();
		screen2.addCommand(playCommand);
		screen2.addCommand(volumenCommand);
		screen2.addCommand(controlesCommand);
		screen2.addCommand(exitCommand);
		screen2.setCommandListener(this);

		screen3=new Level3();
		screen3.addCommand(playCommand);
		screen3.addCommand(volumenCommand);
		screen3.addCommand(controlesCommand);
		screen3.addCommand(exitCommand);
		screen3.setCommandListener(this);

		nivel2Tocado = false;

		// La pantalla de configuración del volumen
		volumen=new Form("Volumen");
		StringItem nota=new StringItem("Nota","El volumen no se actualizará hasta que no estés jugando");
		volumenMusica=new Gauge("Música",true,10,2);
		volumenEfectos=new Gauge("Efectos",true,10,10);
		volumen.append(nota);
		volumen.append(volumenMusica);
		volumen.append(volumenEfectos);
		volumen.setItemStateListener(this);
		volumen.addCommand(atrasCommand);
		volumen.setCommandListener(this);

		// La pantalla del modo de juego (con sonido o sin sonido)
		String[] modos={"Jugar con sonido","Jugar sin sonido"};
		modo=new List("Modo de juego",List.IMPLICIT,modos,null);
		modo.addCommand(atrasCommand);
		modo.addCommand(valeCommand);
		modo.setCommandListener(this);

		// La pantalla de los controles
		controles=new Form("Controles");
		StringItem texto=new StringItem("Nave","\n--Izquierda: Tecla izquierda o 4\n--Derecha: Tecla derecha o 6\n--Arriba: Tecla arriba o 2\n--Abajo: Tecla abajo o 8\n--Disparar: Tecla fuego o 5\n--Cambiar de arma: Tecla GAME C (* o 7)\n");
		StringItem texto2=new StringItem("Otros controles","\n--Poner sonido en mute: Tecla GAME D (# o 9)\n--Menú: Tecla de navegación izquierda o derecha");
		controles.append(texto);
		controles.append(texto2);
		controles.addCommand(atrasCommand);
		controles.setCommandListener(this);
	}

	public void startApp() throws MIDletStateChangeException {
		display.setCurrent(screen);
		// Parpadeamiento del título
		screen.cicloTitulo();
		controlNiveles();
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
					screen.removeCommand(modoCommand);
					nivel2Tocado=true;
					new Thread(screen).start();
				}
			}
		}

		if (c == volumenCommand) {
			volumenMusica.setValue(screen.musicaLevel()/10);
			volumenEfectos.setValue(screen.efectosLevel()/10);
			display.setCurrent(volumen);
		}

		if (c == modoCommand) {
			display.setCurrent(modo);
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

		if (c == valeCommand) {
			int i=modo.getSelectedIndex();
			if (i == 0) {
				screen.sonido(true);
				screen2.sonido(true);
				screen3.sonido(true);
			}
			if (i == 1) {
				screen.sonido(false);
				screen2.sonido(false);
				screen3.sonido(false);
			}
			display.setCurrent(screen);
		}

		if (c == controlesCommand) {
			display.setCurrent(controles);
		}
	}

	public void itemStateChanged(Item elemento) {
		if (elemento == volumenMusica) {
			int i=volumenMusica.getValue()*10;
			screen.setVolumen("musica",i);
			screen2.setVolumen("musica",i);
			screen3.setVolumen("musica",i);
		}

		if (elemento == volumenEfectos) {
			int i=volumenEfectos.getValue()*10;
			screen.setVolumen("efectos",i);
			screen2.setVolumen("efectos",i);
			screen3.setVolumen("efectos",i);
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