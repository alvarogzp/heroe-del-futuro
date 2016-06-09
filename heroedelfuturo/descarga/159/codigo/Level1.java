package code;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;

class Level1 extends GameCanvas implements Runnable {
	private int score,sleepTime,cicle,lives,shield,indice_in,indice,xTiles,yTiles,deltaX,deltaY,finishLevel,cicloCount,cicloEspera,heroBullets,monsterLives,tiempoFin,volumenMusica,volumenEfectos,sleepTimeStatic,pLenght,explosionMonstruo,tiempoNuevoNivel,nivel,ciclosDisparo,puntosBonus25,puntosBonus50;
	private boolean playing,fireOn,cargandoActivo,tiempoFinOn,muerto,tecla,pausa,ganado,teclaIzquierda,teclaDerecha,teclaArriba,teclaAbajo,gameDpress,cargarSonido,ganadoForever,comprobarPaint;
	private boolean[] musicaApagada;
	private long tiempoInicio,tiempoActual,tiempoPausa;
	private Sprite hero=new Sprite(1);
	private Enemy[] enemies=new Enemy[7];
	private Bullet[] aBullet=new Bullet[30];
	private Sprite intro=new Sprite(1);
	private Sprite cargando=new Sprite(25);
	private Sprite spacio=new Sprite(1);
	private Sprite muertoImg=new Sprite(1);
	private Sprite introduccion=new Sprite(4);
	private Sprite pausaImg=new Sprite(1);
	private Sprite titulo=new Sprite(1);
	private Sprite startAnimacion=new Sprite(25);
	private Monster monster;
	private Explode explode0,explode3;
	private Explode[] explode1=new Explode[7];
	private Explode[] explode2=new Explode[7];
	private Sprite[] tile=new Sprite[5];
	private Sonido[] musica = new Sonido[12];
	private Sonido portadaMusica;
	private Icono[] iconos = new Icono[4];

	// Mapa del juego
	int map[] ={1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,
				1,2,1,1,1,1,1,1,
				1,1,1,4,1,1,1,1,
				1,1,1,1,1,1,1,1,
				1,1,3,1,1,1,1,1,
				1,1,1,1,1,1,4,1,
				1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,
				1,1,1,1,2,1,1,1,
				1,4,1,1,1,1,1,1,
				1,1,1,3,1,1,1,2,
				1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,
				1,2,1,1,1,1,1,1,
				1,1,1,4,1,1,1,1,
				1,1,1,1,1,1,1,1,
				1,1,3,1,1,1,1,1,
				1,1,1,1,1,1,4,1,
				1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,
				1,1,1,1,2,1,1,1,
				1,4,1,1,1,1,1,1,
				1,1,1,3,1,1,1,2};

	public Level1() {
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

		// Ponemos la música de la portada
		portadaMusica = new Sonido("/music/portadamusic.wav",50,cargarSonido);
		portadaMusica.on(true);

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
	}

