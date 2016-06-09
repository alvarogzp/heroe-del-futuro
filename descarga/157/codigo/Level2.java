package code;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;

class Level2 extends GameCanvas {
	private int score,sleepTime,cicle,lives,shield,indice_in,indice,xTiles,yTiles,deltaX,deltaY,finishLevel,cicloCount,cicloEspera,heroBullets,monsterLives,tiempoFin,volumenMusica,volumenEfectos,sleepTimeStatic,pLenght,scoreLevel1,ciclosDisparo,tiempoNuevoNivel,superDisparos,superDisparoActivo,superDisparosTotales,puntosBonus25,puntosBonus50;
	private boolean playing,fireOn,cargandoActivo,tiempoFinOn,muerto,tecla,pausa,ganado,superDisparo,monstruoAnimacion,monstruoAnimacionEstado,teclaIzquierda,teclaDerecha,teclaArriba,teclaAbajo,gameCpress,gameDpress,cargarSonido,ganadoForever,comprobarPaint;
	private boolean[] tiles,musicaApagada;
	private long tiempoInicio,tiempoBase,tiempoActual,tiempoPausa;
	private Sprite hero=new Sprite(1);
	private Enemy[] enemies=new Enemy[7];
	private Bullet[] aBullet=new Bullet[30];
	private Sprite intro=new Sprite(1);
	private Sprite cargando=new Sprite(25);
	private Sprite inferno=new Sprite(1);
	private Sprite muertoImg=new Sprite(1);
	private Sprite introduccion=new Sprite(3);
	private Sprite pausaImg=new Sprite(1);
	private Sprite titulo=new Sprite(1);
	private Sprite hud=new Sprite(2);
	private Sprite startAnimacion=new Sprite(25);
	private Monster monster;
	private Explode explode0,explode3;
	private Explode[] explode1=new Explode[7];
	private Explode[] explode2=new Explode[7];
	private Explode[] explode4=new Explode[7];
	private Sprite[] tile=new Sprite[7];
	private Sonido[] musica = new Sonido[15];
	private Icono[] iconos = new Icono[4];

	// Mapa del juego
	int map[] ={4,4,4,4,4,4,4,4,
				4,4,4,4,5,4,4,4,
				4,2,4,4,4,4,1,4,
				4,4,4,1,4,4,4,4,
				4,6,4,4,4,3,4,4,
				4,4,3,4,4,4,4,4,
				4,4,4,4,2,4,1,4,
				4,4,6,4,4,4,4,4,
				4,3,4,4,4,5,4,4,
				4,4,4,4,2,4,4,4,
				4,1,4,4,4,6,4,4,
				4,4,4,3,4,4,4,2,
				4,4,4,4,4,4,4,4,
				4,4,4,4,5,4,4,4,
				4,2,4,4,4,4,1,4,
				4,4,4,1,4,4,4,4,
				4,6,4,4,4,3,4,4,
				4,4,3,4,4,4,4,4,
				4,4,4,4,2,4,1,4,
				4,4,6,4,4,4,4,4,
				4,3,4,4,4,5,4,4,
				4,4,4,4,2,4,4,4,
				4,1,4,4,4,6,4,4,
				4,4,4,3,4,4,4,2};

	public Level2() {
		// Lamada a la clase padre canvas
		super(true);
		// Pantalla completa
		setFullScreenMode(true);
		// Cargamos los sprites
		intro.addFrame(1,"/images/intro.png");
		muertoImg.addFrame(1,"/images/muerto.png");
		pausaImg.addFrame(1,"/images/pause.png");
		titulo.addFrame(1,"/images/HeroeDelFuturo.png");

		// Iniciamos los Sprites
		intro.on();
		muertoImg.on();
		pausaImg.on();
		titulo.off();

		ganado = false;
		ganadoForever = ganado;
		cargarSonido = true;
		comprobarPaint = true;

		// Variables estáticas
		sleepTime=50;
		sleepTimeStatic=sleepTime*20;
		volumenMusica=25;
		volumenEfectos=100;
		ciclosDisparo=4;
		xTiles=8;
		yTiles=12;
		finishLevel=250;
		cicloEspera=40;
		tiempoNuevoNivel=100;
		superDisparosTotales=7;
	}

	void cargaIntroduccion() {
		cargandoActualiza(2);
		introduccion.setX(getWidth()/2);
		introduccion.setY(getHeight()/2);
		introduccion.addFrame(1,"/images/2introduccion1.png");
		cargandoActualiza(3);
		introduccion.addFrame(2,"/images/2introduccion2.png");
		cargandoActualiza(4);
		introduccion.addFrame(3,"/images/2introduccion3.png");
		cargandoActualiza(5);
		introduccion.selFrame(1);

		cicle = 0;
		pausa=false;
		tecla=false;
		gameDpress=false;
		puntosBonus25=0;
		puntosBonus50=0;

		cargaMusica();

		cargandoActualiza(20);
		cargandoActualiza(21);
		cargandoActualiza(22);
		cargandoActualiza(23);
		cargandoActualiza(24);
		cargandoActualiza(25);

		cargandoDescarga();

		introduccion.on();
		repaint();
		serviceRepaints();
	}

