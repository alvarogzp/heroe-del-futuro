package code;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;

class Level3 extends GameCanvas {
	private int score,sleepTime,cicle,lives,shield,indice_in,indice,xTiles,yTiles,deltaX,deltaY,finishLevel,cicloCount,cicloEspera,heroBullets,monsterLives,tiempoFin,sleepTimeStatic,scoreLevel2,ciclosDisparo,tiempoCreditos,heroDobleBullets,puntosBonus25,puntosBonus50,vidaBonus,superDisparos,superDisparoActivo,superDisparosTotales;
	private boolean playing,fireOn,cargandoActivo,tiempoFinOn,muerto,tecla,pausa,dobleDisparo,teclaIzquierda,teclaDerecha,teclaArriba,teclaAbajo,tiles,gameCpress,superDisparo,comprobarPaint;
	private long tiempoInicio,tiempoBase,tiempoActual,tiempoPausa;
	private Sprite hero=new Sprite(1);
	private Enemy3[] enemies=new Enemy3[7];
	private Bullet[] aBullet=new Bullet[30];
	private Sprite intro=new Sprite(1);
	private Sprite cargando=new Sprite(25);
	private Sprite ruina=new Sprite(1);
	private Sprite muertoImg=new Sprite(1);
	private Sprite introduccion=new Sprite(3);
	private Sprite pausaImg=new Sprite(1);
	private Sprite titulo=new Sprite(1);
	private Sprite[] hud=new Sprite[3];
	private Sprite startAnimacion=new Sprite(25);
	private Monster monster;
	private Explode explode0,explode5;
	private Explode[] explode1=new Explode[7];
	private Explode[] explode2=new Explode[7];
	private Explode[] explode3=new Explode[7];
	private Explode[] explode4=new Explode[7];
	private Sprite[] tile=new Sprite[7];
	private Icono[] iconos = new Icono[4];

	// Mapa del juego
	int map[] ={4,6,6,2,6,6,6,6,
				6,6,6,6,5,6,6,1,
				6,5,6,6,2,6,6,6,
				6,6,5,6,6,6,2,4,
				5,6,3,4,5,6,6,6,
				6,6,3,6,6,6,6,1,
				6,6,6,6,2,6,3,6,
				6,3,6,5,6,3,6,6,
				1,3,6,6,6,5,6,6,
				1,6,6,6,6,6,6,6,
				6,6,6,6,5,6,3,5,
				6,6,6,3,6,6,6,2,
				4,6,6,2,6,6,6,6,
				6,6,6,6,5,6,6,1,
				6,5,6,6,2,6,6,6,
				6,6,5,6,6,6,2,4,
				5,6,3,4,5,6,6,6,
				6,6,3,6,6,6,6,1,
				6,6,6,6,2,6,3,6,
				6,3,6,5,6,3,6,6,
				1,3,6,6,6,5,6,6,
				1,6,6,6,6,6,6,6,
				6,6,6,6,5,6,3,5,
				6,6,6,3,6,6,6,2};

	public Level3() {
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

		comprobarPaint = true;

		// Variables est�ticas
		sleepTime=50;
		sleepTimeStatic=sleepTime*20;
		ciclosDisparo=4;
		xTiles=8;
		yTiles=12;
		finishLevel=250;
		cicloEspera=40;
		tiempoCreditos=20;
		superDisparosTotales=7;
	}

	void cargaIntroduccion() {
		cargandoActualiza(2);
		introduccion.setX(getWidth()/2);
		introduccion.setY(getHeight()/2);
		introduccion.addFrame(1,"/images/3introduccion1.png");
		cargandoActualiza(3);
		introduccion.addFrame(2,"/images/3introduccion2.png");
		cargandoActualiza(4);
		introduccion.addFrame(3,"/images/3introduccion3.png");
		cargandoActualiza(5);
		introduccion.selFrame(1);

		cicle=0;
		pausa=false;
		tecla=false;
		puntosBonus25=0;
		puntosBonus50=0;
		vidaBonus=0;
		tiempoFin=50;

		cargandoActualiza(24);
		cargandoActualiza(25);

		cargandoDescarga();

		introduccion.on();
		repaint();
		serviceRepaints();
	}

