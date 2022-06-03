/*
Henry Hu and Sahan Weeratunga
February 11, 2019
ICS4U - 01
This game is a turn-based game where the players shoot arrows at each other.
Objects are created and drawn here, the game's own methods are here as well
*/

package com.mygdx.game;

import java.util.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.audio.Music;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	private static int PLAYER1 = 1;
	private static int PLAYER2 = 2;

	private BitmapFont font;//Pokemon Font
	private static Music pokemon;//Background music

	private SpriteBatch batch;
	private ShapeRenderer sr;
	private Random rand;

	private Intersector intersect;

	private Sprite p1sprite;//1st Player sprites
	private Sprite p2sprite;//2nd player sprites
	private Texture projectile;//Arrow sprite

	private Texture healPU;//Heal powerup sprite
	private Texture triplePU;//triple arrow sprite

	private Texture p1head;//head sprites
	private Texture p1body;//body sprites
	private Texture p1bow;//bow and hand sprite
	private Sprite p1headsprite;
	private Sprite p1bowsprite;

	private Texture p2head;//same as player 1
	private Texture p2body;
	private Texture p2bow;
	private Sprite p2headsprite;
	private Sprite p2bowsprite;

	private static Texture clouds;//background clouds
	private static Sprite cloudssprite;

	private ground terrain;//the ground object
	private Polygon groundPolygon;//the polygon the ground is enclosed with
	private ArrayList<Integer> gXCoords;//All x coordinated of ground
	private ArrayList<Integer> gYCoords;// All y coordinates of the ground

	private player p1,p2;//creates the players
	private int turn = PLAYER1;//sets who's turn it is

	private int x = 0;
	private int y = 0;
	private int vx = 3;//the arrow speed
	private int vy = 3;

	private Sprite background;//this is the filled polygon

	private float power = 0;//amount of power charged up for each shot
	private int up_or_down=0;//whether the power increases or decreases
	private static int UP = 1;//directions
	private static int DOWN = - 1;

	private static int WSIDE = 0;//when it collides with the wall
	private static int WGROUND = 1;//when collides with the player

	private ArrayList<arrow> arrows;//the arrows on the screen at the time
	private arrow bottom, top;

	private ArrayList<powerup> powerupstriple;//amount of powerups on screen
	private ArrayList<powerup> powerupsheal;//amount of heals on the screen
	private ArrayList<Integer> takenpowerups;//who many powerups are taken

	private boolean filled = false;//checks whether the polygon is filled yet

	private boolean menu = false;//decides whether your in menu or game

	@Override
	public void create () {
		Gdx.graphics.setWindowedMode(1024, 768);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		rand = new Random();
		intersect = new Intersector();

		clouds = new Texture("core/assets/Clouds.jpg");
		cloudssprite = new Sprite(clouds);
		cloudssprite.setSize(1024, 768);//sets size to cover entire background

		font = new BitmapFont(Gdx.files.internal("core/assets/pokemonGB.fnt"));//gets specialized font form assets
		font.getData().setScale(1f);//changes the size of the font

		pokemon = Gdx.audio.newMusic(Gdx.files.internal("core/assets/Pokémon Diamond  Pearl - Route 209 (Day).mp3"));//loads up the background music
		pokemon.setLooping(true);//makes it so if the music ends it loops again
		pokemon.setVolume(0.5f);//sets the volume so it's not too loud
		pokemon.play();//plays the music as soon as the program begins

		p1body = new Texture("core/assets/p1 body.png");
		p1bow = new Texture("core/assets/p1 bow.png");
		p1head = new Texture("core/assets/p1 head.png");

		p2body = new Texture("core/assets/p2 body.png");
		p2bow = new Texture("core/assets/p2 bow.png");
		p2head = new Texture("core/assets/p2 head.png");

		triplePU = new Texture("core/assets/three.png");
		healPU = new Texture("core/assets/health.png");
		projectile = new Texture("core/assets/arrow.png");

		p1sprite = new Sprite(p1body);
		p1sprite.setSize(50, 70);//sets size to a smaller size
		p1bowsprite = new Sprite(p1bow);
		p1bowsprite.setSize(40,40);
		p1bowsprite.setOrigin(9, 20);//sets origin because the hands need to follow the mouse as it moves around the screen
		p1headsprite = new Sprite(p1head);
		p1headsprite.setSize(60, 60);


		p2sprite = new Sprite(p2body);
		p2sprite.setSize(50, 70);
		p2bowsprite = new Sprite(p2bow);
		p2bowsprite.setSize(40,40);
		p2bowsprite.setOrigin(31, 20);
		p2headsprite = new Sprite(p2head);
		p2headsprite.setSize(60, 60);

		//p2sprite = new Sprite(img);

		arrows = new ArrayList<arrow>();
		bottom = null;//this is the arrow that comes out of triple power up that leaves at the lowest point
		top = null;//this is the arrow that comes out of triples power up that leaves at the highest point

		powerupstriple = new ArrayList<powerup>();
		powerupsheal = new ArrayList<powerup>();
		takenpowerups = new ArrayList<Integer>();

		terrain = new ground();//creates the ground
		groundPolygon = new Polygon(terrain.polygonVertices());//gets the surrounding vertices of the ground

		gXCoords = terrain.xCoords();
		gYCoords = terrain.yCoords();

		p1 = new player(0,gYCoords.get(1)-8);//Creates the player at a certian point
		p2 = new player(974,gYCoords.get(gYCoords.size()-2)-8);

		x = 0;
		y = 0;

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if ( menu == false) {
			menu();//checks if the program should be in the game or in the menu screen
		}else {
			int mx = Gdx.input.getX();
			int my = Gdx.input.getY();

			batch.begin();//has to be separated from the background or else they will overlap and cover each other up
			cloudssprite.draw(batch);
			batch.end();

			batch.begin();
			if (filled ==  false ) {
				background = groundfill();
				filled = true;//sets this so it doesn't constantly keep finding the same points in side the polygon
			}
			background.draw(batch);
			batch.end();

			sr.begin(ShapeType.Filled);
			sr.setColor(Color.BLACK);
			//POWER LEVELS || PLAYER INFORMATION

			sr.rect(5f,637f,0f,0f,418f,126f,1f,1f,0f,Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY);//behind the goldenrod rectangle
			sr.rect(601f,637f,0f,0f,418f,126f,1f,1f,0f,Color.GRAY,Color.GRAY,Color.GRAY,Color.GRAY);
			if (turn == PLAYER1) {
				sr.rect(5f,637f,0f,0f,418f,126f,1f,1f,0f,Color.RED,Color.RED,Color.RED,Color.RED);//draw a red rectangle around the golden rod rectangle to show who's turn it is

			}else {
				sr.rect(601f,637f,0f,0f,418f,126f,1f,1f,0f,Color.RED,Color.RED,Color.RED,Color.RED);//if not player 1 must be player 2
			}
			sr.rect(8f,640f,0f,0f,412f,120f,1f,1f,0f, Color.GOLDENROD,Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD);//this is where all the information is  held (Power, KO's, health)

			sr.rect(604f,640f,0f,0f,412f,120f,1f,1f,0f, Color.GOLDENROD,Color.GOLDENROD, Color.GOLDENROD, Color.GOLDENROD);

			//player 1 health
			if (p1.getHealth()>1) {//checks if players health is greater than 1, if so fills in the bottom rect
				sr.rect(328, 650, 0f, 0f, 80, 33, 1, 1, 0,
						Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
				if (p1.getHealth()>2) {///checks if players health is greater than 2 , if so fills in the second bottom rect
					sr.rect(328, 683, 0f, 0f, 80, 33, 1, 1, 0,
							Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
					if ( p1.getHealth() > 3) {//checks if the players rect is greater than 3, if so fills in the 3 rect
						sr.rect(328, 716, 0f, 0f, 80, 33, 1, 1, 0,
								Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
					}else {//if no greater than 3, must be 3 and only 1/2 of the 3rd rect filled
						sr.rect(328, 716, 0f, 0f, 80, 33, 1, 1, 0,
								Color.GREEN, Color.GREEN, Color.RED, Color.RED);
					}
				}else {//if not greater than 2, then only 1/2 of the 2nd bar should be filled
					sr.rect(328, 683, 0f, 0f, 80, 33, 1, 1, 0,
							Color.GREEN, Color.GREEN, Color.RED, Color.RED);
					sr.rect(328, 716, 0f, 0f, 80, 33, 1, 1, 0,
							Color.RED, Color.RED, Color.RED, Color.RED);
				}

			}
			else{//if not 1 the only 1/2 of the bottom rect should be filled
				sr.rect(328, 650, 0f, 0f, 80, 33, 1, 1, 0,
						Color.GREEN, Color.GREEN, Color.RED, Color.RED);
				sr.rect(328, 683, 0f, 0f, 80, 33, 1, 1, 0,
						Color.RED, Color.RED, Color.RED, Color.RED);
				sr.rect(328, 716, 0f, 0f, 80, 33, 1, 1, 0,
						Color.RED, Color.RED, Color.RED, Color.RED);
			}


			sr.rectLine(328,650,328,749,3);//these are the bars on the side of the health
			sr.rectLine(408,650,408,749,3);//indicate where your health is at
			sr.rectLine(328,650,408,650,3);
			sr.rectLine(328,683,408,683,3);
			sr.rectLine(328,716,408,716,3);
			sr.rectLine(328,749,408,749,3);

			//player 2 health
			if (p2.getHealth()>1) {//same thing as player 1
				sr.rect(616, 650, 0f, 0f, 80, 33, 1, 1, 0,
						Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
				if (p2.getHealth()>2) {
					sr.rect(616, 683, 0f, 0f, 80, 33, 1, 1, 0,//just drawn in different location
							Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
					if (p2.getHealth()>3) {
						sr.rect(616, 716, 0f, 0f, 80, 33, 1, 1, 0,
								Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
					}else {
						sr.rect(616, 716, 0f, 0f, 80, 33, 1, 1, 0,
								Color.GREEN, Color.GREEN, Color.RED, Color.RED);
					}
				}else {
					sr.rect(616, 683, 0f, 0f, 80, 33, 1, 1, 0,
							Color.GREEN, Color.GREEN, Color.RED, Color.RED);
					sr.rect(616, 716, 0f, 0f, 80, 33, 1, 1, 0,
							Color.RED, Color.RED, Color.RED, Color.RED);
				}
			}else{
				sr.rect(616, 650, 0f, 0f, 80, 33, 1, 1, 0,
						Color.GREEN, Color.GREEN, Color.RED, Color.RED);
				sr.rect(616, 683, 0f, 0f, 80, 33, 1, 1, 0,
						Color.RED, Color.RED, Color.RED, Color.RED);
				sr.rect(616, 716, 0f, 0f, 80, 33, 1, 1, 0,
						Color.RED, Color.RED, Color.RED, Color.RED);
			}

			sr.rectLine(616,650,616,749,3);//same thing as player 1
			sr.rectLine(696,650,696,749,3);
			sr.rectLine(616,650,696,650,3);
			sr.rectLine(616,683,696,683,3);
			sr.rectLine(616,716,696,716,3);
			sr.rectLine(616,749,696,749,3);

			if ( p1.checkDead() == true || p2.checkDead() == true) {//checks if either player is dead, if so sends you back to menu
				sr.end();//ends the shape renderer so it can start a new one in the menu
				menu = false;
				menu();
			}

			sr.end();

			batch.begin();
			font.draw(batch,Integer.toString(p1.getScore()),11,757);//displays the score of player 1
			font.draw(batch,Integer.toString(p2.getScore()),985-(Integer.toString(p2.getScore()).length()-1)*33,757);
			//for player 2, the score will be displayed starting from the right, so there's a problem with the score
			//going out of the info box
			//to fix this, the length of the score as a string is multiplied by 33 for size, then subtracted from its
			//starting position at 985
			batch.end();

			sr.begin(ShapeType.Line);
			sr.setColor(Color.BLACK);
			sr.polygon(terrain.polygonVertices());//draws the outline of the polygon
			sr.end();
			//-----------------------------------------------------------------------------------------------------

			if (arrows.size()==0 || arrows.get(arrows.size()-1).isStopped()) {
				if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

					powerCalc();//calculates the power the arrow is shot with

					sr.begin(ShapeType.Filled);
					sr.setColor(Color.BLACK);

					Color powerColor = new Color(power/104,1-power/104,0,1);
					//as power increases, the amount of red increases and green decreases

					if (turn == PLAYER1) {
						sr.triangle(18, 650, 18 + 3 * power, 650, 18 + 3 * power, (int) (650 + 1 * power),
								Color.GREEN, powerColor, powerColor); //power triangle for player 1

						drawGuide(p1,mx,my);//draws path the guide will take
					}

					if (turn == PLAYER2) {
						sr.triangle(1006, 650, (int) (1006 - power * 3), (int) (650 + 1 * power),
								(int) (1006 - power * 3), 650, Color.GREEN, powerColor, powerColor);
						//power triangle for player 2

						drawGuide(p2,mx,my); //guide for player 2's arrow
					}
					sr.end();
				}
			}

			sr.begin(ShapeType.Filled);
			sr.setColor(Color.BLACK);
			sr.rectLine(1006, 650, 706, 650, 3);//draws the outline of the triangle for player 2
			sr.rectLine( 706, 650, 706, 750, 3);
			sr.rectLine(706, 750, 1006, 650, 3);

			sr.rectLine(18, 650, 318, 650, 3);
			sr.rectLine( 318, 650, 318, 750, 3);//draws the same triangle for player 1
			sr.rectLine(318, 750, 18, 650, 3);


			sr.end();

			// move arrows

			batch.begin();


			if (turn == PLAYER1) {
				float angle = (float)(Math.toDegrees(Math.atan2(760 - my - p1.getArrowShotY(),mx - p1.getArrowShotX())));//gets the angle the players bow should be pointed at by using inverse tan
				p1bowsprite.setRotation(angle);//only happens during player 1's turn
			}
			if (turn == PLAYER2) {
				float angle = (float)(Math.toDegrees(Math.atan2(760 - my - p2.getArrowShotY(),mx - p2.getArrowShotX())));//smae for player 2
				p2bowsprite.setRotation(angle + 180);//must add 180 because the angle is found backwards

			}
			p1sprite.draw(batch);
			p1headsprite.draw(batch);
			p1bowsprite.draw(batch);


			p2sprite.draw(batch);
			p2headsprite.draw(batch);
			p2bowsprite.draw(batch);

			if (powerupstriple.size()>0){//only draws them if they exist
				for (powerup pu : powerupstriple){
					batch.draw(triplePU,pu.getXPos(),pu.getYPos());//goes through each and draws them at their x and y
				}
			}
			if (powerupsheal.size()>0){//same thing as triple
				for (powerup pu : powerupsheal){
					batch.draw(healPU,pu.getXPos(),pu.getYPos());
				}
			}
			batch.end();

			drawArrows(arrows);//draw all arrows on to the screen using x,y,angle

			//here, iterating through the ArrayList wasn't a smart choice but it worked out anyways
			for (arrow action : arrows){
				for (powerup puT : powerupstriple){
					if (puT.checkInside(action.getVectorF())){//Checks if the point on the arrow is inside the circle of the powerup
						takenpowerups.add(powerupstriple.indexOf(puT));//adds it to the list of used powerups
						bottom = new arrow(action,puT.getXPos(),puT.getYPos(),arrow.BELOW);//creates two new arrows in both going at two different angles and speeds
						top = new arrow(action,puT.getXPos(),puT.getYPos(),arrow.ABOVE);
					}
				}
				for (int i : takenpowerups){
					try {
						powerupstriple.remove(i); //takes out the power-up
					}
					catch (Exception e){}
				}
				takenpowerups = new ArrayList<Integer>(); //erases all old data
				for (powerup puH : powerupsheal){
					if (puH.checkInside(action.getVectorF())){
						takenpowerups.add(powerupsheal.indexOf(puH));//same thing as triple arrow
						if (turn == PLAYER1) {
							p2.addHealth(2);
							//adds two health to the player who hits it, but turn changing is a little unorthodox
						}
						else if (turn == PLAYER2){
							p1.addHealth(2);
						}
					}
				}
				for (int i : takenpowerups){
					try {
						powerupsheal.remove(i);
					}
					catch (Exception e){}
				}
				takenpowerups = new ArrayList<Integer>();
			}
			if (bottom != null && top != null){
				arrows.add(bottom);//adds the created arrow to the current arrow list
				arrows.add(top);
				drawArrows(arrows);//draws them to the screen
				bottom = null;//sets the bottom and top objects back to null for the next time
				top = null;
			}

			stop();//checks if arrows need to be stopped(collide)
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		sr.dispose();
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	// Mouse up/touch released
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	// Mouse down/touched
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (arrows.size()==0 || arrows.get(arrows.size()-1).isStopped()){// checks to make sure their are no arrows in the air from the the other player before you shoot
			if (keycode == Keys.SPACE) {
				fire();//fires the arrow
				power = 0;//sets power back to zero
				int randomPowerUp = rand.nextInt(5)+1;//has a random chance to spawn in a power up
				if (randomPowerUp == powerup.triple){
					powerupstriple.add(new powerup(randomPowerUp,rand.nextInt(624)+200,//makes sure that it can't spawn inside the ground
							rand.nextInt(200)+Collections.max(gYCoords)));//puts it at a random x and y coordinate
				}
				else if (randomPowerUp == powerup.heal){
					powerupsheal.add(new powerup(randomPowerUp,rand.nextInt(624)+200,
							rand.nextInt(200)+Collections.max(gYCoords)));
				}
			}
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode){
		return false;
	}

	private Sprite groundfill() { //fills the ground
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);//gets all of the pixels on the screen

		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);//creates the pixmap that is the same size as the screen
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		pixmap.setColor( 0, 1, 0, 0.75f );
		int basecolor;//most of this is just for fun
		basecolor = 16033962;
		int red = (basecolor >> 16) & 0xFF;
		int green = (basecolor >> 8) & 0xFF;//Separates the base color into it's rgb values
		int blue = basecolor & 0xFF;
		for ( int x = 0;x < 1024 ; x++) {
			for ( int y = 0; y< 768; y++) {
				if (groundPolygon.contains(x, y)) {//colors all of the pixels that are inside the polygon the color we want it to be
					int plus_or_minus = rand.nextInt(2)+1;
					if (plus_or_minus == 1){//this would be a solid color if we comment out all of this
						green += 1;
					}else {
						green -= 10;//gives the ground a cool texture
					}
					pixmap.drawPixel(x, 768 - y, Color.rgb565(red, green, blue));//this wasn't supposed to happen but it looks sick
				}
			}
		}
		Texture pixmaptex = new Texture( pixmap );
		Sprite background = new Sprite(pixmaptex);//creates the sprite
		background.setPosition(0, 0);


		pixmap.dispose();
		return background;//returns the sprite
	}

	private void menu(){ //draws a menu at the start or whenever someone wins

		sr.begin(ShapeType.Filled);
		sr.rect(0f,0f,0f,0f,1024f,768f,1f,1f,0f, Color.ROYAL,Color.SKY, Color.ROYAL, Color.SKY);//draws the fading background
		sr.end();

		Sprite headsprite = new Sprite(p1head);//creates a larger player
		headsprite.setSize(225, 225);
		headsprite.setPosition(115, 425);
		Sprite bodysprite = new Sprite(p1body);
		bodysprite.setSize(250, 350);
		bodysprite.setPosition(100, 100);
		Sprite bowsprite = new Sprite(p1bow);
		bowsprite.setSize(200,200);
		bowsprite.setPosition(175, 300);
		bowsprite.setOrigin(50, 100);//have to set origin because this is the only sprite that rotates
		bowsprite.rotate(280);

		batch.begin();


		if (p1.getHealth() == 0|| p2.getHealth() == 0) {
			if(p1.getHealth() == 0) {
				font.draw(batch, "Player 2 WON!", 385, 450);//if player 2 wins, then player 2 is shown at the end of the game
				bodysprite = new Sprite(p2body);
				bodysprite.setSize(250, 350);//have to do all of this again because the player is facing the other direction
				bodysprite.setPosition(100, 100);//also have to reset sprites because their different from p1 sprites
				headsprite = new Sprite(p2head);
				headsprite.setSize(225, 225);
				headsprite.setPosition(115, 425);
				bowsprite = new Sprite(p2bow);
				bowsprite.setSize(200,200);
				bowsprite.setPosition(75, 300);
				bowsprite.setOrigin(150, 100);
				bowsprite.rotate(80);
			}else if (p2.getHealth() == 0){
				font.draw(batch, "Player 1 WON!", 385, 450);
				bodysprite = new Sprite(p1body);
				bodysprite.setSize(250, 350);
				bodysprite.setPosition(100, 100);
				headsprite = new Sprite(p1head);
				headsprite.setSize(225, 225);
				headsprite.setPosition(115, 425);
				bowsprite = new Sprite(p1bow);
				bowsprite.setSize(200,200);
				bowsprite.setPosition(175, 300);
				bowsprite.setOrigin(150, 100);//Different origin than player 2 because it rotates at a different point according to the player
				bowsprite.rotate(10);
			}
			font.draw(batch, "Click ENTER for a", 365, 260);
			font.draw(batch, "REMATCH", 550, 200);
		}else {
			font.draw(batch, "Click ENTER to play", 385, 460);
		}
		bodysprite.draw(batch);
		headsprite.draw(batch);
		bowsprite.draw(batch);
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {//checks if you click ENTER
			menu = true;
			matchstart();//starts the match
		}

	}

	private void powerCalc(){ //calculates power for the arrow to use
		if (power<=0){ //once the power is at a minimum, it goes up
			up_or_down = UP;
		}
		else if (power>=100){ //once the power is at a maximum, it goes down
			up_or_down = DOWN;
		}
		if (power<=10){//if power is lower than it will take a longer amount of time for it to increase
			power+=up_or_down * 0.5;//0.5
		}
		else if (power<=30){
			power+=up_or_down ;//1
		}
		else if (power<=60){
			power+=up_or_down * 1.5;//1.5
		}
		else if (power<=80){//when the power is higher the power the increases at a rapid rate making it harder to get to a hundred
			power+=up_or_down * 2;//2
		}
		else if (power<=104){
			power+=up_or_down * 4;//4
		}
	}

	private void drawGuide(player who, int mouseX, int mouseY){ //draws a guide for the arrows
		double arcHypotenuse; //distance from the mouse position to the arrow's shooting position on the player
		int sx,sy; //guide's coordinates
		ArrayList<Integer> points = new ArrayList<Integer>(); //holds coordinates for the circle guides
		//calculating distance using pythagoras
		arcHypotenuse = Math.pow(Math.pow((mouseX - who.getArrowShotX() )/ 16, 2) + Math.pow(((760 - mouseY - who.getArrowShotY()) / 16), 2), 0.5);
		//same as the speed of the arrow, how far in the x and y the guide goes
		sx = (int) (((mouseX - who.getArrowShotX()) / 16) / arcHypotenuse * (power / 2));
		sy = (int) (((760 - mouseY - who.getArrowShotY()) / 16) / arcHypotenuse * (power / 2));
		for (int c = 0; c < Math.abs(mouseX - who.getArrowShotX()); c += 2) { //loops through all the x coordinates 2 at a time
			sy -= 1; //decreases y for each
			if (sy > 0) { //before the y goes below the player, it stops adding points
				//points has the x and y of the arrows with the guide's distance added on
				points.add(who.getArrowShotX()+ sx * c); //sx and sy are like the velocity and c is the time
				points.add(who.getArrowShotY() + sy * c);
			}
		}
		for ( int k = 0 ; k < points.size()*3/4 ; k+=2) { //draws only 3/4 of the circles, going two at a time
			sr.circle(points.get(k), points.get(k+1), 3); //draws using x, y and radius
		}
	}

	private void fire(){ //creates arrows
		int mx = Gdx.input.getX();
		int my = Gdx.input.getY();
		if (turn==PLAYER1) { //using player 1's position to create arrows, adds instead
			arrow action = new arrow(p1.getX()+16, p1.getY()+65, //position, then mouse distance from player, scaled down
					(mx - p1.getArrowShotX()) / 16, (760 - my - p1.getArrowShotY()) / 16, power);
			action.doing();
			arrows.add(action);
			turn = PLAYER2; //changes the player's turn right after the arrow is created,
			// so adding health goes to the player who's turn it isn't (player 1's arrow hits a health power-up but it's player 2's turn)
			// even checking who the arrow hits is different - player 2 gets hit when it's player 2's turn
		} else if (turn == PLAYER2) {  //player 2's position means subtracting
			arrow action = new arrow(p2.getX() - 16, p2.getY() + 65,
					(mx - p2.getX()) / 16, (760 - my - p2.getY()) / 16, power);
			arrows.add(action);
			action.doing();
			turn = PLAYER1;
		}
		if (arrows.size()>7){ //once 7 arrows are on the screen, some start to get removed
			arrows.remove(0);
		}
	}

	private void drawArrows(ArrayList<arrow> arrowList){ //draws arrows, called at multiple spots
		batch.begin();
		for(int i = 0; i < arrowList.size(); i++) { //arrow ArrayList
			arrow action = arrowList.get(i);
			Sprite arrowSprite = new Sprite(projectile);
			arrowSprite.setPosition(action.getXPos(),action.getYPos());
			vy = action.getYSpeed(); //speeds
			vx = action.getXSpeed();
			arrowSprite.setOrigin(25, 6); //rotations
			arrowSprite.setRotation(action.getAngle());
			arrowSprite.setSize(50,13); //resizing

			if (action.isStopped()==false) { //the arrow is flying
				action.setYSpeed(vy-1); //continuously decrease y speed
				action.setAngle(); //change the angle using the speeds
				arrowSprite.translateX(vx); //move the arrow
				arrowSprite.translateY(vy);
			}
			action.setXPos((int)arrowSprite.getX()); //sets new position
			action.setYPos((int)arrowSprite.getY());

			arrowSprite.draw(batch);
		}
		batch.end();
	}

	private void stop(){ //checking if the arrows need to be stopped
		for (int i=0; i<arrows.size(); i++){ //arrow ArrayList
			arrow action = arrows.get(i);
			if (action.getXPos()>974 || action.getXPos()<0){ //collision with walls
				arrowStop(action,WSIDE);
			}
			else if (turn == PLAYER2 && p2.getPlayerRect().contains(action.getVectorF())){ //collision with player 2
				p2.addHealth(-1); //health and points
				p1.addPoints(1);
				action.hitNoise(); //sound
				clear_and_next(); //new terrain
			}
			else if (turn == PLAYER1 && p1.getPlayerRect().contains(action.getVectorF())){ //collision with player 1
				p1.addHealth(-1); //health and points
				p2.addPoints(1);
				action.hitNoise(); //sound
				clear_and_next(); //new terrain
			}

			else{ //checking for collision with the ground
				for (int j=0; j<gXCoords.size()-1; j++){
					//here the Intersector object was used to calculate the distance from the line segment of the ground to the arrow's tip
					if (intersect.distanceSegmentPoint(gXCoords.get(j),gYCoords.get(j)-5, //-5 to push the ground down for a more obvious collision
							gXCoords.get(j + 1),gYCoords.get(j + 1)-5,action.frontX(),action.frontY())
							< Math.hypot((double)action.getYSpeed(),(double)action.getXSpeed())/2){
						//if the distance is less than half the arrow's length to ensure a more obvious collision
						arrowStop(action,WGROUND);
					}
				}
			}
		}
	}

	private void arrowStop(arrow stopping, int where) {//stopping the arrow's movement
		if (where == WSIDE) { //if it's colliding with the side
			stopping.setXSpeed(0); //instantly stop the speeds
			stopping.setYSpeed(0);
		} else if (where == WGROUND) { //if it's colliding with the ground
			stopping.setXSpeed(stopping.getXSpeed() / 2); //takes a longer time to stop so that the arrow goes into the ground more
			stopping.setYSpeed(stopping.getYSpeed() / 2);
		}
		stopping.setAngle(stopping.getAngle()); //stop the angle rotation as it is
		stopping.setStop(); //set the arrow to stop
	}

	private void clear_and_next(){ //after a player is hit, this resets the terrain
		arrows = new ArrayList<arrow>(); //arrows resets
		powerupsheal = new ArrayList<powerup>(); //all power-ups reset
		powerupstriple = new ArrayList<powerup>();

		terrain = new ground(); //a new ground is created
		groundPolygon = new Polygon(terrain.polygonVertices()); //a new polygon is made
		gXCoords = terrain.xCoords(); //the new x and y coordinates are given
		gYCoords = terrain.yCoords();
		filled = false; //ground is no longer filled

		p1.setY(gYCoords.get(1)-10); //setting new y coordinates for the players
		p2.setY(gYCoords.get(gYCoords.size()-2)-10);

		//setting all the player's sprite parts, the sprites aren't symmetrical, so the positioning is different
		p1sprite.setPosition(p1.getX(),p1.getY());
		p1bowsprite.setPosition(p1.getX() + 16, p1.getY()+35);
		p1headsprite.setPosition((float)(p1.getX() - 5), p1.getY() + 68);

		p2sprite.setPosition(p2.getX(),p2.getY());
		p2bowsprite.setPosition(p2.getX() - 4, p2.getY()+35);
		p2headsprite.setPosition((float)(p2.getX()), p2.getY() + 68);
	}
	private void matchstart(){ //after a player has lost all of their health and a new match is started

		clear_and_next();

		p1.setHealth(4); //resets health
		p2.setHealth(4);
	}
}