	void cargaIntroduccion() {
		cargandoActualiza(2);
		introduccion.setX(getWidth()/2);
		introduccion.setY(getHeight()/2);
		introduccion.addFrame(1,"/images/introduccion1.png");
		cargandoActualiza(3);
		introduccion.addFrame(2,"/images/introduccion2.png");
		cargandoActualiza(4);
		introduccion.addFrame(3,"/images/introduccion3.png");
		cargandoActualiza(5);
		introduccion.addFrame(4,"/images/introduccion4.png");
		cargandoActualiza(6);
		introduccion.selFrame(1);

		cicle = 0;
		pausa=false;
		tecla=false;
		gameDpress=false;
		puntosBonus25=0;
		puntosBonus50=0;

		cargaMusica();

		cargandoActualiza(18);
		cargandoActualiza(19);
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

	void start() {
		int i;
		cargandoActiva();
		cargandoActualiza(2);
		fireOn=false;
		tiempoFinOn=false;
		muerto=false;
		tecla=false;
		ganado=false;
		ganadoForever=ganado;
		hero.setX(getWidth()/2-5);
		hero.setY(getHeight()-45);
		deltaX=0;
		deltaY=0;
		cicle=0;
		indice=map.length-(xTiles*yTiles);
		indice_in=0;
		score=0;
		lives=7;
		shield=0;
		cicloCount=0;
		heroBullets=6;
		monsterLives=20;
		tiempoFin=50;
		tiempoPausa=0;

		// Inicializamos héroe
		hero.addFrame(1,"/images/hero.png");
		hero.on();

		// Inicializar enemigos
		for (i=1 ; i<=6 ; i++) {
			enemies[i]=new Enemy(2);
			enemies[i].addFrame(1,"/images/enemy1.png");
			enemies[i].addFrame(2,"/images/enemy2.png");
			enemies[i].off();
		}

		// Inicializar monstruo
		monster=new Monster(1);
		monster.addFrame(1,"/images/monster.png");
		monster.off();


		cargandoActualiza(3);

		// Inicializar balas
		for (i=1 ; i<=29 ; i++) {
			aBullet[i]=new Bullet(3);
			aBullet[i].addFrame(1,"/images/mybullet.png");
			aBullet[i].addFrame(2,"/images/enemybullet.png");
			aBullet[i].addFrame(3,"/images/monsterbullet.png");
			aBullet[i].off();
			if (i%4 == 0) {
				cargandoActualiza(i/4+3);
			 }
		}

		// Inicializamos los tiles
		for (i=1 ; i<=4 ; i++) {
			tile[i]=new Sprite(1);
			tile[i].on();
		}

		tile[1].addFrame(1,"/images/tile1.png");
		tile[2].addFrame(1,"/images/tile2.png");
		tile[3].addFrame(1,"/images/tile3.png");
		tile[4].addFrame(1,"/images/tile4.png");

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
			explode1[i]=new Explode(11);
			explode1[i].addFrame(1,"/images/explode1-1.png");
			explode1[i].addFrame(2,"/images/explode1-2.png");
			explode1[i].addFrame(3,"/images/explode1-3.png");
			explode1[i].addFrame(4,"/images/explode1-4.png");
			explode1[i].addFrame(5,"/images/explode1-5.png");
			explode1[i].addFrame(6,"/images/explode1-6.png");
			explode1[i].addFrame(7,"/images/explode1-7.png");
			explode1[i].addFrame(8,"/images/explode1-8.png");
			explode1[i].addFrame(9,"/images/explode1-9.png");
			explode1[i].addFrame(10,"/images/explode1-10.png");
			explode1[i].addFrame(11,"/images/explode1-11.png");

			cargandoActualiza(i+11);

		}

		// Explosiones 2
		for (i=1 ; i<=6 ; i++) {
			explode2[i]=new Explode(11);
			explode2[i].addFrame(1,"/images/explode2-1.png");
			explode2[i].addFrame(2,"/images/explode2-2.png");
			explode2[i].addFrame(3,"/images/explode2-3.png");
			explode2[i].addFrame(4,"/images/explode2-4.png");
			explode2[i].addFrame(5,"/images/explode2-5.png");
			explode2[i].addFrame(6,"/images/explode2-6.png");
			explode2[i].addFrame(7,"/images/explode2-7.png");
			explode2[i].addFrame(8,"/images/explode2-8.png");
			explode2[i].addFrame(9,"/images/explode2-9.png");
			explode2[i].addFrame(10,"/images/explode2-10.png");
			explode2[i].addFrame(11,"/images/explode2-11.png");

			cargandoActualiza(i+17);

		}

		// Explosiones 3
		explode3=new Explode(22);
		explode3.addFrame(1,"/images/explode3-1.png");
		explode3.addFrame(2,"/images/explode3-2.png");
		explode3.addFrame(3,"/images/explode3-3.png");
		explode3.addFrame(4,"/images/explode3-4.png");
		explode3.addFrame(5,"/images/explode3-5.png");
		explode3.addFrame(6,"/images/explode3-6.png");
		explode3.addFrame(7,"/images/explode3-7.png");
		explode3.addFrame(8,"/images/explode3-8.png");
		explode3.addFrame(9,"/images/explode3-9.png");
		explode3.addFrame(10,"/images/explode3-10.png");
		explode3.addFrame(11,"/images/explode3-11.png");
		explode3.addFrame(12,"/images/explode3-12.png");
		explode3.addFrame(13,"/images/explode3-13.png");
		explode3.addFrame(14,"/images/explode3-14.png");
		explode3.addFrame(15,"/images/explode3-15.png");
		explode3.addFrame(16,"/images/explode3-16.png");
		explode3.addFrame(17,"/images/explode3-17.png");
		explode3.addFrame(18,"/images/explode3-18.png");
		explode3.addFrame(19,"/images/explode3-19.png");
		explode3.addFrame(20,"/images/explode3-20.png");
		explode3.addFrame(21,"/images/explode3-21.png");
		explode3.addFrame(22,"/images/explode3-22.png");

		// Inicializar iconos
		for (i=1 ; i<=3 ; i++) {
			iconos[i]=new Icono(2,getHeight(),getWidth());
			iconos[i].addFrame(1,"/images/icono25.png");
			iconos[i].addFrame(2,"/images/icono50.png");
		}

		cargandoActualiza(24);

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

		cargandoActualiza(25);

		cargandoDescarga();
	}