	void start(int vidasNivel2,int puntosNivel2,long tiempoNivel2,int balasSuperDisparoNivel2) {
		int i;
		cargandoActiva();
		cargandoActualiza(2);
		fireOn=false;
		tiempoFinOn=false;
		muerto=false;
		tecla=false;
		dobleDisparo=false;
		gameCpress=false;
		superDisparo=false;
		hero.setX(getWidth()/2-5);
		hero.setY(getHeight()-45);
		deltaX=0;
		deltaY=0;
		cicle=0;
		indice=map.length-(xTiles*yTiles);
		indice_in=0;
		score=0;
		scoreLevel2=puntosNivel2;
		lives=vidasNivel2+5;
		shield=0;
		cicloCount=0;
		heroBullets=6;
		heroDobleBullets=heroBullets*2;
		monsterLives=40;
		superDisparos=0;
		superDisparoActivo=0;
		tiempoBase=tiempoNivel2;
		tiempoPausa=0;

		// Inicializamos h�roes
		hero.addFrame(1,"/images/hero.png");
		hero.on();

		// Actualizar hud's
		hud[1]=new Sprite(2);
		hud[1].addFrame(1,"/images/3hud1.png");
		hud[1].addFrame(2,"/images/3hud1t.png");
		hud[1].off();

		hud[2]=new Sprite(2);
		hud[2].addFrame(1,"/images/2hud1.png");
		hud[2].addFrame(2,"/images/2hud1t.png");
		hud[2].off();

		// Inicializar enemigos
		for (i=1 ; i<=6 ; i++) {
			if (i%3 == 1) {
				cargandoActualiza(i/3+3);
			 }
			enemies[i]=new Enemy3(9);
			enemies[i].addFrame(1,"/images/3enemy1(1).png");
			enemies[i].addFrame(2,"/images/3enemy2(1).png");
			enemies[i].addFrame(3,"/images/3enemy2(2).png");
			enemies[i].addFrame(4,"/images/3enemy1(2).png");
			enemies[i].addFrame(5,"/images/3enemy1(2)-1.png");
			enemies[i].addFrame(6,"/images/3enemy1(2)-2.png");
			enemies[i].addFrame(7,"/images/3enemy1(2)-3.png");
			enemies[i].addFrame(8,"/images/3enemy1(2)-4.png");
			enemies[i].addFrame(9,"/images/3enemy1(2)-5.png");
			enemies[i].off();
		}

		// Inicializar monstruo
		monster=new Monster(1);
		monster.addFrame(1,"/images/3monster.png");
		monster.off();


		cargandoActualiza(5);

		// Inicializar balas
		for (i=1 ; i<=29 ; i++) {
			aBullet[i]=new Bullet(10);
			aBullet[i].addFrame(1,"/images/mybullet.png");
			aBullet[i].addFrame(2,"/images/2enemybullet1.png");
			aBullet[i].addFrame(3,"/images/3monsterbullet1.png");
			aBullet[i].addFrame(4,"/images/3enemybullet1(2).png");
			aBullet[i].addFrame(5,"/images/2mysuperbullet.png");
			aBullet[i].addFrame(6,"/images/3mydoblebullet.png");
			aBullet[i].addFrame(7,"/images/3monsterbullet2.png");
			aBullet[i].addFrame(8,"/images/3monsterbullet2.png");
			aBullet[i].addFrame(9,"/images/3monsterbullet3.png");
			aBullet[i].addFrame(10,"/images/3monsterbullet4.png");
			aBullet[i].off();
			if (i%3 == 2) {
				cargandoActualiza(i/3+6);
			 }
		}

		// Inicializamos los tiles

		// tile 1
		tile[1]=new Sprite(1);
		tile[1].on();
		tile[1].addFrame(1,"/images/3tile1.png");

		// tile 2
		tile[2]=new Sprite(1);
		tile[2].on();
		tile[2].addFrame(1,"/images/3tile2.png");

		// tile 3
		tile[3]=new Sprite(1);
		tile[3].on();
		tile[3].addFrame(1,"/images/3tile3.png");

		// tile 4
		tile[4]=new Sprite(1);
		tile[4].on();
		tile[4].addFrame(1,"/images/3tile4.png");

		// tile 5
		tile[5]=new Sprite(4);
		tile[5].on();
		tile[5].addFrame(1,"/images/3tile5-1.png");
		tile[5].addFrame(2,"/images/3tile5-2.png");
		tile[5].addFrame(3,"/images/3tile5-3.png");
		tile[5].addFrame(4,"/images/3tile5-4.png");

		// tile 6
		tile[6]=new Sprite(1);
		tile[6].on();
		tile[6].addFrame(1,"/images/3tile6.png");

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

		cargandoActualiza(16);

		// Explosiones 1
		for (i=1 ; i<=6 ; i++) {
			explode1[i]=new Explode(14);
			explode1[i].addFrame(1,"/images/3explode1(1)-1.png");
			explode1[i].addFrame(2,"/images/3explode1(1)-2.png");
			explode1[i].addFrame(3,"/images/3explode1(1)-3.png");
			explode1[i].addFrame(4,"/images/3explode1(1)-4.png");
			explode1[i].addFrame(5,"/images/3explode1(1)-5.png");
			explode1[i].addFrame(6,"/images/3explode1(1)-6.png");
			explode1[i].addFrame(7,"/images/3explode1(1)-7.png");
			explode1[i].addFrame(8,"/images/3explode1(1)-8.png");
			explode1[i].addFrame(9,"/images/3explode1(1)-9.png");
			explode1[i].addFrame(10,"/images/3explode1(1)-10.png");
			explode1[i].addFrame(11,"/images/3explode1(1)-11.png");
			explode1[i].addFrame(12,"/images/3explode1(1)-12.png");
			explode1[i].addFrame(13,"/images/3explode1(1)-13.png");
			explode1[i].addFrame(14,"/images/3explode1(1)-14.png");
			if (i%3 == 0) {
				cargandoActualiza(i/3+16);
			 }
		}

		// Explosiones 2
		for (i=1 ; i<=6 ; i++) {
			explode2[i]=new Explode(16);
			explode2[i].addFrame(1,"/images/3explode2(1)-1.png");
			explode2[i].addFrame(2,"/images/3explode2(1)-2.png");
			explode2[i].addFrame(3,"/images/3explode2(1)-3.png");
			explode2[i].addFrame(4,"/images/3explode2(1)-4.png");
			explode2[i].addFrame(5,"/images/3explode2(1)-5.png");
			explode2[i].addFrame(6,"/images/3explode2(1)-6.png");
			explode2[i].addFrame(7,"/images/3explode2(1)-7.png");
			explode2[i].addFrame(8,"/images/3explode2(1)-8.png");
			explode2[i].addFrame(9,"/images/3explode2(1)-9.png");
			explode2[i].addFrame(10,"/images/3explode2(1)-10.png");
			explode2[i].addFrame(11,"/images/3explode2(1)-11.png");
			explode2[i].addFrame(12,"/images/3explode2(1)-12.png");
			explode2[i].addFrame(13,"/images/3explode2(1)-13.png");
			explode2[i].addFrame(14,"/images/3explode2(1)-14.png");
			explode2[i].addFrame(15,"/images/3explode2(1)-15.png");
			explode2[i].addFrame(16,"/images/3explode2(1)-16.png");
			if (i%3 == 0) {
				cargandoActualiza(i/3+18);
			 }
		}

		// Explosiones 3
		for (i=1 ; i<=6 ; i++) {
			explode3[i]=new Explode(12);
			explode3[i].addFrame(1,"/images/3explode2(2)-1.png");
			explode3[i].addFrame(2,"/images/3explode2(2)-2.png");
			explode3[i].addFrame(3,"/images/3explode2(2)-3.png");
			explode3[i].addFrame(4,"/images/3explode2(2)-4.png");
			explode3[i].addFrame(5,"/images/3explode2(2)-5.png");
			explode3[i].addFrame(6,"/images/3explode2(2)-6.png");
			explode3[i].addFrame(7,"/images/3explode2(2)-7.png");
			explode3[i].addFrame(8,"/images/3explode2(2)-8.png");
			explode3[i].addFrame(9,"/images/3explode2(2)-9.png");
			explode3[i].addFrame(10,"/images/3explode2(2)-10.png");
			explode3[i].addFrame(11,"/images/3explode2(2)-11.png");
			explode3[i].addFrame(12,"/images/3explode2(2)-12.png");
			if (i%3 == 0) {
				cargandoActualiza(i/3+20);
			 }
		}


		// Explosiones 4
		for (i=1 ; i<=6 ; i++) {
			explode4[i]=new Explode(12);
			explode4[i].addFrame(1,"/images/3explode1(2)-1.png");
			explode4[i].addFrame(2,"/images/3explode1(2)-2.png");
			explode4[i].addFrame(3,"/images/3explode1(2)-3.png");
			explode4[i].addFrame(4,"/images/3explode1(2)-4.png");
			explode4[i].addFrame(5,"/images/3explode1(2)-5.png");
			explode4[i].addFrame(6,"/images/3explode1(2)-6.png");
			explode4[i].addFrame(7,"/images/3explode1(2)-7.png");
			explode4[i].addFrame(8,"/images/3explode1(2)-8.png");
			explode4[i].addFrame(9,"/images/3explode1(2)-9.png");
			explode4[i].addFrame(10,"/images/3explode1(2)-10.png");
			explode4[i].addFrame(11,"/images/3explode1(2)-11.png");
			explode4[i].addFrame(12,"/images/3explode1(2)-12.png");
			if (i%3 == 0) {
				cargandoActualiza(i/3+22);
			 }
		}

		// Explosiones 5
		explode5=new Explode(41);
		explode5.addFrame(1,"/images/3explode3-1.png");
		explode5.addFrame(2,"/images/3explode3-2.png");
		explode5.addFrame(3,"/images/3explode3-3.png");
		explode5.addFrame(4,"/images/3explode3-4.png");
		explode5.addFrame(5,"/images/3explode3-5.png");
		explode5.addFrame(6,"/images/3explode3-6.png");
		explode5.addFrame(7,"/images/3explode3-7.png");
		explode5.addFrame(8,"/images/3explode3-8.png");
		explode5.addFrame(9,"/images/3explode3-9.png");
		explode5.addFrame(10,"/images/3explode3-10.png");
		explode5.addFrame(11,"/images/3explode3-11.png");
		explode5.addFrame(12,"/images/3explode3-12.png");
		explode5.addFrame(13,"/images/3explode3-13.png");
		explode5.addFrame(14,"/images/3explode3-14.png");
		explode5.addFrame(15,"/images/3explode3-15.png");
		explode5.addFrame(16,"/images/3explode3-16.png");
		explode5.addFrame(17,"/images/3explode3-17.png");
		explode5.addFrame(18,"/images/3explode3-18.png");
		explode5.addFrame(19,"/images/3explode3-19.png");
		explode5.addFrame(20,"/images/3explode3-20.png");
		explode5.addFrame(21,"/images/3explode3-21.png");
		explode5.addFrame(22,"/images/3explode3-22.png");
		explode5.addFrame(23,"/images/3explode3-23.png");
		explode5.addFrame(24,"/images/3explode3-24.png");
		explode5.addFrame(25,"/images/3explode3-25.png");
		explode5.addFrame(26,"/images/3explode3-26.png");
		explode5.addFrame(27,"/images/3explode3-27.png");
		explode5.addFrame(28,"/images/3explode3-28.png");
		explode5.addFrame(29,"/images/3explode3-29.png");
		explode5.addFrame(30,"/images/3explode3-30.png");
		explode5.addFrame(31,"/images/3explode3-31.png");
		explode5.addFrame(32,"/images/3explode3-32.png");
		explode5.addFrame(33,"/images/3explode3-33.png");
		explode5.addFrame(34,"/images/3explode3-34.png");
		explode5.addFrame(35,"/images/3explode3-35.png");
		explode5.addFrame(36,"/images/3explode3-36.png");
		explode5.addFrame(37,"/images/3explode3-37.png");
		explode5.addFrame(38,"/images/3explode3-38.png");
		explode5.addFrame(39,"/images/3explode3-39.png");
		explode5.addFrame(40,"/images/3explode3-40.png");
		explode5.addFrame(41,"/images/3explode3-41.png");

		cargandoActualiza(25);

		// Inicializar iconos
		for (i=1 ; i<=3 ; i++) {
			iconos[i]=new Icono(5,getHeight(),getWidth());
			iconos[i].addFrame(1,"/images/3icono1.png");
			iconos[i].addFrame(2,"/images/2icono1.png");
			iconos[i].addFrame(3,"/images/icono25.png");
			iconos[i].addFrame(4,"/images/icono50.png");
			iconos[i].addFrame(5,"/images/3live.png");
		}

		// Comprobar si se ten�a el bazooka en el nivel 2
		comprobarSuperDisparoNivel2(balasSuperDisparoNivel2);

		// Animaci�n del principio
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
		cargando.setY(getHeight()/2+20);
		cargando.addFrame(1,"/images/loading1.png");
		ruina.addFrame(1,"/images/3level3.png");
		ruina.setX(getWidth()/2);
		ruina.setY(cargando.getY()-cargando.getH()/2-ruina.getH()/2-20);
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

	void pausar() {
		if (isPlaying()) {
			long inicioPausa=System.currentTimeMillis();
			pausa=true;
			tecla=false;
			repaint();
			serviceRepaints();

			while (pausa) {
				teclas();
				if (tecla || !isPlaying()) {
					pausa=false;
					tecla=false;
					tiempoPausa+=System.currentTimeMillis()-inicioPausa;
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
	}

	void computeTile() {
		switch (tile[5].getFrame()) {
			case 1:
				tile[5].selFrame(2);
				tiles=false;
				break;
			case 2:
				if (tiles) {
					tile[5].selFrame(1);
				} else {
					tile[5].selFrame(3);
				}
				break;
			case 3:
				if (tiles) {
					tile[5].selFrame(2);
				} else {
					tile[5].selFrame(4);
				}
				break;
			case 4:
				tile[5].selFrame(3);
				tiles=true;
				break;
		}
	}

	void computeIconos() {
		int i;
		Random random = new java.util.Random();

		// Procesamos los iconos
		for (i=1 ; i<=3 ; i++) {
			iconos[i].proceso(Math.abs(random.nextInt()),Math.abs(random.nextInt()),Math.abs(random.nextInt()));
		}

		// Ahora los bonus de puntos y vida
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
		if (vidaBonus > 0) {
			vidaBonus--;
			if (vidaBonus == 0) {
				lives+=1;
			}
		}
	}

	void regalo(int tipo) {
		// Doble disparo
		if (tipo == 1) {
			// Activar la imagen del HUD
			hud[tipo].on();
			// Que si tienes el super disparo no se active el doble disparo
			if (!superDisparo) {
				dobleDisparo=true;
			}
		}
		// Super disparo
		if (tipo == 2) {
			// Activar la imagen del HUD (activando tambi�n el super disparo)
			hud[tipo].on();
			superDisparo=true;
			// Si est� la pistola, desactivarla
			if (dobleDisparo) {
				dobleDisparo=false;
			}
			// A�adimos las balas
			superDisparos+=superDisparosTotales;
		}
		// Bonus 25 puntos
		if (tipo == 3) {
			// Que se dibuje que tenemos 25 puntos m�s
			puntosBonus25=10;
		}
		if (tipo == 4) {
			// Que se dibuje que tenemos 50 puntos m�s
			puntosBonus50=10;
		}
		if (tipo == 5) {
			// Que se dibuje que tenemos 1 vida m�s
			vidaBonus=10;
		}
	}

	void regaloOff(int tipo) {
		// Doble disparo
		if (tipo == 1 && dobleDisparo) {
			dobleDisparo=false;
		}
		// Super disparo
		if (tipo == 2 && superDisparo) {
			superDisparo=false;
		}
	}

	void superDisparoOff() {
		hud[2].off();
		superDisparo=false;
		// Restaurar balas
		superDisparos=0;
	}

	void comprobarSuperDisparoNivel2(int balasSuperDisparoNivel2) {
		if (balasSuperDisparoNivel2 > 0) {
			// Activar la imagen del HUD (activando tambi�n el super disparo
			hud[2].on();
			superDisparo=true;
			// A�adimos las balas
			superDisparos=balasSuperDisparoNivel2;
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

		// Si no se ha llegado al l�mite de puntos para que aparezca el monstruo
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
				// Asignar enemigo si hay una posici�n libre
				// en el array de enemigos
				if (freeEnemy != 0) {
					enemies[freeEnemy].on();
					enemies[freeEnemy].setX((Math.abs(random.nextInt()) % getWidth()) + 1);
					enemies[freeEnemy].setY(0);
					enemies[freeEnemy].setState(1);
					enemies[freeEnemy].setType((Math.abs(random.nextInt()) % 4) + 1);
					enemies[freeEnemy].init(hero.getX());
				}
			}
		} else {
			// Esperar a que desaparezcan todos los enemigos en pantalla

			if (enemies[1].isActive() || enemies[2].isActive() || enemies[3].isActive() || enemies[4].isActive() || enemies[5].isActive() || enemies[6].isActive()) {
			} else {
				cicloCount++;
				if (cicloCount == cicloEspera) {
					monster.on();
					monster.setX(getWidth()/2);
					monster.setY(-monster.getH());
					monster.init();
				}

				// Si ya se ha matado al monstruo
				if (tiempoFinOn)
					tiempoFin--;
			}
		}
		// Mover los enemigos
		for (i=1 ; i<=6 ; i++) {
			if (enemies[i].isActive()) {
				enemies[i].doMovement();
			}
			// Mirar si la nave sali� de la pantalla
			if ((enemies[i].getY()-enemies[i].getH()/2 > getHeight()) || (enemies[i].getX()-enemies[i].getW()/2 > getWidth()) || (enemies[i].getX()+enemies[i].getW()/2 < 0)) {
				enemies[i].off();
			}
		}
	}

	void computeBullets() {
		int freeBullet,theEnemy,i,j;

		// Desactivamos el super disparo si ya no est� en la pantalla
		if (superDisparoActivo != 0) {
			if (!aBullet[superDisparoActivo].isActive()) {
				superDisparoActivo=0;
			}
		}

		// Crear disparo del jugador
		freeBullet=0;
		if (fireOn && cicle%ciclosDisparo == 0) {
			//Desactivamos las balas
			fireOn=false;

			if (!superDisparo) {
				if (shield < 36 && cicle != ciclosDisparo) {
					if (!dobleDisparo) {
						if (heroBullets > 0) {
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
							}
						}
					} else {
						if (heroDobleBullets > 0) {
							// Primer disparo
							// Buscar un disparo libre
							for (i=1 ; i<=29 ; i++) {
								if (!aBullet[i].isActive()) {
									freeBullet=i;
								}
							}
							if (freeBullet !=0) {
								aBullet[freeBullet].on();
								aBullet[freeBullet].setX(hero.getX()-hero.getW()/2);
								aBullet[freeBullet].setY(hero.getY()-10);
								aBullet[freeBullet].setOwner(6);
								heroDobleBullets--;
							}

							// Segundo disparo
							// Buscar un disparo libre
							for (i=1 ; i<=29 ; i++) {
								if (!aBullet[i].isActive()) {
									freeBullet=i;
								}
							}
							if (freeBullet !=0) {
								aBullet[freeBullet].on();
								aBullet[freeBullet].setX(hero.getX()+hero.getW()/2);
								aBullet[freeBullet].setY(hero.getY()-10);
								aBullet[freeBullet].setOwner(6);
								heroDobleBullets--;
							}
						}
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
					}
				}
				// Enemigos de tipo 4
				if (enemies[i].getType() == 9 && cicle%4 == 0) {
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
					}
				}
			}
		}

		// Crear disparo de monstruo
		freeBullet=0;
		if (monster.isActive() && monster.getDeltaX() != 0 && cicle%32 == 0) {

			// Bala vertical
			for (i=1 ; i<=29 ; i++) {
				if (!aBullet[i].isActive()) {
					freeBullet=i;
				}
			}
			if (freeBullet !=0) {
				aBullet[freeBullet].on();
				aBullet[freeBullet].setX(monster.getX());
				aBullet[freeBullet].setY(monster.getY()+monster.getH()/2);
				aBullet[freeBullet].setOwner(3);
			}

			// Bala horizontal izquierda
			for (i=1 ; i<=29 ; i++) {
				if (!aBullet[i].isActive()) {
					freeBullet=i;
				}
			}
			if (freeBullet !=0) {
				aBullet[freeBullet].on();
				aBullet[freeBullet].setX(monster.getX()-monster.getW()/2);
				aBullet[freeBullet].setY(monster.getY());
				aBullet[freeBullet].setOwner(7);
			}

			// Bala horizontal derecha
			for (i=1 ; i<=29 ; i++) {
				if (!aBullet[i].isActive()) {
					freeBullet=i;
				}
			}
			if (freeBullet !=0) {
				aBullet[freeBullet].on();
				aBullet[freeBullet].setX(monster.getX()+monster.getW()/2);
				aBullet[freeBullet].setY(monster.getY());
				aBullet[freeBullet].setOwner(8);
			}

			// Bala diagonal izquierda
			for (i=1 ; i<=29 ; i++) {
				if (!aBullet[i].isActive()) {
					freeBullet=i;
				}
			}
			if (freeBullet !=0) {
				aBullet[freeBullet].on();
				aBullet[freeBullet].setX(monster.getX()-monster.getW()/2);
				aBullet[freeBullet].setY(monster.getY()+monster.getH()/2);
				aBullet[freeBullet].setOwner(9);
			}

			// Bala diagonal derecha
			for (i=1 ; i<=29 ; i++) {
				if (!aBullet[i].isActive()) {
					freeBullet=i;
				}
			}
			if (freeBullet !=0) {
				aBullet[freeBullet].on();
				aBullet[freeBullet].setX(monster.getX()+monster.getW()/2);
				aBullet[freeBullet].setY(monster.getY()+monster.getH()/2);
				aBullet[freeBullet].setOwner(10);
			}
		}

		// Mover los disparos
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].isActive()) {
				aBullet[i].doMovement();

				// Mirar si el disparo sali� de la pantalla
				if ((aBullet[i].getY()-aBullet[i].getH()/2 > getHeight()) || (aBullet[i].getY()+aBullet[i].getH()/2 < 0) || aBullet[i].getX()+aBullet[i].getW()/2 < 0 || aBullet[i].getX()-aBullet[i].getW()/2 > getWidth()) {
					if (aBullet[i].getOwner() == 1) {
						heroBullets++;
					}
					if (aBullet[i].getOwner() == 6) {
						heroDobleBullets++;
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
			explode3[i].doMovement();
			explode4[i].doMovement();
		}
		explode0.doMovement();
		explode5.doMovement();
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
			}
		}

