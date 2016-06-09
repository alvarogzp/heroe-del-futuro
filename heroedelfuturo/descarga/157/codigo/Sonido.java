package code;

import java.io.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

class Sonido {

	private Player sonido;
	private VolumeControl controlVolumen;
	private boolean conSonido;

	public Sonido(String ruta, int volumen, boolean cargarSonido) {
		conSonido=cargarSonido;
		if (conSonido) {
			InputStream entrada = getClass().getResourceAsStream(ruta);
			try {
				sonido = Manager.createPlayer(entrada, "audio/x-wav");
				sonido.prefetch();
			} catch (Exception e) {
				System.out.println("Imposible cargar sonido " + ruta + ": " + e.toString());
			}
			controlVolumen = (VolumeControl)sonido.getControl("VolumeControl");
			setVolumen(volumen);
		}
	}

	public void setVolumen(int porcentaje) {
		if (conSonido) {
			// El valor de entrada es un número del 0 al 100 que indica el volumen en porcentaje
			controlVolumen.setLevel(porcentaje);
		}
	}

	public int getVolumen() {
		if (conSonido) {
			return controlVolumen.getLevel();
		} else {
			return 0;
		}
	}

	public void mute(boolean mute) {
		if (conSonido) {
			controlVolumen.setMute(mute);
		}
	}

	public boolean muteActivo() {
		if (conSonido) {
			return controlVolumen.isMuted();
		} else {
			return true;
		}
	}

	public void on(boolean reiniciar) {
		if (conSonido) {
			if (reiniciar) {
				setTime(0);
			}
			try {
				sonido.start();
			} catch (MediaException e) {
				System.out.println("ERROR: No se pudo reproducir el sonido: " + e.toString());
			}
		}
	}

	public void off() {
		if (conSonido) {
			try {
				sonido.stop();
			} catch (MediaException e) {
				System.out.println("ERROR: No se pudo parar el archivo: " + e.toString());
			}
		}
	}

	public int getState() {
		if (conSonido) {
			return sonido.getState();
		} else {
			return 0;
		}
	}

	public void setTime(long time) {
		if (conSonido) {
			try {
				sonido.setMediaTime(time);
			} catch (MediaException e) {
				System.out.println("ERROR: No se pudo elegir el tiempo del archivo: " + e.toString());
			}
		}
	}

	public void apagar() {
		if (conSonido) {
			sonido.close();
		}
	}
}