	void start(int vidasNivel1,int puntosNivel1,long tiempoNivel1) {
		int i;
		cargandoActiva();
		fireOn=false;
		tiempoFinOn=false;
		muerto=false;
		tecla=false;
		ganado=false;
		ganadoForever=ganado;
		monstruoAnimacion=false;
		superDisparo=false;
		gameCpress=false;
		hero.setX(getWidth()/2-5);
		hero.setY(getHeight()-45);
		deltaX=0;
		deltaY=0;
		cicle=0;
		indice=map.length-(xTiles*yTiles);
		indice_in=0;
		score=0;
		scoreLevel1=puntosNivel1;
		lives=vidasNivel1+5;
		shield=0;
		cicloCount=0;
		heroBullets=6;
		monsterLives=40;
		tiempoFin=50;
		superDisparos=0;
		superDisparoActivo=0;
		tiempoBase=tiempoNivel1;
		tiempoPausa=0;

		// Inicializamos héroes
		hero.addFrame(1,"/images/hero.png");
		hero.on();

		// Actualizar hud
		hud.addFrame(1,"/images/2hud1.png");
		hud.addFrame(2,"/images/2hud1t.png");
		hud.off();

		cargandoActualiza(2);

		// Inicializar enemigos
		for (i=1 ; i<=6 ; i++) {
			enemies[i]=new Enemy(7);
			enemies[i].addFrame(1,"/images/2enemy1.png");
			enemies[i].addFrame(2,"/images/2enemy2.png");
			enemies[i].addFrame(3,"/images/2enemy3-1.png");
			enemies[i].addFrame(4,"/images/2enemy3-2.png");
			enemies[i].addFrame(5,"/images/2enemy3-3.png");
			enemies[i].addFrame(6,"/images/2enemy3-4.png");
			enemies[i].addFrame(7,"/images/2enemy3-5.png");
			enemies[i].off();
			if (i%3 == 2) {
				cargandoActualiza(i/3+3);
			 }
		}

		// Inicializar monstruo
		monster=new Monster(6);
		monster.addFrame(1,"/images/2monster.png");
		monster.addFrame(2,"/images/2monster-1.png");
		monster.addFrame(3,"/images/2monster-2.png");
		monster.addFrame(4,"/images/2monster-3.png");
		monster.addFrame(5,"/images/2monster-4.png");
		monster.addFrame(6,"/images/2monster-5.png");
		monster.off();


		cargandoActualiza(5);

		// Inicializar balas
		for (i=1 ; i<=29 ; i++) {
			aBullet[i]=new Bullet(5);
			aBullet[i].addFrame(1,"/images/mybullet.png");
			aBullet[i].addFrame(2,"/images/2enemybullet1.png");
			aBullet[i].addFrame(3,"/images/2monsterbullet.png");
			aBullet[i].addFrame(4,"/images/2enemybullet3.png");
			aBullet[i].addFrame(5,"/images/2mysuperbullet.png");
			aBullet[i].off();
			if (i%5 == 0) {
				cargandoActualiza(i/5+5);
			 }
		}

		// Inicializamos los tiles
		// tile 1
		tile[1]=new Sprite(4);
		tile[1].on();
		tile[1].addFrame(1,"/images/2tile1-1.png");
		tile[1].addFrame(2,"/images/2tile1-2.png");
		tile[1].addFrame(3,"/images/2tile1-3.png");
		tile[1].addFrame(4,"/images/2tile1-4.png");

		// tile 2
		tile[2]=new Sprite(4);
		tile[2].on();
		tile[2].addFrame(1,"/images/2tile2-1.png");
		tile[2].addFrame(2,"/images/2tile2-2.png");
		tile[2].addFrame(3,"/images/2tile2-3.png");
		tile[2].addFrame(4,"/images/2tile2-4.png");

		// tile 3
		tile[3]=new Sprite(4);
		tile[3].on();
		tile[3].addFrame(1,"/images/2tile3-1.png");
		tile[3].addFrame(2,"/images/2tile3-2.png");
		tile[3].addFrame(3,"/images/2tile3-3.png");
		tile[3].addFrame(4,"/images/2tile3-4.png");

		// Variable tiles con los incrementos y disminuciones
		tiles=new boolean[4];

		// tile 4
		tile[4]=new Sprite(1);
		tile[4].on();
		tile[4].addFrame(1,"/images/2tile4.png");

		// tile 5
		tile[5]=new Sprite(1);
		tile[5].on();
		tile[5].addFrame(1,"/images/2tile5.png");

		// tile 6
		tile[6]=new Sprite(1);
		tile[6].on();
		tile[6].addFrame(1,"/images/2tile6.png");

		// Explosiones 0
		explode0=new Explode(14);
		explode0.addFrame(1,"/images/explode0-1.png");
		explode0.addFrame(2,"/images/explode0-2.png");
		explode0.addFrame(3,"/images/explode0-3.png");
		explode0.addFrame(4,"/images/explode0-4.png");
		explode0.addFrame(5,"/images/explode0-5.png");
		explode0.addFrame(6,"/images/explode0-6.png");
		explode0.addFrame(7,"/images/explode0-7.png");
		explode0.addFrame(8,"/images/explode0-8.png");
		explode0.addFrame(9,"/images/explode0-9.png");
		explode0.addFrame(10,"/images/explode0-10.png");
		explode0.addFrame(11,"/images/explode0-11.png");
		explode0.addFrame(12,"/images/explode0-12.png");
		explode0.addFrame(13,"/images/explode0-13.png");
		explode0.addFrame(14,"/images/explode0-14.png");

		cargandoActualiza(11);

		// Explosiones 1
		for (i=1 ; i<=6 ; i++) {
			explode1[i]=new Explode(15);
			explode1[i].addFrame(1,"/images/2explode1-1.png");
			explode1[i].addFrame(2,"/images/2explode1-2.png");
			explode1[i].addFrame(3,"/images/2explode1-3.png");
			explode1[i].addFrame(4,"/images/2explode1-4.png");
			explode1[i].addFrame(5,"/images/2explode1-5.png");
			explode1[i].addFrame(6,"/images/2explode1-6.png");
			explode1[i].addFrame(7,"/images/2explode1-7.png");
			explode1[i].addFrame(8,"/images/2explode1-8.png");
			explode1[i].addFrame(9,"/images/2explode1-9.png");
			explode1[i].addFrame(10,"/images/2explode1-10.png");
			explode1[i].addFrame(11,"/images/2explode1-11.png");
			explode1[i].addFrame(12,"/images/2explode1-12.png");
			explode1[i].addFrame(13,"/images/2explode1-13.png");
			explode1[i].addFrame(14,"/images/2explode1-14.png");
			explode1[i].addFrame(15,"/images/2explode1-15.png");

			cargandoActualiza(i+11);

		}

		// Explosiones 2
		for (i=1 ; i<=6 ; i++) {
			explode2[i]=new Explode(16);
			explode2[i].addFrame(1,"/images/2explode2-1.png");
			explode2[i].addFrame(2,"/images/2explode2-2.png");
			explode2[i].addFrame(3,"/images/2explode2-3.png");
			explode2[i].addFrame(4,"/images/2explode2-4.png");
			explode2[i].addFrame(5,"/images/2explode2-5.png");
			explode2[i].addFrame(6,"/images/2explode2-6.png");
			explode2[i].addFrame(7,"/images/2explode2-7.png");
			explode2[i].addFrame(8,"/images/2explode2-8.png");
			explode2[i].addFrame(9,"/images/2explode2-9.png");
			explode2[i].addFrame(10,"/images/2explode2-10.png");
			explode2[i].addFrame(11,"/images/2explode2-11.png");
			explode2[i].addFrame(12,"/images/2explode2-12.png");
			explode2[i].addFrame(13,"/images/2explode2-13.png");
			explode2[i].addFrame(14,"/images/2explode2-14.png");
			explode2[i].addFrame(15,"/images/2explode2-15.png");
			explode2[i].addFrame(16,"/images/2explode2-16.png");

			cargandoActualiza(i+17);

		}

		// Explosiones 3
		explode3=new Explode(16);
		explode3.addFrame(1,"/images/2explode3-1.png");
		explode3.addFrame(2,"/images/2explode3-2.png");
		explode3.addFrame(3,"/images/2explode3-3.png");
		explode3.addFrame(4,"/images/2explode3-4.png");
		explode3.addFrame(5,"/images/2explode3-5.png");
		explode3.addFrame(6,"/images/2explode3-6.png");
		explode3.addFrame(7,"/images/2explode3-7.png");
		explode3.addFrame(8,"/images/2explode3-8.png");
		explode3.addFrame(9,"/images/2explode3-9.png");
		explode3.addFrame(10,"/images/2explode3-10.png");
		explode3.addFrame(11,"/images/2explode3-11.png");
		explode3.addFrame(12,"/images/2explode3-12.png");
		explode3.addFrame(13,"/images/2explode3-13.png");
		explode3.addFrame(14,"/images/2explode3-14.png");
		explode3.addFrame(15,"/images/2explode3-15.png");
		explode3.addFrame(16,"/images/2explode3-16.png");

		cargandoActualiza(24);

		// Explosiones 4
		for (i=1 ; i<=6 ; i++) {
			explode4[i]=new Explode(10);
			explode4[i].addFrame(1,"/images/2explode4-1.png");
			explode4[i].addFrame(2,"/images/2explode4-2.png");
			explode4[i].addFrame(3,"/images/2explode4-3.png");
			explode4[i].addFrame(4,"/images/2explode4-4.png");
			explode4[i].addFrame(5,"/images/2explode4-5.png");
			explode4[i].addFrame(6,"/images/2explode4-6.png");
			explode4[i].addFrame(7,"/images/2explode4-7.png");
			explode4[i].addFrame(8,"/images/2explode4-8.png");
			explode4[i].addFrame(9,"/images/2explode4-9.png");
			explode4[i].addFrame(10,"/images/2explode4-10.png");
		}

		// Inicializar iconos
		for (i=1 ; i<=3 ; i++) {
			iconos[i]=new Icono(3,getHeight(),getWidth());
			iconos[i].addFrame(1,"/images/2icono1.png");
			iconos[i].addFrame(2,"/images/icono25.png");
			iconos[i].addFrame(3,"/images/icono50.png");
		}

		cargandoActualiza(25);

		// Animación del principio
		startAnimacion.addFrame(1,"/images/start1.png");
		startAnimacion.addFrame(2,"/images/start2.png");
		startAnimacion.addFrame(3,"/images/start3.png");
		startAnimacion.addFrame(4,"/images/start4.png");
		startAnimacion.addFrame(5,"/images/start5.png");
		startAnimacion.addFrame(6,"/images/start6.png");
		startAnimacion.addFrame(7,"/images/start7.png");
		startAnimacion.addFrame(8,"/images/start8.png");
		startAnimacion.addFrame(9,"/images/start9.png");
		startAnimacion.addFrame(10,"/images/start10.png");
		startAnimacion.addFrame(11,"/images/start11.png");
		startAnimacion.addFrame(12,"/images/start12.png");
		startAnimacion.addFrame(13,"/images/start13.png");
		startAnimacion.addFrame(14,"/images/start14.png");
		startAnimacion.addFrame(15,"/images/start15.png");
		startAnimacion.addFrame(16,"/images/start16.png");
		startAnimacion.addFrame(17,"/images/start17.png");
		startAnimacion.addFrame(18,"/images/start18.png");
		startAnimacion.addFrame(19,"/images/start19.png");
		startAnimacion.addFrame(20,"/images/start20.png");
		startAnimacion.addFrame(21,"/images/start21.png");
		startAnimacion.addFrame(22,"/images/start22.png");
		startAnimacion.addFrame(23,"/images/start23.png");
		startAnimacion.addFrame(24,"/images/start24.png");
		startAnimacion.addFrame(25,"/images/start25.png");
		startAnimacion.on();
		startAnimacion.selFrame(1);

		cargandoDescarga();
	}

