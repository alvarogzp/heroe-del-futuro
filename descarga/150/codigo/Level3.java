import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.*;

class Level3 extends GameCanvas {
	private int score,sleepTime,cicle,lives,shield,indice_in,indice,xTiles,yTiles,deltaX,deltaY;
	private int finishLevel,cicloCount,cicloEspera,heroBullets,monsterLives,tiempoFin,volumenMusica,volumenEfectos,sleepTimeStatic,pLenght,scoreLevel2,ciclosDisparo,tiempoCreditos,heroDobleBullets,secret,puntosBonus25,puntosBonus50,vidaBonus,superDisparos,superDisparoActivo,superDisparosTotales;
	private boolean[] musicaApagada;
	private boolean playing,fireOn;
	private boolean cargandoActivo,tiempoFinOn,muerto,tecla,pausa,dobleDisparo,teclaIzquierda,teclaDerecha,teclaArriba,teclaAbajo,tiles,gameCpress,superDisparo,gameDpress;
	private long tiempoInicio,tiempoBase,tiempoActual;
	private Hero hero=new Hero(5);
	private Enemy3[] enemies=new Enemy3[7];
	private Bullet[] aBullet=new Bullet[30];
	private Sprite intro=new Sprite(1);
	private Sprite cargando=new Sprite(25);
	private Sprite ruina=new Sprite(1);
	private Sprite muertoImg=new Sprite(1);
	private Sprite introduccion=new Sprite(3);
	private Sprite pausaImg=new Sprite(1);
	private Sprite[] hud=new Sprite[3];
	private Monster monster;
	private Explode explode0;
	private Explode[] explode1=new Explode[7];
	private Explode[] explode2=new Explode[7];
	private Explode[] explode3=new Explode[7];
	private Explode[] explode4=new Explode[7];
	private Explode explode5;
	private Sprite[] tile=new Sprite[7];
	private Sonido[] musica = new Sonido[19];
	private Icono[] iconos = new Icono[4];

	// Mapa del juego
	int map[] ={4,1,6,2,6,6,1,6,
				6,6,6,6,5,6,6,1,
				5,5,6,6,2,6,6,6,
				6,6,5,6,6,6,2,4,
				5,6,1,4,5,3,6,6,
				2,6,3,6,6,6,6,1,
				6,1,4,6,2,6,3,6,
				1,3,6,5,6,3,6,6,
				1,3,6,4,2,5,1,6,
				1,4,2,6,6,6,3,6,
				4,6,6,6,5,6,3,5,
				6,6,6,3,6,3,6,2,
				4,1,6,2,6,6,1,6,
				6,6,6,6,5,6,6,1,
				5,5,6,6,2,6,6,6,
				6,6,5,6,6,6,2,4,
				5,6,1,4,5,3,6,6,
				2,6,3,6,6,6,6,1,
				6,1,4,6,2,6,3,6,
				1,3,6,5,6,3,6,6,
				1,3,6,4,2,5,1,6,
				1,4,2,6,6,6,3,6,
				4,6,6,6,5,6,3,5,
				6,6,6,3,6,3,6,2};

	public Level3() {
		// Lamada a la clase padre canvas
		super(true);
		// Cargamos los sprites
		intro.addFrame(1,"/intro.png");
		muertoImg.addFrame(1,"/muerto.png");
		pausaImg.addFrame(1,"/pause.png");
		
		// Iniciamos los Sprites
		intro.on();
		muertoImg.on();
		pausaImg.on();
		
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
		tiempoCreditos=20;
		superDisparosTotales=7;
	}

