package com.kush.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class flappybird extends ApplicationAdapter {
   // ShapeRenderer shapeRenderer ;
	SpriteBatch batch;
	Texture background;
    int score=0;
    int scoringtube=0;

    BitmapFont font;

    Texture gameover;

	Texture[] birds;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
    Circle birdcircle ;

	int gamestate = 0;
	float gravity = (float) 1.2;

    float gap = 400;
    float Maxtubeupset;
    Random randomGenerator;

    float tubevelocity=4;


    int numberoftubes =4;
    float distancebetweentubes;
    float[] tubeX = new float[(int) numberoftubes];
    float[] tubeoffset = new float[numberoftubes];

    Rectangle[] toptuberectangles;
    Rectangle[] bottomtuberectangles;


    Texture toptube;
    Texture bottomtube;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
        gameover= new Texture("gameover.png");
		birds = new Texture[2];
        toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");

        Maxtubeupset = Gdx.graphics.getHeight()/2-gap/2-50;
        randomGenerator = new Random();
        birdcircle = new Circle();
        font =  new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        toptuberectangles = new Rectangle[numberoftubes];
        bottomtuberectangles=new Rectangle[numberoftubes];

     //   shapeRenderer = new ShapeRenderer();
	    distancebetweentubes = Gdx.graphics.getWidth()*(0.6f);
        for(int i=0;i<numberoftubes;i++) {
            toptuberectangles[i] = new Rectangle();
            bottomtuberectangles[i] = new Rectangle();
        }
        startgame();

    }

    public void startgame(){
        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
        for(int i=0;i<numberoftubes;i++){

            tubeoffset[i]=(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-100);
            tubeX[i]=Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + i*distancebetweentubes + Gdx.graphics.getWidth();



        }
    }

	@Override
	public void render () {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		    if(gamestate!=2) {
                Gdx.app.log("reached", "1");
                if (Gdx.input.justTouched()) {


                    gamestate = 1;

                }
            }

		if(gamestate!=0 && gamestate!=2) {


        //    batch.begin();



			if(Gdx.input.justTouched()){

				velocity  = -20;


			}

            for(int i=0;i<numberoftubes;i++) {
                if(tubeX[i] < -toptube.getWidth() ){
                    tubeX[i]+=numberoftubes*distancebetweentubes;
                    tubeoffset[i]=(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-100);
                }else {
                    tubeX[i] = tubeX[i] - tubevelocity;

                    if( tubeX[scoringtube] < Gdx.graphics.getWidth() ){
                        score++;
                        if(scoringtube < numberoftubes -1 ){
                            scoringtube++;
                        }else{
                            scoringtube=0;
                        }
                    }

                }
                batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
                batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);

                toptuberectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
                bottomtuberectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],toptube.getWidth(),toptube.getHeight());


            }

            if(birdY > 0 ) {
                //effective gravity
                velocity += gravity;
                birdY -= velocity;

                if (flapstate == 0) {
                    flapstate = 1;
                } else {
                    flapstate = 0;
                }
            }else{
                gamestate = 2;
                return;
            }
		}

		batch.draw(birds[flapstate],Gdx.graphics.getWidth()/2 - birds[flapstate].getWidth()/2,birdY);


        font.draw(batch,String.valueOf(score),100,230);

        birdcircle.set(Gdx.graphics.getWidth()/2,birdY+birds[0].getHeight()/2,birds[0].getWidth()/2);



        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);
        if(gamestate != 2)
        for(int i=0;i<numberoftubes;i++){
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],toptube.getWidth(),toptube.getHeight());

            if(Intersector.overlaps(birdcircle,toptuberectangles[i]) || Intersector.overlaps(birdcircle,bottomtuberectangles[i]) ){

                gamestate = 2;

            }
        }

        if(gamestate == 2){
            batch.draw(gameover , Gdx.graphics.getWidth()/2 - gameover.getWidth()/2 ,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);


            if(Gdx.input.justTouched()){

                Gdx.app.log("reached","yay");
                gamestate = 0;
                score=0;
                scoringtube=0;
                velocity=0;
                startgame();




            }


        }
        batch.end();
        //shapeRenderer.end();


    }
}