	// Barra de cargando...
	void cargandoCarga() {
		cargandoActiva();
		cargando.setX(getWidth()/2);
		cargando.setY(getHeight()/2);
		cargando.addFrame(1,"/images/loading1.png");
		inferno.addFrame(1,"/images/2level2.png");
		inferno.setX(getWidth()/2);
		inferno.setY(cargando.getY()-cargando.getH()/2-inferno.getH()/2-20);
		cargandoActualiza(1);
		cargando.addFrame(2,"/images/loading2.png");
		cargando.addFrame(3,"/images/loading3.png");
		cargando.addFrame(4,"/images/loading4.png");
		cargando.addFrame(5,"/images/loading5.png");
		cargando.addFrame(6,"/images/loading6.png");
		cargando.addFrame(7,"/images/loading7.png");
		cargando.addFrame(8,"/images/loading8.png");
		cargando.addFrame(9,"/images/loading9.png");
		cargando.addFrame(10,"/images/loading10.png");
		cargando.addFrame(11,"/images/loading11.png");
		cargando.addFrame(12,"/images/loading12.png");
		cargando.addFrame(13,"/images/loading13.png");
		cargando.addFrame(14,"/images/loading14.png");
		cargando.addFrame(15,"/images/loading15.png");
		cargando.addFrame(16,"/images/loading16.png");
		cargando.addFrame(17,"/images/loading17.png");
		cargando.addFrame(18,"/images/loading18.png");
		cargando.addFrame(19,"/images/loading19.png");
		cargando.addFrame(20,"/images/loading20.png");
		cargando.addFrame(21,"/images/loading21.png");
		cargando.addFrame(22,"/images/loading22.png");
		cargando.addFrame(23,"/images/loading23.png");
		cargando.addFrame(24,"/images/loading24.png");
		cargando.addFrame(25,"/images/loading25.png");
	}

	void cargandoActiva() {
		cargandoActivo = true;
	}

	void cargandoDescarga() {
		cargandoActivo = false;
	}

	void cargandoActualiza(int porcentaje) {
		cargando.selFrame(porcentaje);
		repaint();
		serviceRepaints();
	}

