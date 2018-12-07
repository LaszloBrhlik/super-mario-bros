package com.github.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

  private TmxMapLoader mapLoader;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;

  private static final int  DELTA_X = 100;

  public PlayScreen(MarioBros game) {
    this.game = game;
    //create cam used to follow mario through cam world
    gameCam = new OrthographicCamera();

    //create a FitViewport to maintain virtual aspect ratio
    gamePort = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, gameCam);

    //create our game HUD for scores/timers/level info
    hud = new Hud(game.batch);

    mapLoader = new TmxMapLoader();
    map = mapLoader.load("tiles/supermario.tmx");
    renderer = new OrthogonalTiledMapRenderer(map);
    gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    update(delta);

    //clear the game screen with Black
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    renderer.render();

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
