/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.drops;

/**
 *
 * @author Ras
 */
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    
    final Drop game;

	Texture tuboup;
        Texture tubodown;
	Texture bird;
        Texture fondo;
        Texture coin;
        
	Sound coinSound;
	Music rainMusic;
        
	OrthographicCamera camera;
	Rectangle birdbucket;
        
	Array<Rectangle> tubosabajo;
        Array<Rectangle> tubosarriba;
        Array<Rectangle> monedas;
       
	long lastTuboTime;
        long lasTuboTime2;
        long lasTcoin;
        
	int CoinsGathered;
        int TubosGathered;
        float spaceTubo;
        float tamanio;

	public GameScreen(final Drop gam) {
            
		this.game = gam;

		//load the images for the droplet and the bucket, 64x64 pixels each
		tuboup = new Texture(Gdx.files.internal("toptube.png"));
                tubodown = new Texture (Gdx.files.internal("downtube.png"));
                fondo  = new Texture (Gdx.files.internal("bg.png"));
                coin = new Texture (Gdx.files.internal("coin.png"));
                
		bird = new Texture(Gdx.files.internal("flappybird.png"));

		// load the drop sound effect and the rain background "music"
		coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create a Rectangle to logically represent the bucket
		birdbucket = new Rectangle();
		birdbucket.x = 480 / 2 - 64 / 2; // center the bucket horizontally
		birdbucket.y = 50; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		birdbucket.width = 68;
		birdbucket.height = 58;

		// create the raindrops array and spawn the first raindrop
		tubosabajo = new Array<Rectangle>();
                tubosarriba = new Array<Rectangle>();
                monedas = new Array<Rectangle>();
                
		spawnTubodetubosabajo();
                spawnTubodetubosarriba();
                
               

	}

	private void spawnTubodetubosabajo() {
            
               
                
		Rectangle tubo = new Rectangle();   
                tubo.x = 800;
		tubo.y = MathUtils.random(-350, -200);
               // System.out.println("tubo y abajo" + " = " + tubo.y);
                spaceTubo = tubo.y/4;
                //System.out.println("tubo y/4" + " = " + spaceTubo);
		tubo.width = 68;
		tubo.height = 420;
		tubosabajo.add(tubo);
		lastTuboTime = TimeUtils.nanoTime();
                
                
                
	}
        
        private void spawnTubodetubosarriba() {
            
            
		Rectangle tubo = new Rectangle();   
                tubo.x = 800;
		tubo.y = MathUtils.random(250-spaceTubo, 400);
               // System.out.println("tubo y arriba" + " = " + tubo.y);  
               
		tubo.width = 68;
		tubo.height = 420;
		tubosarriba.add(tubo);
		lasTuboTime2 = TimeUtils.nanoTime();
                
               
	}
        
        private void spawnMonedas () {
            
                
                Rectangle coin = new Rectangle();
                coin.x = 800 + 40 ;
                coin.y = 480 / 2 - 37 / 2;
                coin.width = 37;
                coin.height = 37;
                monedas.add(coin);
               
                lasTcoin = TimeUtils.millis();
                System.out.println("LastCoin =" + lasTcoin);
               
        }
        
        
        
      

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
                game.batch.draw(fondo,0, 0, 800, 480);
                
		game.font.draw(game.batch, "Coins Collected: " + CoinsGathered, 0, 380);
                game.font.draw(game.batch, "Tubos Superated: " + TubosGathered, 0, 480);
                
		game.batch.draw(bird, birdbucket.x, birdbucket.y);
                
		for (Rectangle raindrop : tubosabajo) {
                
                    game.batch.draw(tubodown, raindrop.x, raindrop.y);
               
		}
                
                for (Rectangle raindrop : tubosarriba) {
                
                    game.batch.draw(tuboup, raindrop.x, raindrop.y);
               
		}
                
                for (Rectangle raindrop : monedas) {
                
                    game.batch.draw(coin, raindrop.x, raindrop.y);
               
		}
                
                
		game.batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			birdbucket.x = touchPos.x - 64 / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			birdbucket.y -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.UP))
			birdbucket.y += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (birdbucket.y < 0)
			birdbucket.y = 0;
		if (birdbucket.y > 800 - 64)
			birdbucket.y = 800 - 64;
                

		// check if we need to create a new tube
		if (TimeUtils.nanoTime() - lastTuboTime > 1000000000)                   
			spawnTubodetubosabajo();
                        
                        
                // check if we need to create a new tube
		if (TimeUtils.nanoTime() - lasTuboTime2 > 100000000)			
                        spawnTubodetubosarriba();
                
                
                 // check if we need to create a new tube
		if (TimeUtils.millis() - lasTcoin  > 3000){			
                        spawnMonedas();
                                
                }

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we play back
		// a sound effect as well.
		Iterator<Rectangle> iter = tubosabajo.iterator();
                Iterator<Rectangle> iter2 = tubosarriba.iterator();
                Iterator<Rectangle> iter3 = monedas.iterator();
                
		while (iter.hasNext()) {
                    
			Rectangle raindrop = iter.next();
                        Rectangle raindrop2 = iter2.next();
                       
                        
			raindrop.x -= 200 * Gdx.graphics.getDeltaTime();
                        raindrop2.x -= 200 * Gdx.graphics.getDeltaTime();
                     
                       
                        if(raindrop.x + raindrop.width < birdbucket.x && raindrop.x + raindrop.width < 0){
                            
                             TubosGathered++;
                             iter.remove();
                             iter2.remove();
  
                        }
                        
                        if(raindrop.overlaps(birdbucket)){
                            
                            System.out.println("entra1");
                        }
                     
                        if(raindrop2.overlaps(birdbucket)){
                        
                            System.out.println("entra2");
                        }
                
		}
                
                while (iter3.hasNext()) {
			
                        Rectangle raindrop3 = iter3.next();
                       
                        raindrop3.x -= 200 * Gdx.graphics.getDeltaTime();
              
                        
			if (raindrop3.overlaps(birdbucket)) {
                                //game.setScreen(new nombrepantalla, this Drops);
                            
				CoinsGathered++;
				coinSound.play();
				//iter.remove();
                                //iter2.remove();
                                iter3.remove();
                                
                                
			}
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		tuboup.dispose();
		bird.dispose();
		coinSound.dispose();
		rainMusic.dispose();
	}
}