	void cargaMusica() {
		musica[1] = new Sonido("/music/2backmusic.wav",volumenMusica,cargarSonido);
		cargandoActualiza(6);
		musica[2] = new Sonido("/music/2monster.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(7);
		musica[3] = new Sonido("/music/explode0.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(8);
		musica[4] = new Sonido("/music/2explode12.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(9);
		musica[5] = new Sonido("/music/2explode3.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(10);
		musica[6] = new Sonido("/music/2explodemonster.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(11);
		musica[7] = new Sonido("/music/herobullet.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(12);
		musica[8] = new Sonido("/music/2enemybullet1.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(13);
		musica[9] = new Sonido("/music/2monsterbullet.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(14);
		musica[10] = new Sonido("/music/2enemybullet3.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(15);
		musica[11] = new Sonido("/music/pickweapon.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(16);
		musica[12] = new Sonido("/music/2superdisparo.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(17);
		musica[13] = new Sonido("/music/bonus25.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(18);
		musica[14] = new Sonido("/music/bonus50.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(19);

		pLenght=14;
		musicaApagada=new boolean[pLenght+1];

		musica[1].on(true);
	}

	void computeMusic() {
		musica[1].on(false);
	}

	void pararMusica() {
		int i;
		for (i=1 ; i<=pLenght ; i++) {
			if (musica[i].getState() == 400) {
				musicaApagada[i]=true;
				musica[i].off();
			} else {
				musicaApagada[i]=false;
			}
		}
	}

	void restaurarMusica() {
		int i;
		for (i=1 ; i<=pLenght ; i++) {
			if (musicaApagada[i]) {
				musica[i].on(false);
			}
		}
	}

	void pausar() {
		long inicioPausa=System.currentTimeMillis();
		pausa=true;
		tecla=false;
		pararMusica();
		repaint();
		serviceRepaints();

		while (pausa) {
			teclas();
			if (tecla || !playing) {
				pausa=false;
				tecla=false;
				tiempoPausa+=System.currentTimeMillis()-inicioPausa;
				restaurarMusica();
				repaint();
				serviceRepaints();
			} else {
				try {
					Thread.sleep(sleepTimeStatic);
				} catch (InterruptedException e) {
					System.out.println("pausa(sleep) Exception: "+e.toString());
				}
			}
		}
	}

	void comun() {
		// Comprobamos que se haya llamado correctamente a paint
		comprobarPintado();
		// Contador de ciclos
		cicle++;
		// Actualizar pulsación de teclas
		teclas();
		// Comprobar música
		computeMusic();
	}

	void computeTile() {
		if (cicle%2 == 0) {
			int i;
			for (i=1 ; i<=3 ; i++) {
				switch (tile[i].getFrame()) {
					case 1:
						tile[i].selFrame(2);
						tiles[i]=false;
						break;
					case 2:
						if (tiles[i]) {
							tile[i].selFrame(1);
						} else {
							tile[i].selFrame(3);
						}
						break;
					case 3:
						if (tiles[i]) {
							tile[i].selFrame(2);
						} else {
							tile[i].selFrame(4);
						}
						break;
					case 4:
						tile[i].selFrame(3);
						tiles[i]=true;
						break;
				}
			}
		}
	}

	void computeIconos() {
		int i;
		Random random = new java.util.Random();

		// Procesamos los iconos
		for (i=1 ; i<=3 ; i++) {
			iconos[i].proceso(Math.abs(random.nextInt()),Math.abs(random.nextInt()),Math.abs(random.nextInt()));
		}

		// Ahora los bonus de puntos
		if (puntosBonus25 > 0) {
			puntosBonus25--;
			if (puntosBonus25 == 0) {
				score+=25;
			}
		}
		if (puntosBonus50 > 0) {
			puntosBonus50--;
			if (puntosBonus50 == 0) {
				score+=50;
			}
		}
	}

	void regalo(int tipo) {
		// Super disparo
		if (tipo == 1) {
			// Activar la imagen del HUD (activando también el super disparo)
			hud.on();
			hud.selFrame(1);
			superDisparo=true;
			// Añadimos las balas
			superDisparos+=superDisparosTotales;
			// La música de cojer el arma
			musica[11].on(true);
		}
		// Bonus 25 puntos
		if (tipo == 2) {
			// Que se dibuje que tenemos 25 puntos más
			puntosBonus25=10;
			// La música de cojer el bonus
			musica[13].on(true);
		}
		if (tipo == 3) {
			// Que se dibuje que tenemos 50 puntos más
			puntosBonus50=10;
			// La música de cojer el bonus
			musica[14].on(true);
		}
	}

	void superDisparoOff() {
		hud.off();
		superDisparo=false;
		// Restaurar balas
		superDisparos=0;
	}

	void mute() {
		int i;
		if (!musica[1].muteActivo()) {
			for (i=1 ; i<=pLenght ; i++) {
				musica[i].mute(true);
			}
		} else {
			for (i=1 ; i<=pLenght ; i++) {
				musica[i].mute(false);
			}
		}
	}

	void cicloTitulo() {
		int tituloParpadea=5;

		while (tituloParpadea != 0) {
			tituloParpadea--;
			if (titulo.isActive()) {
				titulo.off();
			} else {
				titulo.on();
			}
			repaint();
			serviceRepaints();
			try {
				Thread.sleep(sleepTimeStatic);
			} catch (InterruptedException e) {
				System.out.println("cicloTitulo Exception: "+e.toString());
			}
		}
	}

	void apagarMusica() {
		int i;
		for (i=1 ; i<=pLenght ; i++) {
			musica[i].apagar();
		}
	}

	void comprobarPintado() {
		if (!comprobarPaint) {
			pausar();
		} else {
			comprobarPaint=false;
		}
	}

	void computeEnemies() {
		int freeEnemy,i;
		Random random = new java.util.Random();

		// Si no se ha llegado al límite de puntos para que aparezca el monstruo
		if (score < finishLevel) {

			// Creamos un enemigo cada 20 ciclos
			if (cicle%20 == 0) {
				freeEnemy=0;
				// Buscar un enemigo libre
				for (i=1 ; i<=6 ; i++) {
					if (!enemies[i].isActive()) {
						freeEnemy=i;
					}
				}
				// Asignar enemigo si hay una posición libre
				// en el array de enemigos
				if (freeEnemy != 0) {
					enemies[freeEnemy].on();
					enemies[freeEnemy].setX((Math.abs(random.nextInt()) % getWidth()) + 1);
					enemies[freeEnemy].setY(0);
					enemies[freeEnemy].setState(1);
					enemies[freeEnemy].setType((Math.abs(random.nextInt()) % 3) + 1);
					enemies[freeEnemy].init(hero.getX());
				}
			}
		} else {
			// Esperar a que desaparezcan todos los enemigos en pantalla

			if (enemies[1].isActive() || enemies[2].isActive() || enemies[3].isActive() || enemies[4].isActive() || enemies[5].isActive() || enemies[6].isActive()) {
			} else {
				if (cicloCount == 10) {
					musica[2].on(true);
				}
				cicloCount++;
				if (cicloCount == cicloEspera) {
					monster.on();
					monster.setX(getWidth()/2);
					monster.setY(-monster.getH());
					monster.init();
				}

				// Si ya se ha matado al monstruo
				if (tiempoFinOn) {
					tiempoFin--;
					// Cambiar al siguiente nivel
					if (tiempoFin == -tiempoNuevoNivel) {
						playing=false;
						ganado=true;
						ganadoForever=ganado;
					}
				}
			}
		}
		// Mover los enemigos
		for (i=1 ; i<=6 ; i++) {
			if (enemies[i].isActive()) {
				enemies[i].doMovement();
			}
			// Mirar si la nave salió de la pantalla
			if ((enemies[i].getY()-enemies[i].getH()/2 > getHeight()) || (enemies[i].getX()-enemies[i].getW()/2 > getWidth()) || (enemies[i].getX()+enemies[i].getW()/2 < 0)) {
				enemies[i].off();
			}
		}
	}

	void computeBullets() {
		int freeBullet,theEnemy,i,j;

		// Desactivamos el super disparo si ya no está en la pantalla
		if (superDisparoActivo != 0) {
			if (!aBullet[superDisparoActivo].isActive()) {
				superDisparoActivo=0;
			}
		}

		// Crear disparo del jugador
		freeBullet=0;
		if (fireOn && cicle%ciclosDisparo == 0) {
			// Desactivamos las balas
			fireOn=false;

			if (!superDisparo) {
				if (shield < 36 && heroBullets > 0 && cicle != ciclosDisparo) {
					// Buscar un disparo libre
					for (i=1 ; i<=29 ; i++) {
						if (!aBullet[i].isActive()) {
							freeBullet=i;
						}
					}
					if (freeBullet !=0) {
						aBullet[freeBullet].on();
						aBullet[freeBullet].setX(hero.getX());
						aBullet[freeBullet].setY(hero.getY()-10);
						aBullet[freeBullet].setOwner(1);
						heroBullets--;

						// Poner el disparo del héroe
						musica[7].on(true);
					}
				}
			} else {
				if (superDisparoActivo == 0) {
					// Buscar un disparo libre
					for (i=1 ; i<=29 ; i++) {
						if (!aBullet[i].isActive()) {
							freeBullet=i;
						}
					}
					if (freeBullet !=0) {
						aBullet[freeBullet].on();
						aBullet[freeBullet].setX(hero.getX());
						aBullet[freeBullet].setY(hero.getY()-10);
						aBullet[freeBullet].setOwner(5);

						superDisparoActivo=freeBullet;

						superDisparos--;
						if (superDisparos == 0) {
							superDisparoOff();
						}

						// Poner el disparo del héroe (super disparo)
						musica[12].on(true);
					}
				}
			}
		}

		// Crear disparo de enemigos
		freeBullet=0;
		theEnemy=0;

		for (i=1 ; i<=6 ; i++) {
			if (enemies[i].isActive()) {
				// Enemigos de tipo 1
				if (enemies[i].getType() == 1 && enemies[i].getY() > getHeight()/2 && enemies[i].getY() < (getHeight()/2)+5) {
					// Buscar un disparo libre
					for (j=1 ; j<=29 ; j++) {
						if (!aBullet[j].isActive()) {
							freeBullet=j;
							theEnemy=i;
						}
					}
					if (freeBullet !=0) {
						aBullet[freeBullet].on();
						aBullet[freeBullet].setX(enemies[theEnemy].getX());
						aBullet[freeBullet].setY(enemies[theEnemy].getY()+10);
						aBullet[freeBullet].setOwner(2);
						// Poner el disparo del enemigo
						musica[8].on(true);
					}
				}
				// Enemigos de tipo 3
				if (enemies[i].getType() == 7 && cicle%7 == 0) {
					// Buscar un disparo libre
					for (j=1 ; j<=29 ; j++) {
						if (!aBullet[j].isActive()) {
							freeBullet=j;
							theEnemy=i;
						}
					}
					if (freeBullet !=0) {
						aBullet[freeBullet].on();
						aBullet[freeBullet].setX(enemies[theEnemy].getX());
						aBullet[freeBullet].setY(enemies[theEnemy].getY()+10);
						aBullet[freeBullet].setOwner(4);
						// Poner el disparo del enemigo
						musica[10].on(true);
					}
				}
			}
		}

		// Crear disparo de monstruo
		freeBullet=0;
		if (monster.isActive() && monster.getDeltaX() != 0 && monster.getFrame() == 6) {

			for (i=1 ; i<=29 ; i++) {
				if (!aBullet[i].isActive()) {
					freeBullet=i;
				}
			}
			if (freeBullet !=0) {
				aBullet[freeBullet].on();
				aBullet[freeBullet].setX(monster.getX());
				aBullet[freeBullet].setY(monster.getY()+monster.getH()/2-20);
				aBullet[freeBullet].setOwner(3);

				// Poner el disparo del monstruo
				musica[9].on(true);
			}
		}

		// Mover los disparos
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].isActive()) {
				aBullet[i].doMovement();

				// Mirar si el disparo salió de la pantalla
				if ((aBullet[i].getY()-aBullet[i].getH()/2 > getHeight()) || (aBullet[i].getY()+aBullet[i].getH()/2 <= 0)) {
					if (aBullet[i].getOwner() == 1) {
						heroBullets++;
					}
					aBullet[i].off();
				}
			}
		}
	}

	void computeExplodes() {
		int i;

		for (i=1 ; i<=6 ; i++) {
			explode1[i].doMovement();
			explode2[i].doMovement();
			explode4[i].doMovement();
		}
		explode0.doMovement();
		explode3.doMovement();
	}

	void doScroll() {
		// movimiento del scenario (scroll)
		indice_in+=2;
		if (indice_in>=32) {
			indice_in=0;
			indice-=xTiles;
		}

		if (indice <= 0) {
			// si llegamos al final, empezamos de nuevo.
			indice=map.length-(xTiles*yTiles);
			indice_in=0;
		}
	}

	void createExplode(int posx, int posy, int tipo) {
		int freeExplode,i;
		freeExplode=0;

		// Si la explosión es de tipo 0 (héroe)
		if (tipo == 0) {

			explode0.setState(1);
			explode0.on();
			explode0.setX(posx);
			explode0.setY(posy);

			// Ponemos la explosión del héroe
			musica[3].on(true);
		}

		// Si la explosión es de tipo 1 (enemigo 1)
		if (tipo == 1) {

			// Buscar una explosión libre
			for (i=1 ; i<=6 ; i++) {
				if (!explode1[i].isActive()) {
					freeExplode=i;
				}
			}

			if (freeExplode !=0) {
				explode1[freeExplode].setState(1);
				explode1[freeExplode].on();
				explode1[freeExplode].setX(posx);
				explode1[freeExplode].setY(posy);

				// Ponemos la explosión del enemigo 1
				musica[4].on(true);
			}
		}

		// Si la explosión es de tipo 2 (enemigo 2)
		if (tipo == 2) {

			// Buscar una explosión libre
			for (i=1 ; i<=6 ; i++) {
				if (!explode2[i].isActive()) {
					freeExplode=i;
				}
			}

			if (freeExplode !=0) {
				explode2[freeExplode].setState(1);
				explode2[freeExplode].on();
				explode2[freeExplode].setX(posx);
				explode2[freeExplode].setY(posy);

				// Ponemos la explosión del enemigo 2
				musica[4].on(true);
			}
		}

		// Si la explosión es de tipo 4 (enemigo 3)
		if (tipo > 2 && tipo < 8) {

			// Buscar una explosión libre
			for (i=1 ; i<=6 ; i++) {
				if (!explode4[i].isActive()) {
					freeExplode=i;
				}
			}

			if (freeExplode !=0) {
				explode4[freeExplode].setState(1);
				explode4[freeExplode].on();
				explode4[freeExplode].setX(posx);
				explode4[freeExplode].setY(posy);

				// Ponemos la explosión del enemigo 4
				musica[5].on(true);
			}
		}

		// Si la explosión es de tipo 9 (monstruo)
		if (tipo == 9) {

			explode3.setState(1);
			explode3.on();
			explode3.setX(posx);
			explode3.setY(posy);

			// Ponemos la explosión del monstruo
			musica[6].on(true);
		}
	}

	void checkCollide() {
		int i,j;
		boolean collision,monsterColision;
		collision=false;
		monsterColision=false;

		// Colisión heroe-enemigo
		for (i=1 ; i<=6 ; i++) {
			if (hero.collide(enemies[i]) && enemies[i].isActive() && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				createExplode(enemies[i].getX(),enemies[i].getY(),enemies[i].getType());
				enemies[i].off();
				collision=true;
			}
		}

		// Colisión heroe-disparo
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].isActive() && hero.collide(aBullet[i]) && aBullet[i].getOwner() != 1 && aBullet[i].getOwner() != 5 && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				aBullet[i].off();
				collision=true;
			}
		}

		// colisión enemigo-disparo
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].getOwner() == 1 || aBullet[i].getOwner() == 5) {
				if (aBullet[i].isActive()) {
					for (j=1 ; j<=6 ; j++) {
						if (enemies[j].isActive()) {
							if (aBullet[i].collide(enemies[j])) {
								createExplode(enemies[j].getX(),enemies[j].getY(),enemies[j].getType());
								enemies[j].off();
								if (aBullet[i].getOwner() == 1) {
									aBullet[i].off();
									heroBullets++;
									score+=10;
								} else {
									score+=20;
								}
							}
						}
					}
				}
			}
		}

		// Colisión héroe-icono
		for (i=1 ; i<=3 ; i++) {
			if (iconos[i].isActive()) {
				if (hero.collide(iconos[i])) {
					regalo(iconos[i].getFrame());
					iconos[i].off();
				}
			}
		}

		// colisión con monstruo, si estuviera
		if (monster.isActive()) {

			// Colisión monstruo-disparo
			for (i=1 ; i<=29 ; i++) {
				if (aBullet[i].getOwner() == 1 || aBullet[i].getOwner() == 5) {
					if (aBullet[i].isActive() && aBullet[i].collide(monster) && monster.getDeltaX() != 0) {
						aBullet[i].off();
						if (aBullet[i].getOwner() == 1) {
							heroBullets++;
							monsterLives--;
							score+=1;
						} else {
							monsterLives-=3;
							score+=6;
						}
						monsterColision=true;
					}
				}
			}

			// Colisión monstruo-héroe
			if (hero.collide(monster) && monster.isActive() && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				collision=true;
				monsterLives--;
				monsterColision=true;
			}
		}

		if (collision == true) {
			lives--;

			// Desactivar bonificaciones
			if (hud.isActive()) {
				// Desactivar súper disparo
				superDisparoOff();
			}

			// poner heroe al estado inicial
			hero.setX(getWidth()/2);
			hero.setY(getHeight()-20);

			// Durante 60 ciclos nuestra nave será inmune
			shield=60;

		}

		if (monsterColision == true) {
			if (monsterLives <= 0) {
				createExplode(monster.getX(),monster.getY(),9);
				monster.off();
				tiempoFinOn=true;
			}
		}

		if (shield > 0) {
			shield--;
			// Parar de jugar si estamos sin vidas
			if (shield < 40 && lives <= 0) {
				playing=false;
				muerto=true;
			}
		}
	}

	void computePlayer() {
		// actualizar posición del avión
		if (hero.getX()+deltaX>0 && hero.getX()+deltaX<getWidth() &&
			hero.getY()+deltaY>0 && hero.getY()+deltaY<getHeight() && shield < 38) {
			hero.setX(hero.getX()+deltaX);
			hero.setY(hero.getY()+deltaY);
		}

		// Animación de entrada (si está activa)
		if (startAnimacion.isActive()) {
			startAnimacion.selFrame(startAnimacion.getFrame()+1);
			if (startAnimacion.getFrame() > startAnimacion.frames()) {
				startAnimacion.off();
				// Inicio
				tiempoInicio=System.currentTimeMillis();
			}
		}

		// Y el monstruo, si está activo
		if (monster.isActive()) {

			if (monster.getY() > getHeight()/4 && monster.getDeltaX() == 0) {
				monster.setDeltaX(3);
				monstruoAnimacion=true;
			}
			if (monster.getX() > getWidth()-monster.getW()/2) {
				monster.setDeltaX(-3);
			}
			if (monster.getX() < monster.getW()/2) {
				monster.setDeltaX(3);
			}
			if (monster.getY() < 20) {
				monster.setDeltaY(1);
			}
			if (monster.getY() > getHeight()/2) {
				monster.setDeltaY(-1);
			}

			// La animación del monstruo
			if (monstruoAnimacion) {
				switch (monster.getFrame()) {
					case 1:
						monster.selFrame(2);
						monstruoAnimacionEstado=true;
						break;
					case 2:
						if (monstruoAnimacionEstado) {
							monster.selFrame(3);
						} else {
							monster.selFrame(1);
						}
						break;
					case 3:
						if (monstruoAnimacionEstado) {
							monster.selFrame(4);
						} else {
							monster.selFrame(2);
						}
						break;
					case 4:
						if (monstruoAnimacionEstado) {
							monster.selFrame(5);
						} else {
							monster.selFrame(3);
						}
						break;
					case 5:
						if (monstruoAnimacionEstado) {
							monster.selFrame(6);
						} else {
							monster.selFrame(4);
						}
						break;
					case 6:
						monster.selFrame(5);
						monstruoAnimacionEstado=false;
						break;
				}
			}

			// movemos la nave
			monster.setX(monster.getX()+monster.getDeltaX());
			monster.setY(monster.getY()+monster.getDeltaY());
		}
	}

	void quitGame() {
		playing = false;
	}

	boolean isPlaying() {
		if (playing || muerto) {
			return true;
		} else {
			return false;
		}
	}

	boolean nivelGanado() {
		if (ganado) {
			ganado = false;
			return true;
		} else {
			return false;
		}
	}

	int vidas() {
		return lives;
	}

	int puntos() {
		return score+scoreLevel1;
	}

	long tiempoJugado() {
		return tiempoActual;
	}

	int balasSuperDisparo() {
		return superDisparos;
	}

	void setVolumen(String tipo,int volumen) {
		int i;

		if (tipo == "musica") {
			volumenMusica=volumen;
		}
		if (tipo == "efectos") {
			volumenEfectos=volumen;
		}
		if (isPlaying()) {
			if (tipo == "musica") {
				musica[1].setVolumen(volumen);
			}
			if (tipo == "efectos") {
				for (i=2; i<=pLenght; i++) {
					musica[i].setVolumen(volumen);
				}
			}
		}
	}

	void sonido(boolean activar) {
		if (activar) {
			cargarSonido=true;
		} else {
			cargarSonido=false;
		}
	}

	public void run(int vidasNivel1,int puntosNivel1,long tiempoNivel1) {
		playing = true;

		cargandoCarga();

		cargaIntroduccion();

		// Introducción
		while (playing) {
			// Tareas comunes
			comun();
			if (tecla) {
				tecla=false;
				if (introduccion.frames() <= introduccion.getFrame()) {
					introduccion.off();
					break;
				}
				introduccion.selFrame(introduccion.getFrame()+1);
			}
			repaint();
			serviceRepaints();
			try {
				Thread.sleep(sleepTimeStatic);
			} catch (InterruptedException e) {
				System.out.println("introduccion(sleep) Exception: "+e.toString());
			}
		}

		if (playing) {
			start(vidasNivel1,puntosNivel1,tiempoNivel1);

			while (playing) {
				// Tareas comunes
				comun();
				// Actualizar fondo de pantalla
				doScroll();
				// Actualizar posición del jugador
				computePlayer();
				// Actualizar posición de los enemigos
				computeEnemies();
				// Actualizar balas
				computeBullets();
				// Actualizar explosiones
				computeExplodes();
				// Comprobar colisiones
				checkCollide();
				// Procesar fondo
				computeTile();
				// Procesar los iconos
				computeIconos();
				// Actualizar tiempo
				tiempoActual=System.currentTimeMillis()-tiempoInicio+tiempoBase-tiempoPausa;
				// Actualizar pantalla
				if (!ganadoForever) {
					repaint();
					serviceRepaints();
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					System.out.println("loop(sleep) Exception: "+e.toString());
				}
			}
		}

		// Cuando nos matan, que salga la pantalla de muerte
		if (muerto) {
			// Actualizar pantalla
			repaint();
			serviceRepaints();

			tecla=false;

			while (muerto) {
				// Tareas comunes
				comun();
				// Comprobar si se ha pulsado alguna tecla
				if (tecla) {
					muerto=false;
				}
				try {
					Thread.sleep(sleepTimeStatic);
				} catch (InterruptedException e) {
					System.out.println("muerto(sleep) Exception: "+e.toString());
				}
			}
		}

		// Paramos la música
		apagarMusica();

		if (!ganadoForever) {
			// Repintamos la pantalla
			// para mostrar pantalla de presentación
			repaint();
			serviceRepaints();

			// Y mostramos el título
			cicloTitulo();
		}
	}

	void teclas() {
		int estadoTeclas = getKeyStates();

		// Con esto intentamos que no aparezcan teclas pulsadas cuando empezemos a jugar
		if (cicle > 1 && !startAnimacion.isActive()) {
			// Tecla central (disparo)
			if ((estadoTeclas & FIRE_PRESSED) != 0) {
				teclaDisparo(true);
			} else {
				teclaDisparo(false);
			}

			// Tecla izquierda
			if ((estadoTeclas & LEFT_PRESSED) != 0) {
				teclaIzquierda(true);
			} else {
				teclaIzquierda(false);
			}

			// Tecla derecha
			if ((estadoTeclas & RIGHT_PRESSED) != 0) {
				teclaDerecha(true);
			} else {
				teclaDerecha(false);
			}

			// Tecla arriba
			if ((estadoTeclas & UP_PRESSED) != 0) {
				teclaArriba(true);
			} else {
				teclaArriba(false);
			}

			// Tecla abajo
			if ((estadoTeclas & DOWN_PRESSED) != 0) {
				teclaAbajo(true);
			} else {
				teclaAbajo(false);
			}

			// Tecla GAME C
			if ((estadoTeclas & GAME_C_PRESSED) != 0) {
				teclaGameC(true);
			} else {
				teclaGameC(false);
			}

			// Tecla GAME D (mute)
			if ((estadoTeclas & GAME_D_PRESSED) != 0) {
				teclaGameD(true);
			} else {
				teclaGameD(false);
			}

			// Ahora comprobar si estamos viajando en diagonal, que habría que reducir la velocidad
			if (deltaX != 0 && deltaY != 0) {
				deltaX = 4*(deltaX/Math.abs(deltaX));
				deltaY = 4*(deltaY/Math.abs(deltaY));
			}
		}
		// Ver si se ha pulsado alguna tecla
		if (estadoTeclas != 0) {
			tecla = true;
		}
	}

	void teclaDisparo(boolean pulsada) {
		if (pulsada) {
			fireOn=true;
		}
	}

	void teclaIzquierda(boolean pulsada) {
		if (pulsada) {
			deltaX=-5;
			teclaIzquierda=true;
		} else {
			if (teclaDerecha) {
				deltaX=5;
			} else {
				deltaX=0;
			}
			teclaIzquierda=false;
		}
	}

	void teclaDerecha(boolean pulsada) {
		if (pulsada) {
			deltaX=5;
			teclaDerecha=true;
		} else {
			if (teclaIzquierda) {
				deltaX=-5;
			} else {
				deltaX=0;
			}
			teclaDerecha=false;
		}
	}

	void teclaArriba(boolean pulsada) {
		if (pulsada) {
			deltaY=-5;
			teclaArriba=true;
		} else {
			if (teclaAbajo) {
				deltaY=5;
			} else {
				deltaY=0;
			}
			teclaArriba=false;
		}
	}

	void teclaAbajo(boolean pulsada) {
		if (pulsada) {
			deltaY=5;
			teclaAbajo=true;
		} else {
			if (teclaArriba) {
				deltaY=-5;
			} else {
				deltaY=0;
			}
			teclaAbajo=false;
		}
	}

	void teclaGameC(boolean pulsada) {
		if (pulsada) {
			if (hud.isActive() && !gameCpress) {
				if (superDisparo) {
					superDisparo=false;
				} else {
					superDisparo=true;
					musica[11].on(true);
				}
				gameCpress=true;
			}
		} else {
			gameCpress=false;
		}
	}

	void teclaGameD(boolean pulsada) {
		if (pulsada) {
			if (!gameDpress) {
				mute();
			}
			gameDpress=true;
		} else {
			gameDpress=false;
		}
	}

	public void paint(Graphics g) {
		comprobarPaint=true;

		int x=0,y=0,t=0;
		int i,j;
		int scorePrint;
		long minutos,segundos;
		String segundosPrint;

		if (introduccion.isActive() || cargandoActivo || muerto) {
			if (introduccion.isActive()) {
				g.setColor(0,0,0);
			} else {
			g.setColor(127,0,0);
		}
		} else {
			g.setColor(255,0,0);
		}
		g.fillRect(0,0,getWidth(),getHeight());


		if (playing == false) {
			if (muerto) {
				muertoImg.setX(getWidth()/2);
				muertoImg.setY(getHeight()/2);
				muertoImg.draw(g);
			} else {
				intro.setX(getWidth()/2);
				intro.setY(getHeight()/2);
				intro.draw(g);

				// Dibujar el título
				if (titulo.isActive()) {
					titulo.setX(getWidth()/2);
					titulo.setY(getHeight()/4);
					titulo.draw(g);
				}
			}
		} else {
			if (introduccion.isActive()) {
				introduccion.draw(g);
			} else {

				// Si está cargando
				if (cargandoActivo) {
					cargando.draw(g);
					inferno.draw(g);
					g.setColor(0,0,255);
					g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					g.drawString("Juego hecho por",getWidth()/2,getHeight()*3/4,Graphics.HCENTER|Graphics.BOTTOM);
					g.drawString("Álvaro G. & F. Calzado",getWidth()/2,getHeight()*3/4+20,Graphics.HCENTER|Graphics.BOTTOM);

				} else {

					// Dibujar fondo
					for (i=0 ; i<yTiles ; i++) {
						for (j=0 ; j<xTiles ; j++) {
							t=map[indice+(i*xTiles+j)];
							// calculo de la posición del tile
							x=j*32;
							y=(i-1)*32+indice_in;
							// dibujamos el tile
							tile[t].setX(x);
							tile[t].setY(y);
							tile[t].draw(g);
						}
					}

					// Dibujar iconos
					for (i=1 ; i<=3 ; i++) {
						if (iconos[i].isActive() && !startAnimacion.isActive()) {
							j=cicle+i;
							if (j%6 != 0 && j%6 != 3) {
								iconos[i].draw(g);
							}
						}
					}

					// Dibujar enemigos
					for (i=1 ; i<=6 ; i++) {
						if (enemies[i].isActive() && !startAnimacion.isActive()) {
							enemies[i].setX(enemies[i].getX());
							enemies[i].setY(enemies[i].getY());
							enemies[i].draw(g);
						}
					}

					// Dibujar monstruo1
					if (monster.isActive()) {
						monster.setX(monster.getX());
						monster.setY(monster.getY());
						monster.draw(g);
					}

					// Dibujar el jugador
					if (shield < 40 && !startAnimacion.isActive()) {
						if (shield == 0 || shield%2 == 0) {
							hero.setX(hero.getX());
							hero.setY(hero.getY());
							hero.draw(g);
						}
					}

					// Dibujar disparos
					for (i=1 ; i<=29 ; i++) {
						if (aBullet[i].isActive() && !startAnimacion.isActive()) {
							aBullet[i].setX(aBullet[i].getX());
							aBullet[i].setY(aBullet[i].getY());
							aBullet[i].draw(g);
						}
					}

					// Dibujar explosiones
					for (i=1 ; i<=6 ; i++) {
						if (explode1[i].isActive())
							explode1[i].draw(g);
						if (explode2[i].isActive())
							explode2[i].draw(g);
						if (explode4[i].isActive())
							explode4[i].draw(g);
					}
					if (explode0.isActive())
						explode0.draw(g);
					if (explode3.isActive())
						explode3.draw(g);

					// Dibujar animación de entrada
					if (startAnimacion.isActive()) {
						startAnimacion.setX(getWidth()/2);
						startAnimacion.setY(getHeight()-startAnimacion.getH()/2);
						startAnimacion.draw(g);
					}

					// Dibujar la pistola del HUD y las balas restantes
					if (hud.isActive()) {
						if (!superDisparo) {
							hud.selFrame(2);
						} else {
							hud.selFrame(1);
						}
						hud.setX(getWidth()/2-getWidth()/4);
						hud.setY(20-hud.getH()/2);
						hud.draw(g);

						// Dibujar las balas restantes
						g.setColor(0,0,255);
						g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
						g.drawString(" "+superDisparos,hud.getX()+hud.getW(),20,Graphics.HCENTER|Graphics.BOTTOM);
					}

					scorePrint = score+scoreLevel1;
					g.setColor(0,0,255);
					g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					if (!startAnimacion.isActive()) {
						g.drawString(" "+scorePrint,getWidth()-20,20,Graphics.HCENTER|Graphics.BOTTOM);
						g.drawString(" "+lives,20,20,Graphics.HCENTER|Graphics.BOTTOM);

						// Dibujar minutos y segundos
						minutos=tiempoActual/1000/60;
						segundos=tiempoActual/1000-60*minutos;
						if (segundos < 10) {
							segundosPrint="0"+segundos;
						} else {
							segundosPrint=""+segundos;
						}
						g.drawString(minutos+":"+segundosPrint,3*getWidth()/4,20,Graphics.HCENTER|Graphics.BOTTOM);
					}

					// Si está el monstruo, dibujar sus vidas
					if (monster.isActive() && monster.getDeltaX() != 0) {
						g.drawString(" "+monsterLives,getWidth()/2,20,Graphics.HCENTER|Graphics.BOTTOM);
					}

					// Si hemos cogido el bonus de los 25 puntos
					if (puntosBonus25 > 0) {
						g.drawString("+25 puntos",getWidth()/2,getHeight()/2-getHeight()/8,Graphics.HCENTER|Graphics.BOTTOM);
					}

					// Si hemos cogido el bonus de los 50 puntos
					if (puntosBonus50 > 0) {
						g.drawString("+50 puntos",getWidth()/2,getHeight()/2-getHeight()/4,Graphics.HCENTER|Graphics.BOTTOM);
					}

					// Si ya se ha matado al monstruo
					if (tiempoFinOn) {
						if (tiempoFin <= 0) {
							g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
							g.drawString("Nivel 2: "+scorePrint+" puntos",getWidth()/2,getHeight()/2,Graphics.HCENTER|Graphics.BOTTOM);
						}
					}
				}
			}
		}
		if (pausa) {
			pausaImg.setX(getWidth()/2);
			pausaImg.setY(getHeight()/2);
			pausaImg.draw(g);

			g.setColor(0,0,255);
			g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
			g.drawString("Pulsa una tecla",getWidth()/2,getHeight()/2+getHeight()/4,Graphics.HCENTER|Graphics.BOTTOM);
		}
	}
}