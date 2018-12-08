package com.github.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.mariobros.MarioBros;
import com.github.mariobros.Scenes.Hud;

public class PlayScreen implements Screen {
  //reference to our Game, used to set Screens
  private MarioBros game;

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

  private static final int  DELTA_X = 100;

  public PlayScreen(MarioBros game) {
    this.game = game;
    //create cam used to follow mario through cam world
    gameCam = new OrthographicCamera();

    //create a FitViewport to maintain virtual aspect ratio
    gamePort = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, gameCam);

    //create our game HUD for scores/timers/level info
    hud = new Hud(game.batch);

    //load our map and setup our map renderer
    mapLoader = new TmxMapLoader();
    map = mapLoader.load("tiles/supermario.tmx");
    renderer = new OrthogonalTiledMapRenderer(map);

    //initially set our gameCam to be centered correctly at the start
    gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    world = new World(new Vector2(0,0), true);
    b2dr = new Box2DDebugRenderer();

    BodyDef bdef = new BodyDef();
    PolygonShape shape = new PolygonShape();
    FixtureDef fdef = new FixtureDef();
    Body body;


    //create ground bodies/fixtures
    for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

      body = world.createBody(bdef);

      shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
      fdef.shape = shape;
      body.createFixture(fdef);
    }

    //create pipe bodies/fixtures
    for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

      body = world.createBody(bdef);

      shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
      fdef.shape = shape;
      body.createFixture(fdef);
    }

    //create bricks bodies/fixtures
    for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

      body = world.createBody(bdef);

      shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
      fdef.shape = shape;
      body.createFixture(fdef);
    }

    //create coins bodies/fixtures
    for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
      Rectangle rect = ((RectangleMapObject) object).getRectangle();

      bdef.type = BodyDef.BodyType.StaticBody;
      bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

      body = world.createBody(bdef);

      shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
      fdef.shape = shape;
      body.createFixture(fdef);
    }
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

    //set our batch to now draw what the HUD camera sees
    game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
    hud.stage.draw();
  }

  public void update(float dt) {
    handleInput(dt);

    gameCam.update();
    renderer.setView(gameCam);
  }

  private void handleInput(float dt) {
    if (Gdx.input.isTouched()) gameCam.position.x += DELTA_X * dt;
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

  }
}
