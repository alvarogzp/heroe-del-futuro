import java.io.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;

class Sonido {
	
	private Player sonido;
	private VolumeControl controlVolumen;
	
	public Sonido(String ruta, int volumen) {
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
	
	public void setVolumen(int porcentaje) {
		// El valor de entrada es un número del 0 al 100 que indica el volumen en porcentaje
		controlVolumen.setLevel(porcentaje);
	}
	
	public int getVolumen() {
		return controlVolumen.getLevel();
	}
	
	public void mute(boolean mute) {
		controlVolumen.setMute(mute);
	}
	
	public boolean muteActivo() {
		return controlVolumen.isMuted();
	}
	
	public void on(boolean reiniciar) {
		if (reiniciar) {
			setTime(0);
		}
		try {
			sonido.start();
		} catch (MediaException e) {
			System.out.println("ERROR: No se pudo reproducir el sonido: " + e.toString());
		}
	}
	
	public void off() {
		try {
			sonido.stop();
		} catch (MediaException e) {
			System.out.println("ERROR: No se pudo parar el archivo: " + e.toString());
		}
	}
	
	public int getState() {
		return sonido.getState();
	}
	
	public void setTime(long time) {
		try {
			sonido.setMediaTime(time);
		} catch (MediaException e) {
			System.out.println("ERROR: No se pudo elegir el tiempo del archivo: " + e.toString());
		}
	}
	
	public void apagar() {
		sonido.close();
	}
}