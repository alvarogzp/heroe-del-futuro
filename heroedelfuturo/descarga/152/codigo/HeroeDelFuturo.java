import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class HeroeDelFuturo extends MIDlet implements CommandListener {

	private Command exitCommand,playCommand,volumenMusicaSubir,volumenMusicaBajar,volumenEfectosSubir,volumenEfectosBajar,sinSonidoCommand,conSonidoCommand;
	private Display display;
	private Level1 screen;
	private Level2 screen2;
	private Level3 screen3;
	private boolean jugando,nivel2Tocado;

	public HeroeDelFuturo() {
		display=Display.getDisplay(this);
		playCommand = new Command("Jugar",Command.OK,1);
		sinSonidoCommand = new Command("Desactivar sonidos",Command.SCREEN,1);
		conSonidoCommand = new Command("Activar sonidos",Command.SCREEN,1);
		volumenMusicaSubir = new Command("Volumen Musica +",Command.SCREEN,2);
		volumenMusicaBajar = new Command("Volumen Musica -",Command.SCREEN,3);
		volumenEfectosSubir = new Command("Volumen Efectos +",Command.SCREEN,4);
		volumenEfectosBajar = new Command("Volumen Efectos -",Command.SCREEN,5);
		exitCommand = new Command("Salir",Command.EXIT,2);

		screen=new Level1();
		screen2=new Level2();
		screen3=new Level3();
		nivel2Tocado = false;

		screen.addCommand(playCommand);
		screen.addCommand(sinSonidoCommand);
		screen.addCommand(volumenMusicaSubir);
		screen.addCommand(volumenMusicaBajar);
		screen.addCommand(volumenEfectosSubir);
		screen.addCommand(volumenEfectosBajar);
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
					screen.removeCommand(sinSonidoCommand);
					screen.removeCommand(conSonidoCommand);
					new Thread(screen).start();
				}
			}
		}
		
		if (c == volumenMusicaSubir) {
			if (s == screen) {
				int i = screen.volumen("musica",+10);
				screen2.setVolumen("musica",i);
				screen3.setVolumen("musica",i);
			}
			if (s == screen2) {
				int i = screen2.volumen("musica",+10);
				screen.setVolumen("musica",i);
				screen3.setVolumen("musica",i);
			}
			if (s == screen3) {
				int i = screen3.volumen("musica",+10);
				screen.setVolumen("musica",i);
				screen2.setVolumen("musica",i);
			}
		}
		
		if (c == volumenMusicaBajar) {
			if (s == screen) {
				int i = screen.volumen("musica",-10);
				screen2.setVolumen("musica",i);
				screen3.setVolumen("musica",i);
			}
			if (s == screen2) {
				int i = screen2.volumen("musica",-10);
				screen.setVolumen("musica",i);
				screen3.setVolumen("musica",i);
			}
			if (s == screen3) {
				int i = screen3.volumen("musica",-10);
				screen.setVolumen("musica",i);
				screen2.setVolumen("musica",i);
			}
		}
		
		if (c == volumenEfectosSubir) {
			if (s == screen) {
				int i = screen.volumen("efectos",+10);
				screen2.setVolumen("efectos",i);
				screen3.setVolumen("efectos",i);
			}
			if (s == screen2) {
				int i = screen2.volumen("efectos",+10);
				screen.setVolumen("efectos",i);
				screen3.setVolumen("efectos",i);
			}
			if (s == screen3) {
				int i = screen3.volumen("efectos",+10);
				screen.setVolumen("efectos",i);
				screen2.setVolumen("efectos",i);
			}
		}
		
		if (c == volumenEfectosBajar) {
			if (s == screen) {
				int i = screen.volumen("efectos",-10);
				screen2.setVolumen("efectos",i);
				screen3.setVolumen("efectos",i);
			}
			if (s == screen2) {
				int i = screen2.volumen("efectos",-10);
				screen.setVolumen("efectos",i);
				screen3.setVolumen("efectos",i);
			}
			if (s == screen3) {
				int i = screen3.volumen("efectos",-10);
				screen.setVolumen("efectos",i);
				screen2.setVolumen("efectos",i);
			}
		}
		
		if (c == sinSonidoCommand) {
			screen.sonido(false);
			screen2.sonido(false);
			screen3.sonido(false);
			screen.removeCommand(sinSonidoCommand);
			screen.addCommand(conSonidoCommand);
		}
		
		if (c == conSonidoCommand) {
			screen.sonido(true);
			screen2.sonido(true);
			screen3.sonido(true);
			screen.removeCommand(conSonidoCommand);
			screen.addCommand(sinSonidoCommand);
		}
	}
	
	void nivel1() {
		screen.addCommand(playCommand);
		screen.addCommand(volumenMusicaSubir);
		screen.addCommand(volumenMusicaBajar);
		screen.addCommand(volumenEfectosSubir);
		screen.addCommand(volumenEfectosBajar);
		screen.addCommand(exitCommand);
		screen.setCommandListener(this);
		display.setCurrent(screen);
		new Thread(screen).start();
	}
	
	void nivel2() {
		nivel2Tocado = true;
		screen2.addCommand(playCommand);
		screen2.addCommand(volumenMusicaSubir);
		screen2.addCommand(volumenMusicaBajar);
		screen2.addCommand(volumenEfectosSubir);
		screen2.addCommand(volumenEfectosBajar);
		screen2.addCommand(exitCommand);
		screen2.setCommandListener(this);
		display.setCurrent(screen2);
		screen2.run(screen.vidas(),screen.puntos(),screen.tiempoJugado());
	}
	
	void nivel3() {
		screen3.addCommand(playCommand);
		screen3.addCommand(volumenMusicaSubir);
		screen3.addCommand(volumenMusicaBajar);
		screen3.addCommand(volumenEfectosSubir);
		screen3.addCommand(volumenEfectosBajar);
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