		// Si la explosi�n es de tipo 3 (enemigo 3)
		if (tipo == 3) {

			// Buscar una explosi�n libre
			for (i=1 ; i<=6 ; i++) {
				if (!explode3[i].isActive()) {
					freeExplode=i;
				}
			}

			if (freeExplode !=0) {
				explode3[freeExplode].setState(1);
				explode3[freeExplode].on();
				explode3[freeExplode].setX(posx);
				explode3[freeExplode].setY(posy);
			}
		}

		// Si la explosi�n es de tipo 4 (enemigo 4)
		if (tipo > 3 && tipo < 10) {

			// Buscar una explosi�n libre
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
			}
		}

		// Si la explosi�n es de tipo 10 (monstruo)
		if (tipo == 10) {

			explode5.setState(1);
			explode5.on();
			explode5.setX(posx);
			explode5.setY(posy);
		}
	}

	void checkCollide() {
		int i,j;
		boolean collision,monsterColision;
		collision=false;
		monsterColision=false;

		// Colisi�n heroe-enemigo
		for (i=1 ; i<=6 ; i++) {
			if (hero.collide(enemies[i]) && enemies[i].isActive() && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				createExplode(enemies[i].getX(),enemies[i].getY(),enemies[i].getType());
				enemies[i].off();
				collision=true;
			}
		}

		// Colisi�n heroe-disparo
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].isActive() && hero.collide(aBullet[i]) && aBullet[i].getOwner() != 1 && aBullet[i].getOwner() != 5 && aBullet[i].getOwner() != 6 && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				aBullet[i].off();
				collision=true;
			}
		}

		// colisi�n enemigo-disparo
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].getOwner() == 1 || aBullet[i].getOwner() == 5 || aBullet[i].getOwner() == 6) {
				if (aBullet[i].isActive()) {
					for (j=1 ; j<=6 ; j++) {
						if (enemies[j].isActive()) {
							if (aBullet[i].collide(enemies[j])) {
								createExplode(enemies[j].getX(),enemies[j].getY(),enemies[j].getType());
								enemies[j].off();
								if (aBullet[i].getOwner() == 1) {
									aBullet[i].off();
									heroBullets++;
								}
								if (aBullet[i].getOwner() == 6) {
									aBullet[i].off();
									heroDobleBullets++;
								}
								if (aBullet[i].getOwner() == 5) {
									score+=10;
								}
								score+=10;
							}
						}
					}
				}
			}
		}

		// Colisi�n h�roe-icono
		for (i=1 ; i<=3 ; i++) {
			if (iconos[i].isActive()) {
				if (hero.collide(iconos[i])) {
					regalo(iconos[i].getFrame());
					iconos[i].off();
				}
			}
		}

		// colisi�n con monstruo, si estuviera
		if (monster.isActive()) {

			// Colisi�n monstruo-disparo
			for (i=1 ; i<=29 ; i++) {
				if (aBullet[i].getOwner() == 1 || aBullet[i].getOwner() == 5 || aBullet[i].getOwner() == 6) {
					if (aBullet[i].isActive() && aBullet[i].collide(monster) && monster.getDeltaX() != 0) {
						aBullet[i].off();
						if (aBullet[i].getOwner() == 1) {
							heroBullets++;
							monsterLives--;
							score+=1;
						}
						if (aBullet[i].getOwner() == 6) {
							heroDobleBullets++;
							monsterLives--;
							score+=1;
						}
						if (aBullet[i].getOwner() == 5) {
							monsterLives-=3;
							score+=6;
						}
						monsterColision=true;
					}
				}
			}

			// Colisi�n monstruo-h�roe
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
			if (hud[1].isActive()) {
				// Desactivar doble disparo
				hud[1].off();
				regaloOff(1);
			}
			if (hud[2].isActive()) {
				// Desactivar super disparo
				superDisparoOff();
			}

			// poner heroe al estado inicial
			hero.setX(getWidth()/2);
			hero.setY(getHeight()-20);

			// Durante 60 ciclos nuestra nave ser� inmune
			shield=60;

		}

		if (monsterColision == true) {
			if (monsterLives <= 0) {
				createExplode(monster.getX(),monster.getY(),10);
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
		// actualizar posici�n del avi�n
		if (hero.getX()+deltaX>0 && hero.getX()+deltaX<getWidth() &&
			hero.getY()+deltaY>0 && hero.getY()+deltaY<getHeight() && shield < 38) {
			hero.setX(hero.getX()+deltaX);
			hero.setY(hero.getY()+deltaY);
		}

		// Animaci�n de entrada (si est� activa)
		if (startAnimacion.isActive()) {
			startAnimacion.selFrame(startAnimacion.getFrame()+1);
			if (startAnimacion.getFrame() > startAnimacion.frames()) {
				startAnimacion.off();
				// Inicio
				tiempoInicio=System.currentTimeMillis();
			}
		}

		// Y el monstruo, si est� activo
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

	public void run(int vidasNivel2,int puntosNivel2,long tiempoNivel2,int balasSuperDisparoNivel2) {
		playing = true;

		cargandoCarga();

		cargaIntroduccion();

		// Introducci�n
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
			start(vidasNivel2,puntosNivel2,tiempoNivel2,balasSuperDisparoNivel2);

			while (playing) {
				// Tareas comunes
				comun();
				// Actualizar fondo de pantalla
				doScroll();
				// Actualizar posici�n del jugador
				computePlayer();
				// Actualizar posici�n de los enemigos
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
				repaint();
				serviceRepaints();
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

		// Repintamos la pantalla
		// para mostrar pantalla de presentación
		// y mostrar el título
		cicloTitulo();
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

			// Ahora comprobar si estamos viajando en diagonal, que habr�a que reducir la velocidad
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
			if (!gameCpress) {
				gameCpress=true;
				if (hud[1].isActive() && hud[2].isActive()) {
					if (superDisparo) {
						regaloOff(2);
						regalo(1);
					} else {
						if (dobleDisparo) {
							regaloOff(1);
						} else {
							superDisparo=true;
						}
					}
				} else {
					if (hud[1].isActive()) {
						if (dobleDisparo) {
						   regaloOff(1);
						} else {
							regalo(1);
						}
					}
					if (hud[2].isActive()) {
						if (superDisparo) {
							regaloOff(2);
						} else {
							superDisparo=true;
						}
					}
				}
			}
		} else {
			gameCpress=false;
		}
	}

	public void paint(Graphics g) {
		comprobarPaint=true;

		int x=0,y=0,t=0;
		int i,j;
		int scorePrint;
		long minutos,segundos;
		String segundosPrint;

		if (introduccion.isActive()) {
			g.setColor(0,0,0);
		} else {
			g.setColor(66,66,66);
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

				// Dibujar el t�tulo
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

				// Si est� cargando
				if (cargandoActivo) {
					cargando.draw(g);
					ruina.draw(g);
					g.setColor(0,0,255);
					g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
					g.drawString("Juego hecho por",getWidth()/2,getHeight()*3/4+20,Graphics.HCENTER|Graphics.BOTTOM);
					g.drawString("�lvaro G. & F. Calzado",getWidth()/2,getHeight()*3/4+20+20,Graphics.HCENTER|Graphics.BOTTOM);

				} else {

					// Dibujar fondo
					for (i=0 ; i<yTiles ; i++) {
						for (j=0 ; j<xTiles ; j++) {
							t=map[indice+(i*xTiles+j)];
							// calculo de la posici�n del tile
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

					// Dibujar monstruo
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
						if (explode3[i].isActive())
							explode3[i].draw(g);
						if (explode4[i].isActive())
							explode4[i].draw(g);
					}
					if (explode0.isActive())
						explode0.draw(g);
					if (explode5.isActive())
						explode5.draw(g);

					// Dibujar animaci�n de entrada
					if (startAnimacion.isActive()) {
						startAnimacion.setX(getWidth()/2);
						startAnimacion.setY(getHeight()-startAnimacion.getH()/2);
						startAnimacion.draw(g);
					}

					// Dibujar la pistola del HUD
					if (hud[1].isActive()) {
						if (!dobleDisparo) {
							hud[1].selFrame(2);
						} else {
							hud[1].selFrame(1);
						}
						hud[1].setX(getWidth()/2-getWidth()/4-getWidth()/16);
						hud[1].setY(20-hud[1].getH()/2);
						hud[1].draw(g);
					}
					// Dibujar el bazooka del HUD y las balas restantes
					if (hud[2].isActive() && !startAnimacion.isActive()) {
						if (!superDisparo) {
							hud[2].selFrame(2);
						} else {
							hud[2].selFrame(1);
						}
						hud[2].setX(getWidth()/2-getWidth()/6);
						hud[2].setY(20-hud[2].getH()/2);
						hud[2].draw(g);

						// Dibujar las balas restantes
						g.setColor(255,255,255);
						g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
						g.drawString(" "+superDisparos,hud[2].getX()+2*hud[2].getW()/3,20,Graphics.HCENTER|Graphics.BOTTOM);
					}

					scorePrint = score+scoreLevel2;
					g.setColor(255,255,255);
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
						g.drawString(minutos+":"+segundosPrint,68*getWidth()/100,20,Graphics.HCENTER|Graphics.BOTTOM);
					}

					// Si est� el monstruo, dibujar sus vidas
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

					// Si hemos cogido el bonus de la vida
					if (vidaBonus > 0) {
						g.drawString("+1 vida",getWidth()/2,getHeight()/2-getHeight()/8+20,Graphics.HCENTER|Graphics.BOTTOM);
					}

					// Si ya se ha matado al monstruo
					if (tiempoFinOn) {
						if (tiempoFin <= 0) {
							g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
							i = -tiempoFin-tiempoCreditos;
							if (i >= 0) {
								g.drawString("FIN: "+scorePrint+" puntos",getWidth()/2,getHeight()/2-i,Graphics.HCENTER|Graphics.BOTTOM);
								if (i >= 40) {
									g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_MEDIUM));
									g.drawString("CR�DITOS:",getWidth()/2,getHeight()/2+i-40,Graphics.HCENTER|Graphics.BOTTOM);
									if (i >= 80) {
										g.drawString("Gr�ficos: F. Calzado",getWidth()/2,getHeight()/2-i+80,Graphics.HCENTER|Graphics.BOTTOM);
										if (i >= 120) {
											g.drawString("Programaci�n: �lvaro G.",getWidth()/2,getHeight()/2+i-120,Graphics.HCENTER|Graphics.BOTTOM);
											if (i >= 180) {
												g.drawString("HeroeDelFuturo@gmail.com",getWidth()/2,getHeight()/2-i+180,Graphics.HCENTER|Graphics.BOTTOM);
												if (i >= 220) {
													g.drawString("HeroeDelFuturo.webs.com",getWidth()/2,getHeight()/2+i-220,Graphics.HCENTER|Graphics.BOTTOM);
												}
											}
										}
									}
								}
							} else {
								g.drawString("FIN: "+scorePrint+" puntos",getWidth()/2,getHeight()/2,Graphics.HCENTER|Graphics.BOTTOM);
							}
						}
					}
				}
			}
		}
		if (pausa) {
			pausaImg.setX(getWidth()/2);
			pausaImg.setY(getHeight()/2);
			pausaImg.draw(g);

			g.setColor(255,255,255);
			g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_BOLD,Font.SIZE_LARGE));
			g.drawString("Pulsa una tecla",getWidth()/2,getHeight()/2+getHeight()/4,Graphics.HCENTER|Graphics.BOTTOM);
		}
	}
}