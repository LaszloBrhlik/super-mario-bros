package com.github.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.mariobros.Screens.PlayScreen;

public class MarioBros extends Game {
  public static final int V_WIDTH = 400;
  public static final int V_HEIGHT = 208;
  public static final float PPM = 100f;
  public static final int TILE_SIZE = 16;

  public static final short GROUND_BIT = 1;
  public static final short MARIO_BIT = 2;
  public static final short BRICK_BIT = 4;
  public static final short COIN_BIT = 8;
  public static final short DESTROYED_BIT = 16;
  public static final short OBJECT_BIT = 32;
  public static final short ENEMY_BIT = 64;
  public static final short ENEMY_HEAD_BIT = 128;

  public SpriteBatch batch;

  /**
   * warning: using AssetManager in a static way can cause issues, especially on Android.
   * Instead you may want to pass around AssetManager to those classes that need it.
   * We will use it in the static context to save time for now.
   */
  public static AssetManager assetManager;

  @Override
  public void create() {
    batch = new SpriteBatch();
    assetManager = new AssetManager();
    assetManager.load("audio/music/mario_music.ogg", Music.class);
    assetManager.load("audio/sounds/coin.wav", Sound.class);
    assetManager.load("audio/sounds/bump.wav", Sound.class);
    assetManager.load("audio/sounds/breakblock.wav", Sound.class);
    assetManager.finishLoading();

    setScreen(new PlayScreen(this));
  }

  @Override
  public void render() {
    super.render();
  }

  @Override
  public void dispose() {
    super.dispose();
    assetManager.dispose();
    batch.dispose();
  }
}