	void cargaIntroduccion() {
		// Actualizar la barra de cargar
		cargandoActualiza(3);
		introduccion.setX(getWidth()/2);
		introduccion.setY(getHeight()/2);
		// Actualizar la barra de cargar
		cargandoActualiza(4);
		introduccion.addFrame(1,"/3introduccion1.png");
		// Actualizar la barra de cargar
		cargandoActualiza(5);
		introduccion.addFrame(2,"/3introduccion2.png");
		// Actualizar la barra de cargar
		cargandoActualiza(6);
		introduccion.addFrame(3,"/3introduccion3.png");
		// Actualizar la barra de cargar
		cargandoActualiza(7);
		introduccion.selFrame(1);

		cicle=0;
		pausa=false;
		tecla=false;
		gameDpress=false;
		secret=0;
		puntosBonus25=0;
		puntosBonus50=0;
		vidaBonus=0;
		tiempoFin=50;
		
		// Actualizar la barra de cargar
		cargandoActualiza(8);
		
		cargaMusica();
		
		// Actualizar la barra de cargando
		cargandoActualiza(25);

		cargandoDescarga();
		
		introduccion.on();
		repaint();
		serviceRepaints();
	}
		
	void start(int vidasNivel2,int puntosNivel2,long tiempoNivel2,int balasSuperDisparoNivel2) {
		int i;
		cargandoActiva();
		// Actualizar la barra de cargar
		cargandoActualiza(2);
		fireOn=false;
		tiempoFinOn=false;
		muerto=false;
		tecla=false;
		dobleDisparo=false;
		gameCpress=false;
		superDisparo=false;
		hero.setX(getWidth()/2);
		hero.setY(getHeight()-20);
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
		
		// Inicializamos héroes
		hero.addFrame(1,"/hero.png");
		hero.addFrame(2,"/hero2.png");
		hero.addFrame(3,"/hero3-1.png");
		hero.addFrame(4,"/hero3-2.png");
		hero.addFrame(5,"/hero3-3.png");
		hero.on();
		
		// Actualizar hud's
		hud[1]=new Sprite(2);
		hud[1].addFrame(1,"/3hud1.png");
		hud[1].addFrame(2,"/3hud1t.png");
		hud[1].off();

		hud[2]=new Sprite(2);
		hud[2].addFrame(1,"/2hud1.png");
		hud[2].addFrame(2,"/2hud1t.png");
		hud[2].off();

		
		// Actualizar la barra de cargando
		cargandoActualiza(3);

		// Inicializar enemigos
		for (i=1 ; i<=6 ; i++) {
			enemies[i]=new Enemy3(9);
			enemies[i].addFrame(1,"/3enemy1(1).png");
			enemies[i].addFrame(2,"/3enemy2(1).png");
			enemies[i].addFrame(3,"/3enemy2(2).png");
			enemies[i].addFrame(4,"/3enemy1(2).png");
			enemies[i].addFrame(5,"/3enemy1(2)-1.png");
			enemies[i].addFrame(6,"/3enemy1(2)-2.png");
			enemies[i].addFrame(7,"/3enemy1(2)-3.png");
			enemies[i].addFrame(8,"/3enemy1(2)-4.png");
			enemies[i].addFrame(9,"/3enemy1(2)-5.png");
			enemies[i].off();
		}

		// Inicializar monstruo
		monster=new Monster(1);
		monster.addFrame(1,"/3monster.png");
		monster.off();


		// Actualizar la barra de cargando
		cargandoActualiza(4);

		// Inicializar balas
		for (i=1 ; i<=29 ; i++) {
			aBullet[i]=new Bullet(10);
			aBullet[i].addFrame(1,"/mybullet.png");
			aBullet[i].addFrame(2,"/2enemybullet1.png");
			aBullet[i].addFrame(3,"/3monsterbullet1.png");
			aBullet[i].addFrame(4,"/3enemybullet1(2).png");
			aBullet[i].addFrame(5,"/2mysuperbullet.png");
			aBullet[i].addFrame(6,"/3mydoblebullet.png");
			aBullet[i].addFrame(7,"/3monsterbullet2.png");
			aBullet[i].addFrame(8,"/3monsterbullet2.png");
			aBullet[i].addFrame(9,"/3monsterbullet3.png");
			aBullet[i].addFrame(10,"/3monsterbullet4.png");
			aBullet[i].off();

			// Actualizar la barra de cargando
			if (i%4 == 0) {
				cargandoActualiza(i/4+4);
			 }
		}

		// Inicializamos los tiles
		
		// tile 1
		tile[1]=new Sprite(1);
		tile[1].on();
		tile[1].addFrame(1,"/3tile1.png");
		
		// tile 2
		tile[2]=new Sprite(1);
		tile[2].on();
		tile[2].addFrame(1,"/3tile2.png");
		
		// tile 3
		tile[3]=new Sprite(1);
		tile[3].on();
		tile[3].addFrame(1,"/3tile3.png");
		
		// tile 4
		tile[4]=new Sprite(1);
		tile[4].on();
		tile[4].addFrame(1,"/3tile4.png");
		
		// tile 5
		tile[5]=new Sprite(4);
		tile[5].on();
		tile[5].addFrame(1,"/3tile5-1.png");
		tile[5].addFrame(2,"/3tile5-2.png");
		tile[5].addFrame(3,"/3tile5-3.png");
		tile[5].addFrame(4,"/3tile5-4.png");
		
		// tile 6
		tile[6]=new Sprite(1);
		tile[6].on();
		tile[6].addFrame(1,"/3tile6.png");
		
		
		// Actualizar la barra de cargando
		cargandoActualiza(11);

		// Inicializamos explosiones

		// Explosiones 0
		explode0=new Explode(14);
		explode0.addFrame(1,"/explode0-1.png");
		explode0.addFrame(2,"/explode0-2.png");
		explode0.addFrame(3,"/explode0-3.png");
		explode0.addFrame(4,"/explode0-4.png");
		explode0.addFrame(5,"/explode0-5.png");
		explode0.addFrame(6,"/explode0-6.png");
		explode0.addFrame(7,"/explode0-7.png");
		explode0.addFrame(8,"/explode0-8.png");
		explode0.addFrame(9,"/explode0-9.png");
		explode0.addFrame(10,"/explode0-10.png");
		explode0.addFrame(11,"/explode0-11.png");
		explode0.addFrame(12,"/explode0-12.png");
		explode0.addFrame(13,"/explode0-13.png");
		explode0.addFrame(14,"/explode0-14.png");

		// Actualizar la barra de cargando
		cargandoActualiza(12);

		// Explosiones 1
		for (i=1 ; i<=6 ; i++) {
			explode1[i]=new Explode(14);
			explode1[i].addFrame(1,"/3explode1(1)-1.png");
			explode1[i].addFrame(2,"/3explode1(1)-2.png");
			explode1[i].addFrame(3,"/3explode1(1)-3.png");
			explode1[i].addFrame(4,"/3explode1(1)-4.png");
			explode1[i].addFrame(5,"/3explode1(1)-5.png");
			explode1[i].addFrame(6,"/3explode1(1)-6.png");
			explode1[i].addFrame(7,"/3explode1(1)-7.png");
			explode1[i].addFrame(8,"/3explode1(1)-8.png");
			explode1[i].addFrame(9,"/3explode1(1)-9.png");
			explode1[i].addFrame(10,"/3explode1(1)-10.png");
			explode1[i].addFrame(11,"/3explode1(1)-11.png");
			explode1[i].addFrame(12,"/3explode1(1)-12.png");
			explode1[i].addFrame(13,"/3explode1(1)-13.png");
			explode1[i].addFrame(14,"/3explode1(1)-14.png");

			// Actualizar la barra de cargando
			cargandoActualiza(i+12);

		}

		// Explosiones 2
		for (i=1 ; i<=6 ; i++) {
			explode2[i]=new Explode(16);
			explode2[i].addFrame(1,"/3explode2(1)-1.png");
			explode2[i].addFrame(2,"/3explode2(1)-2.png");
			explode2[i].addFrame(3,"/3explode2(1)-3.png");
			explode2[i].addFrame(4,"/3explode2(1)-4.png");
			explode2[i].addFrame(5,"/3explode2(1)-5.png");
			explode2[i].addFrame(6,"/3explode2(1)-6.png");
			explode2[i].addFrame(7,"/3explode2(1)-7.png");
			explode2[i].addFrame(8,"/3explode2(1)-8.png");
			explode2[i].addFrame(9,"/3explode2(1)-9.png");
			explode2[i].addFrame(10,"/3explode2(1)-10.png");
			explode2[i].addFrame(11,"/3explode2(1)-11.png");
			explode2[i].addFrame(12,"/3explode2(1)-12.png");
			explode2[i].addFrame(13,"/3explode2(1)-13.png");
			explode2[i].addFrame(14,"/3explode2(1)-14.png");
			explode2[i].addFrame(15,"/3explode2(1)-15.png");
			explode2[i].addFrame(16,"/3explode2(1)-16.png");

			// Actualizar la barra de cargando
			cargandoActualiza(i+18);

		}
		
		// Actualizar la barra de cargando
		cargandoActualiza(25);
		
		// Explosiones 3
		for (i=1 ; i<=6 ; i++) {
			explode3[i]=new Explode(12);
			explode3[i].addFrame(1,"/3explode2(2)-1.png");
			explode3[i].addFrame(2,"/3explode2(2)-2.png");
			explode3[i].addFrame(3,"/3explode2(2)-3.png");
			explode3[i].addFrame(4,"/3explode2(2)-4.png");
			explode3[i].addFrame(5,"/3explode2(2)-5.png");
			explode3[i].addFrame(6,"/3explode2(2)-6.png");
			explode3[i].addFrame(7,"/3explode2(2)-7.png");
			explode3[i].addFrame(8,"/3explode2(2)-8.png");
			explode3[i].addFrame(9,"/3explode2(2)-9.png");
			explode3[i].addFrame(10,"/3explode2(2)-10.png");
			explode3[i].addFrame(11,"/3explode2(2)-11.png");
			explode3[i].addFrame(12,"/3explode2(2)-12.png");
		}

		
		// Explosiones 4
		for (i=1 ; i<=6 ; i++) {
			explode4[i]=new Explode(12);
			explode4[i].addFrame(1,"/3explode1(2)-1.png");
			explode4[i].addFrame(2,"/3explode1(2)-2.png");
			explode4[i].addFrame(3,"/3explode1(2)-3.png");
			explode4[i].addFrame(4,"/3explode1(2)-4.png");
			explode4[i].addFrame(5,"/3explode1(2)-5.png");
			explode4[i].addFrame(6,"/3explode1(2)-6.png");
			explode4[i].addFrame(7,"/3explode1(2)-7.png");
			explode4[i].addFrame(8,"/3explode1(2)-8.png");
			explode4[i].addFrame(9,"/3explode1(2)-9.png");
			explode4[i].addFrame(10,"/3explode1(2)-10.png");
			explode4[i].addFrame(11,"/3explode1(2)-11.png");
			explode4[i].addFrame(12,"/3explode1(2)-12.png");
		}
		
		// Explosiones 5
		explode5=new Explode(41);
		explode5.addFrame(1,"/3explode3-1.png");
		explode5.addFrame(2,"/3explode3-2.png");
		explode5.addFrame(3,"/3explode3-3.png");
		explode5.addFrame(4,"/3explode3-4.png");
		explode5.addFrame(5,"/3explode3-5.png");
		explode5.addFrame(6,"/3explode3-6.png");
		explode5.addFrame(7,"/3explode3-7.png");
		explode5.addFrame(8,"/3explode3-8.png");
		explode5.addFrame(9,"/3explode3-9.png");
		explode5.addFrame(10,"/3explode3-10.png");
		explode5.addFrame(11,"/3explode3-11.png");
		explode5.addFrame(12,"/3explode3-12.png");
		explode5.addFrame(13,"/3explode3-13.png");
		explode5.addFrame(14,"/3explode3-14.png");
		explode5.addFrame(15,"/3explode3-15.png");
		explode5.addFrame(16,"/3explode3-16.png");
		explode5.addFrame(17,"/3explode3-17.png");
		explode5.addFrame(18,"/3explode3-18.png");
		explode5.addFrame(19,"/3explode3-19.png");
		explode5.addFrame(20,"/3explode3-20.png");
		explode5.addFrame(21,"/3explode3-21.png");
		explode5.addFrame(22,"/3explode3-22.png");
		explode5.addFrame(23,"/3explode3-23.png");
		explode5.addFrame(24,"/3explode3-24.png");
		explode5.addFrame(25,"/3explode3-25.png");
		explode5.addFrame(26,"/3explode3-26.png");
		explode5.addFrame(27,"/3explode3-27.png");
		explode5.addFrame(28,"/3explode3-28.png");
		explode5.addFrame(29,"/3explode3-29.png");
		explode5.addFrame(30,"/3explode3-30.png");
		explode5.addFrame(31,"/3explode3-31.png");
		explode5.addFrame(32,"/3explode3-32.png");
		explode5.addFrame(33,"/3explode3-33.png");
		explode5.addFrame(34,"/3explode3-34.png");
		explode5.addFrame(35,"/3explode3-35.png");
		explode5.addFrame(36,"/3explode3-36.png");
		explode5.addFrame(37,"/3explode3-37.png");
		explode5.addFrame(38,"/3explode3-38.png");
		explode5.addFrame(39,"/3explode3-39.png");
		explode5.addFrame(40,"/3explode3-40.png");
		explode5.addFrame(41,"/3explode3-41.png");
		
		// Inicializar iconos
		for (i=1 ; i<=3 ; i++) {
			iconos[i]=new Icono(5,getHeight(),getWidth());
			iconos[i].addFrame(1,"/3icono1.png");
			iconos[i].addFrame(2,"/2icono1.png");
			iconos[i].addFrame(3,"/icono25.png");
			iconos[i].addFrame(4,"/icono50.png");
			iconos[i].addFrame(5,"/3live.png");
		}
		
		// Comprobar si se tenía el bazooka en el nivel 2
		comprobarSuperDisparoNivel2(balasSuperDisparoNivel2);
		
		cargandoDescarga();
	}

