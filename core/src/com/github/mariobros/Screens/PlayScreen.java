package com.github.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Scenes.Hud;
import com.github.mariobros.Sprites.Mario;
import com.github.mariobros.Tools.B2WorldCreator;
import com.github.mariobros.Tools.WorldContactListener;

public class PlayScreen implements Screen {
  //reference to our Game, used to set Screens
  private MarioBros game;

  private TextureAtlas atlas;

  //basic playscreen variables
  private OrthographicCamera gameCam;
  private Viewport gamePort;
  private Hud hud;

  //tiled map variables
  private TmxMapLoader mapLoader;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;

  //Box2d variables
  private World world;
  private Box2DDebugRenderer b2dr;

  //mario
  private Mario player;

  private static final int  DELTA_X = 100;

  public PlayScreen(MarioBros game) {
    atlas = new TextureAtlas("Mario_and_Enemies.pack");

    this.game = game;
    //create cam used to follow mario through cam world
    gameCam = new OrthographicCamera();

    //create a FitViewport to maintain virtual aspect ratio
    gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);

    //create our game HUD for scores/timers/level info
    hud = new Hud(game.batch);

    //load our map and setup our map renderer
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("tiles/supermario.tmx");
    renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);

    //initially set our gameCam to be centered correctly at the start
    gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    //create our box2d world, setting no gravity in x, -10 gravity to y, and allow bodies to sleep
    world = new World(new Vector2(0,-10), true);

    //allow debug lines of our box2d world
    b2dr = new Box2DDebugRenderer();

    //create world
    new B2WorldCreator(world, map);

    //create mario
    player = new Mario(world, this);

    world.setContactListener(new WorldContactListener());
  }

  public TextureAtlas getAtlas() {
    return atlas;
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    //separate our update logic from render
    update(delta);

    //clear the game screen with Black
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    //render our game map
    renderer.render();

    //render our Box2DDebugLines
    b2dr.render(world, gameCam.combined);

    game.batch.setProjectionMatrix(gameCam.combined);
    game.batch.begin();
    player.draw(game.batch);
    game.batch.end();

    //set our batch to now draw what the HUD camera sees
    game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    hud.stage.draw();
  }

  public void update(float dt) {
    //handle user input first
    handleInput(dt);

    //takes 1 step in the physics simulation(60 times per second)
    world.step(1/60f, 6, 2);

    player.update(dt);
    hud.update(dt);

    //set the camera track mario's x coordinate
    gameCam.position.x = player.b2body.getPosition().x;

    //update our gamecam with correct coordinates after changes
    gameCam.update();

    //tell our renderer to draw only what our camera can see in our game world
    renderer.setView(gameCam);
  }

  private void handleInput(float dt) {
    if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
      player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2) {
      player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2) {
      player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
    }

  }

  @Override
  public void resize(int width, int height) {
    //update our game viewport
    gamePort.update(width, height);
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    map.dispose();
    renderer.dispose();
    world.dispose();
    b2dr.dispose();
    hud.dispose();
  }
}