	// Barra de cargando...
	void cargandoCarga() {
		cargandoActiva();
		cargando.setX(getWidth()/2);
		cargando.setY(getHeight()/2+20);
		cargando.addFrame(1,"/images/loading1.png");
		spacio.addFrame(1,"/images/level1.png");
		spacio.setX(getWidth()/2);
		spacio.setY(cargando.getY()-cargando.getH()/2-spacio.getH()/2-20);
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
		musica[1] = new Sonido("/music/backmusic.wav",volumenMusica,cargarSonido);
		cargandoActualiza(7);
		musica[2] = new Sonido("/music/monster.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(8);
		musica[3] = new Sonido("/music/explode0.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(9);
		musica[4] = new Sonido("/music/explode1.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(10);
		musica[5] = new Sonido("/music/explode2.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(11);
		musica[6] = new Sonido("/music/explode3.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(12);
		musica[7] = new Sonido("/music/herobullet.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(13);
		musica[8] = new Sonido("/music/enemybullet.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(14);
		musica[9] = new Sonido("/music/monsterbullet.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(15);
		musica[10] = new Sonido("/music/bonus25.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(16);
		musica[11] = new Sonido("/music/bonus50.wav",volumenEfectos,cargarSonido);
		cargandoActualiza(17);

		pLenght=11;
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
		if (isPlaying()) {
			long inicioPausa=System.currentTimeMillis();
			pausa=true;
			tecla=false;
			pararMusica();
			repaint();
			serviceRepaints();

			while (pausa) {
				teclas();
				if (tecla || !isPlaying()) {
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
		// Bonus 25 puntos
		if (tipo == 1) {
			// Que se dibuje que tenemos 25 puntos más
			puntosBonus25=10;
			// La música de cojer el bonus
			musica[10].on(true);
		}
		// Bonus 50 puntos
		if (tipo == 2) {
			// Que se dibuje que tenemos 50 puntos más
			puntosBonus50=10;
			// La música de cojer el bonus
			musica[11].on(true);
		}
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
		titulo.off();

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
					enemies[freeEnemy].setType((Math.abs(random.nextInt()) % 2) + 1);
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

		// Crear disparo del jugador
		freeBullet=0;
		if (fireOn && cicle%ciclosDisparo == 0) {
			//Desactivamos las balas
			fireOn=false;

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
		}

		// Crear disparo de enemigos
		freeBullet=0;
		theEnemy=0;

		for (i=1 ; i<=6 ; i++) {
			if (enemies[i].getType() == 1 && enemies[i].isActive() && enemies[i].getY() > getHeight()/2 && enemies[i].getY() < (getHeight()/2)+5) {

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
		}

		// Crear disparo de monstruo
		freeBullet=0;
		if (monster.isActive() && monster.getDeltaX() != 0 && cicle%8 == 0) {

			for (i=1 ; i<=29 ; i++) {
				if (!aBullet[i].isActive()) {
					freeBullet=i;
				}
			}
			if (freeBullet !=0) {
				aBullet[freeBullet].on();

				// Posición de la bala, una vez sale de cada lado
				if (cicle%16 == 0) {
					aBullet[freeBullet].setX(monster.getX()-monster.getW()/2+4);
				} else {
					aBullet[freeBullet].setX(monster.getX()+monster.getW()/2-4);
				}

				aBullet[freeBullet].setY(monster.getY()+monster.getH()/2-6);
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
		}
		explode0.doMovement();

		// Los tres primeros frames de la explosión del monstruo se repiten 5 veces
		if (explode3.getState() < 4) {
			if (explosionMonstruo < 6) {
				explosionMonstruo++;
			} else {
				explosionMonstruo=1;
				explode3.doMovement();
			}
		} else {
			explode3.doMovement();
		}
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
				musica[5].on(true);
			}
		}

		// Si la explosión es de tipo 3 (monstruo)
		if (tipo == 3) {

			explode3.setState(1);
			explode3.on();
			explode3.setX(posx);
			explode3.setY(posy);

			// Ponemos a cero (1) la repetición de los primeros frames
			explosionMonstruo=1;

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
			if (aBullet[i].isActive() && hero.collide(aBullet[i]) && aBullet[i].getOwner() != 1 && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				aBullet[i].off();
				collision=true;
			}
		}

		// colisión enemigo-disparo
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].getOwner() == 1 && aBullet[i].isActive()) {
				for (j=1 ; j<=6 ; j++) {
					if (enemies[j].isActive()) {
						if (aBullet[i].collide(enemies[j])) {
							createExplode(enemies[j].getX(),enemies[j].getY(),enemies[j].getType());
							enemies[j].off();
							aBullet[i].off();
							heroBullets++;
							score+=10;
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
				if (aBullet[i].getOwner() == 1 && aBullet[i].isActive() && aBullet[i].collide(monster) && monster.getDeltaX() != 0) {
					aBullet[i].off();
					heroBullets++;
					score+=1;
					monsterColision=true;
				}
			}

			// Colisión monstruo-héroe
			if (hero.collide(monster) && monster.isActive() && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				collision=true;
				monsterColision=true;
			}
		}

		if (collision == true) {
			lives--;

			// poner heroe al estado inicial
			hero.setX(getWidth()/2);
			hero.setY(getHeight()-20);

			// Durante 60 ciclos nuestra nave será inmune
			shield=60;

		}

		if (monsterColision == true) {
			monsterLives--;
			if (monsterLives == 0) {
				createExplode(monster.getX(),monster.getY(),3);
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
		if (hero.getX()+deltaX>0 && hero.getX()+deltaX<getWidth() && hero.getY()+deltaY>0 && hero.getY()+deltaY<getHeight() && shield < 38) {
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
		return score;
	}

	long tiempoJugado() {
		return tiempoActual;
	}

	void setVolumen(int tipo,int volumen) {
		int i;

		if (tipo == 0) {
			volumenMusica=volumen;
		}
		if (tipo == 1) {
			volumenEfectos=volumen;
		}
		if (isPlaying()) {
			if (tipo == 0) {
				musica[1].setVolumen(volumen);
			}
			if (tipo == 1) {
				for (i=2; i<=pLenght; i++) {
					musica[i].setVolumen(volumen);
				}
			}
		}
	}

	void sonido(boolean activar) {
		if (activar) {
			cargarSonido=true;
			portadaMusica.on(false);
		} else {
			cargarSonido=false;
			portadaMusica.off();
		}
	}

	int musicaLevel() {
		return volumenMusica;
	}

	int efectosLevel() {
		return volumenEfectos;
	}

	public void run() {
		playing = true;

		// Apagar la música de la portada
		portadaMusica.apagar();

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
			start();

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
				// Procesar los iconos
				computeIconos();
				// Actualizar tiempo
				tiempoActual=System.currentTimeMillis()-tiempoInicio-tiempoPausa;
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
				// Comprobar si se ha pulsada alguna tecla
				if (tecla) {
					muerto=false;
				}
				repaint();
				serviceRepaints();
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
			// y mostrar el título
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
		long minutos,segundos;
		String segundosPrint;

		g.setColor(0,0,0);
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
					spacio.draw(g);
					g.setColor(0,0,255);
					g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					g.drawString("Juego hecho por",getWidth()/2,getHeight()*3/4+20,Graphics.HCENTER|Graphics.BOTTOM);
					g.drawString("Álvaro G. & F. Calzado",getWidth()/2,getHeight()*3/4+20+20,Graphics.HCENTER|Graphics.BOTTOM);

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

					g.setColor(200,200,0);
					g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));

					if (!startAnimacion.isActive()) {
						g.drawString(" "+score,getWidth()-20,20,Graphics.HCENTER|Graphics.BOTTOM);
						g.drawString(" "+lives,20,20,Graphics.HCENTER|Graphics.BOTTOM);

						// Dibujar minutos y segundos
						minutos=tiempoActual/1000/60;
						segundos=tiempoActual/1000-60*minutos;
						if (segundos < 10) {
							segundosPrint="0"+segundos;
						} else {
							segundosPrint=""+segundos;
						}
						g.drawString(minutos+":"+segundosPrint,68*getWidth()/100,20,Graphics.HCENTER|Graphics.BOTTOM);
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
							g.drawString("Nivel 1: "+score+" puntos",getWidth()/2,getHeight()/2,Graphics.HCENTER|Graphics.BOTTOM);
						}
					}
				}
			}
		}
		if (pausa) {
			pausaImg.setX(getWidth()/2);
			pausaImg.setY(getHeight()/2);
			pausaImg.draw(g);

			g.setColor(200,200,0);
			g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
			g.drawString("Pulsa una tecla",getWidth()/2,getHeight()/2+getHeight()/4,Graphics.HCENTER|Graphics.BOTTOM);
		}
	}
}