	// Barra de cargando...
	void cargandoCarga() {
		cargandoActiva();
		cargando.setX(getWidth()/2);
		cargando.setY(getHeight()/2+40);
		cargando.addFrame(1,"/loading1.png");
		ruina.addFrame(1,"/3level3.png");
		ruina.setX(getWidth()/2);
		ruina.setY(cargando.getY()-cargando.getH()/2-ruina.getH()/2-10);
		cargandoActualiza(1);
		cargando.addFrame(2,"/loading2.png");
		cargando.addFrame(3,"/loading3.png");
		cargando.addFrame(4,"/loading4.png");
		cargando.addFrame(5,"/loading5.png");
		cargando.addFrame(6,"/loading6.png");
		cargando.addFrame(7,"/loading7.png");
		cargando.addFrame(8,"/loading8.png");
		cargando.addFrame(9,"/loading9.png");
		cargando.addFrame(10,"/loading10.png");
		cargando.addFrame(11,"/loading11.png");
		cargando.addFrame(12,"/loading12.png");
		cargando.addFrame(13,"/loading13.png");
		cargando.addFrame(14,"/loading14.png");
		cargando.addFrame(15,"/loading15.png");
		cargando.addFrame(16,"/loading16.png");
		cargando.addFrame(17,"/loading17.png");
		cargando.addFrame(18,"/loading18.png");
		cargando.addFrame(19,"/loading19.png");
		cargando.addFrame(20,"/loading20.png");
		cargando.addFrame(21,"/loading21.png");
		cargando.addFrame(22,"/loading22.png");
		cargando.addFrame(23,"/loading23.png");
		cargando.addFrame(24,"/loading24.png");
		cargando.addFrame(25,"/loading25.png");
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
		// Actualizar la barra de cargar
		cargandoActualiza(10);
		musica[1] = new Sonido("/3backmusic.wav",volumenMusica);
		// Actualizar la barra de cargar
		cargandoActualiza(11);
		musica[2] = new Sonido("/3monster.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(12);
		musica[3] = new Sonido("/explode0.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(13);
		musica[4] = new Sonido("/2explode12.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(14);
		musica[5] = new Sonido("/3explode2(2).wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(15);
		musica[6] = new Sonido("/3explodemonster.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(16);
		musica[7] = new Sonido("/herobullet.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(17);
		musica[8] = new Sonido("/2enemybullet1.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(18);
		musica[9] = new Sonido("/2monsterbullet.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(19);
		musica[10] = new Sonido("/2enemybullet3.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(20);
		musica[11] = new Sonido("/3explode1(2).wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(21);
		musica[12] = new Sonido("/pickweapon.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(22);
		musica[13] = new Sonido("/2superdisparo.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(23);
		musica[14] = new Sonido("/3dobledisparo.wav",volumenEfectos);
		// Actualizar la barra de cargar
		cargandoActualiza(24);
		musica[15] = new Sonido("/3finalmusic.wav",volumenEfectos);
		musica[16] = new Sonido("/3live.wav",volumenEfectos);
		musica[17] = new Sonido("/bonus25.wav",volumenEfectos);
		musica[18] = new Sonido("/bonus50.wav",volumenEfectos);
		
		pLenght=18;
		musicaApagada=new boolean[pLenght+1];
		
		musica[1].on(true);
	}
	
	void computeMusic() {
		musica[1].on(false);
		if (tiempoFin == 0) {
			musica[15].on(true);
		}
	}

	void apagarMusica() {
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
	
	void checkPause() {
		if (pausa) {
			tecla=false;
			repaint();
			serviceRepaints();
		}
		while (pausa) {
			teclas();
			if (tecla) {
				pausa=false;
				tecla=false;
				restaurarMusica();
				repaint();
				serviceRepaints();
			}
			try {
				Thread.sleep(sleepTimeStatic);
			} catch (InterruptedException e) {
				System.out.println("pausa(sleep) Exception: "+e.toString());
			}
		}
	}
	
	void comun() {
		// Contador de ciclos
		cicle++;
		// Actualizar pulsación de teclas
		teclas();
		// Comprobar música
		computeMusic();
		// Comprobar pausa
		checkPause();
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
			// La música de cojer el arma
			musica[12].on(true);
		}
		// Super disparo
		if (tipo == 2) {
			// Activar la imagen del HUD (activando también el super disparo)
			hud[tipo].on();
			superDisparo=true;
			// Si está la pistola, desactivarla
			if (dobleDisparo) {
				dobleDisparo=false;
			}
			// Añadimos las balas
			superDisparos+=superDisparosTotales;
			// La música de cojer el arma
			musica[12].on(true);
		}
		// Bonus 25 puntos
		if (tipo == 3) {
			// Que se dibuje que tenemos 25 puntos más
			puntosBonus25=10;
			// La música del bonus
			musica[17].on(true);
		}
		if (tipo == 4) {
			// Que se dibuje que tenemos 50 puntos más
			puntosBonus50=10;
			// La música del bonus
			musica[18].on(true);
		}
		if (tipo == 5) {
			// Que se dibuje que tenemos 1 vida más
			vidaBonus=10;
			// La música del bonus
			musica[16].on(true);
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
	
	void comprobarSuperDisparoNivel2(int balasSuperDisparoNivel2) {
		if (balasSuperDisparoNivel2 > 0) {
			// Activar la imagen del HUD (activando también el super disparo
			hud[2].on();
			superDisparo=true;
			// Añadimos las balas
			superDisparos=balasSuperDisparoNivel2;
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
					enemies[freeEnemy].setType((Math.abs(random.nextInt()) % 4) + 1);
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
				if (tiempoFinOn)
					tiempoFin--;
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
				
								// Poner el disparo del héroe
								musica[7].on(true);
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
				
								// Poner el disparo del héroe (doble disparo)
								musica[14].on(true);
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
		
						// Poner el disparo del héroe
						musica[13].on(true);
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
						// Poner el disparo del enemigo
						musica[10].on(true);
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
			
			// Poner el disparo del monstruo
			musica[9].on(true);
		}

		// Mover los disparos
		for (i=1 ; i<=29 ; i++) {
			if (aBullet[i].isActive()) {
				aBullet[i].doMovement();

				// Mirar si el disparo salió de la pantalla
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
		
		// Si la explosión es de tipo 3 (enemigo 3)
		if (tipo == 3) {

			// Buscar una explosión libre
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

				// Ponemos la explosión del enemigo 3
				musica[5].on(true);
			}
		}
		
		// Si la explosión es de tipo 4 (enemigo 4)
		if (tipo > 3 && tipo < 10) {

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
				musica[11].on(true);
			}
		}

		// Si la explosión es de tipo 10 (monstruo)
		if (tipo == 10) {

			explode5.setState(1);
			explode5.on();
			explode5.setX(posx);
			explode5.setY(posy);

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
			if (aBullet[i].isActive() && hero.collide(aBullet[i]) && aBullet[i].getOwner() != 1 && aBullet[i].getOwner() != 5 && aBullet[i].getOwner() != 6 && shield == 0) {
				createExplode(hero.getX(),hero.getY(),0);
				aBullet[i].off();
				collision=true;
			}
		}

		// colisión enemigo-disparo
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

			// Durante 60 ciclos nuestra nave será inmune
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
		// actualizar posición del avión
		if (hero.getX()+deltaX>0 && hero.getX()+deltaX<getWidth() && 
			hero.getY()+deltaY>0 && hero.getY()+deltaY<getHeight() && shield < 38) {
			hero.setX(hero.getX()+deltaX);
			hero.setY(hero.getY()+deltaY);
		}
		
		// Actualizar la animación del héroe si se ha seleccionado esa imagen
		hero.doAnimation();
		
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

	void pausar() {
		pausa=true;
		apagarMusica();
	}
	
	void nave(int naveFrame) {
		hero.selFrame(naveFrame);
	}
	
	public void run(int vidasNivel2,int puntosNivel2,long tiempoNivel2,int balasSuperDisparoNivel2) {
		playing = true;
		
		cargandoCarga();
		
		// Actualizar la barra de cargar
		cargandoActualiza(2);
		
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
				repaint();
				serviceRepaints();
			}
			try {
				Thread.sleep(sleepTimeStatic);
			} catch (InterruptedException e) {
				System.out.println("introduccion(sleep) Exception: "+e.toString());
			}
		}

		if (playing) {
			start(vidasNivel2,puntosNivel2,tiempoNivel2,balasSuperDisparoNivel2);
			
			// Inicio
			tiempoInicio=System.currentTimeMillis();
			
			while (playing) {
				// Actualizar tiempo
				tiempoActual=System.currentTimeMillis();
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
		repaint();
		serviceRepaints();

		// Y paramos la música
		apagarMusica();
	}

	void teclas() {
		int estadoTeclas = getKeyStates();
		
		// Con esto intentamos que no aparezcan teclas pulsadas cuando empezemos a jugar
		if (cicle > 1) {
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
			
			// Tecla GAME D y GAME B (¡¡TRUCO!!)
			if ((estadoTeclas & GAME_D_PRESSED) != 0 && (estadoTeclas & GAME_B_PRESSED) != 0 && cicle%16 == 0) {
				if (!hud[1].isActive()) {
					regalo(1); // Activa directamente el doble disparo
				}
				regalo(2); // Activa directamente el super disparo
			}
			
			// TRUCO: Ir directamente al siguiente nivel
			if ((estadoTeclas & GAME_A_PRESSED) != 0 && (estadoTeclas & GAME_B_PRESSED) != 0) {
				secret++;
				if (secret >= 25) {
					score++;
				}
			} else {
				secret=0;
			}
			
			// Ver si se ha pulsado alguna tecla
			if (estadoTeclas != 0) {
				tecla = true;
			}
			
			// Ahora comprobar si estamos viajando en diagonal, que habría que reducir la velocidad
			if (deltaX != 0 && deltaY != 0) {
				deltaX = 4*(deltaX/Math.abs(deltaX));
				deltaY = 4*(deltaY/Math.abs(deltaY));
			}
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
							musica[12].on(true);
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
							musica[12].on(true);
						}
					}
				}
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
			}
		} else {
			if (introduccion.isActive()) {
				introduccion.draw(g);
			} else {

				// Si está cargando
				if (cargandoActivo) {
					cargando.draw(g);
					ruina.draw(g);
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
						if (iconos[i].isActive()) {
							j=cicle+i;
							if (j%6 != 0 && j%6 != 3) {
								iconos[i].draw(g);
							}
						}
					}

					// Dibujar enemigos
					for (i=1 ; i<=6 ; i++) {
						if (enemies[i].isActive()) {
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
					if (shield < 40) {
						if (shield == 0 || shield%2 == 0) {
							hero.setX(hero.getX());
							hero.setY(hero.getY());
							hero.draw(g);
						}
					}

					// Dibujar disparos
					for (i=1 ; i<=29 ; i++) {
						if (aBullet[i].isActive()) {
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
					if (hud[2].isActive()) {
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
					g.drawString(" "+scorePrint,getWidth()-20,20,Graphics.HCENTER|Graphics.BOTTOM);
					g.drawString(" "+lives,20,20,Graphics.HCENTER|Graphics.BOTTOM);
					
					// Dibujar minutos y segundos
					minutos=(System.currentTimeMillis()-tiempoInicio+tiempoBase)/1000/60;
					segundos=((System.currentTimeMillis()-tiempoInicio+tiempoBase)/1000)-(60*((System.currentTimeMillis()-tiempoInicio+tiempoBase)/1000/60));
					if (segundos < 10) {
						segundosPrint="0"+segundos;
					} else {
						segundosPrint=""+segundos;
					}
					g.drawString(minutos+":"+segundosPrint,3*getWidth()/4,20,Graphics.HCENTER|Graphics.BOTTOM);

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
						g.drawString("+50 puntos",getWidth()/2,getHeight()/2-getHeight()/8+20,Graphics.HCENTER|Graphics.BOTTOM);
					}
					
					// Si hemos cogido el bonus de la vida
					if (vidaBonus > 0) {
						g.drawString("+1 vida",getWidth()/2,getHeight()/2-getHeight()/8-20,Graphics.HCENTER|Graphics.BOTTOM);
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
									g.drawString("CRÉDITOS:",getWidth()/2,getHeight()/2+i-40,Graphics.HCENTER|Graphics.BOTTOM);
									if (i >= 80) {
										g.drawString("Gráficos: F. Calzado",getWidth()/2,getHeight()/2-i+80,Graphics.HCENTER|Graphics.BOTTOM);
										if (i >= 120) {
											g.drawString("Programación: Álvaro G.",getWidth()/2,getHeight()/2+i-120,Graphics.HCENTER|Graphics.BOTTOM);
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
		}
	}
}